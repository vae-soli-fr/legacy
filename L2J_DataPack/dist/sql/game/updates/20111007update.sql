ALTER TABLE `auto_announcements` ADD `isCritical` enum('true','false') NOT NULL DEFAULT 'false' AFTER `memo`;
