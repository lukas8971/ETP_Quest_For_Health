package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;

public interface QuestService {

    public Quest getOneById(int id) throws NotFoundException;
}
