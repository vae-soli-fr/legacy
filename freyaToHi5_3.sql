-- donn�es dans les tables
DELETE FROM `clanhall` WHERE `clanhall`.`id` = 21;
DELETE FROM `clanhall` WHERE `clanhall`.`id` = 34;
DELETE FROM `clanhall` WHERE `clanhall`.`id` = 35;
DELETE FROM `clanhall` WHERE `clanhall`.`id` = 62;
DELETE FROM `clanhall` WHERE `clanhall`.`id` = 63;
DELETE FROM `clanhall` WHERE `clanhall`.`id` = 64;

REPLACE INTO `custom_npc` (`id`, `idTemplate`, `name`, `serverSideName`, `title`, `serverSideTitle`, `class`, `collision_radius`, `collision_height`, `level`, `sex`, `type`, `attackrange`, `hp`, `mp`, `hpreg`, `mpreg`, `str`, `con`, `dex`, `int`, `wit`, `men`, `exp`, `sp`, `patk`, `pdef`, `matk`, `mdef`, `atkspd`, `critical`, `aggro`, `matkspd`, `rhand`, `lhand`, `enchant`, `walkspd`, `runspd`, `targetable`, `show_name`, `dropHerbGroup`, `basestats`) VALUES
(50007, 31324, 'Andromeda', 1, 'L2J Wedding Manager', 1, 'NPC.a_casino_FDarkElf', '8.00', '23.00', 70, 'female', 'L2WeddingManager', 40, '2444.000000000000000', '2444.000000000000000', '0.000000000000000', '0.000000000000000', 10, 10, 10, 10, 10, 10, 0, 0, '500.00000', '500.00000', '500.00000', '500.00000', 278, 1, 0, 333, 0, 0, 0, '28.00000', '120.00000', 0, 0, 0, 0),
(70010, 31606, 'Catrina', 1, 'L2J TvT Event Manager', 1, 'Monster2.queen_of_cat', '8.00', '15.00', 70, 'female', 'L2TvTEventNpc', 40, '2444.000000000000000', '2444.000000000000000', '0.000000000000000', '0.000000000000000', 10, 10, 10, 10, 10, 10, 0, 0, '500.00000', '500.00000', '500.00000', '500.00000', 278, 1, 0, 333, 0, 0, 0, '28.00000', '120.00000', 0, 0, 0, 0),
(900100, 20432, 'Elpy', 1, '', 1, 'LineageMonster.elpy', '5.00', '4.50', 1, 'male', 'L2EventMonster', 40, '40.000000000000000', '36.000000000000000', '3.160000000000000', '0.910000000000000', 40, 43, 30, 21, 20, 20, 35, 2, '8.00000', '40.00000', '7.00000', '25.00000', 230, 1, 0, 333, 0, 0, 0, '50.00000', '80.00000', 0, 0, 0, 0),
(900101, 32365, 'Snow', 1, 'Event Manager', 1, 'LineageNPC2.TP_game_staff', '5.00', '12.50', 70, 'male', 'L2Npc', 40, '2444.000000000000000', '1225.000000000000000', '0.000000000000000', '0.000000000000000', 40, 43, 30, 21, 20, 20, 0, 0, '1086.00000', '471.00000', '749.00000', '313.00000', 230, 1, 0, 333, 0, 0, 0, '68.00000', '109.00000', 1, 1, 0, 0),
(900102, 13098, 'Event Treasure Chest', 1, '', 1, 'LineageMonster.mimic_even', '8.50', '8.50', 80, 'male', 'L2EventChest', 40, '2880.000000000000000', '1524.000000000000000', '0.000000000000000', '0.000000000000000', 40, 43, 30, 21, 20, 20, 0, 0, '1499.00000', '577.00000', '1035.00000', '384.00000', 230, 1, 0, 253, 0, 0, 0, '1.00000', '1.00000', 0, 0, 0, 0),
(900103, 32365, 'Start', 1, 'Event Manager', 1, 'LineageNPC2.TP_game_staff', '5.00', '12.50', 70, 'male', 'L2Npc', 40, '2444.000000000000000', '1225.000000000000000', '0.000000000000000', '0.000000000000000', 40, 43, 30, 21, 20, 20, 0, 0, '1086.00000', '471.00000', '749.00000', '313.00000', 230, 1, 0, 333, 0, 0, 0, '68.00000', '109.00000', 0, 0, 0, 0),
(900104, 32365, 'Finish', 1, 'Event Manager', 1, 'LineageNPC2.TP_game_staff', '5.00', '12.50', 70, 'male', 'L2Npc', 40, '2444.000000000000000', '1225.000000000000000', '0.000000000000000', '0.000000000000000', 40, 43, 30, 21, 20, 20, 0, 0, '1086.00000', '471.00000', '749.00000', '313.00000', 230, 1, 0, 333, 0, 0, 0, '68.00000', '109.00000', 0, 0, 0, 0),
(1000003, 32226, 'Shiela', 1, 'L2J NPC Buffer', 1, 'LineageNPC2.K_F1_grand', '11.00', '22.25', 70, 'male', 'L2NpcBuffer', 40, '2444.000000000000000', '2444.000000000000000', '0.000000000000000', '0.000000000000000', 10, 10, 10, 10, 10, 10, 0, 0, '500.00000', '500.00000', '500.00000', '500.00000', 278, 1, 0, 333, 0, 0, 0, '28.00000', '120.00000', 0, 0, 0, 0);
