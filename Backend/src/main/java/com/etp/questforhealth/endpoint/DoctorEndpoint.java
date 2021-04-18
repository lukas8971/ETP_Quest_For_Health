package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.DoctorDto;
import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.endpoint.mapper.DoctorMapper;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(DoctorEndpoint.BASE_URL)
public class DoctorEndpoint {
    static final String BASE_URL = "/doctors";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorEndpoint(DoctorService doctorService, DoctorMapper doctorMapper){
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    /**
     * Checks if the given email and password are correct.
     * @param email of the doctor to login.
     * @param password of the doctor to login.
     * @return a DoctorDto object if the login is valid
     */
    @PostMapping(value="/session")
    public DoctorDto checkLogin(@PathVariable("email") String email, @PathVariable("password") String password){
        LOGGER.info("GET " + BASE_URL + "/session");
        try{
            return doctorMapper.entityToDto(doctorService.checkLogin(email, password));
        } catch (InvalidLoginException e){
            LOGGER.error("Wrong email or password!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password!");
        }
    }
}
