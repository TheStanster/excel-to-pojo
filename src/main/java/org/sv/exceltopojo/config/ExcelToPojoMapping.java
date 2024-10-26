package org.sv.exceltopojo.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class ExcelToPojoMapping {

    private final Map<String, String> columnToFieldMap = new HashMap<>();

    public ExcelToPojoMapping() {
        columnToFieldMap.put("sepal_length", "sepalLength");
        columnToFieldMap.put("sepal_width", "sepalWidth");
        columnToFieldMap.put("petal_length", "petalLength");
        columnToFieldMap.put("petal_width", "petalWidth");
        columnToFieldMap.put("species", "species");
    }

}