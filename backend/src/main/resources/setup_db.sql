CREATE DATABASE IF NOT EXISTS Game CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'game_admin_user'@'%' IDENTIFIED BY 'KochamWymyslacHaslaUzytkownikowPoNocach123$';
GRANT ALL PRIVILEGES ON Game.* TO 'game_admin_user'@'%';

CREATE USER IF NOT EXISTS 'game_app_user'@'%' IDENTIFIED BY 'JeszczeBardziejKochamSystemyBazDanych123$';
GRANT SELECT, INSERT, UPDATE, DELETE ON Game.* TO 'game_app_user'@'%';

FLUSH PRIVILEGES;