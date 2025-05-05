package com.floormatt.taskmanagement.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ControllerFactory {
    private final ApplicationContext context;

    @Autowired
    public ControllerFactory(ApplicationContext context) {
        this.context = context;
    }

    public Object create(Class<?> controllerClass) {
        return context.getBean(controllerClass);
    }
}
