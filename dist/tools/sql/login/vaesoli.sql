ALTER TABLE `characters` ADD `email` varchar(255) NOT NULL,
`lastVote` bigint(20) NOT NULL DEFAULT '0',
`votes` int(10) NOT NULL DEFAULT '0',
`nodelete` smallint(1) NOT NULL DEFAULT '0';