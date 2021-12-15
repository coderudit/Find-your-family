DROP DATABASE /*!32312 IF EXISTS*/ `findyourfamily`;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`findyourfamily` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `findyourfamily`;

DROP TABLE IF EXISTS `locations`;

/*Table structure for table `locations` */
CREATE TABLE `locations` (
    `locationId` INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `locationName` VARCHAR(200) NOT NULL,
    `city` VARCHAR(100) DEFAULT NULL,
    `province` VARCHAR(100) DEFAULT NULL,
    `country` VARCHAR(100) DEFAULT NULL
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


DROP TABLE IF EXISTS `occupations`;

/*Table structure for table `occupations` */
CREATE TABLE `occupations` (
    `occupationId` INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `occupationName` VARCHAR(100) NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


DROP TABLE IF EXISTS `gender`;

/*Table structure for table `gender` */
CREATE TABLE `gender` (
    `genderId` INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `genderName` VARCHAR(30) NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


DROP TABLE IF EXISTS `persons`;

/*Table structure for table `persons` */
CREATE TABLE `persons` (
    `personId` INT AUTO_INCREMENT PRIMARY KEY,
    `personName` VARCHAR(200) NOT NULL,
    `genderId` INT DEFAULT NULL,
    `dateOfBirth` DATE DEFAULT NULL,
    `locationOfBirthId` INT DEFAULT NULL,
    `dateOfDeath` DATE DEFAULT NULL,
    `locationOfDeathId` INT DEFAULT NULL,
    `occupationId` INT DEFAULT NULL,
    CHECK (`dateOfBirth` <= `dateOfDeath`),
    CONSTRAINT `persons_gender_fk_1` FOREIGN KEY (`genderId`)
        REFERENCES `gender` (`genderId`),
    CONSTRAINT `persons__locations_fk_2` FOREIGN KEY (`locationOfBirthId`)
        REFERENCES `locations` (`locationId`),
    CONSTRAINT `persons_locations_fk_3` FOREIGN KEY (`locationOfDeathId`)
        REFERENCES `locations` (`locationId`),
    CONSTRAINT `persons_occupations_fk_4` FOREIGN KEY (`occupationId`)
        REFERENCES `occupations` (`occupationId`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


/*Table structure for table `person_attribute_type` */
DROP TABLE IF EXISTS `person_attribute_type`;

CREATE TABLE `person_attribute_type` (
    `attributeTypeId` INT AUTO_INCREMENT PRIMARY KEY,
    `attributeType` VARCHAR(50) NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;

/*Inserting default values inside `person_attribute_type` */
INSERT INTO person_attribute_type
VALUES
	  (1, 'references'),
      (2, 'notes');

      
/*Table structure for table `person_attributes` */
DROP TABLE IF EXISTS `person_attributes`;

CREATE TABLE `person_attributes` (
    `personId` INT NOT NULL,
    `attributeTypeId` INT NOT NULL,
    `attributeValue` VARCHAR(500) NOT NULL,
    `updateDateTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`personId` , `attributeTypeId` , `attributeValue`),
    CONSTRAINT `personAttributes_persons_fk_1` FOREIGN KEY (`personId`)
        REFERENCES `persons` (`personId`)
        ON DELETE CASCADE,
    CONSTRAINT `personAttributes_personAttributeType_fk_2` FOREIGN KEY (`attributeTypeId`)
        REFERENCES `person_attribute_type` (`attributeTypeId`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


/*Table structure for table `media_attribute_type` */
DROP TABLE IF EXISTS `media_attribute_type`;

CREATE TABLE `media_attribute_type` (
    `attributeTypeId` INT AUTO_INCREMENT PRIMARY KEY,
    `attributeType` VARCHAR(50) NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;

/*Inserting default values inside `media_attribute_type` */
INSERT INTO media_attribute_type
VALUES(1, 'tag');


DROP TABLE IF EXISTS `media`;

/*Table structure for table `media` */
CREATE TABLE `media` (
    `mediaId` INT AUTO_INCREMENT PRIMARY KEY,
    `mediaName` VARCHAR(100),
    `mediaLocation` VARCHAR(1000) NOT NULL,
    `dateOfPicture` DATE DEFAULT NULL,
    `locationOfPictureId` INT DEFAULT NULL,
    UNIQUE (`mediaLocation` , `mediaName`),
    CONSTRAINT `media_locations_fk_1` FOREIGN KEY (`locationOfPictureId`)
        REFERENCES `locations` (`locationId`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


DROP TABLE IF EXISTS `media_attributes`;

/*Table structure for table `media_attributes` */
CREATE TABLE `media_attributes` (
    `mediaId` INT NOT NULL,
    `attributeTypeId` INT NOT NULL,
    `attributeValue` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`mediaId` , `attributeTypeId` , `attributeValue`),
    CONSTRAINT `mediaAttributes_media_fk_1` FOREIGN KEY (`mediaId`)
        REFERENCES `media` (`mediaId`)
        ON DELETE CASCADE,
    CONSTRAINT `mediaAttributes_mediaAttributeType__fk_2` FOREIGN KEY (`attributeTypeId`)
        REFERENCES `media_attribute_type` (`attributeTypeId`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


DROP TABLE IF EXISTS `persons_media`;

/*Table structure for table `persons_media` */
CREATE TABLE `persons_media` (
    `personId` INT NOT NULL,
    `mediaId` INT NOT NULL,
    PRIMARY KEY (`personId` , `mediaId`),
    CONSTRAINT `persons_media_persons_fk_1` FOREIGN KEY (`personId`)
        REFERENCES `persons` (`personId`),
    CONSTRAINT `persons_media_persons_fk_2` FOREIGN KEY (`mediaId`)
        REFERENCES `media` (`mediaId`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


/*Table structure for table `person_relation_type` */
DROP TABLE IF EXISTS `person_relation_type`;

CREATE TABLE `person_relation_type` (
    `relationTypeId` INT(2) PRIMARY KEY,
    `relationType` VARCHAR(20) NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;

INSERT INTO `person_relation_type`
VALUES (1, 'ParentChild'), (2, 'Partner');


/*Table structure for table `person_relations` */
DROP TABLE IF EXISTS `person_relations`;

CREATE TABLE `person_relations` (
    `personRelationTypeId` INT NOT NULL,
    `person1Id` INT NOT NULL,
    `person2Id` INT NOT NULL,
    `isActive` TINYINT NOT NULL,
    PRIMARY KEY (`personRelationTypeId` , `person1Id` , `person2Id`),
    CONSTRAINT `personrelations_fk_1` FOREIGN KEY (`personRelationTypeId`)
        REFERENCES `person_relation_type` (`relationTypeId`),
    CONSTRAINT `personrelations_fk_2` FOREIGN KEY (`person1Id`)
        REFERENCES `persons` (`personId`),
    CONSTRAINT `personrelations_fk_3` FOREIGN KEY (`person2Id`)
        REFERENCES `persons` (`personId`)
)  ENGINE=INNODB DEFAULT CHARSET=LATIN1;


DROP PROCEDURE IF EXISTS `add_person`;

/*Stored procedure for adding a person inside persons table.*/
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_person`(IN personName varchar(100),
OUT out_ID INT)
BEGIN
	INSERT INTO persons(`personName`)
    VALUES (personName);
    
    /* Returns the last inserted id inside the database i.e. person id */
    SET out_ID = LAST_INSERT_ID(); 
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `update_person`;

/*Stored procedure for updating a person inside persons table.*/
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_person`(IN personId INT,
								  IN personName VARCHAR(100),
                                  IN genderValue VARCHAR(30),
                                  IN dateOfBirth DATE,
                                  IN locationOfBirthValue VARCHAR(100),
                                  IN dateOfDeath DATE,
                                  IN locationOfDeathValue VARCHAR(100),
                                  IN occupationValue VARCHAR(100),
                                  IN birthCity VARCHAR(100),
                                  IN birthProvince VARCHAR(100),
                                  IN birthCountry VARCHAR(100),
                                  IN deathCity VARCHAR(100),
                                  IN deathProvince VARCHAR(100),
                                  IN deathCountry VARCHAR(100))
BEGIN
	
    /*Variables for storing newly created locations, occupation and gender id if any.*/
    DECLARE locationOfBirthIdValue INT DEFAULT NULL;
    DECLARE locationOfDeathIdValue INT DEFAULT NULL;
    DECLARE occupationIdValue INT DEFAULT NULL;
    DECLARE genderIdValue INT DEFAULT NULL;
    
    BEGIN
		IF (genderValue IS NOT NULL) THEN
			BEGIN
					/*Check if gender already exists, if it does use its id.*/
					IF EXISTS(SELECT 1 FROM gender WHERE genderName = genderValue) THEN
						BEGIN
								SELECT genderId INTO genderIdValue FROM gender WHERE genderName = genderValue;
						END;
					ELSE /*Creates a new gender if does not exists already.*/
						BEGIN 
							INSERT INTO gender(`genderName`)
							VALUES(genderValue);
							SET genderIdValue = LAST_INSERT_ID();
						END;
					END IF;
			END;
		END IF;
		
		IF (locationOfBirthValue IS NOT NULL) THEN
			BEGIN
            /*Check if location already exists, if it does use its id.*/
				IF EXISTS(SELECT 1 FROM locations WHERE locationName = locationOfBirthValue) THEN
					BEGIN
							SELECT locationId INTO locationOfBirthIdValue FROM locations WHERE locationName = locationOfBirthValue;
							UPDATE locations L
                            SET L.city = birthCity,
                                L.province = birthProvince,
                                L.country = birthCountry
                            WHERE L.locationId = locationOfBirthIdValue;
                    END;				
				ELSE /*Creates a new location if does not exists already.*/
					BEGIN
						INSERT INTO locations(`locationName`, `city`, `province`, `country`)
						VALUES(locationOfBirthValue, birthCity, birthProvince, birthCountry);
						SET locationOfBirthIdValue = LAST_INSERT_ID();
					END;
				END IF;
			END;
		END IF;
		
		IF (locationOfDeathValue IS NOT NULL) THEN
			BEGIN 
            /*Check if location already exists, if it does use its id.*/
				IF EXISTS(SELECT 1 FROM locations WHERE locationName = locationOfDeathValue) THEN
					BEGIN
						SELECT locationId INTO locationOfDeathIdValue FROM locations WHERE locationName = locationOfDeathValue;
						UPDATE locations L
                            SET L.city = deathCity,
                                L.province = deathProvince,
                                L.country = deathCountry
                            WHERE L.locationId = locationOfDeathIdValue;
                    END;
				ELSE /*Creates a new location if does not exists already.*/
					BEGIN
						INSERT INTO locations(`locationName`, `city`, `province`, `country`)
						VALUES(locationOfDeathValue, deathCity, deathProvince, deathCountry);
						SET locationOfDeathIdValue = LAST_INSERT_ID();
					END;
				END IF;
			END;
		END IF;
		
		 IF (occupationValue IS NOT NULL) THEN
			BEGIN 
            /*Check if occupation already exists, if it does use its id.*/
				IF EXISTS(SELECT 1 FROM occupations WHERE occupationName = occupationValue) THEN
					BEGIN
						SELECT occupationId INTO occupationIdValue  FROM occupations WHERE occupationName = occupationValue;
					END;
				ELSE /*Creates a new occupation if does not exists already.*/
					BEGIN
						INSERT INTO occupations(`occupationName`)
						VALUES(occupationValue);
						SET occupationIdValue = LAST_INSERT_ID();
					END;
				END IF;
				
			END;
		END IF;
		
SELECT personId;
		UPDATE persons P 
SET 
    P.personName = personName,
    P.genderId = genderIdValue,
    P.dateOfBirth = dateOfBirth,
    P.locationOfBirthId = locationOfBirthIdValue,
    P.dateOfDeath = dateOfDeath,
    P.locationOfDeathId = locationOfDeathIdValue,
    P.occupationId = occupationIdValue
WHERE
    P.personId = personId;
		END;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `add_personAttributeType`;

/*Stored procedure for adding a person attribute inside person_attribute_type table.*/
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_personAttributeType`(IN attributeType VARCHAR(50),
 OUT out_ID INT)
BEGIN
	INSERT INTO person_attribute_type(attributeType)
    VALUES(attributeType);
	SET out_ID = LAST_INSERT_ID();
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `get_personAttributeTypes`;

/*Stored procedure for getting person attributes from person  attribute type table.*/
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_personAttributeTypes`()
BEGIN
	SELECT * FROM person_attribute_type;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `add_personAttribute`;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_personAttribute`(IN personIdValue INT, IN attributeTypeIdValue INT, IN attributeTypeValue VARCHAR(100))
BEGIN

	/*If attribute is not of type reference or note and has been encountered
	 *previously, update it with the latest value.*/
    IF EXISTS(SELECT 1 FROM person_attributes PA
    WHERE PA.personId = personIdValue 
    AND PA.attributeTypeId = attributeTypeIdValue
    AND attributeTypeIdValue NOT IN (1, 2)) THEN
		BEGIN
				UPDATE person_attributes PA
				SET PA.attributeValue = attributeTypeValue
                WHERE PA.personId = personIdValue AND PA.attributeTypeId = attributeTypeIdValue;
		END;
	/*Else either the attribute is of reference or note or a new attribute. For reference or
     *note, multiple entries are possible.*/
	ELSE
		BEGIN
				INSERT INTO person_attributes (personId, attributeTypeId, attributeValue)
				VALUES(personIdValue, attributeTypeIdValue, attributeTypeValue);
		END;
	END IF;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `get_personAttributes`;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_personAttributes`(IN personIdValue INT,
IN allAttributes TINYINT)
BEGIN
	IF(allAttributes = 1) THEN
    BEGIN
	SELECT PAT.attributeType, PA.attributeValue FROM person_attributes PA 
    LEFT JOIN person_attribute_type PAT
    ON PA.attributeTypeId = PAT.attributeTypeId
    WHERE PA.personId = personIdValue
	ORDER BY PA.updateDateTime;
    END;
    ELSE
    BEGIN
    SELECT PAT.attributeType, PA.attributeValue FROM person_attributes PA 
    LEFT JOIN person_attribute_type PAT
    ON PA.attributeTypeId = PAT.attributeTypeId
    WHERE PA.personId = personIdValue AND PAT.attributeTypeId IN (1,2)
    ORDER BY PA.updateDateTime;
    END;
    END IF;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `is_personExists`;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `is_personExists`(IN personID INT, OUT out_ID INT)
BEGIN
	IF EXISTS(SELECT 1 FROM persons WHERE personID = personID) THEN
		SET out_ID = 1;
	ELSE 
		SET out_ID = -1;
	END IF;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `add_relations`;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_relations`(IN personId1 INT, IN personId2 INT, IN relationTypeId TINYINT,
OUT out_ID TINYINT)
BEGIN
	IF (relationTypeId = 3) THEN
		BEGIN
        IF EXISTS(SELECT 1 FROM person_relations PR WHERE ((PR.person1Id = personId1 AND PR.person2Id = personId2)
					OR (PR.person1Id = personId2
        AND PR.person2Id = personId1))) THEN
			BEGIN 
            UPDATE person_relations PR
            SET isActive = 0
            WHERE PR.person1Id = personId1 AND PR.person2Id = personId2;
            
UPDATE person_relations PR 
SET 
    isActive = 0
WHERE
    PR.person1Id = personId2
        AND PR.person2Id = personId1;
        SET out_ID = 1;
        END;
        ELSE
			BEGIN
			SET out_ID = -1;
            END;
		END IF;
	END;
	ELSE IF (relationTypeId = 2) THEN
    BEGIN
		IF EXISTS (SELECT 1 FROM person_relations PR WHERE
					/*Parent-child relationship*/
                    ((PR.person1Id = personId1 AND PR.person2Id = personId2)
					OR (PR.person1Id = personId2
        AND PR.person2Id = personId1)) AND PR.personRelationTypeId = 1) THEN
        BEGIN
        SET out_ID = -1;
        END;
        ELSE
		BEGIN
			INSERT INTO person_relations(personRelationTypeId, person1Id, person2Id, isActive)
			VALUES(relationTypeId, personId1, personId2, 1);
            
            INSERT INTO person_relations(personRelationTypeId, person1Id, person2Id, isActive)
			VALUES(relationTypeId, personId2, personId1, 1);
            
            SET out_ID = 1;
        END;
        END IF;
        END;
	ELSE
    IF EXISTS(SELECT 1 FROM person_relations PR WHERE (
					/*If there exists a partnership/dissolution.*/
					((PR.person1Id = personId1 AND PR.person2Id = personId2)
					OR (PR.person1Id = personId2 AND PR.person2Id = personId1)) AND PR.personRelationTypeId = 2)
                    /*Inverse relation of parent-child.*/
                    OR ((PR.person1Id = personId2 AND PR.person2Id = personId1) AND PR.personRelationTypeId = 1)) THEN
    BEGIN
		SET out_ID = -1;
    END;
    ELSE 
		BEGIN
			INSERT INTO person_relations(personRelationTypeId, person1Id, person2Id, isActive)
			VALUES(relationTypeId, personId1, personId2, 1);
            SET out_ID = 1;
        END;
        
	END IF;
    END IF;
	END IF;
END$$
DELIMITER ;



DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_person`(IN personNameValue VARCHAR(100))
BEGIN
	SELECT P.personId, P.personName, G.genderName, P.dateOfBirth,
    L1.locationName, L1.city, L1.province, L1.country,
    P.dateOfDeath, L2.locationName, L2.city, L2.province, L2.country,
    O.occupationName AS 'occupation'
    FROM persons P
	LEFT JOIN locations L1
	ON P.locationOfBirthId = L1.locationId
    LEFT JOIN locations L2
    ON P.locationOfDeathId = L2.locationId
    LEFT JOIN occupations O
    ON P.occupationId = O.occupationId
    LEFT JOIN gender G
    ON P.genderId = G.genderId
    WHERE P.personName = personNameValue
    LIMIT 1;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_name`(IN personID INT)
BEGIN
	SELECT personName FROM persons
    WHERE persons.personID = personID;
END$$
DELIMITER ;



DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_personAttributes`(IN personID INT)
BEGIN
	SELECT * FROM persons P
    INNER JOIN person_attributes PA
    on p.personID = PA.personID
    WHERE p.personID = personID;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_media`(IN filelocation varchar(500),
 IN fileName varchar(100),
OUT out_ID INT)
BEGIN
	IF EXISTS (SELECT 1 FROM media WHERE media.mediaLocation = filelocation AND media.mediaName = fileName) THEN
    BEGIN
		SELECT mediaId INTO out_ID FROM media WHERE mediaLocation = filelocation AND mediaName = fileName;
    END;
    ELSE
    BEGIN
		INSERT INTO media(`mediaLocation`, `mediaName`)
		VALUES (filelocation, fileName);
		SET out_ID = LAST_INSERT_ID();
    END;
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_media`(IN mediaIdValue INT, IN mediaNameValue VARCHAR(100),
 IN mediaLocationValue VARCHAR(1000), IN dateOfPictureValue DATE, IN locationOfPictureValue VARCHAR(100),
  IN city VARCHAR(100), IN province VARCHAR(100), IN country VARCHAR(100))
BEGIN
	DECLARE locationOfPictureIdValue INT DEFAULT NULL;
    
    IF (locationOfPictureValue IS NOT NULL) THEN
			BEGIN
				IF EXISTS(SELECT 1 FROM locations WHERE locationName = locationOfPictureValue) THEN
					BEGIN
							SELECT locationId INTO locationOfPictureIdValue FROM locations WHERE locationName = locationOfPictureValue;
							UPDATE locations L 
SET 
    L.city = city,
    L.province = province,
    L.country = country
WHERE
    L.locationId = locationOfPictureIdValue;
                    END;
				ELSE
					BEGIN
						INSERT INTO locations(`locationName`, `city`, `province`, `country`)
						VALUES(locationOfPictureValue, city, province, country);
						SET locationOfPictureIdValue = LAST_INSERT_ID();
					END;
				END IF;
			END;
		END IF;
    
	UPDATE media M 
SET 
    M.mediaLocation = mediaLocationValue,
    M.mediaName = mediaNameValue,
    M.dateOfPicture = dateOfPictureValue,
    M.locationOfPictureId = locationOfPictureIdValue
WHERE
    mediaId = mediaIdValue;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_mediaAttributeType`(IN attributeType VARCHAR(50),
 OUT out_ID INT)
BEGIN
	INSERT INTO media_attribute_type(attributeType)
    VALUES(attributeType);
	SET out_ID = LAST_INSERT_ID();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_mediaAttribute`(IN mediaId INT, IN attributeTypeId INT, IN attributeValue VARCHAR(100))
BEGIN
IF EXISTS(SELECT 1 FROM media_attributes MA
    WHERE MA.mediaId = mediaId 
    AND MA.attributeTypeId = attributeTypeId
    AND attributeTypeId NOT IN (1)) THEN
		BEGIN
				UPDATE media_attributes MA
				SET MA.attributeValue = attributeValue
                WHERE MA.mediaId = mediaId AND MA.attributeTypeId = attributeTypeId;
		END;
	ELSE
		BEGIN
				INSERT INTO media_attributes (mediaId, attributeTypeId, attributeValue)
				VALUES(mediaId, attributeTypeId, attributeValue);
		END;
	END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_mediaAttributeTypes`()
BEGIN
	SELECT * FROM media_attribute_type;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_mediaFile`(IN mediaName VARCHAR(100))
BEGIN
	SELECT M.mediaId, M.mediaName, M.mediaLocation, M.dateOfPicture, 
           L.locationName, L.city, L.province, L.country 
	FROM media M
    LEFT JOIN locations L
    ON M.locationOfPictureId = L.locationId
	WHERE M.mediaName = mediaName
    LIMIT 1;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_mediaFileName`(IN mediaId INT)
BEGIN
	SELECT M.mediaName 
	FROM media M
	WHERE M.mediaId = mediaId;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_mediaByTag`(IN tag VARCHAR(100), IN startDate DATE, IN endDate DATE)
BEGIN
	
    SELECT M.mediaId, M.mediaName, M.mediaLocation, M.dateOfPicture, 
          L.locationName, L.city, L.province, L.country 
	FROM media_attributes MA
    LEFT JOIN media M
    ON M.mediaId = MA.mediaId
    LEFT JOIN locations L
    ON M.locationOfPictureId = L.locationId
    WHERE MA.attributeTypeId = 1 AND MA.attributeValue = tag
	AND (
		CASE 
			WHEN startDate IS NOT NULL AND endDate IS NOT NULL 
				THEN (M.dateOfPicture BETWEEN startDate AND endDate)
			WHEN startDate IS NULL AND endDate IS NOT NULL 
				THEN (M.dateOfPicture IS NULL OR (M.dateOfPicture <= endDate))
			WHEN startDate IS NOT NULL AND endDate IS NULL 
				THEN (M.dateOfPicture IS NULL OR (M.dateOfPicture >= startDate))
			WHEN startDate IS NULL AND endDate IS NULL 
				THEN (M.dateOfPicture IS NOT NULL OR M.dateOfPicture IS NULL) END)
                 ORDER BY (M.dateOfPicture IS NULL), M.dateOfPicture, M.mediaName;
    
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_mediaByLocation`(IN location VARCHAR(200), IN startDate DATE, IN endDate DATE)
BEGIN
	SELECT M.mediaId, M.mediaName, M.mediaLocation, M.dateOfPicture, 
           L.locationName, L.city, L.province, L.country 
	FROM media M
    LEFT JOIN locations L
    ON M.locationOfPictureId = L.locationId
    WHERE ((L.locationName = location OR L.city = location OR L.province = location OR L.country = location)
    AND (
		CASE 
			WHEN startDate IS NOT NULL AND endDate IS NOT NULL 
				THEN (M.dateOfPicture BETWEEN startDate AND endDate)
			WHEN startDate IS NULL AND endDate IS NOT NULL 
				THEN (M.dateOfPicture IS NULL OR (M.dateOfPicture <= endDate))
			WHEN startDate IS NOT NULL AND endDate IS NULL 
				THEN (M.dateOfPicture IS NULL OR (M.dateOfPicture >= startDate))
			WHEN startDate IS NULL AND endDate IS NULL 
				THEN (M.dateOfPicture IS NOT NULL OR M.dateOfPicture IS NULL) END))
                 ORDER BY (M.dateOfPicture IS NULL), M.dateOfPicture, M.mediaName;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_mediaattributes`(IN mediaIdValue INT)
BEGIN
	SELECT MAT.attributeType, MA.attributeValue FROM media M
    LEFT JOIN media_attributes MA
    ON M.mediaId = MA.mediaId
    LEFT JOIN media_attribute_type MAT
    ON MA.attributeTypeId = MAT.attributeTypeId
    WHERE M.mediaId = mediaIdValue;
END$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_personRelations`(IN personId INT)
BEGIN
	SELECT PR.person1Id, PR.person2Id, PR.personRelationTypeId, PR.isActive FROM findyourfamily.person_relations PR
	WHERE (PR.person1Id = personId OR (PR.person2Id = personId AND PR.personRelationTypeId = 1));
END$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_personById`(IN personIdValue INT)
BEGIN
	SELECT P.personId, P.personName, G.genderName AS 'gender', P.dateOfBirth,
    L1.locationName AS 'locationofbirth', P.dateOfDeath, L2.locationName AS 'locationofdeath', O.occupationName AS 'occupation'
    FROM persons P
	LEFT JOIN locations L1
	ON P.locationOfBirthId = L1.locationId
    LEFT JOIN locations L2
    ON P.locationOfDeathId = L2.locationId
    LEFT JOIN occupations O
    ON P.occupationId = O.occupationId
    LEFT JOIN gender G
    ON P.genderId = G.genderId
    WHERE P.personId = personIdValue
    LIMIT 1;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_personmedia`(IN personId VARCHAR(1000), IN mediaId INT)
BEGIN
    INSERT INTO persons_media(personId, mediaId)
	VALUES(personId, mediaId);
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_mediaByPerson`(IN personIdValue INT, IN startDate DATE, IN endDate DATE)
BEGIN
	SELECT DISTINCT M.mediaId, M.mediaName, M.mediaLocation, M.dateOfPicture, 
           L.locationName AS 'locationofpicture'
	FROM persons_media PM
    INNER JOIN media M
    ON PM.mediaId = M.mediaId
    LEFT JOIN locations L
    ON M.locationOfPictureId = L.locationId
    WHERE PM.personId = personIdValue
    AND (
		CASE 
			WHEN startDate IS NOT NULL AND endDate IS NOT NULL 
				THEN (M.dateOfPicture BETWEEN startDate AND endDate)
			WHEN startDate IS NULL AND endDate IS NOT NULL 
				THEN (M.dateOfPicture IS NULL OR (M.dateOfPicture <= endDate))
			WHEN startDate IS NOT NULL AND endDate IS NULL 
				THEN (M.dateOfPicture IS NULL OR (M.dateOfPicture >= startDate))
			WHEN startDate IS NULL AND endDate IS NULL 
				THEN (M.dateOfPicture IS NOT NULL OR M.dateOfPicture IS NULL) END)
                 ORDER BY (M.dateOfPicture IS NULL), M.dateOfPicture, M.mediaName;
END$$
DELIMITER ;





