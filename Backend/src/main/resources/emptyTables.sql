use Quest_For_Health;

SET FOREIGN_KEY_CHECKS=0;

DELETE FROM character_level;
DELETE FROM doctor;
DELETE FROM doctor_has_patients;
DELETE FROM doctor_quest;
DELETE FROM equipment;
DELETE FROM quest;
DELETE FROM story_chapter;
DELETE FROM user;
DELETE FROM user_accepted_quest;
DELETE FROM user_completed_quest;
DELETE FROM user_has_equipment;
DELETE FROM user_wears_equipment;

SET FOREIGN_KEY_CHECKS=1;
