package com.etp.questforhealth.endpoint.dto;

import java.util.Objects;

public class PictureDto {
    String base64;

    public PictureDto() {}

    public PictureDto(String base64) {
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
        PictureDto that = (PictureDto) o;
        return Objects.equals(base64, that.base64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base64);
    }

    @Override
    public String toString() {
        return "PictureDto{" +
                "base64='" + base64 + '\'' +
                '}';
    }
}
