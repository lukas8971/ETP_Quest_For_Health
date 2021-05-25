package com.etp.questforhealth.entity;

import java.util.Objects;

public class Picture {
    String base64;

    public Picture() {}

    public Picture(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(base64, picture.base64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base64);
    }

    @Override
    public String toString() {
        return "Picture{" +
                "base64='" + base64 + '\'' +
                '}';
    }
}
