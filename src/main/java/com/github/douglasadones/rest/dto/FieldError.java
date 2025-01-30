package com.github.douglasadones.rest.dto;

public class FieldError {

    private String field;
    private String name;

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public FieldError(String field, String name) {
        this.field = field;
        this.name = name;
    }

}
