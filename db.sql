-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Generation Time: 2020-01-19 09:29:34
-- 服务器版本： 8.0.16
-- PHP Version: 7.0.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `nastool`
--
CREATE DATABASE IF NOT EXISTS `nastool` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `nastool`;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user`
(
    `id`            int(10) UNSIGNED                                       NOT NULL,
    `username`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `passwd`        char(32) CHARACTER SET utf8 COLLATE utf8_general_ci    NOT NULL,
    `salt`          char(8) CHARACTER SET utf8 COLLATE utf8_general_ci     NOT NULL,
    `create_time`   datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime                                                        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `uuid`          char(32) CHARACTER SET utf8 COLLATE utf8_general_ci    NOT NULL,
    `email`         varchar(256)                                                    DEFAULT NULL,
    `email_checked` int(11)                                                NOT NULL DEFAULT '0' COMMENT '0-未验证 1-已验证'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `idx_uni_username` (`username`),
    ADD UNIQUE KEY `idx_uni_uuid` (`uuid`),
    ADD KEY `idx_email` (`email`, `email_checked`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
    MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;
COMMIT;
