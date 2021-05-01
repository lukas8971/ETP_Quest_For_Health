package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.endpoint.EquipmentEndpoint;
import com.etp.questforhealth.endpoint.UserEndpoint;
import com.etp.questforhealth.endpoint.dto.EquipmentDto;
import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.endpoint.mapper.UserMapper;
import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.entity.enums.EquipmentType;
import com.etp.questforhealth.entity.enums.mapper.EquipmentTypeMapper;
import com.etp.questforhealth.persistence.EquipmentDao;
import com.etp.questforhealth.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class EquipmentEndpointTest {

    @Autowired
    EquipmentEndpoint equipmentEndpoint;
    @Autowired
    EquipmentDao equipmentDao;
    @Autowired
    UserEndpoint userEndpoint;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    @BeforeAll
    public static void testData() {
        DatabaseTestData.insertTestData();
    }

    /**
     * Just so that the equipment doesnt have to be created on each run
     *
     * @return a list of all created equipments
     */
    private List<Equipment> createSomeValidTestEquipment() {
        List<Equipment> e = new ArrayList<>();
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(1, 2, EquipmentTypeMapper.stringToEnum("head"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(10, 20, EquipmentTypeMapper.stringToEnum("head"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(100, 200, EquipmentTypeMapper.stringToEnum("head"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(3, 4, EquipmentTypeMapper.stringToEnum("arms"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(30, 40, EquipmentTypeMapper.stringToEnum("arms"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(300, 240, EquipmentTypeMapper.stringToEnum("arms"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(5, 6, EquipmentTypeMapper.stringToEnum("torso"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(50, 60, EquipmentTypeMapper.stringToEnum("torso"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(500, 600, EquipmentTypeMapper.stringToEnum("torso"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(7, 8, EquipmentTypeMapper.stringToEnum("legs"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(70, 80, EquipmentTypeMapper.stringToEnum("legs"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(700, 800, EquipmentTypeMapper.stringToEnum("legs"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(11, 22, EquipmentTypeMapper.stringToEnum("right hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(111, 222, EquipmentTypeMapper.stringToEnum("right hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(1111, 2222, EquipmentTypeMapper.stringToEnum("right hand"))));

        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(33, 44, EquipmentTypeMapper.stringToEnum("left hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(333, 444, EquipmentTypeMapper.stringToEnum("left hand"))));
        e.add(equipmentDao.createNewEquipment(TestData.getNewEquipmentWOid(3333, 4444, EquipmentTypeMapper.stringToEnum("left hand"))));
        if (e.size() > 0) return e;
        return null;
    }

    @Test
    @DisplayName("Buying not existing item should throw ResponseStatusException")
    public void buying_notExistingItem_shouldThrowResponseStatusException() {
        UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUser());
        User u = userMapper.dtoToEntity(ud);
        assertNotNull(u);
        assertThrows(ResponseStatusException.class, () -> {
            equipmentEndpoint.buyNewEquipment(new UserEquipment(758765, u.getId()));
        });
    }

    @Test
    @DisplayName("Equipping not existing equipment should throw ResponseStatusException")
    public void equipping_notExistingItem_shouldThrowResponseStatusException() {
        UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUserDifferentCharacter());
        User u = userMapper.dtoToEntity(ud);
        assertNotNull(u);
        EquipmentDto eq = new EquipmentDto(758765, "Should not work", "Do not equip", 1, 2, EquipmentType.LEGS);
        assertThrows(ResponseStatusException.class, () -> {
            equipmentEndpoint.equipItem(u.getId(), eq);
        });
    }

    @Test
    @DisplayName("Unequipping not existing equipment should throw ResponseStatusException")
    public void unequipping_notExistingItem_shouldThrowResponseStatusException() {
        UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUserDifferentCharacter2());
        User u = userMapper.dtoToEntity(ud);
        assertNotNull(u);
        EquipmentDto eq = new EquipmentDto(758765, "Should not work", "Do not equip", 1, 2, EquipmentType.LEGS);
        assertThrows(ResponseStatusException.class, () -> {
            equipmentEndpoint.unequipItem(u.getId(), eq);
        });
    }

    @Test
    @DisplayName("Buying equipment with not enough money should throw ResponseStatusException")
    public void buying_notEnoughMoney_shouldThrowResponseStatusException() {
        UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUserDifferentCharacter3());
        User u = userMapper.dtoToEntity(ud);
        assertNotNull(u);
        assertThrows(ResponseStatusException.class, () -> {
            equipmentEndpoint.buyNewEquipment(new UserEquipment(1, u.getId()));
        });
    }

    @Test
    @DisplayName("Buying and equipping new item with enough gold should not throw anything")
    public void buyingAndEquipping_withEnoughMoney_shouldNotThrow() {
        assertDoesNotThrow(() -> {
            UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUserDifferentCharacter4());
            User u = userMapper.dtoToEntity(ud);
            assertNotNull(u);
            assertTrue(userService.changeUserGold(u.getId(), 12345));
            equipmentEndpoint.buyNewEquipment(new UserEquipment(1, u.getId()));
            EquipmentDto eq = equipmentEndpoint.getOneById(1);
            equipmentEndpoint.equipItem(u.getId(), eq);
        });
    }

    @Test
    @DisplayName("Buying two different items of the same type end equipping them one another should not throw any exception and return the last equipped item")
    public void buyingTwoItems_sameType_equipping_shouldNotThrow() {
        assertDoesNotThrow(() -> {
            UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUserDifferentCharacter5());
            User u = userMapper.dtoToEntity(ud);
            assertNotNull(u);
            assertTrue(userService.changeUserGold(u.getId(), 12345));
            List<Equipment> eqs = createSomeValidTestEquipment();
            assertNotNull(eqs);
            EquipmentDto bought1 = equipmentEndpoint.buyNewEquipment(new UserEquipment(eqs.get(0).getId(), u.getId()));
            assertNotNull(bought1);
            EquipmentDto bought2 = equipmentEndpoint.buyNewEquipment(new UserEquipment(eqs.get(1).getId(), u.getId()));
            assertNotNull(bought2);
            EquipmentDto equipped1 = equipmentEndpoint.equipItem(u.getId(), bought1);
            assertNotNull(equipped1);
            List<EquipmentDto> wearing = equipmentEndpoint.getEquipmentWornByUserId(u.getId());
            assertEquals(wearing.size(), 1);
            assertEquals(wearing.get(0), equipped1);
            EquipmentDto equipped2 = equipmentEndpoint.equipItem(u.getId(), bought2);
            assertNotNull(equipped2);
            List<EquipmentDto> wearingNew = equipmentEndpoint.getEquipmentWornByUserId(u.getId());
            assertEquals(wearingNew.size(), 1);
            assertEquals(wearingNew.get(0), equipped2);
        });
    }

    @Test
    @DisplayName("Equipping 6 different item types should return a list of 6 different items")
    public void equippingDifferentTypes_shouldReturnDifferentTypes() {
        assertDoesNotThrow(() -> {
            UserDto ud = userEndpoint.createUser(TestData.getNewWorkingUserDifferentCharacter6());
            User u = userMapper.dtoToEntity(ud);
            assertNotNull(u);
            assertTrue(userService.changeUserGold(u.getId(), 12345));
            List<Equipment> eqs = createSomeValidTestEquipment();
            assertNotNull(eqs);
            List<EquipmentDto> eqBought = new ArrayList<>();
            List<UserEquipment> uE = new ArrayList<>();
            uE.add(new UserEquipment(eqs.get(0).getId(), u.getId()));// head
            uE.add(new UserEquipment(eqs.get(3).getId(), u.getId()));// arms
            uE.add(new UserEquipment(eqs.get(6).getId(), u.getId()));// torso
            uE.add(new UserEquipment(eqs.get(9).getId(), u.getId()));// legs
            uE.add(new UserEquipment(eqs.get(12).getId(), u.getId()));// right hand
            uE.add(new UserEquipment(eqs.get(15).getId(), u.getId()));// left hand
            for (UserEquipment x: uE) {
                eqBought.add(equipmentEndpoint.buyNewEquipment(x));
            }
            assertEquals(eqBought.size(), 6);
            List<EquipmentDto> eqEquipped = new ArrayList<>();
            for (EquipmentDto x: eqBought) {
                eqEquipped.add(equipmentEndpoint.equipItem(u.getId(), x));
            }
            assertEquals(eqEquipped.size(), 6);
            assertEquals(eqEquipped, eqBought);
            List<EquipmentDto> wearing = equipmentEndpoint.getEquipmentWornByUserId(u.getId());
            assertEquals(wearing.size(), 6);
            assertEquals(wearing, eqEquipped);
            boolean legs = false;
            boolean arms = false;
            boolean torso = false;
            boolean leftHand = false;
            boolean rightHand = false;
            boolean head = false;
            for(int i = 0; i < 6; i++){
                switch (wearing.get(i).getType()){
                    case LEGS: legs = true;
                    break;
                    case ARMS: arms = true;
                    break;
                    case TORSO: torso = true;
                    break;
                    case LEFT_HAND: leftHand = true;
                    break;
                    case RIGHT_HAND: rightHand = true;
                    break;
                    case HEAD: head = true;
                    break;
                }
            }
            assertTrue(legs, "Leg equipment is not worn!");
            assertTrue(arms, "Arms equipment is not worn!");
            assertTrue(torso, "Torso equipment is not worn!");
            assertTrue(leftHand, "Left hand equipment is not worn!");
            assertTrue(rightHand, "Right hand equipment is not worn!");
            assertTrue(head, "Head equipment is not worn!");
        });
    }
}
