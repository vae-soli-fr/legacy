-- ----------------------------
-- Table structure for hellbound
-- ----------------------------
CREATE TABLE IF NOT EXISTS `hellbound` (
  `name` INT UNSIGNED NOT NULL DEFAULT 0,
  `trustLevel` INT UNSIGNED NOT NULL DEFAULT 0,
  `zonesLevel` INT UNSIGNED NOT NULL DEFAULT 1,
  `unlocked` INT UNSIGNED NOT NULL DEFAULT 0,
  `dummy` INT UNSIGNED NOT NULL DEFAULT 0
);