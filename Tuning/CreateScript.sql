CREATE TABLE EINWOHNER ( 
	ID           INTEGER     NOT NULL,
	NAME         VARCHAR(25) NOT NULL,
	VORNAME      VARCHAR(25) NOT NULL,
	GEBURTSDATUM DATE        NOT NULL,
	ADRESSE      VARCHAR(60) NOT NULL,
	WOHNORT      VARCHAR(50) NOT NULL,
	PLZ          VARCHAR(5)  NOT NULL,
	BUNDESLAND   VARCHAR(25) NOT NULL,
	VATERID      INTEGER,
	MUTTERID     INTEGER
);