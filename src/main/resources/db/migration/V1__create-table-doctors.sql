CREATE TABLE TB_DOCTORS (
	ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL UNIQUE,
    CRM VARCHAR(6) NOT NULL UNIQUE,
    EXPERTISE VARCHAR(100) NOT NULL,
    STREET VARCHAR(100) NOT NULL,
	NEIGHBORHOOD VARCHAR(100) NOT NULL,
	ZIP_CODE VARCHAR(9) NOT NULL,
	CITY VARCHAR(100) NOT NULL,
	STATE VARCHAR(2) NOT NULL,
	NUMBER VARCHAR(20) NOT NULL,
	COMPLEMENT VARCHAR(100)
);