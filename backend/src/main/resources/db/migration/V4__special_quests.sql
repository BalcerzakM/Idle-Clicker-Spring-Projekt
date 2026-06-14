ALTER TABLE quest_entity MODIFY quest_tier ENUM('EASY', 'MEDIUM', 'HARD', 'BOSS') NOT NULL;
INSERT INTO opponent_entity (name,
                             base_rizz,
                             base_strength,
                             base_agility,
                             base_endurance,
                             base_luck,
                             image_path)
VALUES ('Andrzej Miękkowski', 50, 0, 5, 200, 5, 'boss_andrzej.png'),
       ('Borys Piotrenko', 0, 80, 10, 300, 30, 'boss_piotrenko.png');



INSERT INTO quest_entity (title,
                          description,
                          quest_tier,
                          quest_type,
                          opponent_id,
                          image_path)
VALUES ('Członek samorządu', 'Studenci wydziału pracują nad pewnym niebezpiecznym dla rządu projektem. Trzeba go jakoś przekabacić, by im go uwalił i pozbyć się ich z uczelni. Dasz radę to zrobić?', 'BOSS', 'RIZZ_FIGHT', (SELECT id FROM opponent_entity WHERE name = 'Andrzej Miękkowski'), ''),
       ('Psychol z polibudy', 'Zwany także niekiedy "Bestią z Pieprzniczek", czyli miejscowości z której pochodzi. Absolutny szaleniec i zwyrol, ktoś musi się nim zająć. Zrobisz to?', 'BOSS', 'STRENGTH_FIGHT', (SELECT id FROM opponent_entity WHERE name = 'Borys Piotrenko'), '');