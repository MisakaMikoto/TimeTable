CREATE TABLE `schedule` (
  `userId` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `time` tinyint(1) NOT NULL,
  `subject` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`userId`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
