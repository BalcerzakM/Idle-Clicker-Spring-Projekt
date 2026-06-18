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
    base_agility   INTEGER                                                                       NOT NULL,
    base_endurance INTEGER                                                                       NOT NULL,
    base_luck      INTEGER                                                                       NOT NULL,
    base_price     INTEGER                                                                       NOT NULL,
    base_rizz      INTEGER                                                                       NOT NULL,
    base_strength  INTEGER                                                                       NOT NULL,
    id             BIGINT                                                                        NOT NULL AUTO_INCREMENT,
    description    VARCHAR(255),
    image_path     VARCHAR(255)                                                                  NOT NULL,
    name           VARCHAR(255)                                                                  NOT NULL,
    item_type      ENUM ('EQUIPMENT','ITEM_TOKEN')                                               NOT NULL,
    slot_type      ENUM ('EMBLEM','FEET','HEAD','LOWER_BODY','NECK','NONE','UPPER_BODY','WRIST') NOT NULL,
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
CREATE TABLE equipment_item
(
    character_id BIGINT                                                                        NOT NULL,
    id           BIGINT                                                                        NOT NULL AUTO_INCREMENT,
    item_id      BIGINT                                                                        NOT NULL,
    slot         ENUM ('EMBLEM','FEET','HEAD','LOWER_BODY','NECK','NONE','UPPER_BODY','WRIST') NOT NULL,
    PRIMARY KEY (id)
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
    ADD CONSTRAINT uklsnddfmp8upx8vkv1snfh0a2r UNIQUE (item_id);
ALTER TABLE base_item_entity
    ADD CONSTRAINT ukc5779hv4tgqwir145qsrdbnjo UNIQUE (name);
ALTER TABLE base_vehicle_entity
    ADD CONSTRAINT ukkkrahlhw2olrsoi02q5blnuhp UNIQUE (name);
ALTER TABLE character_class_entity
    ADD CONSTRAINT ukisjrcjqucvxes6n0pue7cywmh UNIQUE (class_name);
ALTER TABLE character_entity
    ADD CONSTRAINT ukyko7noskna2ili8vk3e0ombq UNIQUE (active_quest_id);
ALTER TABLE character_entity
    ADD CONSTRAINT ukp58e3os649et88jejco39ktqf UNIQUE (active_vehicle_id);
ALTER TABLE character_entity
    ADD CONSTRAINT ukjx3i4nay0f00ue7s6pak2qxfm UNIQUE (bouncer_duty_id);
ALTER TABLE character_entity
    ADD CONSTRAINT ukheta67icofutts7yfjm5vebvv UNIQUE (name);
ALTER TABLE equipment_item
    ADD CONSTRAINT uk55mno4vattoea0punlvt8g7uc UNIQUE (character_id, slot);
ALTER TABLE equipment_item
    ADD CONSTRAINT ukp8j9tkx5eiptbrx84abhhn5v8 UNIQUE (item_id);
ALTER TABLE opponent_entity
    ADD CONSTRAINT ukteektgyhlupyxibgqew61w54e UNIQUE (name);
ALTER TABLE shop_offer_entity
    ADD CONSTRAINT uksqwwsthmkeuwjudlgvhic1atd UNIQUE (item_id);
ALTER TABLE user_entity
    ADD CONSTRAINT uk4xad1enskw4j1t2866f7sodrx UNIQUE (email);
ALTER TABLE user_entity
    ADD CONSTRAINT uk2jsk4eakd0rmvybo409wgwxuw UNIQUE (username);
ALTER TABLE active_quest_entity
    ADD CONSTRAINT fk71svi1v1dry0jekjdoqme4xyo FOREIGN KEY (opponent_id) REFERENCES opponent_entity (id);
ALTER TABLE active_vehicle_entity
    ADD CONSTRAINT fk6f6uce2nj9e39ra9wkwe26md FOREIGN KEY (base_vehicle_id) REFERENCES base_vehicle_entity (id);
ALTER TABLE backpack_item
    ADD CONSTRAINT fk5toi1aujoouxec6hid2qwc1wx FOREIGN KEY (item_id) REFERENCES item_entity (id);
ALTER TABLE backpack_item
    ADD CONSTRAINT fkiaeegd901buycrfx50ql1u8rc FOREIGN KEY (character_id) REFERENCES character_entity (id);
ALTER TABLE character_entity
    ADD CONSTRAINT fk4wolqf8shj0e6qr4q8g9996rx FOREIGN KEY (active_quest_id) REFERENCES active_quest_entity (id);
ALTER TABLE character_entity
    ADD CONSTRAINT fkainfyry5jqipc1epxdd3olnv0 FOREIGN KEY (active_vehicle_id) REFERENCES active_vehicle_entity (id);
ALTER TABLE character_entity
    ADD CONSTRAINT fkrhpo3pccewd738omdrw6bgfwc FOREIGN KEY (bouncer_duty_id) REFERENCES bouncer_duty_entity (id);
ALTER TABLE character_entity
    ADD CONSTRAINT fkt3sjwa1i8kfqgbxi7tim4wxhc FOREIGN KEY (character_class_id) REFERENCES character_class_entity (id);
ALTER TABLE character_entity
    ADD CONSTRAINT fk67arbvwqttkf55kybokn3fvgx FOREIGN KEY (user_id) REFERENCES user_entity (id);
ALTER TABLE equipment_item
    ADD CONSTRAINT fk6dr2um3h2hbveun47astn10p2 FOREIGN KEY (item_id) REFERENCES item_entity (id);
ALTER TABLE equipment_item
    ADD CONSTRAINT fk44k8s4drbmy38x4ulhhnr1ps5 FOREIGN KEY (character_id) REFERENCES character_entity (id);
ALTER TABLE item_entity
    ADD CONSTRAINT fkre9sqek7y3q3vklt27kn5cwap FOREIGN KEY (base_item_id) REFERENCES base_item_entity (id);
ALTER TABLE quest_entity
    ADD CONSTRAINT fkcomjuy4exx9m0x60fenegwm2d FOREIGN KEY (opponent_id) REFERENCES opponent_entity (id);
ALTER TABLE quest_offer_entity
    ADD CONSTRAINT fk7r1ht67f2jfvs8ofeehw6jqkv FOREIGN KEY (character_id) REFERENCES character_entity (id);
ALTER TABLE quest_offer_entity
    ADD CONSTRAINT fkkxclqn5ukh7wefdcmgx9rpwd8 FOREIGN KEY (quest_id) REFERENCES quest_entity (id);
ALTER TABLE report_entity
    ADD CONSTRAINT fki3gqs7c17bvk71lq7v1df2lrc FOREIGN KEY (user_id) REFERENCES user_entity (id);
ALTER TABLE shop_offer_entity
    ADD CONSTRAINT fkig2kd0ilvm6dvsns208ekcy84 FOREIGN KEY (character_id) REFERENCES character_entity (id);
ALTER TABLE shop_offer_entity
    ADD CONSTRAINT fkfffa897bdo8evtnb5iy317geg FOREIGN KEY (item_id) REFERENCES item_entity (id);
ALTER TABLE transaction_entity
    ADD CONSTRAINT fkmmsoavuac0clvx8rmstmavy6r FOREIGN KEY (character_id) REFERENCES character_entity (id);