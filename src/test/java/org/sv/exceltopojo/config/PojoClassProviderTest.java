package org.sv.exceltopojo.config;

import org.junit.jupiter.api.Test;
import org.sv.exceltopojo.model.Iris;

import static org.junit.jupiter.api.Assertions.*;

public class PojoClassProviderTest {

    @Test
    public void testGetPojoClass() {
        PojoClassProvider provider = new PojoClassProvider();
        Class<?> pojoClass = provider.getPojoClass();

        assertNotNull(pojoClass);
        assertEquals(Iris.class, pojoClass);
    }
}