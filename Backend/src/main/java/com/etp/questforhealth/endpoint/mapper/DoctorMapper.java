package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.DoctorDto;
import com.etp.questforhealth.entity.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class DoctorMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DoctorDto entityToDto (Doctor doctor){
        LOGGER.trace("entityToDto({})",doctor);
        if(doctor == null) return null;
        return new DoctorDto(doctor.getId(), doctor.getFirstname(), doctor.getLastname(), doctor.getEmail());
    }

    public List<DoctorDto> entityToDto (List<Doctor> doctors){
        LOGGER.trace("entityToDto({})", doctors);
        if(doctors == null) return null;
        List<DoctorDto> doctorsDto = new ArrayList<>();
        for (Doctor doc: doctors) {
            doctorsDto.add(new DoctorDto(doc.getId(), doc.getFirstname(), doc.getLastname(), doc.getEmail()));
        }
        return doctorsDto;
    }

    public Doctor dtoToEntity (DoctorDto doctorDto){
        LOGGER.trace("dtoToEntity({})",doctorDto);
        if(doctorDto == null) return null;
        return new Doctor(doctorDto.getId(), doctorDto.getFirstname(), doctorDto.getLastname(), doctorDto.getEmail());
    }
}
