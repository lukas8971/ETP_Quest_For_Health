package com.etp.questforhealth.entity;

import com.etp.questforhealth.endpoint.dto.CredentialsDto;

import java.util.Objects;

public class Credentials {
    private String email;
    private String password;

    public Credentials(){}

    public Credentials(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credentials credentials = (Credentials) o;
        return email == credentials.email &&
                Objects.equals(password, credentials.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "email=" + email +
                ", password='" + password + "'" +
                '}';
    }
}
