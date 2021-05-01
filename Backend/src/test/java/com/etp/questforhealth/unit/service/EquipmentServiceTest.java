package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.entity.enums.EquipmentType;
import com.etp.questforhealth.entity.enums.mapper.EquipmentTypeMapper;
import com.etp.questforhealth.exception.NotEnoughGoldException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.EquipmentDao;
import com.etp.questforhealth.service.EquipmentService;
import com.etp.questforhealth.service.UserService;
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
public class EquipmentServiceTest {

    @Autowired
    EquipmentService equipmentService;
    @Autowired
    EquipmentDao equipmentDao;
    @Autowired
    UserService userService;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    /**
     * Just so that the equipment doesnt have to be created on each run
     * @return a list of all created equipments
     */
    private List<Equipment> createSomeValidTestEquipment(){
        List<Equipment> e = new ArrayList<>();
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(1,2, EquipmentTypeMapper.stringToEnum("head"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(10,20, EquipmentTypeMapper.stringToEnum("head"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(100,200, EquipmentTypeMapper.stringToEnum("head"))));

        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(3,4, EquipmentTypeMapper.stringToEnum("arms"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(30,40, EquipmentTypeMapper.stringToEnum("arms"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(300,240, EquipmentTypeMapper.stringToEnum("arms"))));

        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(5,6, EquipmentTypeMapper.stringToEnum("torso"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(50,60, EquipmentTypeMapper.stringToEnum("torso"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(500,600, EquipmentTypeMapper.stringToEnum("torso"))));

        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(7,8, EquipmentTypeMapper.stringToEnum("legs"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(70,80, EquipmentTypeMapper.stringToEnum("legs"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(700,800, EquipmentTypeMapper.stringToEnum("legs"))));

        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(11,22, EquipmentTypeMapper.stringToEnum("right hand"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(111,222, EquipmentTypeMapper.stringToEnum("right hand"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(1111,2222, EquipmentTypeMapper.stringToEnum("right hand"))));

        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(33,44, EquipmentTypeMapper.stringToEnum("left hand"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(333,444, EquipmentTypeMapper.stringToEnum("left hand"))));
        e.add(equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(3333,4444, EquipmentTypeMapper.stringToEnum("left hand"))));
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

    @Test
    @DisplayName("Creating equipment with negative strength should throw ValidationException")
    public void creatingEquipment_negativeStrength_throwValidationException(){
        assertThrows(ValidationException.class, () -> {
            equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(300,-98, EquipmentType.ARMS));
        });
    }

    @Test
    @DisplayName("Creating equipment with negative price should throw ValidationException")
    public void creatingEquipment_negativePrice_throwValidationException(){
        assertThrows(ValidationException.class, () -> {
            equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(-300, 1, EquipmentType.HEAD));
        });
    }

    @Test
    @DisplayName("Creating equipment with invalid type should throw IllegalArgumentException")
    public void creatingEquipment_invalidType_throwIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> {
            equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(2, 1, EquipmentTypeMapper.stringToEnum("nothing")));
        });
    }

    @Test
    @DisplayName("Creating equipment w/o name should throw ValidationException")
    public void creatingEquipment_noName_throwValidationException(){
        assertThrows(ValidationException.class, () -> {
            equipmentService.createNewEquipment(new Equipment(null, "Never equip this item", 1, 2, EquipmentType.ARMS));
        });
    }

    @Test
    @DisplayName("Creating equipment with space only name should throw ValidationException")
    public void creatingEquipment_spaceOnlyName_throwValidationException(){
        assertThrows(ValidationException.class, () -> {
            equipmentService.createNewEquipment(new Equipment("      ", "Never equip this item", 4, 8, EquipmentType.LEFT_HAND));
        });
    }

    @Test
    @DisplayName("Creating a valid equipment should not throw anything")
    public void creatingEquipment_valid_notThrows(){
        assertDoesNotThrow(() -> {
            equipmentService.createNewEquipment(TestData.getNewEquipmentWOid(2,3,EquipmentType.LEGS));
        });
    }

    @Test
    @DisplayName("Buying a too expensive equipment item for a user should throw NotEnoughGoldException")
    public void buying_tooExpensiveItem_shouldThrowNotEnoughGoldException(){
        assertThrows(NotEnoughGoldException.class, () -> {
            User u = userService.createUser(TestData.getNewWorkingUser());
            assertNotNull(u);
            assertTrue(userService.changeUserGold(u.getId(), 1));
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            equipmentService.buyNewEquipment(new UserEquipment(eq.get(5).getId(), u.getId()));
        });
    }

    @Test
    @DisplayName("Buying items for a user should not throw any exception")
    public void buying_validItems_shouldNotThrow(){
        assertDoesNotThrow(() -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter());
            userService.changeUserGold(u.getId(), 500000);
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            for (Equipment e: eq) {
                Equipment bought = equipmentService.buyNewEquipment(new UserEquipment(e.getId(), u.getId()));
                assertEquals(bought.getId(), e.getId());
            }
        });
    }

    @Test
    @DisplayName("Buying equipment and equipping should not throw any exception")
    public void buyingEquipment_usingIt_shouldNotThrow(){
        assertDoesNotThrow(() -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter2());
            userService.changeUserGold(u.getId(), 500000);
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            Equipment bought = equipmentService.buyNewEquipment(new UserEquipment(eq.get(1).getId(), u.getId()));
            Equipment equipped = equipmentService.equipItem(u.getId(), bought);
            assertEquals(bought, equipped);
            List<Equipment> worn = equipmentService.getWornEquipmentFromUserId(u.getId());
            assertEquals(worn.size(), 1);
            for (Equipment ew: worn) {
                if (ew.getType() == equipped.getType()) assertEquals(ew, equipped);
            }
        });
    }

    @Test
    @DisplayName("Buying and equipping two different equipment items of the same type should unequip the first and equip the second and should not throw any exception")
    public void buyingAndEquipping_twoDifferentItems_SameType_shouldNotThrow(){
        assertDoesNotThrow(() -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter3());
            userService.changeUserGold(u.getId(), 500000);
            List<UserEquipment> x = equipmentDao.getAllUserEquipments(u.getId());
            assertEquals(0, x.size());
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            Equipment bought1 = equipmentService.buyNewEquipment(new UserEquipment(eq.get(1).getId(), u.getId()));
            Equipment equipped1 = equipmentService.equipItem(u.getId(), bought1);
            assertEquals(bought1, equipped1);
            List<Equipment> w = equipmentService.getWornEquipmentFromUserId(u.getId());
            assertEquals(w.size(), 1);
            Equipment bought2 = equipmentService.buyNewEquipment(new UserEquipment(eq.get(2).getId(), u.getId()));
            Equipment equipped2 = equipmentService.equipItem(u.getId(), bought2);
            assertEquals(bought2, equipped2);
            List<Equipment> worn = equipmentService.getWornEquipmentFromUserId(u.getId());
            assertEquals(worn.size(), 1);
            for (Equipment ew : worn) {
                if (ew.getType() == equipped2.getType()) assertEquals(ew, equipped2);
            }
        });
    }

    @Test
    @DisplayName("Unequipping not wearing item should throw ValidationException")
    public void unequippingNotWearingEquipment_shouldThrowValidationException(){
        assertThrows(ValidationException.class, () -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter4());
            assertNotNull(u);
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            List<Equipment> w = equipmentService.getWornEquipmentFromUserId(u.getId());
            assertEquals(w.size(), 0);
            equipmentService.unequipItem(u.getId(), eq.get(1).getId());
        });
    }

    @Test
    @DisplayName("Equipping not owning item should throw ValidationException")
    public void equipping_notOwningEquipment_shouldThrowValidationException(){
        assertThrows(ValidationException.class, () -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter5());
            assertNotNull(u);
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            List<Equipment> w = equipmentService.getWornEquipmentFromUserId(u.getId());
            assertEquals(w.size(), 0);
            equipmentService.equipItem(u.getId(), eq.get(8));
        });
    }

    @Test
    @DisplayName("Buying the same equipment multiple times should throw ValidationException")
    public void buyingSameEquipment_multiple_shouldThrowValidationException(){
        assertThrows(ValidationException.class, () -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter6());
            assertNotNull(u);
            userService.changeUserGold(u.getId(), 500000);
            List<Equipment> eq = createSomeValidTestEquipment();
            assertNotNull(eq);
            List<Equipment> w = equipmentService.getWornEquipmentFromUserId(u.getId());
            assertEquals(w.size(), 0);
            UserEquipment ue1 = new UserEquipment(eq.get(1).getId(), u.getId());
            Equipment bought1 = equipmentService.buyNewEquipment(ue1);
            Equipment bought2 = equipmentService.buyNewEquipment(ue1);
        });
    }
}
