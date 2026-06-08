INSERT INTO base_item_entity (name,
                              description,
                              item_type,
                              slot_type,
                              base_rizz,
                              base_strength,
                              base_agility,
                              base_endurance,
                              base_luck,
                              base_price,
                              image_path)
VALUES
    ('Zimowa czapka',           'opis', 'EQUIPMENT','HEAD', 5, 2, 0, 7, 5, 40, 'item-winter-hat.png'),
    ('Słomiany kapelusz',       'opis', 'EQUIPMENT','HEAD', 3, 0, 10, 6, 1, 40, 'item-straw-hat.png'),
    ('Różowe okulary',          'opis', 'EQUIPMENT','HEAD', 7, 3, 3, 6, 8, 52, 'item-pink-sunglasses.png'),
    ('Czapka rolnika',          'opis', 'EQUIPMENT','HEAD', 4, 3, 0, 9, 0, 35, 'item-farmer-cap.png'),
    ('Pomarańczowe okulary',    'opis', 'EQUIPMENT','HEAD', 5, 0, 4, 5, 3, 35, 'item-orange-sunglasses.png'),

    ('Karta do akademika',      'opis', 'EQUIPMENT','NECK', 3, 3, 0, 8, 0, 35, 'item-dormitory-card.png'),
    ('Szalik Gajelonii',        'opis', 'EQUIPMENT','NECK', 2, 7, 3, 4, 0, 40, 'item-sport-scarf.png'),
    ('Wisiorek z krucyfiksem',  'opis', 'EQUIPMENT','NECK', 3, 3, 3, 3, 1, 40, 'item-crucifix-necklace.png'),
    ('Czerwone korale',         'opis', 'EQUIPMENT','NECK', 4, 4, 4, 4, 2, 44, 'item-red-corals.png'),

    ('Biała marynarka',         'opis', 'EQUIPMENT','UPPER_BODY', 7, 3, 3, 12, 1, 48, 'item-white-suit.png'),
    ('Hawajska koszula',        'opis', 'EQUIPMENT','UPPER_BODY', 6, 3, 5, 8, 1, 45, 'item-hawaiian-shirt.png'),
    ('Czarna suknia',           'opis', 'EQUIPMENT','UPPER_BODY', 8, 0, 5, 10, 1, 44, 'item-black-dress.png'),
    ('Patriotyczna bluza',      'opis', 'EQUIPMENT','UPPER_BODY', 0, 11, 6, 15, 0, 52, 'item-patriotic-hoodie.png'),
    ('Fartuch kuchenny',        'opis', 'EQUIPMENT','UPPER_BODY', 8, 4, 8, 7, 4, 50, 'item-kitchen-apron.png'),
    ('Biała bluzka',            'opis', 'EQUIPMENT','UPPER_BODY', 4, 1, 5, 12, 0, 45, 'item-white-blouse.png'),
    ('Skórzana kurtka',         'opis', 'EQUIPMENT','UPPER_BODY', 7, 8, 0, 16, 1, 50, 'item-leather-jacket.png'),

    ('Stare Jeansy',            'opis', 'EQUIPMENT','LOWER_BODY', 3, 0, 2, 10, 0, 38, 'item-old-jeans.png'),
    ('Spodnie od garniaka',     'opis', 'EQUIPMENT','LOWER_BODY', 6, 3, 4, 7, 1, 45, 'item-white-suit-pants.png'),
    ('Czarna spódniczka',       'opis', 'EQUIPMENT','LOWER_BODY', 9, 0, 8, 8, 1, 47, 'item-black-skirt.png'),

    ('Wysokie buty',            'opis', 'EQUIPMENT','FEET', 6, 6, 0, 9, 0, 44, 'item-high-soled-shoes.png'),
    ('Kalosze',                 'opis', 'EQUIPMENT','FEET', 2, 8, 1, 11, 0, 43, 'item-rain-boots.png'),
    ('Stare trampki',           'opis', 'EQUIPMENT','FEET', 1, 5, 5, 6, 0, 32, 'item-old-sneakers.png'),
    ('Crocsy',                  'opis', 'EQUIPMENT','FEET', 3, 8, 5, 10, 0, 50, 'item-crocs.png'),
    ('Sztyblety',               'opis', 'EQUIPMENT','FEET', 5, 8, 1, 11, 0, 46, 'item-chelsea-boots.png'),
    ('Czarne szpilki',          'opis', 'EQUIPMENT','FEET', 7, 6, 0, 6, 1, 40, 'item-black-heels.png'),

    ('Złota branzoletka',       'opis', 'EQUIPMENT','WRIST', 4, 5, 0, 3, 2, 40, 'item-golden-bracelet.png'),
    ('Sikor',                   'opis', 'EQUIPMENT','WRIST', 7, 0, 0, 1, 3, 40, 'item-leather-watch.png'),
    ('Branzoletka przyjaźni',   'opis', 'EQUIPMENT','WRIST', 4, 6, 0, 5, 1, 45, 'item-friendship-bracelet.png'),

    ('Zapalniczka',             'opis', 'EQUIPMENT','EMBLEM', 6, 4, 3, 0, 3, 45, 'item-lighter.png'),
    ('Paczka szlugów',          'opis', 'EQUIPMENT','EMBLEM', 9, 6, 0, 0, 4, 50, 'item-cigarettes.png'),
    ('Tani vape',               'opis', 'EQUIPMENT','EMBLEM', 5, 6, 0, 0, 2, 40, 'item-cheap-vape.png'),
    ('Pluszak Babulu',          'opis', 'EQUIPMENT','EMBLEM', 6, 3, 1, 0, 5, 55, 'item-babulu-plush.png'),
    ('Dezodorant',              'opis', 'EQUIPMENT','EMBLEM', 7, 7, 3, 1, 0, 48, 'item-deodorant.png'),
    ('Pudełko snusów',          'opis', 'EQUIPMENT','EMBLEM', 6, 5, 1, 1, 2, 48, 'item-snus.png'),
    ('Różowa torebka',          'opis', 'EQUIPMENT','EMBLEM', 5, 8, 2, 2, 5, 52, 'item-pink-bag.png'),
    ('Karty tarota',            'opis', 'EQUIPMENT','EMBLEM', 4, 4, 4, 4, 12, 65, 'item-tarot.png'),


    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-03.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-12.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-13.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-21.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-34.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-55.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-58.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 10, 0, 'item-token-67.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-77.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-89.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 1, 0, 'item-token-99.png'),
    ('Numerek do szatni', 'Wymień go u szatniarza.', 'ITEM_TOKEN', 'NONE',0, 0, 0, 0, 10, 0, 'item-token-420.png');

INSERT INTO opponent_entity (name,
                             base_rizz,
                             base_strength,
                             base_agility,
                             base_endurance,
                             base_luck,
                             image_path)
VALUES
    ('Gruba',       6, 9, 1, 40, 1, 'opponent_gruba.png'),
    ('Dresiarz',    9, 11, 5, 35, 2, 'opponent_dresiarz.png'),
    ('Hiszpan',    13, 9, 6, 30, 2, 'opponent_hiszpan.png'),
    ('Ochroniarz',  10, 12, 4, 50, 2, 'opponent_ochroniarz.png'),
    ('Blondyna',    15, 7, 7, 28, 3, 'opponent_blondyna.png'),
    ('Menel',    10, 9, 6,   35, 10, 'opponent_menel.png'),
    ('DJ',    13, 11, 5, 45, 4, 'opponent_dj.png');




INSERT INTO quest_entity (title,
                          description,
                          quest_tier,
                          quest_type,
                          opponent_id,
                          image_path)
VALUES
    ('Rizzowanie grubej', 'opis', 'EASY', 'RIZZ_FIGHT', 1, 'rizz_quest1.png'),
    ('Wyjaśnij Hiszpana', 'opis', 'EASY', 'STRENGTH_FIGHT', 3, 'fight_quest6.png'),
    ('Pozbądź się menela', 'Jakiś menel wtargnął nam do lokalu. Pozbędziesz się go?', 'EASY', 'STRENGTH_FIGHT', 6, 'fight_quest7.png'),
    ('Ogarnij DJ-a', 'Przemów do rozsądku temu DJ-owi, żeby puścił coś normalnego.', 'EASY', 'STRENGTH_FIGHT', 7, 'fight_quest8.png'),

    ('Erazmus z Hiszpanii', 'opis', 'MEDIUM', 'RIZZ_FIGHT', 3, 'rizz_quest2.png'),
    ('Gruba ma problem', 'Ktoś zapytał się czy gruba pije i teraz trzeba ją ogarnąć. Załatw to!', 'MEDIUM', 'STRENGTH_FIGHT', 1, 'fight_quest5.png'),
    ('Dresiarz robi młyn', 'opis', 'MEDIUM', 'STRENGTH_FIGHT', 2, 'fight_quest2.png'),
    ('Poderwij dresiarza', 'opis', 'MEDIUM', 'RIZZ_FIGHT', 2, 'rizz_quest4.png'),
    ('Samotny menel', 'Szkoda mi się zrobiło tego menela, weź idź z nim zatańcz czy coś.', 'MEDIUM', 'RIZZ_FIGHT', 6, 'rizz_quest7.png'),


    ('Pijany ochroniarz', 'opis', 'HARD', 'STRENGTH_FIGHT', 4, 'fight_quest4.png'),
    ('Poderwij blondynę', 'opis', 'HARD', 'RIZZ_FIGHT', 5, 'rizz_quest4.png'),
    ('Zrizzuj ochroniarza', 'Ja cie nie podpuszczam, ale...', 'HARD', 'RIZZ_FIGHT', 4, 'fight_quest4.png'),
    ('Wywal DJ-a z klubu', 'Wywal tego DJ-a z lokalu, już się nie da słuchać tego co on puszcza!', 'HARD', 'STRENGTH_FIGHT', 7, 'fight_quest8.png');

INSERT INTO character_class_entity (class_name,
                                    base_endurance,
                                    base_rizz,
                                    base_strength,
                                    base_agility,
                                    base_luck,
                                    base_money)
VALUES
    ('Nerd', 20, 1, 1, 5, 30, 100),
    ('Erasmus', 30, 10, 5, 5, 5, 200),
    ('Dres', 40, 5, 10, 1, 1, 50);

INSERT INTO base_vehicle_entity (name,
                                 image_path,
                                 price,
                                 time_reduction_percent)
VALUES
    ("Rower składak", "bicycle.png", 5, 10),
    ("Hulajnoga ksiajomi", "scooter.png", 10, 15),
    ("Sejczento", "sejczento.png", 20, 25),
    ("Olep asra", "olep_asra.png", 50, 50),
    ("Bwm f67", "bwm_f67.png", 100, 60),
    ("Traktor USRUS", "tractor.png", 200, 90);