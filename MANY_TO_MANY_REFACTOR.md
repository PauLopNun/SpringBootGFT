# Refactor: OneToMany → ManyToMany + Optimización N+1

## El problema de antes

La relación original entre `User` y `Allergy` era **OneToMany / ManyToOne**:

```
users          allergies
┌────────┐     ┌──────────────────────┐
│ id: 1  │────▶│ id: 1, name: Pollen  │
│ name   │     │ user_id: 1 (FK)      │
└────────┘     ├──────────────────────┤
               │ id: 2, name: Peanuts │
               │ user_id: 1 (FK)      │
               └──────────────────────┘
```

Cada alergia **pertenecía a un único usuario**. Si dos usuarios son alérgicos al polen, en la base de datos había **dos filas "Pollen"**, una por cada usuario. Eso no es correcto conceptualmente: "Pollen" es una entidad que existe por sí sola, no depende de ningún usuario.

---

## La solución: ManyToMany

En la realidad, un usuario puede tener muchas alergias, y una alergia puede afectar a muchos usuarios. Eso es una relación **Many-to-Many**, y se resuelve con una **tabla intermedia** (join table):

```
users          user_allergies      allergies
┌────────┐     ┌─────────────────┐  ┌──────────────────┐
│ id: 1  │────▶│ user_id: 1      │  │ id: 1            │
│ Alice  │     │ allergy_id: 1   │◀─│ name: Pollen     │
│        │     ├─────────────────┤  ├──────────────────┤
│ id: 2  │────▶│ user_id: 1      │  │ id: 2            │
│ Bob    │     │ allergy_id: 2   │◀─│ name: Peanuts    │
└────────┘     ├─────────────────┤  └──────────────────┘
               │ user_id: 2      │
               │ allergy_id: 1   │  ← Bob también tiene Pollen
               └─────────────────┘    (misma fila, no duplicada)
```

Ahora "Pollen" existe **una sola vez** en la tabla `allergies`. La tabla `user_allergies` simplemente registra quién tiene qué.

---

## Cómo se define en JPA

### User (lado owner — el que define la join table)

```java
@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
    name = "user_allergies",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "allergy_id")
)
private List<Allergy> allergies;
```

- `User` es el **lado dueño** de la relación porque él declara el `@JoinTable`.
- `CascadeType.PERSIST` → al crear un usuario con alergias nuevas, las alergias se guardan también.
- `CascadeType.MERGE` → al actualizar un usuario con alergias ya existentes, se sincronizan sin error.

### Allergy (lado inverso)

```java
@ManyToMany(mappedBy = "allergies")
@JsonIgnore
private List<User> users;
```

- `mappedBy = "allergies"` → le dice a JPA que esta relación ya está gestionada por el campo `allergies` de `User`. Allergy no duplica la definición.
- `@JsonIgnore` → evita la referencia circular al serializar a JSON (`User → Allergy → User → ...`).

---

## El problema N+1 y cómo se resuelve

### ¿Qué es N+1?

Es uno de los problemas de rendimiento más comunes en aplicaciones con JPA. Ocurre cuando tienes una colección lazy y la recorres en código:

```
1 query  → SELECT * FROM users           (trae 30 usuarios)
30 queries → SELECT * FROM user_allergies   (una por cada usuario)
             JOIN allergies WHERE user_id = ?

Total: 31 queries para obtener 30 usuarios con sus alergias
```

Con 1000 usuarios serían **1001 queries**. La base de datos recibe un martillazo.

### La causa en JPA

JPA por defecto carga las colecciones con `FetchType.LAZY`. Esto significa: "no cargues las alergias hasta que alguien las pida". Cuando el código itera sobre los usuarios y accede a `.getAllergies()`, JPA lanza una query nueva por cada uno.

### La solución: JOIN FETCH

```java
// UserRepository
@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.allergies")
List<User> findAllWithAllergies();
```

Esto genera **1 sola query SQL** que trae todo de golpe:

```sql
SELECT DISTINCT u.*, a.*
FROM users u
LEFT JOIN user_allergies ua ON u.id = ua.user_id
LEFT JOIN allergies a ON ua.allergy_id = a.id
```

- `JOIN FETCH` → le ordena a JPA que cargue las alergias en la misma query, no después.
- `LEFT JOIN` → incluye también usuarios sin alergias (no los excluye).
- `DISTINCT` → el JOIN genera filas duplicadas por cada alergia (un usuario con 3 alergias aparece 3 veces). `DISTINCT` elimina esos duplicados.

### Comparativa

| Situación | Queries a BD | Con 1000 usuarios |
|---|---|---|
| Sin optimización (LAZY) | N+1 | 1001 queries |
| Con `JOIN FETCH` | **1** | **1 query** |

---

## El bug de DataInitializer: Detached Entity

Al migrar a M2M apareció un error nuevo:

```
Detached entity passed to persist: Allergy
```

### ¿Por qué ocurría?

```java
// CommandLineRunner — sin transacción
List<Allergy> allergies = allergyRepository.saveAll(...); // ← las alergias se guardan
// La sesión JPA se cierra → las entidades quedan "detached" (desconectadas)

user.setAllergies(allergies); // ← referencias a entidades detached
userRepository.save(user);    // ← cascade PERSIST intenta persistirlas de nuevo → ERROR
```

El `CommandLineRunner` no tiene transacción propia. Cada llamada al repositorio abre y cierra su propia sesión. Cuando termina `saveAll()`, las alergias quedan **detached** (existen en BD pero JPA ya no las gestiona en memoria). Al intentar hacer cascade `PERSIST` sobre entidades detached, JPA lo rechaza.

### La solución: @Transactional en run()

```java
@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    @Transactional  // ← todo el método corre en una sola transacción
    public void run(String... args) {
        List<Allergy> allergies = allergyRepository.saveAll(...);
        // Las alergias siguen MANAGED (la sesión no se ha cerrado)

        for (int i = 1; i <= 30; i++) {
            user.setAllergies(allergies.subList(...));
            userRepository.save(user); // cascade PERSIST sobre entidades managed → OK
        }
    }
}
```

Con `@Transactional`, toda la ejecución de `run()` ocurre dentro de una única sesión JPA. Las alergias guardadas por `saveAll()` permanecen **managed** (en el primer nivel de caché), así que cuando el cascade de `persist` llega a ellas, JPA simplemente las ignora porque ya están persistidas.

---

## Flujo completo del endpoint

```
GET /api/users/allergies
        │
        ▼
UserController.getUsersWithAllergies()
        │
        ▼
UserService.getUsersWithAllergies()
        │
        ▼
UserRepository.findAllWithAllergies()   ← 1 sola query SQL con JOIN FETCH
        │
        ▼
List<User> con allergies ya cargadas    ← sin lazy, sin N+1
        │
        ▼
.stream().map(u → new UserDTO(...))     ← mapeo a DTO para evitar referencias circulares
        │
        ▼
List<UserDTO>  →  JSON response
```

### ¿Por qué mapear a DTO?

Si devolviéramos la entidad `User` directamente, Jackson intentaría serializar `User.allergies`, luego cada `Allergy.users`, luego cada `User.allergies` de nuevo... bucle infinito. El `@JsonIgnore` en `Allergy.users` corta el ciclo, pero los DTOs son la solución limpia: son objetos planos sin referencias circulares.
