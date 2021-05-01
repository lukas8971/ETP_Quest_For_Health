create database if not exists Quest_For_Health_Test;

use Quest_For_Health_Test;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `character_level`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `character_level` ;

CREATE TABLE IF NOT EXISTS `character_level` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `total_strength` INT NOT NULL,
                                                 `needed_exp` INT NOT NULL,
                                                 `level` INT NOT NULL,
                                                 `rank` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`))
    AUTO_INCREMENT = 1,ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `story_chapter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `story_chapter` ;

CREATE TABLE IF NOT EXISTS `story_chapter` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `strength_requirement` INT NOT NULL,
    `prev_chapter` INT NULL,
    `next_chapter` INT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_story_chapter_story_chapter2_idx` (`prev_chapter` ASC),
    INDEX `fk_story_chapter_story_chapter1_idx` (`next_chapter` ASC),
    CONSTRAINT `fk_story_chapter_story_chapter2`
    FOREIGN KEY (`prev_chapter`)
    REFERENCES `story_chapter` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_story_chapter_story_chapter1`
    FOREIGN KEY (`next_chapter`)
    REFERENCES `story_chapter` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    AUTO_INCREMENT = 1,ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
                                      `id` INT NOT NULL AUTO_INCREMENT,
                                      `firstname` VARCHAR(255) NOT NULL,
    `lastname` VARCHAR(255) NOT NULL,
    `character_name` VARCHAR(45) NOT NULL,
    `character_strength` INT NOT NULL,
    `character_level` INT NOT NULL,
    `character_exp` INT NOT NULL,
    `character_gold` INT NOT NULL,
    `password` VARCHAR(512) NOT NULL,
    `email` VARCHAR(255) NULL,
    `story_chapter` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_user_character_level1_idx` (`character_level` ASC),
    INDEX `fk_user_story_chapter1_idx` (`story_chapter` ASC),
    CONSTRAINT `fk_user_character_level1`
    FOREIGN KEY (`character_level`)
    REFERENCES `character_level` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_story_chapter1`
    FOREIGN KEY (`story_chapter`)
    REFERENCES `story_chapter` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    AUTO_INCREMENT = 1,ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `doctor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor` ;

CREATE TABLE IF NOT EXISTS `doctor` (
                                        `id` INT NOT NULL AUTO_INCREMENT,
                                        `firstname` VARCHAR(255) NOT NULL,
    `lastname` VARCHAR(255) NOT NULL,
    `password` VARCHAR(512) NOT NULL,
    `email` VARCHAR(255) NULL,
    PRIMARY KEY (`id`))
    AUTO_INCREMENT = 1,ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `doctor_has_patients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_has_patients` ;

CREATE TABLE IF NOT EXISTS `doctor_has_patients` (
                                                     `doctor` INT NOT NULL,
                                                     `user` INT NOT NULL,
                                                     PRIMARY KEY (`doctor`, `user`),
    INDEX `fk_doctor_has_user_user1_idx` (`user` ASC),
    INDEX `fk_doctor_has_user_doctor_idx` (`doctor` ASC),
    CONSTRAINT `fk_doctor_has_user_doctor`
    FOREIGN KEY (`doctor`)
    REFERENCES `doctor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_doctor_has_user_user1`
    FOREIGN KEY (`user`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quest` ;

CREATE TABLE IF NOT EXISTS `quest` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    `exp_reward` INT NOT NULL,
    `gold_reward` INT NOT NULL,
    `repetition_cycle` TIME NULL,
    PRIMARY KEY (`id`))
    AUTO_INCREMENT = 1,ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `doctor_quest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `doctor_quest` ;

CREATE TABLE IF NOT EXISTS `doctor_quest` (
                                              `id` INT NOT NULL,
                                              `doctor` INT NOT NULL,
                                              `exp_penalty` INT NULL,
                                              `gold_penalty` INT NULL,
                                              PRIMARY KEY (`id`),
    INDEX `fk_doctor_quest_doctor1_idx` (`doctor` ASC),
    CONSTRAINT `fk_doctor_quest_quest1`
    FOREIGN KEY (`id`)
    REFERENCES `quest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_doctor_quest_doctor1`
    FOREIGN KEY (`doctor`)
    REFERENCES `doctor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_accepted_quest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_accepted_quest` ;

CREATE TABLE IF NOT EXISTS `user_accepted_quest` (
                                                     `user` INT NOT NULL,
                                                     `quest` INT NOT NULL,
                                                     `accepted_on` DATE NOT NULL,
                                                     PRIMARY KEY (`user`, `quest`),
    INDEX `fk_user_has_quest_quest1_idx` (`quest` ASC),
    INDEX `fk_user_has_quest_user1_idx` (`user` ASC),
    CONSTRAINT `fk_user_has_quest_user1`
    FOREIGN KEY (`user`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_has_quest_quest1`
    FOREIGN KEY (`quest`)
    REFERENCES `quest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_completed_quest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_completed_quest` ;

CREATE TABLE IF NOT EXISTS `user_completed_quest` (
                                                      `id` INT NOT NULL AUTO_INCREMENT,
                                                      `user` INT NOT NULL,
                                                      `quest` INT NOT NULL,
                                                      `completed_on` DATE NOT NULL,
                                                      PRIMARY KEY (`id`),
    INDEX `fk_user_has_quest_quest2_idx` (`quest` ASC),
    INDEX `fk_user_has_quest_user2_idx` (`user` ASC),
    CONSTRAINT `fk_user_has_quest_user2`
    FOREIGN KEY (`user`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_has_quest_quest2`
    FOREIGN KEY (`quest`)
    REFERENCES `quest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    AUTO_INCREMENT = 1,ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `equipment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `equipment` ;

CREATE TABLE IF NOT EXISTS `equipment` (
                                           `id` INT NOT NULL AUTO_INCREMENT,
                                           `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    `price` INT NOT NULL,
    `strength` INT NOT NULL,
    `type` ENUM('Head', 'Arms', 'Torso', 'Legs', 'Right hand', 'Left hand') NOT NULL,
    PRIMARY KEY (`id`))
    AUTO_INCREMENT = 1,ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user_has_equipment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_has_equipment` ;

CREATE TABLE IF NOT EXISTS `user_has_equipment` (
                                                    `user` INT NOT NULL,
                                                    `equipment` INT NOT NULL,
                                                    PRIMARY KEY (`user`, `equipment`),
    INDEX `fk_user_has_equipment_equipment1_idx` (`equipment` ASC),
    INDEX `fk_user_has_equipment_user1_idx` (`user` ASC),
    CONSTRAINT `fk_user_has_equipment_user1`
    FOREIGN KEY (`user`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_has_equipment_equipment1`
    FOREIGN KEY (`equipment`)
    REFERENCES `equipment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_wears_equipment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_wears_equipment` ;

CREATE TABLE IF NOT EXISTS `user_wears_equipment` (
                                                      `user` INT NOT NULL,
                                                      `equipment` INT NOT NULL,
                                                      PRIMARY KEY (`user`, `equipment`),
    CONSTRAINT `fk_table1_user_has_equipment1`
    FOREIGN KEY (`user` , `equipment`)
    REFERENCES `user_has_equipment` (`user` , `equipment`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
