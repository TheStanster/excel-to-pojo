package org.sv.exceltopojo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
            description = "Accepts an Excel file with .xls or .xlsx extension containing Iris dataset records and returns a list of POJOs."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed the file"),
            @ApiResponse(responseCode = "400", description = "Invalid file type"),
            @ApiResponse(responseCode = "413", description = "Uploaded file exceeds maximum file size."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(
            @Parameter(description = "Excel file(.xlsx/.xls) to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null || !(filename.endsWith(".xls") || filename.endsWith(".xlsx"))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid file type. Please upload an Excel file with .xls or .xlsx extension."));
            }

            List<Object> pojoList = excelProcessingService.processExcelFile(file);
            return ResponseEntity.ok(pojoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while processing the file.", "error", e.getMessage()));
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("message", "File too large. Maximum allowed size is 10MB.", "error", e.getMessage()));
    }
}