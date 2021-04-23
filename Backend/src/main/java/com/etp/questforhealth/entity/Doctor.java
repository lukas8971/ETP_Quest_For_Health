package com.etp.questforhealth.entity;

import java.util.Objects;

public class Doctor {
    private int id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;

    public Doctor(){}

    public Doctor(int id, String firstname, String lastname, String email){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public Doctor(int id, String firstname, String lastname, String email, String password){
        this(id, firstname, lastname, email);
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id &&
                Objects.equals(firstname, doctor.firstname) &&
                Objects.equals(lastname, doctor.lastname)  &&
                Objects.equals(email, doctor.email) &&
                Objects.equals(password, doctor.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", firstname='" + firstname + "'" +
                ", lastname='" + lastname + "'" +
                ", email='" + email + "'" +
                ", password='" + password + "'" +
                '}';
    }
}
