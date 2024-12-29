package com.example.StudentCRUD.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;

    private String name;
    private String password;
    private String email;
    private String dob;
    private Integer age;
    private List<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UserDTO(Long id, String name, String password, String email, String dob, Integer age, List<String> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.age = age;
        this.roles = roles;
    }

    public UserDTO(String name, String password, String email, String dob, Integer age, List<String> roles) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.age = age;
        this.roles = roles;
    }

    public UserDTO() {
    }

}
