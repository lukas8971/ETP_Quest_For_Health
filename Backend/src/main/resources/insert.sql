INSERT IGNORE INTO quest (id, name, description, exp_reward, gold_reward, repetition_cycle) VALUES
    (1,'Trink Wasser', 'Trinke über den Tag verteilt mindestens 2 Liter Wasser.', 10, 1, TIME('24:00:00')),
    (2,'Geh Spazieren', 'Mach einen Spaziergang, der mindestens eine halbe Stunde dauert', 50, 5, NULL),
    (3,'Wöchentlicher Lauf', 'Geh ein Mal die Woche mindestens eine Stunde laufen.', 200, 20, TIME('168:00:00'));