package org.sv.exceltopojo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sv.exceltopojo.service.ExcelProcessingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
@Tag(name = "Excel Processing", description = "APIs for uploading Excel files and converting them to POJOs")
public class ExcelToPojoController {

    private final ExcelProcessingService excelProcessingService;

    public ExcelToPojoController(ExcelProcessingService excelProcessingService) {
        this.excelProcessingService = excelProcessingService;
    }

    @Operation(
            summary = "Upload an Excel file and convert it to POJOs",
            description = "Accepts an Excel file containing Iris dataset records and returns a list of POJOs."
    )
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(
            @Parameter(description = "Excel file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            List<Object> pojoList = excelProcessingService.processExcelFile(file);
            return ResponseEntity.ok(pojoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while processing the file.", "error", e.getMessage()));
        }
    }
}