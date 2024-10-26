package org.sv.exceltopojo.config;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelToPojoMappingTest {

    @Test
    public void testGetColumnToFieldMap() {
        ExcelToPojoMapping mapping = new ExcelToPojoMapping();
        Map<String, String> columnToFieldMap = mapping.getColumnToFieldMap();

        assertNotNull(columnToFieldMap);
        assertEquals("sepalLength", columnToFieldMap.get("sepal_length"));
        assertEquals("sepalWidth", columnToFieldMap.get("sepal_width"));
        assertEquals("petalLength", columnToFieldMap.get("petal_length"));
        assertEquals("petalWidth", columnToFieldMap.get("petal_width"));
        assertEquals("species", columnToFieldMap.get("species"));
    }
}