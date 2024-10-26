package org.sv.exceltopojo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExcelToPojoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadXlsFile() throws Exception {
        ClassPathResource fileResource = new ClassPathResource("iris.xls");

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "iris.xls",
                "application/vnd.ms-excel",
                fileResource.getInputStream());

        mockMvc.perform(multipart("/api/excel/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(150));
    }

    @Test
    public void testUploadXlsxFile() throws Exception {
        ClassPathResource fileResource = new ClassPathResource("iris.xlsx");

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "iris.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                fileResource.getInputStream());

        mockMvc.perform(multipart("/api/excel/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(150));
    }

    @Test
    public void testUploadInvalidFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Invalid content".getBytes());

        mockMvc.perform(multipart("/api/excel/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid file type. Please upload an Excel file with .xls or .xlsx extension."));
    }
}
