use
Quest_For_Health;

SET
FOREIGN_KEY_CHECKS=0;

INSERT INTO quest (id, name, description, exp_reward, gold_reward, repetition_cycle)
VALUES (1, 'Drink water', 'Drink 2 litres of water throughout the day', 10, 1, TIME ('24:00:00')),
       (2, 'Go for a walk', 'Take a walk that lasts at least half an hour.', 50, 5, NULL),
       (3, 'Weekly run', 'Go for a run that lasts at least one hour.', 200, 20, TIME ('168:00:00')),
       (4, 'Balance Training', 'Stand on one leg for 1 minute straight, then switch legs. Do that 3 times.', 50, 20, TIME ('24:00:00')),
       (5, 'Grip strength rehabilitation', 'Hang from a bar for a total of 90 seconds', 10, 3, TIME ('24:00:00')),
       (6, 'Weekly swim training', 'Swim 50 lanes', 1, 1, TIME ('168:00:00')),
       (7, 'Bandage change', 'Change your bandage', 10, 5, NULL),
       (8, 'Test Doc not assigned', 'Test Doc Inc', 20, 7, NULL),
       (9, 'Do 50 Pushups', 'Gonna get those gainz', 10, 3, TIME ('72:00:00'));

insert into doctor_quest (id, doctor, exp_penalty, gold_penalty)
values (4, 3, 1, 0),
       (5, 3, 10, 8),
       (6, 3, 3, 4),
       (7, 1, 10, 0);

insert into user_accepted_quest(user, quest, accepted_on)
VALUES (2, 6, '2021-04-19'),
       (2, 7, '2021-04-19'),
       (2, 1, '2021-04-19'),
       (2, 2, '2021-04-19'),
       (2, 3, '2021-04-19'),
       (1, 1, '2021-02-10'),
       (1, 5, '2021-04-18');

insert into user_completed_quest(user, quest, completed_on, completed)
VALUES (1, 1, '2021-03-17', TRUE),
       (1, 5, '2021-04-10', TRUE),
       (2, 6, '2021-04-3', FALSE);



insert into story_chapter (id, name, description, strength_requirement, prev_chapter, next_chapter)
values (1, 'Chapter 1: The Beginning',
        'You find yourself in a dark inn nearby the old Gramsworth-Castle. Rumors has it that the former baron of this land has issued a bounty on a terrifying monster which threatens his land. As a young adventurer, desperately searching for money and glory, you decided to take on the bounty and start to make yourself a name',
        0, null, 2),
       (2, 'Chapter 2: Start of the Journey', 'You loaf around the inn, sipping on your ale before calling it a day, when suddenly a bunch of worn out, clearly shocked men storm in. Their looks are anything but pleasant, and so is their smell. Hastily, one of them shouts: "Make room, clear the table and get some alcohol!". Quite perplexed, the people of the nearest table got up and cleared the table while the barkeeper hastily grabbed the next-best bottle of rum. Just what was going on here?', 25, 1, 3),
       (3, 'Chapter 3: What happened?', 'Intriqued by all the ruckus', 54, 2, null);

insert into character_level (total_strength, needed_exp, level, character_level.rank) values
(0,0,1,'Beginner'),
(2, 10, 2, 'Beginner'),
(4, 40, 3, 'Beginner'),
(6, 90, 4, 'Beginner'),
(8, 160, 5, 'Beginner'),
(11,250,6,'Better Beginner'),
(14, 360, 7, 'Better Beginner'),
(17, 490, 8, 'Better Beginner'),
(20, 640, 9, 'Better Beginner'),
(21, 810, 10, 'Better Beginner'),
(25,1000,11,'Pupil'),
(29, 1210, 12, 'Pupil'),
(33, 1440, 13, 'Pupil'),
(37, 1690, 14, 'Pupil'),
(41, 1960, 15, 'Pupil'),
(46,2250,16,'Apprentice'),
(51, 2560, 17, 'Apprentice'),
(56, 2890, 18, 'Apprentice'),
(61, 3240, 19, 'Apprentice'),
(66, 3610, 20, 'Apprentice'),
(72,4000,21,'Experienced'),
(78, 4410, 22, 'Experienced'),
(84, 4840, 23, 'Experienced'),
(90, 5290, 24, 'Experienced'),
(96, 5760, 25, 'Experienced'),
(103,6250,26,'Advanced'),
(110, 6760, 27, 'Advanced'),
(117, 7290, 28, 'Advanced'),
(124, 7840, 29, 'Advanced'),
(131, 8410, 30, 'Advanced'),
(139,9000,31,'Expert'),
(147, 9610, 32, 'Expert'),
(155, 10240, 33, 'Expert'),
(163, 10890, 34, 'Expert'),
(171, 11560, 35, 'Expert'),
(180,12250,36,'Master'),
(189, 12960, 37, 'Master'),
(198, 13690, 38, 'Master'),
(207, 14440, 39, 'Master'),
(216, 15210, 40, 'Master'),
(226,16000,41,'Prodigy'),
(236, 16810, 42, 'Prodigy'),
(246, 17640, 43, 'Prodigy'),
(256, 18490, 44, 'Prodigy'),
(266, 19360, 45, 'Prodigy'),
(277,20250,46,'Legend'),
(288, 21160, 47, 'Legend'),
(299, 22090, 48, 'Legend'),
(310, 23040, 49, 'Legend'),
(321, 24010, 50, 'Legend'),
(333,25000,51,'Beyond Legend'),
(345, 26010,52, 'Beyond Legend'),
(357, 27040, 53, 'Beyond Legend'),
(369, 28090, 54, 'Beyond Legend'),
(381, 29160, 55, 'Beyond Legend'),
(394,30250,56,'King of Kings'),
(407, 31360,57, 'King of Kings'),
(420, 32490, 58, 'King of Kings'),
(433, 33640, 59, 'King of Kings'),
(446, 34810, 60, 'King of Kings'),
(1000, 100000, 61, 'Sage'),
(10000, 2147483647, 62, 'Pretty sure you cheated your way up here');

insert into doctor(id, firstname, lastname, email, password)
values (1, 'Franz', 'Pamperl', 'e.m@il.com', 'QWERT1234'),
       (2, 'Gerald', 'Steinhardt', 'sig@na.nz', 'BesterProf'),
       (3, 'Anton', 'Alfred', 'a-a@doc.at', 'pass');

insert into user(id, firstname, lastname, character_name, character_strength, character_level, character_exp,
                 character_gold, password, story_chapter)
values (1, 'Peter', 'Baumgartner', 'Stannis', 1, 1, 0, 0, '8619bfda1175395ff7a167c68e143a37a4bd56abf44a50a6f69d7a0bf5bfa877a8518c00563b864105e83959dfe6d1d46b17fe730f61f4072a50eee9823e93bf', 1),
       (2, 'Kurt', 'Freilich', 'Shootz', 1, 1, 5, 500, '8619bfda1175395ff7a167c68e143a37a4bd56abf44a50a6f69d7a0bf5bfa877a8518c00563b864105e83959dfe6d1d46b17fe730f61f4072a50eee9823e93bf', 2),
       (3, 'Maria', 'Morticia', 'MaryChristmas', 1, 1, 0, 100, '8619bfda1175395ff7a167c68e143a37a4bd56abf44a50a6f69d7a0bf5bfa877a8518c00563b864105e83959dfe6d1d46b17fe730f61f4072a50eee9823e93bf', 1),
       (4, 'Lisa', 'Neumair', 'Elis', 1, 1, 0, 0, '8619bfda1175395ff7a167c68e143a37a4bd56abf44a50a6f69d7a0bf5bfa877a8518c00563b864105e83959dfe6d1d46b17fe730f61f4072a50eee9823e93bf', 1),
       (5, 'Jakob', 'Fröhlich', 'CrazyJack', 1, 1, 0, 0, '8619bfda1175395ff7a167c68e143a37a4bd56abf44a50a6f69d7a0bf5bfa877a8518c00563b864105e83959dfe6d1d46b17fe730f61f4072a50eee9823e93bf', 1);

insert into doctor_has_patients(doctor, user)
values (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (1, 3),
       (1, 4);

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
       ('Glued Wood', 'It helps to weaken some attacks, but breaks pretty easy.', 8, 5, 'Left hand'),
       ('Leather cap', 'Used for working in the stables or for training fights.', 20, 4, 'head'),
       ('Leather Gloves', 'Perfect for riding a horse or handling a falcon', 40, 3, 'arms'),
       ('Empty Barrel', 'While it might not be comfortable or stylish, it surely can take a lot of hits.', 29, 8, 'torso'),
       ('Riding Pants', 'A pair of sturdy pants, usually used for riding', 16, 5, 'legs'),
       ('Wooden sword', 'This sword is used for training purposes only, but when you are out of options...', 15, 4, 'Right hand'),
       ('Wooden shield', 'Basically just a fortified piece of wood', 12, 6, 'Left hand'),
       ('Fortified leather cap', 'This leather cap has iron bars worked into it. Shields quite good against horse kicks.', 35, 7, 'head'),
       ('Wooden bracers', 'Provide some basic protection against blows', 40, 5, 'arms'),
       ('Chain mail', 'A must-have for any knight.', 50, 12, 'torso'),
       ('Wooden shin bracers', 'Protects against blows to the shin.', 20, 6, 'legs'),
       ('Rusty sword', 'A knight must have forgotten it...', 23, 6, 'Right hand'),
       ('Buckler', 'Comes with an ergonomic handle!', 16, 7, 'Left hand');



SET
FOREIGN_KEY_CHECKS=1;
