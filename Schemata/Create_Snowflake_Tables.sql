CREATE TABLE snow_Jahr (
    ID NUMBER(20) NOT NULL,
    JAHR NUMBER(4) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE snow_Monat
(
    ID NUMBER(20) NOT NULL,
    MONAT Number(2) NOT NULL,
    J_ID NUMBER (20),
    PRIMARY KEY(ID),
    FOREIGN KEY (J_ID) REFERENCES snow_Jahr(ID)
);

CREATE TABLE snow_Tag
(
    ID NUMBER(20) NOT NULL,
    TAG NUMBER(20) NOT NULL,
    M_ID NUMBER(20),
    PRIMARY KEY(ID),
    FOREIGN KEY (M_ID) REFERENCES snow_Monat(ID)
);

CREATE TABLE snow_Hersteller
(
    ID NUMBER(20) NOT NULL,
    H_Name VARCHAR(20) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE snow_Fahrzeug
(
    ID NUMBER(20) NOT NULL,
    F_Name VARCHAR(50) NOT NULL,
    H_ID NUMBER(20),
    J_ID NUMBER(20),
    PRIMARY KEY(ID),
    FOREIGN KEY (H_ID) REFERENCES snow_Hersteller(ID),
    FOREIGN KEY (J_ID) REFERENCES snow_JAHR(ID)
);

CREATE TABLE snow_Bundesland
(
    ID NUMBER(20) NOT NULL,
    B_Name VARCHAR(20) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE snow_Werkstatt
(
    ID NUMBER(20) NOT NULL,
    W_Name VARCHAR(20) NOT NULL,
    W_Typ VARCHAR(9) NOT NULL,
    W_MAZahl INTEGER NOT NULL,
    B_ID NUMBER(20),
    PRIMARY KEY(ID),
    FOREIGN KEY (B_ID) REFERENCES snow_Bundesland(ID)
);

CREATE TABLE snow_Straﬂe
(
    ID NUMBER(20) NOT NULL,
    S_Name VARCHAR(50) NOT NULL,
    B_ID NUMBER(20),
    PRIMARY KEY(ID),
    FOREIGN KEY (B_ID) REFERENCES snow_Bundesland(ID)
);

CREATE TABLE snow_Kunde
(
    ID NUMBER(20) NOT NULL,
    K_Name VARCHAR(50) NOT NULL,
    K_Vorname VARCHAR(50) NOT NULL,
    S_ID NUMBER(20),
    PRIMARY KEY(ID),
    FOREIGN KEY (S_ID) REFERENCES snow_Straﬂe(ID)
);

CREATE TABLE snow_Reparaturen
(
    REP_Nummer NUMBER(20) NOT NULL,
    REP_Kosten DECIMAL NOT NULL,
    F_ID NUMBER(20),
    K_ID NUMBER(20),
    W_ID NUMBER(20),
    T_ID_Anfang NUMBER(20),
    T_ID_Ende NUMBER(20),
    PRIMARY KEY(REP_Nummer),
    FOREIGN KEY (F_ID) REFERENCES snow_Fahrzeug(ID),
    FOREIGN KEY (K_ID) REFERENCES snow_Kunde(ID),
    FOREIGN KEY (W_ID) REFERENCES snow_Werkstatt(ID),
    FOREIGN KEY (T_ID_Anfang) REFERENCES snow_Tag(ID),
    FOREIGN KEY (T_ID_Ende) REFERENCES snow_Tag(ID)
);

CREATE TABLE snow_Mitarbeiter
(
    ID NUMBER(20) NOT NULL,
    MA_Name VARCHAR(50) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE snow_Lohnkosten
(
    L_Kosten DECIMAL NOT NULL,
    REP_Nummer NUMBER(20),
    MA_ID NUMBER(20),
    FOREIGN KEY (REP_Nummer) REFERENCES snow_Reparaturen(REP_Nummer),
    FOREIGN KEY (MA_ID) REFERENCES snow_Mitarbeiter(ID)
);
