-- phpMyAdmin SQL Dump
-- version 4.0.5
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1:3306
-- Czas wygenerowania: 07 Lip 2014, 15:46
-- Wersja serwera: 5.6.17-0ubuntu0.14.04.1
-- Wersja PHP: 5.5.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Baza danych: `congregation-assistant`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `acl_class`
--

CREATE TABLE IF NOT EXISTS `acl_class` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `class` (`class`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Zrzut danych tabeli `acl_class`
--

INSERT INTO `acl_class` (`id`, `class`) VALUES
(1, 'pl.exsio.frameset.core.entity.BaseFrame'),
(2, 'pl.exsio.frameset.vaadin.entity.VaadinFrameImpl');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `acl_entry`
--

CREATE TABLE IF NOT EXISTS `acl_entry` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `acl_object_identity` bigint(20) unsigned NOT NULL,
  `ace_order` int(10) unsigned NOT NULL,
  `sid` bigint(20) unsigned NOT NULL,
  `mask` int(11) NOT NULL,
  `granting` tinyint(1) NOT NULL,
  `audit_success` tinyint(1) NOT NULL,
  `audit_failure` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_entry_idx_1` (`acl_object_identity`,`ace_order`),
  KEY `sid` (`sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=71 ;

--
-- Zrzut danych tabeli `acl_entry`
--

INSERT INTO `acl_entry` (`id`, `acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`, `audit_success`, `audit_failure`) VALUES
(1, 1, 0, 1, 16, 1, 1, 1),
(2, 2, 0, 1, 16, 1, 1, 1),
(3, 3, 0, 1, 16, 1, 1, 1),
(4, 4, 0, 1, 16, 1, 1, 1),
(5, 5, 0, 1, 16, 1, 1, 1),
(6, 6, 0, 1, 16, 1, 1, 1),
(7, 7, 0, 1, 16, 1, 1, 1),
(8, 1, 1, 1, 8, 1, 1, 1),
(9, 2, 1, 1, 8, 1, 1, 1),
(10, 3, 1, 1, 8, 1, 1, 1),
(11, 4, 1, 1, 8, 1, 1, 1),
(12, 5, 1, 1, 8, 1, 1, 1),
(13, 6, 1, 1, 8, 1, 1, 1),
(14, 7, 1, 1, 8, 1, 1, 1),
(15, 1, 2, 1, 4, 1, 1, 1),
(16, 2, 2, 1, 4, 1, 1, 1),
(17, 3, 2, 1, 4, 1, 1, 1),
(18, 4, 2, 1, 4, 1, 1, 1),
(19, 5, 2, 1, 4, 1, 1, 1),
(20, 6, 2, 1, 4, 1, 1, 1),
(21, 7, 2, 1, 4, 1, 1, 1),
(22, 1, 3, 1, 2, 1, 1, 1),
(23, 2, 3, 1, 2, 1, 1, 1),
(24, 3, 3, 1, 2, 1, 1, 1),
(25, 4, 3, 1, 2, 1, 1, 1),
(26, 5, 3, 1, 2, 1, 1, 1),
(27, 6, 3, 1, 2, 1, 1, 1),
(28, 7, 3, 1, 2, 1, 1, 1),
(29, 1, 4, 1, 1, 1, 1, 1),
(30, 2, 4, 1, 1, 1, 1, 1),
(31, 3, 4, 1, 1, 1, 1, 1),
(32, 4, 4, 1, 1, 1, 1, 1),
(33, 5, 4, 1, 1, 1, 1, 1),
(34, 6, 4, 1, 1, 1, 1, 1),
(35, 7, 4, 1, 1, 1, 1, 1),
(36, 1, 5, 2, 1, 1, 1, 1),
(37, 2, 5, 2, 1, 1, 1, 1),
(59, 8, 0, 1, 1, 1, 0, 0),
(60, 8, 1, 1, 1, 1, 0, 0),
(61, 8, 2, 1, 16, 1, 0, 0),
(62, 8, 3, 1, 8, 1, 0, 0),
(63, 8, 4, 1, 4, 1, 0, 0),
(64, 8, 5, 1, 2, 1, 0, 0),
(65, 8, 6, 2, 1, 1, 0, 0),
(66, 9, 0, 1, 1, 1, 0, 0),
(67, 10, 0, 1, 1, 1, 0, 0),
(68, 11, 0, 1, 1, 1, 0, 0),
(69, 12, 0, 1, 1, 1, 0, 0),
(70, 13, 0, 1, 1, 1, 0, 0);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `acl_object_identity`
--

CREATE TABLE IF NOT EXISTS `acl_object_identity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `object_id_class` bigint(20) unsigned NOT NULL,
  `object_id_identity` bigint(20) unsigned NOT NULL,
  `parent_object` bigint(20) unsigned DEFAULT NULL,
  `owner_sid` bigint(20) unsigned DEFAULT NULL,
  `entries_inheriting` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_object_identity_idx_1` (`object_id_class`,`object_id_identity`),
  KEY `parent_object` (`parent_object`),
  KEY `owner_sid` (`owner_sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- Zrzut danych tabeli `acl_object_identity`
--

INSERT INTO `acl_object_identity` (`id`, `object_id_class`, `object_id_identity`, `parent_object`, `owner_sid`, `entries_inheriting`) VALUES
(1, 2, 1, NULL, 1, 0),
(2, 2, 2, NULL, 1, 0),
(3, 2, 3, NULL, 1, 0),
(4, 2, 4, NULL, 1, 0),
(5, 2, 5, NULL, 1, 0),
(6, 2, 6, NULL, 1, 0),
(7, 2, 7, NULL, 1, 0),
(8, 2, 8, NULL, 3, 1),
(9, 2, 9, NULL, 3, 1),
(10, 2, 10, NULL, 3, 1),
(11, 2, 11, NULL, 3, 1),
(12, 2, 13, NULL, 3, 1),
(13, 2, 12, NULL, 3, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `acl_sid`
--

CREATE TABLE IF NOT EXISTS `acl_sid` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `principal` tinyint(1) NOT NULL,
  `sid` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `acl_sid_idx_1` (`sid`,`principal`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Zrzut danych tabeli `acl_sid`
--

INSERT INTO `acl_sid` (`id`, `principal`, `sid`) VALUES
(3, 1, 'admin@frameset.com'),
(1, 0, 'ROLE_ADMIN'),
(2, 0, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_preachers`
--

CREATE TABLE IF NOT EXISTS `ca_preachers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone_no` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_kdu88d24t45cty544che467sb` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_preacher_assignments`
--

CREATE TABLE IF NOT EXISTS `ca_preacher_assignments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_active` tinyint(1) NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `preacher_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_obg27dmq08kovbjbfumr4v0fj` (`group_id`),
  KEY `FK_qs8gbwnh7kl82snok4lm28762` (`preacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_preacher_priviledges`
--

CREATE TABLE IF NOT EXISTS `ca_preacher_priviledges` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `priviledge` varchar(255) NOT NULL,
  `preacher_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fbd8jrmboxx39p0uqxqurhw59` (`preacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_service_groups`
--

CREATE TABLE IF NOT EXISTS `ca_service_groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `group_no` bigint(20) NOT NULL,
  `overseer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4ttbyud76dmd7xl5bsu2prs44` (`group_no`),
  KEY `FK_9dvo2pycwed4xho2o5q3txuqg` (`overseer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_terrains`
--

CREATE TABLE IF NOT EXISTS `ca_terrains` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `terrain_no` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueTerrain` (`type`,`terrain_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_terrains_ca_terrain_assignments`
--

CREATE TABLE IF NOT EXISTS `ca_terrains_ca_terrain_assignments` (
  `ca_terrains_id` bigint(20) NOT NULL,
  `assignments_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ca_terrains_id`,`assignments_id`),
  UNIQUE KEY `UK_d6i98socnofoutkd0domd350` (`assignments_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_terrains_ca_terrain_files`
--

CREATE TABLE IF NOT EXISTS `ca_terrains_ca_terrain_files` (
  `ca_terrains_id` bigint(20) NOT NULL,
  `files_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ca_terrains_id`,`files_id`),
  UNIQUE KEY `UK_2janetmmehydaax2rjobrw92p` (`files_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_terrain_assignments`
--

CREATE TABLE IF NOT EXISTS `ca_terrain_assignments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_active` tinyint(1) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `terrain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nrhtskx7upe2wshs1arcw6xtj` (`group_id`),
  KEY `FK_jsqdrijti3j7dhpo01icl6f7k` (`terrain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_terrain_files`
--

CREATE TABLE IF NOT EXISTS `ca_terrain_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `file_data` longblob NOT NULL,
  `file_description` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) NOT NULL,
  `terrain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qpo7v59ri3tqq0id8sct0nf92` (`terrain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ca_terrain_notifications`
--

CREATE TABLE IF NOT EXISTS `ca_terrain_notifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `terrain_assignment_id` bigint(20) NOT NULL,
  `override_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eml25xy4koa1rbb8068xrilqo` (`terrain_assignment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `core_frames`
--

CREATE TABLE IF NOT EXISTS `core_frames` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_default` bit(1) DEFAULT NULL,
  `tree_left` bigint(20) DEFAULT NULL,
  `tree_level` bigint(20) DEFAULT NULL,
  `menu_label` varchar(255) NOT NULL,
  `module_id` varchar(255) DEFAULT NULL,
  `tree_right` bigint(20) DEFAULT NULL,
  `slug` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e6f2uek7gtfks9yybu6m1os1w` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `persistent_logins`
--

CREATE TABLE IF NOT EXISTS `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `persistent_logins`
--

INSERT INTO `persistent_logins` (`username`, `series`, `token`, `last_used`) VALUES
('admin@frameset.com', '2A3hXcGOkfJyK6ZdSP08ug==', 'c90D73c1fuqrG4V2zI3Fkw==', '2014-06-06 05:42:35');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `security_groups`
--

CREATE TABLE IF NOT EXISTS `security_groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Zrzut danych tabeli `security_groups`
--

INSERT INTO `security_groups` (`id`, `group_name`) VALUES
(1, 'Admins'),
(2, 'Users');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `security_groups_roles`
--

CREATE TABLE IF NOT EXISTS `security_groups_roles` (
  `group_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`role_id`),
  KEY `FK_8ypp543ldubcu4lfddgw0c8ej` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `security_groups_roles`
--

INSERT INTO `security_groups_roles` (`group_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `security_roles`
--

CREATE TABLE IF NOT EXISTS `security_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_label` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Zrzut danych tabeli `security_roles`
--

INSERT INTO `security_roles` (`id`, `role_label`, `role_name`) VALUES
(1, 'Administrator', 'ROLE_ADMIN'),
(2, 'User', 'ROLE_USER');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `security_users`
--

CREATE TABLE IF NOT EXISTS `security_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `is_enabled` tinyint(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Zrzut danych tabeli `security_users`
--

INSERT INTO `security_users` (`id`, `first_name`, `is_enabled`, `last_name`, `password`, `username`) VALUES
(1, 'Application', 1, 'Administrator', '$2a$10$q1jYQ2xdqywKqlxiINu0xujkYLLn7rRSUtbzavThYunDC.8bafKcq', 'admin@frameset.com'),
(2, 'John', 1, 'Doe', '$2a$10$M8GnZv6AlK0OkgqC.dLXLOOzmlW6PjjS9VdMexFAiPNAIzDnMa3uS', 'user@frameset.com');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `security_users_groups`
--

CREATE TABLE IF NOT EXISTS `security_users_groups` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `FK_a76lfqakcs10ccy81qowus1q0` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Zrzut danych tabeli `security_users_groups`
--

INSERT INTO `security_users_groups` (`user_id`, `group_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `security_users_roles`
--

CREATE TABLE IF NOT EXISTS `security_users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_h0w5is7i2gixt4bk8jfey8743` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `vaadin_frames`
--

CREATE TABLE IF NOT EXISTS `vaadin_frames` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `icon` varchar(255) DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT NULL,
  `tree_left` bigint(20) DEFAULT NULL,
  `tree_level` bigint(20) DEFAULT NULL,
  `menu_label` varchar(255) NOT NULL,
  `module_id` varchar(255) DEFAULT NULL,
  `tree_right` bigint(20) DEFAULT NULL,
  `slug` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hxb6jbp6flyvak2xd6kv59v9j` (`parent_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Zrzut danych tabeli `vaadin_frames`
--

INSERT INTO `vaadin_frames` (`id`, `icon`, `is_default`, `tree_left`, `tree_level`, `menu_label`, `module_id`, `tree_right`, `slug`, `title`, `parent_id`) VALUES
(1, NULL, 0, 1, 0, '', '', 16, '', 'FramesTree', NULL),
(2, 'HOME', 1, 3, 2, 'core.frames.home', 'homeModule', 4, 'home', 'core.frames.home', 8),
(3, 'COGS', 0, 6, 1, 'core.frames.settings', NULL, 15, 'settings', 'core.frames.settings', 1),
(4, 'USER', 0, 7, 2, 'core.frames.settings.users', 'manageUsersModule', 8, 'users', 'core.frames.settings.users', 3),
(5, 'USERS', 0, 9, 2, 'core.frames.settings.groups', 'manageGroupsModule', 10, 'groups', 'core.frames.settings.groups', 3),
(6, 'LOCK', 0, 11, 2, 'core.frames.settings.roles', 'manageRolesModule', 12, 'roles', 'core.frames.settings.roles', 3),
(7, 'SITEMAP', 0, 13, 2, 'core.frames.settings.frames', 'manageFramesModule', 14, 'frames', 'core.frames.settings.frames', 3),
(8, 'HOME', 0, 2, 1, 'core.frames.start', NULL, 5, '', 'core.frames.start', 1);

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `acl_entry`
--
ALTER TABLE `acl_entry`
  ADD CONSTRAINT `acl_entry_ibfk_1` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`),
  ADD CONSTRAINT `acl_entry_ibfk_2` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`);

--
-- Ograniczenia dla tabeli `acl_object_identity`
--
ALTER TABLE `acl_object_identity`
  ADD CONSTRAINT `acl_object_identity_ibfk_1` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  ADD CONSTRAINT `acl_object_identity_ibfk_2` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`),
  ADD CONSTRAINT `acl_object_identity_ibfk_3` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`);

--
-- Ograniczenia dla tabeli `ca_preachers`
--
ALTER TABLE `ca_preachers`
  ADD CONSTRAINT `FK_kdu88d24t45cty544che467sb` FOREIGN KEY (`user_id`) REFERENCES `security_users` (`id`);

--
-- Ograniczenia dla tabeli `ca_preacher_assignments`
--
ALTER TABLE `ca_preacher_assignments`
  ADD CONSTRAINT `FK_qs8gbwnh7kl82snok4lm28762` FOREIGN KEY (`preacher_id`) REFERENCES `ca_preachers` (`id`),
  ADD CONSTRAINT `FK_obg27dmq08kovbjbfumr4v0fj` FOREIGN KEY (`group_id`) REFERENCES `ca_service_groups` (`id`);

--
-- Ograniczenia dla tabeli `ca_preacher_priviledges`
--
ALTER TABLE `ca_preacher_priviledges`
  ADD CONSTRAINT `FK_fbd8jrmboxx39p0uqxqurhw59` FOREIGN KEY (`preacher_id`) REFERENCES `ca_preachers` (`id`);

--
-- Ograniczenia dla tabeli `ca_service_groups`
--
ALTER TABLE `ca_service_groups`
  ADD CONSTRAINT `FK_9dvo2pycwed4xho2o5q3txuqg` FOREIGN KEY (`overseer_id`) REFERENCES `ca_preachers` (`id`);

--
-- Ograniczenia dla tabeli `ca_terrains_ca_terrain_assignments`
--
ALTER TABLE `ca_terrains_ca_terrain_assignments`
  ADD CONSTRAINT `FK_50ku9lmtsm3tsqo1lxyscuka6` FOREIGN KEY (`ca_terrains_id`) REFERENCES `ca_terrains` (`id`),
  ADD CONSTRAINT `FK_d6i98socnofoutkd0domd350` FOREIGN KEY (`assignments_id`) REFERENCES `ca_terrain_assignments` (`id`);

--
-- Ograniczenia dla tabeli `ca_terrains_ca_terrain_files`
--
ALTER TABLE `ca_terrains_ca_terrain_files`
  ADD CONSTRAINT `FK_st1m2jatwc939m9hjpdjt97hh` FOREIGN KEY (`ca_terrains_id`) REFERENCES `ca_terrains` (`id`),
  ADD CONSTRAINT `FK_2janetmmehydaax2rjobrw92p` FOREIGN KEY (`files_id`) REFERENCES `ca_terrain_files` (`id`);

--
-- Ograniczenia dla tabeli `ca_terrain_assignments`
--
ALTER TABLE `ca_terrain_assignments`
  ADD CONSTRAINT `FK_jsqdrijti3j7dhpo01icl6f7k` FOREIGN KEY (`terrain_id`) REFERENCES `ca_terrains` (`id`),
  ADD CONSTRAINT `FK_nrhtskx7upe2wshs1arcw6xtj` FOREIGN KEY (`group_id`) REFERENCES `ca_service_groups` (`id`);

--
-- Ograniczenia dla tabeli `ca_terrain_files`
--
ALTER TABLE `ca_terrain_files`
  ADD CONSTRAINT `FK_qpo7v59ri3tqq0id8sct0nf92` FOREIGN KEY (`terrain_id`) REFERENCES `ca_terrains` (`id`);

--
-- Ograniczenia dla tabeli `ca_terrain_notifications`
--
ALTER TABLE `ca_terrain_notifications`
  ADD CONSTRAINT `FK_eml25xy4koa1rbb8068xrilqo` FOREIGN KEY (`terrain_assignment_id`) REFERENCES `ca_terrain_assignments` (`id`),
  ADD CONSTRAINT `FK_eml25xy4koa1rbb8068xrifds` FOREIGN KEY (`override_group_id`) REFERENCES `ca_service_groups` (`id`);

--
-- Ograniczenia dla tabeli `core_frames`
--
ALTER TABLE `core_frames`
  ADD CONSTRAINT `FK_e6f2uek7gtfks9yybu6m1os1w` FOREIGN KEY (`parent_id`) REFERENCES `core_frames` (`id`);

--
-- Ograniczenia dla tabeli `security_groups_roles`
--
ALTER TABLE `security_groups_roles`
  ADD CONSTRAINT `FK_8ypp543ldubcu4lfddgw0c8ej` FOREIGN KEY (`role_id`) REFERENCES `security_roles` (`id`),
  ADD CONSTRAINT `FK_muujmsb31emg6darjuxw9m4ea` FOREIGN KEY (`group_id`) REFERENCES `security_groups` (`id`);

--
-- Ograniczenia dla tabeli `security_users_groups`
--
ALTER TABLE `security_users_groups`
  ADD CONSTRAINT `FK_a76lfqakcs10ccy81qowus1q0` FOREIGN KEY (`group_id`) REFERENCES `security_groups` (`id`),
  ADD CONSTRAINT `FK_m5ucgo4tnvhi8m2ub8b74npa9` FOREIGN KEY (`user_id`) REFERENCES `security_users` (`id`);

--
-- Ograniczenia dla tabeli `security_users_roles`
--
ALTER TABLE `security_users_roles`
  ADD CONSTRAINT `FK_a1eork42b5cqoauw1u4y7gwwg` FOREIGN KEY (`user_id`) REFERENCES `security_users` (`id`),
  ADD CONSTRAINT `FK_h0w5is7i2gixt4bk8jfey8743` FOREIGN KEY (`role_id`) REFERENCES `security_roles` (`id`);

--
-- Ograniczenia dla tabeli `vaadin_frames`
--
ALTER TABLE `vaadin_frames`
  ADD CONSTRAINT `FK_hxb6jbp6flyvak2xd6kv59v9j` FOREIGN KEY (`parent_id`) REFERENCES `vaadin_frames` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
ALTER TABLE `security_users` MODIFY `username` VARCHAR(255) NOT NULL, MODIFY `password` VARCHAR(255) NOT NULL;
ALTER IGNORE TABLE `security_users` ADD UNIQUE (`username`);

ALTER TABLE `security_users` ADD `phone_no` varchar(255) NULL;

