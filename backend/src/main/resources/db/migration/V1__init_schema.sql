CREATE TABLE active_quest_entity
(
    bonus_aura  INTEGER                              NOT NULL,
    bonus_money INTEGER                              NOT NULL,
    end_time    DATETIME(6)                          NOT NULL,
    id          BIGINT                               NOT NULL AUTO_INCREMENT,
    opponent_id BIGINT                               NOT NULL,
    start_time  DATETIME(6)                          NOT NULL,
    image_path  VARCHAR(255)                         NOT NULL,
    title       VARCHAR(255)                         NOT NULL,
    quest_tier  ENUM ('BOSS','EASY','HARD','MEDIUM') NOT NULL,
    quest_type  ENUM ('RIZZ_FIGHT','STRENGTH_FIGHT') NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE active_vehicle_entity
(
    base_vehicle_id BIGINT      NOT NULL,
    expiry_time     DATETIME(6) NOT NULL,
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE backpack_item
(
    character_id BIGINT NOT NULL,
    id           BIGINT NOT NULL AUTO_INCREMENT,
    item_id      BIGINT NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE base_item_entity
(
    base_agility        INTEGER                                                                       NOT NULL,
    base_endurance      INTEGER                                                                       NOT NULL,
    base_luck           INTEGER                                                                       NOT NULL,
    base_price          INTEGER                                                                       NOT NULL,
    base_rizz           INTEGER                                                                       NOT NULL,
    base_strength       INTEGER                                                                       NOT NULL,
    id                  BIGINT                                                                        NOT NULL AUTO_INCREMENT,
    description         VARCHAR(255),
    image_path          VARCHAR(255)                                                                  NOT NULL,
    name                VARCHAR(255)                                                                  NOT NULL,
    is_premium          BOOLEAN                                                                       NOT NULL DEFAULT FALSE,
    item_type           ENUM ('EQUIPMENT','ITEM_TOKEN', 'DRINK')                                      NOT NULL,
    slot_type           ENUM ('EMBLEM','FEET','HEAD','LOWER_BODY','NECK','NONE','UPPER_BODY','WRIST') NOT NULL,
    duration_in_seconds INTEGER DEFAULT 0,
    effect_type         ENUM ('STAT_BONUS', 'AURA_MULTIPLIER', 'MONEY_MULTIPLIER', 'RIZZ_MULTIPLIER', 'STRENGTH_MULTIPLIER', 'LUCK_MULTIPLIER', 'NONE') DEFAULT 'NONE',
    effect_value        INTEGER DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE base_vehicle_entity
(
    price                  INTEGER      NOT NULL,
    time_reduction_percent INTEGER      NOT NULL CHECK ((time_reduction_percent <= 99) AND (time_reduction_percent >= 1)),
    id                     BIGINT       NOT NULL AUTO_INCREMENT,
    image_path             VARCHAR(255) NOT NULL,
    name                   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE bouncer_duty_entity
(
    reward                  INTEGER     NOT NULL,
    bouncer_duty_end_time   DATETIME(6) NOT NULL,
    bouncer_duty_start_time DATETIME(6) NOT NULL,
    id                      BIGINT      NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE character_class_entity
(
    base_agility   INTEGER      NOT NULL,
    base_endurance INTEGER      NOT NULL,
    base_luck      INTEGER      NOT NULL,
    base_money     INTEGER      NOT NULL,
    base_rizz      INTEGER      NOT NULL,
    base_strength  INTEGER      NOT NULL,
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    class_name     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE character_entity
(
    agility            INTEGER      NOT NULL,
    aura               INTEGER      NOT NULL,
    aura_lvl           INTEGER      NOT NULL,
    cristals           INTEGER      NOT NULL,
    current_boss       INTEGER      NOT NULL,
    endurance          INTEGER      NOT NULL,
    gang_id            BIGINT,
    luck               INTEGER      NOT NULL,
    money              INTEGER      NOT NULL,
    rizz               INTEGER      NOT NULL,
    strength           INTEGER      NOT NULL,
    active_quest_id    BIGINT,
    active_vehicle_id  BIGINT,
    bouncer_duty_id    BIGINT,
    character_class_id BIGINT       NOT NULL,
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    user_id            BIGINT       NOT NULL,
    avatar_picture     VARCHAR(255) NOT NULL,
    name               VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE effect_entity
(
    character_id   BIGINT              NOT NULL,
    effect_end_time   DATETIME(6)      NOT NULL,
    effect_start_time DATETIME(6)      NOT NULL,
    id             BIGINT              NOT NULL AUTO_INCREMENT,
    item_id        BIGINT              NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE equipment_item
(
    character_id BIGINT                                                                        NOT NULL,
    id           BIGINT                                                                        NOT NULL AUTO_INCREMENT,
    item_id      BIGINT                                                                        NOT NULL,
    slot         ENUM ('EMBLEM','FEET','HEAD','LOWER_BODY','NECK','NONE','UPPER_BODY','WRIST') NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE gang_entity
(
cristal_bank            INTEGER                                                             NOT NULL DEFAULT 0,
emblem_picture_path     VARCHAR(255)                                                        NOT NULL,
gang_description        VARCHAR(255)                                                        NOT NULL,
gang_name               VARCHAR(255)                                                        NOT NULL,
gang_to_attack          VARCHAR(255),
leader_id               BIGINT,
last_combat             JSON,
id                      BIGINT                                                              NOT NULL AUTO_INCREMENT,
money_bank              INTEGER                                                             NOT NULL DEFAULT 0,
votes                   INTEGER,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE gang_entity_requests
(
    gang_entity_id          BIGINT NOT NULL,
    requests_id             BIGINT NOT NULL,
    PRIMARY KEY (gang_entity_id, requests_id)
) ENGINE = InnoDB;
CREATE TABLE item_entity
(
    price           INTEGER NOT NULL,
    total_agility   INTEGER NOT NULL,
    total_endurance INTEGER NOT NULL,
    total_luck      INTEGER NOT NULL,
    total_rizz      INTEGER NOT NULL,
    total_strength  INTEGER NOT NULL,
    base_item_id    BIGINT  NOT NULL,
    id              BIGINT  NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE opponent_entity
(
    base_agility   INTEGER      NOT NULL,
    base_endurance INTEGER      NOT NULL,
    base_luck      INTEGER      NOT NULL,
    base_rizz      INTEGER      NOT NULL,
    base_strength  INTEGER      NOT NULL,
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    image_path     VARCHAR(255) NOT NULL,
    name           VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE quest_entity
(
    id          BIGINT                               NOT NULL AUTO_INCREMENT,
    opponent_id BIGINT                               NOT NULL,
    description VARCHAR(255),
    image_path  VARCHAR(255)                         NOT NULL,
    title       VARCHAR(255)                         NOT NULL,
    quest_tier  ENUM ('BOSS','EASY','HARD','MEDIUM') NOT NULL,
    quest_type  ENUM ('RIZZ_FIGHT','STRENGTH_FIGHT') NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE quest_offer_entity
(
    character_id BIGINT NOT NULL,
    id           BIGINT NOT NULL AUTO_INCREMENT,
    quest_id     BIGINT NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE report_entity
(
    created_at  DATETIME(6),
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    description VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE shop_offer_entity
(
    offer_date   DATE   NOT NULL,
    character_id BIGINT NOT NULL,
    id           BIGINT NOT NULL AUTO_INCREMENT,
    item_id      BIGINT NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE transaction_entity
(
    cristals_amount          INTEGER      NOT NULL,
    payment_amount_in_grosze INTEGER      NOT NULL,
    character_id             BIGINT       NOT NULL,
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    transaction_date         DATETIME(6)  NOT NULL,
    package_code             VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
CREATE TABLE user_entity
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    is_banned BOOLEAN     NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
) ENGINE = InnoDB;
ALTER TABLE backpack_item
    ADD CONSTRAINT uk_backpack_item_item_id UNIQUE (item_id);

ALTER TABLE base_item_entity
    ADD CONSTRAINT uk_base_item_entity_name UNIQUE (name);

ALTER TABLE base_vehicle_entity
    ADD CONSTRAINT uk_base_vehicle_entity_name UNIQUE (name);

ALTER TABLE character_class_entity
    ADD CONSTRAINT uk_character_class_entity_class_name UNIQUE (class_name);

ALTER TABLE character_entity
    ADD CONSTRAINT uk_character_entity_active_quest_id UNIQUE (active_quest_id);

ALTER TABLE character_entity
    ADD CONSTRAINT uk_character_entity_active_vehicle_id UNIQUE (active_vehicle_id);

ALTER TABLE character_entity
    ADD CONSTRAINT uk_character_entity_bouncer_duty_id UNIQUE (bouncer_duty_id);

ALTER TABLE character_entity
    ADD CONSTRAINT uk_character_entity_name UNIQUE (name);

ALTER TABLE character_entity
    ADD CONSTRAINT fk_character_entity_gang
        FOREIGN KEY (gang_id)
            REFERENCES gang_entity (id);

ALTER TABLE equipment_item
    ADD CONSTRAINT uk_equipment_item_character_id_slot
        UNIQUE (character_id, slot);

ALTER TABLE equipment_item
    ADD CONSTRAINT uk_equipment_item_item_id
        UNIQUE (item_id);

ALTER TABLE opponent_entity
    ADD CONSTRAINT uk_opponent_entity_name UNIQUE (name);

ALTER TABLE shop_offer_entity
    ADD CONSTRAINT uk_shop_offer_entity_item_id UNIQUE (item_id);

ALTER TABLE user_entity
    ADD CONSTRAINT uk_user_entity_email UNIQUE (email);

ALTER TABLE user_entity
    ADD CONSTRAINT uk_user_entity_username UNIQUE (username);

ALTER TABLE active_quest_entity
    ADD CONSTRAINT fk_active_quest_entity_opponent
        FOREIGN KEY (opponent_id)
            REFERENCES opponent_entity (id);

ALTER TABLE active_vehicle_entity
    ADD CONSTRAINT fk_active_vehicle_entity_base_vehicle
        FOREIGN KEY (base_vehicle_id)
            REFERENCES base_vehicle_entity (id);

ALTER TABLE backpack_item
    ADD CONSTRAINT fk_backpack_item_item
        FOREIGN KEY (item_id)
            REFERENCES item_entity (id);

ALTER TABLE backpack_item
    ADD CONSTRAINT fk_backpack_item_character
        FOREIGN KEY (character_id)
            REFERENCES character_entity (id);

ALTER TABLE character_entity
    ADD CONSTRAINT fk_character_entity_active_quest
        FOREIGN KEY (active_quest_id)
            REFERENCES active_quest_entity (id);

ALTER TABLE character_entity
    ADD CONSTRAINT fk_character_entity_active_vehicle
        FOREIGN KEY (active_vehicle_id)
            REFERENCES active_vehicle_entity (id);

ALTER TABLE character_entity
    ADD CONSTRAINT fk_character_entity_bouncer_duty
        FOREIGN KEY (bouncer_duty_id)
            REFERENCES bouncer_duty_entity (id);

ALTER TABLE character_entity
    ADD CONSTRAINT fk_character_entity_character_class
        FOREIGN KEY (character_class_id)
            REFERENCES character_class_entity (id);

ALTER TABLE character_entity
    ADD CONSTRAINT fk_character_entity_user
        FOREIGN KEY (user_id)
            REFERENCES user_entity (id);

ALTER TABLE equipment_item
    ADD CONSTRAINT fk_equipment_item_item
        FOREIGN KEY (item_id)
            REFERENCES item_entity (id);

ALTER TABLE equipment_item
    ADD CONSTRAINT fk_equipment_item_character
        FOREIGN KEY (character_id)
            REFERENCES character_entity (id);

ALTER TABLE item_entity
    ADD CONSTRAINT fk_item_entity_base_item
        FOREIGN KEY (base_item_id)
            REFERENCES base_item_entity (id);

ALTER TABLE quest_entity
    ADD CONSTRAINT fk_quest_entity_opponent
        FOREIGN KEY (opponent_id)
            REFERENCES opponent_entity (id);

ALTER TABLE quest_offer_entity
    ADD CONSTRAINT fk_quest_offer_entity_character
        FOREIGN KEY (character_id)
            REFERENCES character_entity (id);

ALTER TABLE quest_offer_entity
    ADD CONSTRAINT fk_quest_offer_entity_quest
        FOREIGN KEY (quest_id)
            REFERENCES quest_entity (id);

ALTER TABLE report_entity
    ADD CONSTRAINT fk_report_entity_user
        FOREIGN KEY (user_id)
            REFERENCES user_entity (id);

ALTER TABLE shop_offer_entity
    ADD CONSTRAINT fk_shop_offer_entity_character
        FOREIGN KEY (character_id)
            REFERENCES character_entity (id);

ALTER TABLE shop_offer_entity
    ADD CONSTRAINT fk_shop_offer_entity_item
        FOREIGN KEY (item_id)
            REFERENCES item_entity (id);

ALTER TABLE transaction_entity
    ADD CONSTRAINT fk_transaction_entity_character
        FOREIGN KEY (character_id)
            REFERENCES character_entity (id);

ALTER TABLE gang_entity
    ADD CONSTRAINT fk_gang_entity_leader
        FOREIGN KEY (leader_id)
            REFERENCES character_entity (id);

ALTER TABLE gang_entity_requests
    ADD CONSTRAINT fk_gang_entity_requests_gang
        FOREIGN KEY (gang_entity_id)
            REFERENCES gang_entity (id);

ALTER TABLE gang_entity_requests
    ADD CONSTRAINT fk_gang_entity_requests_character
        FOREIGN KEY (requests_id)
            REFERENCES character_entity (id);
