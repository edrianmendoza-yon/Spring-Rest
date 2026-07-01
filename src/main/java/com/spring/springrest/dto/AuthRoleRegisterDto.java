package com.spring.springrest.dto;

public class AuthRoleRegisterDto {
    private String name;

    public AuthRoleRegisterDto(String name) {
        //super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
