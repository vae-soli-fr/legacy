CREATE TABLE IF NOT EXISTS `clan_data` (
	`clan_id` INT NOT NULL DEFAULT '0',
	`clan_level` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`hasCastle` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`hasFortress` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`hasHideout` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`ally_id` INT NOT NULL DEFAULT '0',
	`crest` VARBINARY(256) NULL DEFAULT NULL,
	`largecrest` mediumblob,
	`reputation_score` INT NOT NULL DEFAULT '0',
	`warehouse` INT NOT NULL DEFAULT '0',
	`expelled_member` INT UNSIGNED NOT NULL DEFAULT '0',
	`leaved_ally` INT UNSIGNED NOT NULL DEFAULT '0',
	`dissolved_ally` INT UNSIGNED NOT NULL DEFAULT '0',
	`auction_bid_at` INT NOT NULL DEFAULT '0',
	`airship` SMALLINT NOT NULL DEFAULT '-1',
	PRIMARY KEY (`clan_id`),
	KEY `ally_id` (`ally_id`)
) ENGINE=MyISAM;
