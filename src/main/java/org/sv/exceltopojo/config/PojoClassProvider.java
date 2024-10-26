package org.sv.exceltopojo.config;

import org.springframework.stereotype.Component;
import org.sv.exceltopojo.model.Iris;

@Component
public class PojoClassProvider {
    public Class<?> getPojoClass() {
        return Iris.class;
    }
}