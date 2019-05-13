CREATE TABLE `course_info` (
  `course_title_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `source` varchar(100) DEFAULT NULL,
  `processed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`course_title_id`),
  UNIQUE KEY `course_title_id_UNIQUE` (`course_title_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `topic_info` (
  `topic_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `course_id_fk` int(10) unsigned NOT NULL,
  PRIMARY KEY (`topic_id`),
  UNIQUE KEY `topic_id_UNIQUE` (`topic_id`),
  KEY `topic_course_fk_idx` (`course_id_fk`),
  CONSTRAINT `topic_info_course_info` FOREIGN KEY (`course_id_fk`) REFERENCES `course_info` (`course_title_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `subtopic_info` (
  `sub_topic_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `content` blob,
  `topic_id_fk` int(10) unsigned NOT NULL,
  PRIMARY KEY (`sub_topic_id`),
  UNIQUE KEY `sub_topic_id_UNIQUE` (`sub_topic_id`),
  KEY `subtopic_info_idx` (`topic_id_fk`),
  CONSTRAINT `subtopic_info` FOREIGN KEY (`topic_id_fk`) REFERENCES `topic_info` (`topic_id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
