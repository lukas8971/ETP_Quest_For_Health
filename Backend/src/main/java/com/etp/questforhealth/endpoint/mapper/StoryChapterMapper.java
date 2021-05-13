package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.StoryChapterDto;
import com.etp.questforhealth.entity.StoryChapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class StoryChapterMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public StoryChapterDto entityToDto(StoryChapter storyChapter) {
        LOGGER.trace("entityToDto({})", storyChapter);
        return new StoryChapterDto(
                storyChapter.getId(),
                storyChapter.getName(),
                storyChapter.getDescription(),
                storyChapter.getStrength_requirement(),
                storyChapter.getPrev_chapter(),
                storyChapter.getNext_chapter()
        );
    }

    public StoryChapter dtoToEntity(StoryChapterDto storyChapterDto) {
        LOGGER.trace("dtoToEntity({})", storyChapterDto);
        return new StoryChapter(
                storyChapterDto.getId(),
                storyChapterDto.getName(),
                storyChapterDto.getDescription(),
                storyChapterDto.getStrength_requirement(),
                storyChapterDto.getPrev_chapter(),
                storyChapterDto.getNext_chapter()
        );
    }

    public List<StoryChapterDto> entityToDto(List<StoryChapter> storyChapterList) {
        LOGGER.trace("entityToDto({})", storyChapterList);
        if (storyChapterList.size() == 0) return null;
        List<StoryChapterDto> storyChapterDtoList = new ArrayList<>();
        for (StoryChapter sc: storyChapterList) {
            storyChapterDtoList.add(entityToDto(sc));
        }
        return storyChapterDtoList;
    }
}
