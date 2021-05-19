package com.etp.questforhealth.util;


import com.etp.questforhealth.entity.*;
import com.etp.questforhealth.entity.enums.mapper.EquipmentTypeMapper;
import com.etp.questforhealth.exception.NotEnoughGoldException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.DoctorDao;
import com.etp.questforhealth.persistence.EquipmentDao;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.persistence.StoryChapterDao;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QuestDao questDao;
    private final DoctorDao doctorDao;
    private final UserDao userDao;
    private final EquipmentDao equipmentDao;
    private final StoryChapterDao storyDao;

    @Autowired
    public Validator(QuestDao questDao, DoctorDao doctorDao, UserDao userDao, EquipmentDao equipmentDao, StoryChapterDao storyDao){
        this.questDao = questDao;
        this.doctorDao = doctorDao;
        this.userDao = userDao;
        this.equipmentDao = equipmentDao;
        this.storyDao = storyDao;
    }
  
      public void validateNewUser (User user){
        LOGGER.trace("validateNewUser({})", user.toString());
        String errorMsg = "";
        if(user.getFirstname() == null || user.getFirstname().isBlank()){
            errorMsg += "You must enter a firstname. \n";
        }
        if(user.getLastname() == null || user.getLastname().isBlank()){
            errorMsg += "You must enter a lastname. \n";
        }
        if(user.getCharacterName() == null || user.getCharacterName().isBlank()){
            errorMsg += "You must enter a character-name. \n";
        }
        if(user.getPassword() == null || user.getPassword().isBlank()){
            errorMsg += "You must enter a password. \n";
        }
        if(!errorMsg.isBlank()){
            throw new ValidationException(errorMsg);
        }
    }

    /**
     * Checks if a doctor user relationship is given
     * @param doctor id of the doctor
     * @param user id of the user
     */
    public void validateDoctorUserRelation(int doctor, int user){
        LOGGER.trace("validateDoctorUserRelation({}, {})", doctor, user);
        validateExistingDoctor(doctor);
        validateExistingUser(user);
        if (!doctorDao.checkIfDoctorUserRelationshipExists(doctor, user)){
            throw new ValidationException("User " + user + " is not under treatment of doctor " + doctor);
        }
    }

    /**
     * Checks if an AcceptedQuest is valid an not already accepted by a user
     * @param acceptedQuest to be added to a user
     */
    public void validateAcceptedQuest(AcceptedQuest acceptedQuest){
        LOGGER.trace("validateAcceptedQuest({})", acceptedQuest);
        validateExistingQuest(acceptedQuest.getQuest());
        validateExistingUser(acceptedQuest.getUser());
        if (questDao.checkIfQuestAlreadyAccepted(acceptedQuest)) throw new ValidationException("User already has this quest (accepted or in repetition)");
    }

    /**
     * Checks if a user is able to be assigned to a doctor quest
     * @param acceptedQuest the quest to be assigned
     */
    public void validateAcceptedDoctorQuest(AcceptedQuest acceptedQuest) {
        Integer doctorId = questDao.getDoctorIdOfQuest(acceptedQuest.getQuest());
        if (doctorId == null) throw new ValidationException("Requested quest is not a doctor created quest!");
        boolean rel = doctorDao.checkIfDoctorUserRelationshipExists(doctorId, acceptedQuest.getUser());
        if (!rel) throw new ValidationException("User is not in treatment at this doctor!");
    }

    /**
     * Checks if a doctor quest is assigned to a user
     * And if the doctor user relationship exists
     * @param quest id of the quest
     * @param user id of the user
     */
    public void validateDeleteDoctorQuest(int quest, int user){
        validateExistingUser(user);
        Integer doctorId = questDao.getDoctorIdOfQuest(quest);
        if (doctorId == null) throw new ValidationException("Quest to remove is not a doctor quest!");
        boolean rel = doctorDao.checkIfDoctorUserRelationshipExists(doctorId, user);
        if (!rel) throw new ValidationException("User is not in treatment at this doctor!");
        boolean accepted = questDao.checkIfQuestAlreadyAccepted(new AcceptedQuest(quest, user, null));
        if (!accepted) throw new ValidationException("Can not remove not accepted quest!");
    }

    /**
     * Validates an existing user via its id
     * @param user id of the user to check
     */
    public void validateExistingUser(int user){
        LOGGER.trace("validateUser({})", user);
        if (user < 0) throw new IllegalArgumentException("ID of an user has to be greater than 0");
        User u = userDao.getOneById(user);
        if (u == null) throw new ValidationException("User does not exist!");
    }

    /**
     * Validates an existing doctor via its id
     * @param doctor id of the doctor to check
     */
    public void validateExistingDoctor(int doctor){
        LOGGER.trace("validateDoctor({})", doctor);
        if (doctor < 0) throw new IllegalArgumentException("ID of a doctor has to be greater than 0");
        Doctor d = doctorDao.getOneById(doctor);
        if (d == null) throw new ValidationException("Doctor does not exist!");
    }

    /**
     * Validates an existing quest via its id
     * @param quest id of the quest to validate
     */
    public void validateExistingQuest(int quest){
        LOGGER.trace("validateQuest({})", quest);
        if (quest < 0) throw new IllegalArgumentException("ID of a Quest has to be greater than 0");
        Quest q = questDao.getOneById(quest);
        if (q == null) throw new ValidationException("Quest does not exist!");
    }

    /**
     * Validates an existing equipment via its id
     * @param equipment id of the equipment to check
     */
    public void validateExistingEquipment(int equipment){
        LOGGER.trace("validateExistingEquipment({})", equipment);
        if (equipment < 0) throw new IllegalArgumentException("ID of an equipment has to be greater than 0");
        Equipment e = equipmentDao.getOneById(equipment);
        if (e == null) throw new ValidationException("Equipment does not exist!");
    }

    /**
     * Validates the equipment type and user
     * @param user to validate
     * @param type to validate
     */
    public void validateEquipmentByTypeAndId(int user, String type){
        LOGGER.trace("validateEquipmentByTypeAndId({}, {})", user, type);
        validateExistingUser(user);
        EquipmentTypeMapper.stringToEnum(type);
    }

    /**
     * Validates if a user can buy a specific equipment item
     * @param userEquipment the object with the userId and equipmentId
     */
    public void validateBuyEquipment(UserEquipment userEquipment){
        LOGGER.trace("validateBuyEquipment({})", userEquipment);
        validateExistingUser(userEquipment.getUserId());
        validateExistingEquipment(userEquipment.getEquipmentId());
        User u = userDao.getOneById(userEquipment.getUserId());
        Equipment e = equipmentDao.getOneById(userEquipment.getEquipmentId());
        List<UserEquipment> allUserEquipments = equipmentDao.getAllUserEquipments(userEquipment.getUserId());
        for (UserEquipment ue: allUserEquipments) {
            if (ue.getEquipmentId() == userEquipment.getEquipmentId()) throw new ValidationException("You already own this item!");
        }
        if (u.getCharacterGold() < e.getPrice()) throw new NotEnoughGoldException("Not enough gold to buy the equipment! You need " + (e.getPrice() - u.getCharacterGold()) + " more gold.");
    }

    /**
     * Checks if an item is allowed to be worn by a character
     * @param id of the user that wants to wear the equipment
     * @param equipment that should be worn
     */
    public void validateEquipItem(int id, Equipment equipment){
        LOGGER.trace("validateEquipItem({}, {})", id, equipment);
        validateExistingUser(id);
        validateExistingEquipment(equipment.getId());
        if (!equipmentDao.checkIfUserOwnsEquipment(id, equipment.getId())) throw new ValidationException("You do not own that equipment!");
    }

    /**
     * Checks if an item is allowed to be unworn by a character
     * @param id of the user that wants to unwear the equipment
     * @param equipment that should be unworn
     */
    public void validateUnequipItem(int id, Equipment equipment){
        LOGGER.trace("validateUnequipItem({}, {})", id, equipment);
        validateEquipItem(id, equipment);
        Equipment worn = equipmentDao.getWornEquipmentByTypeAndUser(id, equipment.getType());
        if (worn != null) {
            if (worn.getId() != equipment.getId()) throw new ValidationException("Can not unequip not wearing equipment item!");
        }
    }

    /**
     * Checks if a new equipment is valid
     * @param equipment that should be checked for validity
     */
    public void validateNewEquipment(Equipment equipment){
        LOGGER.trace("validateNewEquipment({})", equipment);
        //if (equipment == null) throw new ValidationException("Equipment has to exist!");
        if (equipment.getPrice() < 0) throw new ValidationException("Equipment price can not be negative!");
        if (equipment.getStrength() < 0) throw new ValidationException("Equipment strength can not be negative!");
        if (equipment.getName() == null) throw new ValidationException("Equipment has to have a name!");
        if (equipment.getName().trim().length() == 0) throw new ValidationException("Equipment name has to contain characters!");
    }

    /**
     * Checks if the amount of gold change is valid for a user
     * @param userId that gets or loses gold
     * @param changeValue the amount of gold that is going to change (positive or negative!)
     */
    public void validateUserGold(int userId, int changeValue){
        LOGGER.trace("validateUserGold({}, {})", userId, changeValue);
        validateExistingUser(userId);
        User u = userDao.getOneById(userId);
        if (u.getCharacterGold() + changeValue < 0) throw new ValidationException("Can not get a negative amount of gold!");
    }

    /**
     * Checks if a new doctor user relatinoship is valid
     * @param doctorId id of the doctor
     * @param userId id of the user
     */
    public void validateNewUserDoctorRelationship(int doctorId, int userId) {
        LOGGER.trace("validateNewUserDoctorRelationship({}, {})", doctorId, userId);
        validateExistingDoctor(doctorId);
        validateExistingUser(userId);
        boolean alreadyExists = doctorDao.checkIfDoctorUserRelationshipExists(doctorId, userId);
        if (alreadyExists) throw new ValidationException("This patient is already treated by the doctor!");
    }

    /**
     * Checks if a story chapter is valid
     * @param storyChapter that should be checked
     * @param user that requested
     */
    public void validateStoryChapter(StoryChapter storyChapter, User user) {
        LOGGER.trace("validateStoryChapter({}, {})", storyChapter, user);
        validateExistingUser(user.getId());
        if (storyChapter.getId() < 0) throw new ValidationException("Id of a story chapter can not be smaller than 0!");
        StoryChapter sc = storyDao.getOneById(storyChapter.getId());
        User u = userDao.getOneById(user.getId());
        List<Equipment> equipments = equipmentDao.getWornEquipmentFromUserId(u.getId());
        int st = u.getCharacterStrength();
        for (Equipment e: equipments) {
            st += e.getStrength();
        }
        if (sc.getStrength_requirement() > st) throw new ValidationException("The strength requirements for this story is higher than your strength!");
    }

    /**
     * Checks if the previous story chapter can be loaded
     * @param storyChapter that should be checked
     * @param user that requested
     */
    public void validatePrevStoryChapter(StoryChapter storyChapter, User user) {
        LOGGER.trace("validatePrevStoryChapter({}, {})", storyChapter, user);
        if (storyChapter.getPrev_chapter() == null) throw new ValidationException("This is already the first chapter!");
        validateStoryChapter(storyDao.getOneById(storyChapter.getPrev_chapter()), user);
    }

    /**
     * Checks if the next story chapter can be loaded
     * @param storyChapter that should be checked
     */
    public void validateNextStoryChapter(StoryChapter storyChapter) {
        LOGGER.trace("validatePrevStoryChapter({})", storyChapter);
        if (storyChapter.getNext_chapter() == null) throw new ValidationException("This is already the last chapter!");
    }

    /**
     * Checks if the previous story chapter can be loaded
     * @param storyChapter that should be checked
     */
    public void validatePrevStoryChapter(StoryChapter storyChapter) {
        LOGGER.trace("validatePrevStoryChapter({})", storyChapter);
        if (storyChapter.getPrev_chapter() == null) throw new ValidationException("This is the first chapter!");
    }

    /**
     * Checks for the next chapter of a user
     * @param user that requested
     */
    public void validateNextStoryChapter(User user) {
        LOGGER.trace("validateNextStoryChapter({})", user);
        User u = userDao.getOneById(user.getId());
        StoryChapter sc = storyDao.getOneById(u.getStoryChapter());
        if (sc.getNext_chapter() == null) throw new ValidationException("You already reached the last chapter!");
        StoryChapter next = storyDao.getOneById(sc.getNext_chapter());
        List<Equipment> equipments = equipmentDao.getWornEquipmentFromUserId(u.getId());
        int st = u.getCharacterStrength();
        for (Equipment e: equipments) {
            st += e.getStrength();
        }
        if (st < next.getStrength_requirement()) {
            throw new ValidationException("You do not have enough strength to get to the next chapter!");
        }
    }

    /**
     * Checks for the next chapter of a user
     * @param user that requested
     */
    public void validateNextStoryInfo(User user) {
        LOGGER.trace("validateNextStoryInfo({})", user);
        StoryChapter sc = storyDao.getOneById(user.getStoryChapter());
        if (sc.getNext_chapter() == null) throw new ValidationException("You already reached the last chapter!");
        StoryChapter next = storyDao.getOneById(sc.getNext_chapter());
        if (next == null) throw new ValidationException("There is no next level");
    }

    /**
     * Checks for the next chapter of a user
     * @param user that requested
     */
    public void validateNextUserStoryChapter(User user) {
        LOGGER.trace("validateNextUserStoryChapter({})", user);
        User u = userDao.getOneById(user.getId());
        StoryChapter sc = storyDao.getOneById(user.getStoryChapter());
        if (sc.getNext_chapter() == null) throw new ValidationException("You already reached the last chapter!");
        StoryChapter next = storyDao.getOneById(sc.getNext_chapter());
    }

    public void validatePrevStoryChapter(User user) {
        LOGGER.trace("validatePrevStoryChapter({})", user);
        User u = userDao.getOneById(user.getId());
        StoryChapter sc = storyDao.getOneById(user.getStoryChapter());
        if (sc.getPrev_chapter() == null) throw new ValidationException("This is the first chapter!");
    }
}
