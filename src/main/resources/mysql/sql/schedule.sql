CREATE TABLE `schedule` (
    `userId` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
    `day` varchar(9) COLLATE utf8_unicode_ci NOT NULL,
    `time` tinyint(1) NOT NULL,
    `subjectIndex` tinyint(1) NOT NULL,
    PRIMARY KEY (`userId`,`day`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
