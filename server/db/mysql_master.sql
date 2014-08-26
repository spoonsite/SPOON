CREATE SCHEMA IF NOT EXISTS `opencat` DEFAULT CHARACTER SET latin1 ;
USE `opencat` ;

-- -----------------------------------------------------
-- Table `opencat`.`test`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `opencat`.`test` (
  `code` VARCHAR(20) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `active_status` CHAR(1) NOT NULL,
  `create_user` VARCHAR(80) NOT NULL,
  `create_dts` TIMESTAMP NOT NULL,
  `update_user` VARCHAR(80) NOT NULL,
  `update_dts` TIMESTAMP NOT NULL,
  PRIMARY KEY (`code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

