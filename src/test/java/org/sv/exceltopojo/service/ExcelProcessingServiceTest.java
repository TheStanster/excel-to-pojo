package org.sv.exceltopojo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.sv.exceltopojo.config.ExcelToPojoMapping;
import org.sv.exceltopojo.config.PojoClassProvider;
import org.sv.exceltopojo.model.Iris;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExcelProcessingServiceTest {

    @Mock
    private PojoClassProvider pojoClassProvider;

    @Mock
    private ExcelToPojoMapping mapping;

    private ExcelProcessingService service;

    @BeforeEach
    public void setUp() {
        service = new ExcelProcessingService(pojoClassProvider, mapping);
    }

    @Test
    public void testProcessExcelFile_ValidInput() throws Exception {
        when(pojoClassProvider.getPojoClass()).thenReturn((Class) Iris.class);
        when(mapping.getColumnToFieldMap()).thenReturn(Map.of(
                "sepal_length", "sepalLength",
                "sepal_width", "sepalWidth",
                "petal_length", "petalLength",
                "petal_width", "petalWidth",
                "species", "species"
        ));

        ClassPathResource fileResource = new ClassPathResource("iris.xlsx");
        InputStream inputStream = fileResource.getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "iris.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                inputStream);

        List<Object> result = service.processExcelFile(multipartFile);

        assertNotNull(result);
        assertEquals(150, result.size());

        Iris firstIris = (Iris) result.get(0);
        assertEquals(5.1, firstIris.getSepalLength());
        assertEquals(3.5, firstIris.getSepalWidth());
        assertEquals("setosa", firstIris.getSpecies());
    }

    @Test
    public void testProcessExcelFile_InvalidFile() {
        // Mock dependencies as before
        PojoClassProvider pojoClassProvider = mock(PojoClassProvider.class);
        ExcelToPojoMapping mapping = mock(ExcelToPojoMapping.class);

        // Create service instance
        ExcelProcessingService service = new ExcelProcessingService(pojoClassProvider, mapping);

        // Create an invalid file (e.g., a text file)
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Invalid content".getBytes());

        // Expect an exception
        Exception exception = assertThrows(RuntimeException.class, () -> service.processExcelFile(multipartFile));

        String expectedMessage = "Invalid file type";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}