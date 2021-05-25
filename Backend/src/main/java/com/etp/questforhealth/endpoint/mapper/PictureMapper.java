package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.PictureDto;
import com.etp.questforhealth.entity.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class PictureMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PictureDto entityToDto (Picture entity) {
        LOGGER.trace("entityToDto({})",entity);
        return new PictureDto(entity.getBase64());
    }
}
