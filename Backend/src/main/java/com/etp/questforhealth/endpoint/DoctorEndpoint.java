package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.CredentialsDto;
import com.etp.questforhealth.endpoint.dto.DoctorDto;
import com.etp.questforhealth.endpoint.dto.DoctorUserRelationDto;
import com.etp.questforhealth.endpoint.mapper.CredentialsMapper;
import com.etp.questforhealth.endpoint.mapper.DoctorMapper;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(DoctorEndpoint.BASE_URL)
public class DoctorEndpoint {
    static final String BASE_URL = "/doctors";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final CredentialsMapper credentialsMapper;

    @Autowired
    public DoctorEndpoint(DoctorService doctorService, DoctorMapper doctorMapper, CredentialsMapper credentialsMapper){
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.credentialsMapper = credentialsMapper;
    }

    /**
     * Checks if the given email and password are correct.
     * @param credentialsDto for the login.
     * @return a DoctorDto object if the login is valid
     */
    @PostMapping(value="/login")
    public DoctorDto checkLogin(@RequestBody CredentialsDto credentialsDto){
        LOGGER.info("POST " + BASE_URL + "/login");
        try{
            return doctorMapper.entityToDto(doctorService.checkLogin(credentialsMapper.dtoToEntitiy(credentialsDto)));
        } catch (InvalidLoginException e){
            LOGGER.error("Wrong email or password!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password!", e);
        }
    }

    /**
     * Gets all the stored doctors
     * @return a List of all stored DoctorDto objects
     */
    @GetMapping()
    public List<DoctorDto> getAllDoctors(){
        LOGGER.info("GET " + BASE_URL);
        try {
            return doctorMapper.entityToDto(doctorService.getAllDoctors());
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Returns a DoctorDto with the given id.
     * @param id of the doctor to return
     * @return a DoctorDto
     */
    @GetMapping(value="/{id}")
    public DoctorDto getOneById(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/{}",id);
        try{
            return doctorMapper.entityToDto(doctorService.getOneById(id));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Creates a new doctor user relationship
     * @param doctorUserRelationDto to create to
     * @return the created relationship
     */
    @PostMapping(value="/relation")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorUserRelationDto addDoctorUserRelation(@RequestBody DoctorUserRelationDto doctorUserRelationDto){
        LOGGER.info("POST " + BASE_URL + "/relation " + doctorUserRelationDto);
        try{
            if (doctorService.assignNewPatient(doctorUserRelationDto.getDocId(), doctorUserRelationDto.getUserId())) {
                return doctorUserRelationDto;
            } else {
                return null;
            }
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Removes a doctor user relationship
     * @param doc to remove from
     * @param user that should be removed
     * @return true if worked
     */
    @DeleteMapping(value="/relation/{doc}/{user}")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeDoctorUserRelation(@PathVariable("doc") int doc, @PathVariable("user") int user){
        LOGGER.info("DELETE " + BASE_URL + "/relation/{}/{} ", doc, user);
        try{
            return doctorService.removePatient(doc, user);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
