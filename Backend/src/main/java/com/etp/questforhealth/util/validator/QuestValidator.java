package com.etp.questforhealth.util.validator;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

@Component
public class QuestValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int MAX_DESCRIPTION_LENGTH = 10000;
    public QuestValidator(){}

    /**
     * Makes sure the quest is valid e.g. no invalid characters in the strings.
     * @param quest The quest to be validated
     * @throws ValidationException if not valid
     */
    public void validateQuest(Quest quest) throws ValidationException {
        LOGGER.trace("validateQuest({})",quest);
        validateName(quest.getName());
        validateDescription(quest.getDescription());
        validateRepetitionCycle(quest.getRepetition_cycle());
        validateExpReward(quest.getExp_reward());
        validateGoldReward(quest.getGold_reward());
        validateExpPenalty(quest.getExp_penalty());
        validateGoldPenalty(quest.getGold_penalty());
    }

    private void validateName(String name) {
        LOGGER.trace("validateName({})",name);
            if (name == null || name.isBlank()) {
                throw new ValidationException("The name must not be empty!");
            }
            if (name.length() > 255) {
                throw new ValidationException("The name of must not be longer than 255 characters!");
            }
            // remove all allowed Characters, then check if the string is empty
            name = removeAllowedCharacters(name);
            if (!(name.isEmpty())) {
                throw new ValidationException("The name must consist only of letters, numbers, whitespaces and (. , ! ?)!");
            }
    }
    private void validateDescription(String description) {
        LOGGER.trace("validateDescription({})",description);
            if (description == null || description.isBlank()) {
                throw new ValidationException("The description must not be empty!");
            }

            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                throw new ValidationException("The description must not be longer than "+ MAX_DESCRIPTION_LENGTH + " characters!");
            }
            // remove all allowed Characters, then check if the string is empty
            description = removeAllowedCharacters(description);
            if (!(description.isEmpty())) {
                throw new ValidationException("The description must consist only of letters, numbers, whitespaces and (. , ! ?)!");
            }
        }


    private void validateRepetitionCycle(Duration d) {
        LOGGER.trace("validateRepetitionCycle({})",d);
        if(d.isNegative()) throw new ValidationException("The repetition cycle must be a positive time duration!");
    }

    private void validateExpReward(int exp_reward){
        if(exp_reward < 0) throw new ValidationException("The experience reward must be positive!");
    }

    private void validateGoldReward(int gold_reward){
        if(gold_reward < 0) throw new ValidationException("The gold reward must be positive!");
    }

    private void validateExpPenalty(int exp_penalty){
        if(exp_penalty < 0) throw new ValidationException("The experience penalty must be positive!");
    }

    private void validateGoldPenalty(int gold_penalty){
        if(gold_penalty < 0) throw new ValidationException("The gold penalty must be positive!");
    }


    private String removeAllowedCharacters(String s) {
        LOGGER.trace("removeAllowedCharacters({})", s);
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\\.", "");
        s = s.replaceAll(",", "");
        s = s.replaceAll("!", "");
        s = s.replaceAll("\\?", "");
        s = s.replaceAll("ä", "");
        s = s.replaceAll("Ä", "");
        s = s.replaceAll("ö", "");
        s = s.replaceAll("Ö", "");
        s = s.replaceAll("ü", "");
        s = s.replaceAll("Ü", "");
        s = s.replaceAll("ß", "");
        s = s.replaceAll("[a-zA-Z0-9]", "");
        return s;
    }

}
