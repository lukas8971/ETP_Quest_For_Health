package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.enums.mapper.EquipmentTypeMapper;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.persistence.EquipmentDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class EquipmentPersistenceTest {

    @Autowired
    EquipmentDao equipmentDao;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Requesting a not existing equipment should throw NotFoundException")
    public void requestingNotExistingEquipment_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            equipmentDao.getOneById(99999);
        });
    }

    /**
     * Just so that the equipment doesnt have to be created on each run
     * @return a list of all created equipments
     */
    private List<Equipment> createSomeValidTestEquipment(){
        List<Equipment> e = new ArrayList<>();
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(1,2, EquipmentTypeMapper.stringToEnum("head"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(10,20, EquipmentTypeMapper.stringToEnum("head"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(100,200, EquipmentTypeMapper.stringToEnum("head"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(3,4, EquipmentTypeMapper.stringToEnum("arms"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(30,40, EquipmentTypeMapper.stringToEnum("arms"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(300,240, EquipmentTypeMapper.stringToEnum("arms"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(5,6, EquipmentTypeMapper.stringToEnum("torso"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(50,60, EquipmentTypeMapper.stringToEnum("torso"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(500,600, EquipmentTypeMapper.stringToEnum("torso"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(7,8, EquipmentTypeMapper.stringToEnum("legs"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(70,80, EquipmentTypeMapper.stringToEnum("legs"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(700,800, EquipmentTypeMapper.stringToEnum("legs"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(11,22, EquipmentTypeMapper.stringToEnum("right hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(111,222, EquipmentTypeMapper.stringToEnum("right hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(1111,2222, EquipmentTypeMapper.stringToEnum("right hand"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(33,44, EquipmentTypeMapper.stringToEnum("left hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(333,444, EquipmentTypeMapper.stringToEnum("left hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(3333,4444, EquipmentTypeMapper.stringToEnum("left hand"))));
        if (e.size() > 0) return e;
        return null;
    }

    @Test
    @DisplayName("Creating valid equipment should not throw any exception")
    public void creatingValidEquipment_shouldNotThrow() {
        assertDoesNotThrow(() -> {
            List<Equipment> eqs = createSomeValidTestEquipment();
            assertNotNull(eqs);
        });
    }


}
