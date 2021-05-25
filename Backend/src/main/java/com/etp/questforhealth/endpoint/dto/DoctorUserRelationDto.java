package com.etp.questforhealth.endpoint.dto;

import java.util.Objects;

public class DoctorUserRelationDto {
    private Integer docId;
    private Integer userId;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public DoctorUserRelationDto(Integer docId, Integer userId) {
        this.docId = docId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorUserRelationDto that = (DoctorUserRelationDto) o;
        return docId.equals(that.docId) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, userId);
    }

    @Override
    public String toString() {
        return "DoctorUserRelationDto{" +
                "docId=" + docId +
                ", userId=" + userId +
                '}';
    }
}
