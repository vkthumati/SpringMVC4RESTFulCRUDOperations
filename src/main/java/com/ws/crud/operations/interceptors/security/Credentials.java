package com.ws.crud.operations.interceptors.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class Credentials {
    private String username = "";
    private String password = "";
    private List<String> scope;

    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password;
    }

    public Credentials(String username, String password, List<String> scope) {
        this.username = username.toLowerCase();
        this.password = password;
        this.scope = scope;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
