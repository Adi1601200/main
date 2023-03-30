package com.project.learn.main.entity;

public enum RoleType {
    MENTEE("MENTEE"),MENTOR("MENTOR");
    private String value;
    RoleType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
