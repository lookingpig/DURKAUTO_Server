-- lose the key detection
SET FOREIGN_KEY_CHECKS=0;

-- Creating a database
DROP DATABASE IF EXISTS `durkauto_shops`;
CREATE DATABASE `durkauto_shops`;

-- Use new database
USE `durkauto_shops`;

-- Create a user table
DROP TABLE IF EXISTS `durkauto_user`;
CREATE TABLE `durkauto_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `realname` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Establish the root user
insert into `durkauto_user` values(null, 'root', 'root', now(), null, null, '¸ùÓÃ»§');