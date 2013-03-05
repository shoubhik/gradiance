package com.ncsu.gradiance.domain;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
    @NotNull
    private String uid;
    @NotNull
    private String password;

    private String uname;

    private String roleName;

    private Map<String, String> mappedPages;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    private List<String> roles;

    private String homePage;

    public User(){
        this.roles = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRole() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role) ;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public Map<String, String> getMappedPages() {
        return mappedPages;
    }

    public void setMappedPages(Map<String, String> mappedPages) {
        this.mappedPages = mappedPages;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
