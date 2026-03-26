package com.exampleinyection.clase2parte2.config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AppConfigTest {
    @Test
    void testConfig() {
        AppConfig config = new AppConfig();
        AppConfig.DefaultSettings def = new AppConfig.DefaultSettings();
        def.setName("A");
        def.setAge(10);
        assertEquals("A", def.getName());
        assertEquals(10, def.getAge());
        config.setDefaults(def);
        assertEquals(def, config.getDefaults());
        AppConfig.PaginationSettings pag = new AppConfig.PaginationSettings();
        pag.setMaxSize(50);
        assertEquals(50, pag.getMaxSize());
        config.setPagination(pag);
        assertEquals(pag, config.getPagination());
        AppConfig.UpdateSettings up = new AppConfig.UpdateSettings();
        up.setDisabled(true);
        up.setMessage("No");
        assertEquals(true, up.isDisabled());
        assertEquals("No", up.getMessage());
        config.setUpdate(up);
        assertEquals(up, config.getUpdate());
    }
}
