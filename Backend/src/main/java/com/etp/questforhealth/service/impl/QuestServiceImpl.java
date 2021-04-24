package com.etp.questforhealth.service.impl;


import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.service.QuestService;
import com.etp.questforhealth.util.validator.QuestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class QuestServiceImpl implements QuestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QuestDao questDao;
    private final QuestValidator questValidator;

    @Autowired
    public QuestServiceImpl(QuestDao questDao, QuestValidator questValidator){
        this.questDao = questDao;
        this.questValidator = questValidator;
    }

    @Override
    public Quest getOneById(int id) throws NotFoundException {
        LOGGER.trace("getOneById({})",id);
        if(id < 0) throw new NotFoundException("The Quest ID must be greater than 0.");
        return questDao.getOneById(id);
    }

    @Override
    public Quest createQuest(Quest quest) throws ValidationException {
        LOGGER.trace("createQeust({})",quest);
        questValidator.validateQuest(quest);
        return questDao.createQuest(quest);
    }
}
