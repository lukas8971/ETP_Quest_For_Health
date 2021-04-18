use Quest_For_Health;

INSERT IGNORE INTO quest (id, name, description, exp_reward, gold_reward, repetition_cycle) VALUES
    (1,'Trink Wasser', 'Trinke über den Tag verteilt mindestens 2 Liter Wasser.', 10, 1, TIME('24:00:00')),
    (2,'Geh Spazieren', 'Mach einen Spaziergang, der mindestens eine halbe Stunde dauert', 50, 5, NULL),
    (3,'Wöchentlicher Lauf', 'Geh ein Mal die Woche mindestens eine Stunde laufen.', 200, 20, TIME('168:00:00'));

insert into story_chapter (name, description, strength_requirement)
values ('Chapter 1: The Beginning', 'You find yourself in a dark inn nearby the old Gramsworth-Castle. Rumors has it that the former baron of this land has issued a bounty on a terrifying monster which threatens his land. As a young adventurer, desperately searching for money and glory, you decided to take on the bounty and start to make yourself a name',
        0);

insert into character_level (total_strength, needed_exp, level, rank) values (0,0,1,'Beginner');
select * from character_level;
