CREATE TABLE IF NOT EXISTS `siegable_hall_flagwar_attackers_members` (
`hall_id` tinyint(2) unsigned NOT NULL DEFAULT '0',
`clan_id` int(10) unsigned NOT NULL DEFAULT '0',
`object_id` int(10) unsigned NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
