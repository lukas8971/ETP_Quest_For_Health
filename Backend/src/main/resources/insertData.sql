use Quest_For_Health;
insert into story_chapter (name, description, strength_requirement)
values ('Chapter 1: The Beginning', 'You find yourself in a dark inn nearby the old Gramsworth-Castle. Rumors has it that the former baron of this land has issued a bounty on a terrifying monster which threatens his land. As a young adventurer, desperately searching for money and glory, you decided to take on the bounty and start to make yourself a name',
        0);

insert into character_level (total_strength, needed_exp, level, rank) values (0,0,1,'Beginner');
select * from character_level;
