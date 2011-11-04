-- ITEM: OK
ALTER TABLE `items` 
MODIFY COLUMN `loc` varchar(10)  NULL DEFAULT NULL ,
ADD COLUMN `time_of_use` int(11)  NULL DEFAULT NULL ,
DROP INDEX `key_loc`,
ADD INDEX `loc`(`loc`),
DROP INDEX `key_item_id`,
ADD INDEX `item_id`(`item_id`),
ADD INDEX `time_of_use`(`time_of_use`),
DROP INDEX `key_owner_id`,
ADD INDEX `owner_id`(`owner_id`),
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- ITEM ELEMENTALS: OK
ALTER TABLE `item_elementals`
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- MESSAGES: OK
ALTER TABLE `messages` 
CHANGE `isNews` `isReturned` enum('true','false')  NOT NULL DEFAULT 'false' ,
MODIFY COLUMN `expiration` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
CHANGE `isFourStars` `sendBySystem` tinyint(1)  NOT NULL DEFAULT '0' ;

-- CLAN DATA: OK
ALTER TABLE `clan_data` 
MODIFY COLUMN `char_penalty_expiry_time` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `ally_name` varchar(45)  NULL DEFAULT NULL ,
MODIFY COLUMN `dissolving_expiry_time` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `clan_name` varchar(45)  NULL DEFAULT NULL ,
MODIFY COLUMN `ally_penalty_expiry_time` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- SEVEN SIGNS STATUS: OK
ALTER TABLE `seven_signs_status` 
MODIFY COLUMN `date` bigint(13) unsigned  NOT NULL DEFAULT '0' ;

-- PETS: OK
ALTER TABLE `pets` 
MODIFY COLUMN `curHp` int(9) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `level` smallint(2) unsigned  NOT NULL,
MODIFY COLUMN `fed` int(10) unsigned  NULL DEFAULT '0' ,
ADD COLUMN `restore` enum('true','false')  NOT NULL DEFAULT 'false' ,
ADD COLUMN `ownerId` int(10)  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `sp` int(10) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `exp` bigint(20) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `curMp` int(9) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `item_obj_id` int(10) unsigned  NOT NULL,
DROP COLUMN `armor`, -- ?
DROP COLUMN `jewel`, -- ?
DROP COLUMN `weapon`; -- ?

-- CHARACTER SKILLS: OK
ALTER TABLE `character_skills`
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- DROP COLUMN `verified`;

-- CASTLE FUNCTIONS: OK
ALTER TABLE `castle_functions` 
MODIFY COLUMN `endTime` bigint(13) unsigned  NOT NULL DEFAULT '0' ;

-- CASTLE: OK
ALTER TABLE `castle` 
MODIFY COLUMN `siegeDate` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
ADD COLUMN `bloodAlliance` int(3)  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `regTimeEnd` bigint(13) unsigned  NOT NULL DEFAULT '0' ;

-- CUSTOM NPCSKILLS: OK
-- ALTER TABLE `custom_npcskills` 
-- MODIFY COLUMN `npcid` smallint(5) unsigned  NOT NULL DEFAULT '0' ;

-- CUSTOM SPAWNLIST: 
ALTER TABLE `custom_spawnlist` 
MODIFY COLUMN `npc_templateid` mediumint(7) unsigned  NOT NULL DEFAULT '0' ,
DROP COLUMN `id`,
DROP INDEX `key_npc_templateid`,
DROP PRIMARY KEY;

-- CLAN HALL: OK
ALTER TABLE `clanhall` 
MODIFY COLUMN `paidUntil` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `ownerId` int(11)  NOT NULL DEFAULT '0' ,
ADD INDEX `ownerId`(`ownerId`);

-- CLAN HALL FUNCTIONS: OK
ALTER TABLE `clanhall_functions` 
MODIFY COLUMN `endTime` bigint(13) unsigned  NOT NULL DEFAULT '0' ;

-- CLAN SKILLS: OK
ALTER TABLE `clan_skills` 
MODIFY COLUMN `sub_pledge_id` int(11)  NOT NULL DEFAULT '-2' ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`clan_id`,`skill_id`,`sub_pledge_id`);

-- CHARACTER MACROSES: OK
ALTER TABLE `character_macroses` 
MODIFY COLUMN `commands` varchar(500)  NULL DEFAULT NULL ;

-- CUSTOM NPCAIDATA: OK
ALTER TABLE `custom_npcaidata` 
CHANGE `clan_range` `clanRange` smallint(4) unsigned  NULL DEFAULT '0' ,
CHANGE `spiritshot` `spiritShot` smallint(4) unsigned  NULL DEFAULT '0' ,
ADD COLUMN `minSkillChance` tinyint(3) unsigned  NOT NULL DEFAULT '7' ,
CHANGE `sschance` `ssChance` tinyint(3) unsigned  NULL DEFAULT '0' ,
CHANGE `soulshot` `soulShot` smallint(4) unsigned  NULL DEFAULT '0' ,
CHANGE `spschance` `spsChance` tinyint(3) unsigned  NULL DEFAULT '0' ,
CHANGE `primary_attack` `primarySkillId` smallint(5) unsigned  NULL DEFAULT '0' ,
CHANGE `ischaos` `isChaos` smallint(4) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `dodge` tinyint(3) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `canMove` tinyint(1) unsigned  NOT NULL DEFAULT '1' ,
CHANGE `skill_chance` `maxSkillChance` tinyint(3) unsigned  NOT NULL DEFAULT '15' ,
CHANGE `ai_type` `aiType` varchar(8)  NOT NULL DEFAULT 'fighter' ,
CHANGE `maxrangechance` `maxRangeChance` tinyint(3) unsigned  NULL DEFAULT '0' ,
CHANGE `maxrangeskill` `maxRangeSkill` smallint(5) unsigned  NULL DEFAULT '0' ,
CHANGE `minrangechance` `minRangeChance` tinyint(3) unsigned  NULL DEFAULT '0' ,
MODIFY COLUMN `enemyRange` smallint(4) unsigned  NULL DEFAULT '0' ,
CHANGE `minrangeskill` `minRangeSkill` smallint(5) unsigned  NULL DEFAULT '0' ,
CHANGE `npc_id` `npcId` mediumint(7) unsigned  NOT NULL,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`npcId`);

-- CUSTOM MERCHANT BUYLISTS: OK
ALTER TABLE `custom_merchant_buylists` 
MODIFY COLUMN `savetimer` bigint(13) unsigned  NOT NULL DEFAULT '0' ;

-- CHARACTER SUBCLASSES: OK
-- ALTER TABLE `character_subclasses` 
-- DROP COLUMN `verified`;

-- CLAN SUBPLEDGES: OK
ALTER TABLE `clan_subpledges` 
MODIFY COLUMN `leader_id` int(11)  NOT NULL DEFAULT '0' ,
ADD INDEX `leader_id`(`leader_id`);

-- CUSTOM NPC: OK
ALTER TABLE `custom_npc` 
ADD COLUMN `targetable` tinyint(1)  NOT NULL DEFAULT '1' ,
MODIFY COLUMN `patk` decimal(12,5)  NULL DEFAULT NULL ,
MODIFY COLUMN `hpreg` decimal(30,15)  NULL DEFAULT NULL ,
ADD COLUMN `critical` tinyint(1)  NOT NULL DEFAULT '1' ,
MODIFY COLUMN `mdef` decimal(12,5)  NULL DEFAULT NULL ,
MODIFY COLUMN `pdef` decimal(12,5)  NULL DEFAULT NULL ,
MODIFY COLUMN `mp` decimal(30,15)  NULL DEFAULT NULL ,
MODIFY COLUMN `matk` decimal(12,5)  NULL DEFAULT NULL ,
ADD COLUMN `show_name` tinyint(1)  NOT NULL DEFAULT '1' ,
MODIFY COLUMN `runspd` decimal(10,5)  NOT NULL DEFAULT '120.00000' ,
MODIFY COLUMN `mpreg` decimal(30,15)  NULL DEFAULT NULL ,
MODIFY COLUMN `sex` enum('etc','female','male')  NOT NULL DEFAULT 'etc' ,
MODIFY COLUMN `walkspd` decimal(10,5)  NOT NULL DEFAULT '60.00000' ,
MODIFY COLUMN `hp` decimal(30,15)  NULL DEFAULT NULL ;

-- CUSTOM DROPLIST: OK
-- ALTER TABLE `custom_droplist` 
-- MODIFY COLUMN `mobId` smallint(5) unsigned  NOT NULL DEFAULT '0' ;

-- CLAN WARS: OK
ALTER TABLE `clan_wars` 
MODIFY COLUMN `clan1` varchar(35)  NOT NULL DEFAULT '' ,
MODIFY COLUMN `clan2` varchar(35)  NOT NULL DEFAULT '' ,
ADD INDEX `clan1`(`clan1`),
ADD INDEX `clan2`(`clan2`);

-- CHARACTERS: OK
ALTER TABLE `characters` 
MODIFY COLUMN `account_name` varchar(45)  NULL DEFAULT NULL ,
MODIFY COLUMN `online` tinyint(3) unsigned  NULL DEFAULT NULL ,
MODIFY COLUMN `language` varchar(2)  NULL DEFAULT NULL ,
MODIFY COLUMN `title` varchar(16)  NULL DEFAULT NULL ,
MODIFY COLUMN `title_color` mediumint(8) UNSIGNED NOT NULL default '16777079' AFTER `title`,
MODIFY COLUMN `lastAccess` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `clan_join_expiry_time` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `deletetime` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `clan_create_expiry_time` bigint(13) unsigned  NOT NULL DEFAULT '0' ,
MODIFY COLUMN `char_name` varchar(35)  NOT NULL,
CHANGE `createTime` `createDate` date  NOT NULL DEFAULT '2007-06-10' ,
-- DROP COLUMN `voting`,
DROP COLUMN `last_recom_date`,
-- DROP COLUMN `showFsDamages`,
DROP COLUMN `rec_left`,
DROP COLUMN `rec_have`,
-- DROP COLUMN `verified`,
ADD INDEX `account_name`(`account_name`),
ADD INDEX `online`(`online`),
ADD INDEX `char_name`(`char_name`),
DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- change Engine
ALTER TABLE `castle`
ENGINE=InnoDB;
ALTER TABLE `castle_functions`
ENGINE=InnoDB;
ALTER TABLE `castle_manor_procure`
ENGINE=InnoDB;
ALTER TABLE `castle_manor_production`
ENGINE=InnoDB;
ALTER TABLE `characters`
ENGINE=InnoDB;
ALTER TABLE `character_friends`
ENGINE=InnoDB;
ALTER TABLE `character_hennas`
ENGINE=InnoDB;
ALTER TABLE `character_macroses`
ENGINE=InnoDB;
ALTER TABLE `character_quests`
ENGINE=InnoDB;
ALTER TABLE `character_quest_global_data`
ENGINE=InnoDB;
ALTER TABLE `character_recipebook`
ENGINE=InnoDB;
ALTER TABLE `character_recipeshoplist`
ENGINE=InnoDB;
ALTER TABLE `character_shortcuts`
ENGINE=InnoDB;
ALTER TABLE `character_skills`
ENGINE=InnoDB;
ALTER TABLE `character_subclasses`
ENGINE=InnoDB;
ALTER TABLE `clanhall_functions`
ENGINE=InnoDB;
ALTER TABLE `clan_data`
ENGINE=InnoDB;
ALTER TABLE `clanhall`
ENGINE=InnoDB;
ALTER TABLE `clan_privs`
ENGINE=InnoDB;
ALTER TABLE `clan_skills`
ENGINE=InnoDB;
ALTER TABLE `clan_subpledges`
ENGINE=InnoDB;
ALTER TABLE `clan_wars`
ENGINE=InnoDB;
ALTER TABLE `custom_droplist`
ENGINE=InnoDB;
ALTER TABLE `custom_merchant_buylists`
ENGINE=InnoDB;
ALTER TABLE `custom_merchant_shopids`
ENGINE=InnoDB;
ALTER TABLE `custom_npc`
ENGINE=InnoDB;
ALTER TABLE `custom_npcskills`
ENGINE=InnoDB;
ALTER TABLE `custom_spawnlist`
ENGINE=InnoDB;
ALTER TABLE `custom_teleport`
ENGINE=InnoDB;
ALTER TABLE `items`
ENGINE=InnoDB;
ALTER TABLE `item_attributes`
ENGINE=InnoDB;
ALTER TABLE `item_elementals`
ENGINE=InnoDB;
ALTER TABLE `messages`
ENGINE=InnoDB;
ALTER TABLE `mods_wedding`
ENGINE=InnoDB;
ALTER TABLE `pets`
ENGINE=InnoDB;
ALTER TABLE `seven_signs`
ENGINE=InnoDB;
ALTER TABLE `seven_signs_status`
ENGINE=InnoDB;