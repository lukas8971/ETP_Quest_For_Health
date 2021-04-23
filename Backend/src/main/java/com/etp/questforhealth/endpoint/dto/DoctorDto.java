package com.etp.questforhealth.endpoint.dto;

import java.util.Objects;

public class DoctorDto {
    private int id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;

    public DoctorDto(){}

    public DoctorDto(int id, String firstname, String lastname, String email){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public DoctorDto(int id, String firstname, String lastname, String email, String password){
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
        DoctorDto doctorDto = (DoctorDto) o;
        return id == doctorDto.id &&
                Objects.equals(firstname, doctorDto.firstname) &&
                Objects.equals(lastname, doctorDto.lastname)  &&
                Objects.equals(email, doctorDto.email) &&
                Objects.equals(password, doctorDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password);
    }

    @Override
    public String toString() {
        return "DoctorDto{" +
                "id=" + id +
                ", firstname='" + firstname + "'" +
                ", lastname='" + lastname + "'" +
                ", email='" + email + "'" +
                ", password='" + password + "'" +
                '}';
    }
}
