use
Quest_For_Health;

SET
FOREIGN_KEY_CHECKS=0;

INSERT INTO quest (id, name, description, exp_reward, gold_reward, repetition_cycle)
VALUES (1, 'Trink Wasser', 'Trinke über den Tag verteilt mindestens 2 Liter Wasser.', 10, 1, TIME ('24:00:00')),
       (2, 'Geh Spazieren', 'Mach einen Spaziergang, der mindestens eine halbe Stunde dauert', 50, 5, NULL),
       (3, 'Wöchentlicher Lauf', 'Geh ein Mal die Woche mindestens eine Stunde laufen.', 200, 20, TIME ('168:00:00')),
       (4, 'Created by Steinhardt', 'Best Prof!', 200, 20, TIME ('168:00:00')),
       (5, 'Created by Steinhardt', 'Doc Quest 2', 5, 3, TIME ('24:00:00')),
       (6, 'Assigned to User 4', 'Created by Steinhardt', 1, 1, TIME ('48:00:00')),
       (7, 'Assigned to User 2', 'Created by Test Doc', 2, 5, NULL),
       (8, 'Test Doc not assigned', 'Test Doc Inc', 20, 7, NULL),
       (9, 'Do 50 Pushups', 'Gonna get those gainz', 10, 3, TIME ('72:00:00'));

insert into doctor_quest (id, doctor, exp_penalty, gold_penalty)
values (4, 2, 1, 0),
       (5, 2, 10, 8),
       (6, 2, 3, 4),
       (7, 1, 0, 0);

insert into user_accepted_quest(user, quest, accepted_on)
VALUES (4, 6, '2021-04-19'),
       (2, 7, '2021-04-19'),
       (6, 1, '2021-02-10'),
       (6, 5, '2021-04-18');

insert into user_completed_quest(user, quest, completed_on, completed)
VALUES (4, 6, '2021-03-17', TRUE),
       (3, 4, '2021-04-10', TRUE),
       (3, 4, '2021-04-3', FALSE),
       (6, 2, '2021-05-03 ', FALSE),
       (6, 6, '2021-04-23', TRUE);



insert into story_chapter (id, name, description, strength_requirement, prev_chapter, next_chapter)
values (1, 'Chapter 1: The Beginning',
        'You find yourself in a dark inn nearby the old Gramsworth-Castle. Rumors has it that the former baron of this land has issued a bounty on a terrifying monster which threatens his land. As a young adventurer, desperately searching for money and glory, you decided to take on the bounty and start to make yourself a name',
        0, null, 2),
       (2, 'Chapter 2: Start of the Journey', 'Ja, da kommt noch was.', 25, 1, 3),
       (3, 'Chapter 3: The wrong way?', '----?', 54, 2, null);

insert into character_level (total_strength, needed_exp, level, rank) values
(0,0,1,'Beginner'),
(2, 20, 2, 'Beginner'),
(4, 50, 3, 'Better Beginner'),
(6, 80, 4, 'Better Beginner'),
(9, 132, 5, 'Expierenced');

insert into doctor(id, firstname, lastname, email, password)
values (1, 'Test', 'Doc', 'e.m@il.com', 'QWERT1234'),
       (2, 'Gerald', 'Steinhardt', 'sig@na.nz', 'BesterProf'),
       (3, 'Anton', 'Alfred', 'a-a@doc.at', 'pass');

insert into user(id, firstname, lastname, character_name, character_strength, character_level, character_exp,
                 character_gold, password, story_chapter)
values (1, 'Alex', 'Apple', 'Attila', 3, 1, 12, 3, 'unknackbar', 1),
       (2, 'Berta', 'Binder', 'Batman', 26, 2, 40, 92, 'Joker', 2),
       (3, 'Carla', 'Cloud', 'Crash', 58, 5, 153, 11, 'crappy', 3),
       (4, 'Donkey', 'Kong', 'Diddy', 28, 3, 153, 63, 'Country', 2),
       (5, 'Edmund', 'Ende', 'Eddy', 5, 1, 2, 48, 'Vogelnest', 1),
       (6, 'Florian', 'Flocke', 'FiNCH', 1, 1, 0, 12, 'Fehlzündung', 1);

insert into doctor_has_patients(doctor, user)
values (2, 1),
       (2, 2),
       (3, 1),
       (1, 6),
       (3, 4),
       (2, 4);

insert into equipment (name, description, price, strength, type)
values ('Hat', 'A simple hat.', 5, 1, 'head'),
       ('Winter Gloves', 'These gloves are perfect for keeping you warm in the winter, regarding actual protection in combat, however, they are pretty useless. Better than nothing though...', 5, 1, 'arms'),
       ('Wool Jacket', 'Your basic jacket for the winter.', 15, 4, 'torso'),
       ('Farm Trousers', 'Ideal for working at the farm.', 8, 2, 'legs'),
       ('Wooden Stick', 'A stick some kid found in the woods. It really is nothing special.', 5, 1, 'right hand'),
       ('Piece of Bark', 'This bark fell from a tree during the last storm. Looks kinda cool though.', 5, 1, 'left hand'),
       ('Giant Hat', 'A giant hat that distracts others.', 10, 2, 'head'),
       ('Summer Gloves', 'Fancy gloves for the rich.', 30, 2, 'arms'),
       ('Windshield', 'A jacket that protects you against windy days.', 18, 5, 'torso'),
       ('Worker Trousers', 'A pair of trousers that you can wear for all kind of work.', 12, 4, 'legs'),
       ('Wooden Stick with Thorns', 'This stick can hurt if used correctly. Pay attention that you do not hurt yourself.', 9, 3, 'Right hand'),
       ('Glued Wood', 'It helps to weaken some attacks, but breaks pretty easy.', 8, 5, 'Left hand');



SET
FOREIGN_KEY_CHECKS=1;
