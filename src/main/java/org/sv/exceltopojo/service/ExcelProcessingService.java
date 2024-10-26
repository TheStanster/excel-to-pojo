package org.sv.exceltopojo.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sv.exceltopojo.config.ExcelToPojoMapping;
import org.sv.exceltopojo.config.PojoClassProvider;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class ExcelProcessingService {

    private final PojoClassProvider pojoClassProvider;
    private final ExcelToPojoMapping mapping;

    public ExcelProcessingService(PojoClassProvider pojoClassProvider, ExcelToPojoMapping mapping) {
        this.pojoClassProvider = pojoClassProvider;
        this.mapping = mapping;
    }

    public List<Object> processExcelFile(MultipartFile file) {
        List<Object> pojoList = new ArrayList<>();
        Map<String, String> columnToFieldMap = mapping.getColumnToFieldMap();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook;
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else if (file.getOriginalFilename().endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                throw new IllegalArgumentException("The file is not an Excel file");
            }

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Read header row
            Row headerRow = rowIterator.next();
            Map<Integer, String> columnIndexToFieldName = new HashMap<>();

            for (Cell cell : headerRow) {
                String columnName = cell.getStringCellValue();
                String fieldName = columnToFieldMap.get(columnName);
                if (fieldName != null) {
                    columnIndexToFieldName.put(cell.getColumnIndex(), fieldName);
                }
            }

            Class<?> pojoClass = pojoClassProvider.getPojoClass();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Object pojoInstance = pojoClass.getDeclaredConstructor().newInstance();

                for (Cell cell : row) {
                    String fieldName = columnIndexToFieldName.get(cell.getColumnIndex());
                    if (fieldName != null) {
                        Field field = pojoClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object cellValue = getCellValue(cell, field.getType());
                        field.set(pojoInstance, cellValue);
                    }
                }
                pojoList.add(pojoInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Handle exceptions appropriately
            throw new RuntimeException(e);
        }
        return pojoList;
    }

    private Object getCellValue(Cell cell, Class<?> fieldType) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (fieldType == int.class || fieldType == Integer.class) {
                    return (int) cell.getNumericCellValue();
                } else if (fieldType == double.class || fieldType == Double.class) {
                    return cell.getNumericCellValue();
                } else if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                break;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
        return null;
    }
}