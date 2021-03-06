  
 /*
 * Copyright (c) 2012. The Energy Detective. All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
 /*
  * mySQL script to create the support tables for Aggredata
  * Script for Version: 1.0
  *
  */
 

CREATE SCHEMA IF NOT EXISTS aggredata;

//DROP user 'aggredata'@'localhost';  /*Uncomment this line if re-rerunning the script*/

CREATE user 'aggredata'@'localhost' IDENTIFIED BY 'aggredata';
GRANT ALL PRIVILEGES ON aggredata.* TO 'aggredata'@'localhost' WITH GRANT OPTION;


SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `aggredata`.`weatherlocation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`weatherlocation` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT  ,
  `postal` VARCHAR(200) NULL ,
  `latitude` DECIMAL(10,6) NULL ,
  `longitude` DECIMAL(10,6) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`gateway`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`gateway` (
  `id` INT UNSIGNED NOT NULL,
  `userAccountId` INT NOT NULL ,
  `state` INT(1) NOT NULL ,
  `securityKey` VARCHAR(20) NULL ,
  `description` VARCHAR(1000) NULL ,
  `weatherLocationId` INT UNSIGNED NULL ,
  `custom1` VARCHAR(100) NULL ,
  `custom2` VARCHAR(100) NULL ,
  `custom3` VARCHAR(100) NULL ,
  `custom4` VARCHAR(100) NULL ,
  `custom5` VARCHAR(100) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`mtu`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`mtu` (
  `id` INT UNSIGNED NOT NULL,
  `gatewayId` INT UNSIGNED NOT NULL ,
  `type` INT NULL ,
  `description` VARCHAR(1000) NULL ,
  PRIMARY KEY (`id`, `gatewayId`) ,
  INDEX `gateway_id` (`gatewayId` ASC) ,
  CONSTRAINT `gateway_id`
    FOREIGN KEY (`gatewayId` )
    REFERENCES `aggredata`.`gateway` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`demandCharge'
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`demandcharge` (
  `gatewayId` INT NOT NULL ,
  `timestamp` INT NOT NULL ,
  `peak` DECIMAL(10,5) NULL ,
  `cost` DECIMAL(10,5) NULL ,
  `type` INT NULL ,
  PRIMARY KEY (`gatewayId`, `timestamp`),
  INDEX `timestamp` (`timestamp` ASC, `gatewayId` ASC)
  )
COMMENT = 'Table used for storing demand charges';

-- -----------------------------------------------------
-- Table `aggredata`.`costdata`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`costdata` (
  `gatewayId` INT UNSIGNED NOT NULL ,
  `timestamp` INT UNSIGNED NOT NULL ,
  `meterReadDay` INT UNSIGNED NOT NULL ,
  `meterReadMonth` INT UNSIGNED NOT NULL ,
  `meterReadYear` INT UNSIGNED NOT NULL,
  `fixedCost` DECIMAL(8,5) NULL ,
  `minCost` DECIMAL(8,5) NULL ,
  PRIMARY KEY (`gatewayId`,`timestamp`) ,
  INDEX `timestamp` (`timestamp` ASC, `gatewayId` ASC)
  )
ENGINE = InnoDB
COMMENT = 'Table that stores monthly cost information';

-- -----------------------------------------------------
-- Table `aggredata`.`energydata`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`energydata` (
  `gatewayId` INT UNSIGNED NOT NULL ,
  `mtuId` INT UNSIGNED NOT NULL ,
  `timestamp` INT UNSIGNED NOT NULL ,
  `rate` DECIMAL(10,5) NULL ,
  `energy` DECIMAL(25,4) NULL ,
  `energyDifference` DECIMAL(25,4) NULL ,
  `minuteCost` DECIMAL(8,5) NULL ,
  PRIMARY KEY (`gatewayId`, `mtuId`, `timestamp`) ,
  INDEX `timestamp` (`timestamp` ASC, `gatewayId` ASC, `mtuID` ASC))
ENGINE = InnoDB
COMMENT = 'Table that stores all energy data posted from each gateway';


-- -----------------------------------------------------
-- Table `aggredata`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(200) NULL ,
  `password` VARCHAR(45) NULL ,
  `firstName` VARCHAR(50) NULL ,
  `lastName` VARCHAR(50) NULL ,
  `middleName` VARCHAR(50) NULL ,
  `address` VARCHAR(100) NULL ,
  `city` VARCHAR(50) NULL ,
  `addrState` VARCHAR(15) NULL ,
  `companyName` VARCHAR(100) NULL ,
  `phoneNumber` VARCHAR(25) NULL ,
  `zip` VARCHAR(15) NULL ,
  `custom1` VARCHAR(100) NULL ,
  `custom2` VARCHAR(100) NULL ,
  `custom3` VARCHAR(100) NULL ,
  `custom4` VARCHAR(100) NULL ,
  `custom5` VARCHAR(100) NULL ,
  `role` VARCHAR(45) NULL ,
  `state` INT NULL ,
  `activationKey` VARCHAR(20) NULL ,
  `timezone` VARCHAR(40) NULL ,
  `defaultgroupId` INT UNSIGNED NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`group` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT  ,
  `description` VARCHAR(1000) NULL ,
  `owneruserId` INT UNSIGNED NOT NULL ,
  `custom1` VARCHAR(100) NULL ,
  `custom2` VARCHAR(100) NULL ,
  `custom3` VARCHAR(100) NULL ,
  `custom4` VARCHAR(100) NULL ,
  `custom5` VARCHAR(100) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'groups of gateways.\n\n';


-- -----------------------------------------------------
-- Table `aggredata`.`weatherhistory`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`weatherhistory` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `weatherLocationId` INT UNSIGNED NOT NULL ,
  `timestamp` INT NOT NULL ,
  `temperature` INT NULL ,
  `windSpeed` INT NULL ,
  `direction` INT NULL ,
  `weatherConditions` VARCHAR(200) NULL ,
  `iconLink` VARCHAR(200) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `weather_location_id` (`weatherLocationId` ASC) ,
  CONSTRAINT `weather_location_id_wh`
    FOREIGN KEY (`weatherLocationId` )
    REFERENCES `aggredata`.`weatherlocation` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`usergroup`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`usergroup` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `userId` INT UNSIGNED NOT NULL ,
  `groupId` INT UNSIGNED NOT NULL ,
  `role` INT NULL COMMENT '0 = viewer\n1 = owner\n' ,
  PRIMARY KEY (`id`, `userId`, `groupId`) ,
  INDEX `user_id` (`userId` ASC) ,
  INDEX `group_id` (`groupId` ASC) ,
  CONSTRAINT `user_id`
    FOREIGN KEY (`userId` )
    REFERENCES `aggredata`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `group_id`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`groupLocation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`gatewaygroup` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `groupId` INT UNSIGNED NOT NULL ,
  `gatewayId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`, `groupId`, `gatewayId`) ,
  INDEX `group_id` (`groupId` ASC) ,
  INDEX `gateway_id` (`gatewayId` ASC) ,
  CONSTRAINT `group_id_gl`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `gateway_id_gl`
    FOREIGN KEY (`gatewayId` )
    REFERENCES `aggredata`.`gateway` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `aggredata`.`thirdpartyaccess`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`thirdpartyaccess` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `userId` INT UNSIGNED NOT NULL ,
  `thirdPartyApplicationKey` VARCHAR(45) NOT NULL ,
  `thirdPartyAccessKey` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`thirdpartyaccessgroup`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`thirdpartyaccessgroup` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `thirdPartyAccessId` INT UNSIGNED NOT NULL ,
  `groupId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`, `thirdPartyAccessId`, `groupId`) ,
  INDEX `thirdPartyAccess_id` (`thirdPartyAccessId` ASC) ,
  INDEX `group_id` (`groupId` ASC) ,
  CONSTRAINT `thirdPartyAccess_id_tpag`
    FOREIGN KEY (`thirdPartyAccessId` )
    REFERENCES `aggredata`.`thirdpartyaccess` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `group_id_tpag`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

insert into aggredata.user (username, password, role, state, defaultGroupId) values ('admin','admin','ROLE_ADMIN', 1, 1);
insert into aggredata.group (description, owneruserId) values ('Default', (select id from aggredata.user where username='admin'));


