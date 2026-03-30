package com.exampleinyection.clase2parte2.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppConfigTest {

    @Test
    void testConfig() {
        AppConfig config = new AppConfig();
        
        config.setName("GFTApp");
        assertEquals("GFTApp", config.getName());

        config.setEnvironment("DEV");
        assertEquals("DEV", config.getEnvironment());
        
        config.setPoolSize(50);
        assertEquals(50, config.getPoolSize());

        config.getDatabase().setPassword("mySecurePassword123");
        assertEquals("mySecurePassword123", config.getDatabase().getPassword());
        
        config.getDatabase().setUrl("jdbc:h2:mem:testdb");
        assertEquals("jdbc:h2:mem:testdb", config.getDatabase().getUrl());
        
        AppConfig.DefaultSettings def = new AppConfig.DefaultSettings();
        def.setName("A");
        def.setAge(10);
        assertEquals("A", def.getName());
        assertEquals(10, def.getAge());
        
        config.getCommon().setDefaults(def);
        assertEquals(def, config.getCommon().getDefaults());
        
        AppConfig.PaginationSettings pag = new AppConfig.PaginationSettings();
        pag.setMaxSize(50);
        assertEquals(50, pag.getMaxSize());
        
        config.getCommon().setPagination(pag);
        assertEquals(pag, config.getCommon().getPagination());
        
        AppConfig.UpdateSettings up = new AppConfig.UpdateSettings();
        up.setDisabled(true);
        up.setMessage("No");
        assertEquals(true, up.isDisabled());
        assertEquals("No", up.getMessage());
        
        config.setUpdate(up);
        assertEquals(up, config.getUpdate());
    }

    @Test
    void testSpringContextHelper() {
        SpringContextHelper helper = new SpringContextHelper();
        ApplicationContext context = mock(ApplicationContext.class);
        helper.setApplicationContext(context);
        
        when(context.getBean(String.class)).thenReturn("TestBean");
        
        String result = SpringContextHelper.getBean(String.class);
        assertEquals("TestBean", result);
    }
}
