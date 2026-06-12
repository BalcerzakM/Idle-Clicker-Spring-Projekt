ALTER TABLE quest_entity MODIFY quest_tier ENUM('EASY', 'MEDIUM', 'HARD', 'BOSS') NOT NULL;
INSERT INTO opponent_entity (name,
                             base_rizz,
                             base_strength,
                             base_agility,
                             base_endurance,
                             base_luck,
                             image_path)
VALUES ('Andrzej Miękkowski', 60, 0, 5, 350, 5, 'opponent_gruba.png'),
       ('Borys Piotrenko', 150, 0, 10, 600, 30, 'opponent_gruba.png');



INSERT INTO quest_entity (title,
                          description,
                          quest_tier,
                          quest_type,
                          opponent_id,
                          image_path)
VALUES ('Samorząd studencki', 'opis', 'BOSS', 'RIZZ_FIGHT', (SELECT id FROM opponent_entity WHERE name = 'Andrzej Miękkowski'), 'rizz_quest1.png'),
       ('Psychol z polibudy', 'opis', 'BOSS', 'RIZZ_FIGHT', (SELECT id FROM opponent_entity WHERE name = 'Borys Piotrenko'), 'rizz_quest1.png');