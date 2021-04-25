use Quest_For_Health_Test;


insert into story_chapter (name, description, strength_requirement)
values ('Chapter 1: The Beginning', 'You find yourself in a dark inn nearby the old Gramsworth-Castle. Rumors has it that the former baron of this land has issued a bounty on a terrifying monster which threatens his land. As a young adventurer, desperately searching for money and glory, you decided to take on the bounty and start to make yourself a name',
        0),
       ('Chapter 2: Start of the Journey', 'Ja, da kommt noch was.', 25),
       ('Chapter 3: The wrong way?', '----?', 54);

insert into character_level (total_strength, needed_exp, level, rank) values
    (0,0,1,'Beginner'),
    (4,50,3,'Better Beginner'),
    (9,132,5,'Expierenced');

insert into equipment (name,description,price,strength,type) values
('Hat', 'A simple hat.',5,1,'head'),
('Winter Gloves', 'These gloves are perfect for keeping you warm in the winter, regarding actual protection in combat, however, they are pretty useless. Better than nothing though...',5,1,'arms'),
('Wool Jacket', 'Your basic jacket for the winter.',15,4,'torso'),
('Farm Trousers', 'Ideal for working at the farm.',8,2,'legs'),
('Wooden Stick', 'A stick some kid found in the woods. It really is nothing special.', 5,1,'right hand'),
('Piece of Bark', 'This bark fell from a tree during the last storm. Looks kinda cool though.',5,1,'left hand');