CREATE TABLE star_Fahrzeug
(
    F_ID NUMBER(20) NOT NULL,
    F_Name VARCHAR(50) NOT NULL,
    H_ID NUMBER(20) NOT NULL,
    H_Name VARCHAR(20) NOT NULL,
    J_ID NUMBER(20) NOT NULL,
    JAHR NUMBER(4) NOT NULL,
    PRIMARY KEY (F_ID)
);

CREATE TABLE star_Zeit
(
    T_ID NUMBER(20) NOT NULL,
    TAG NUMBER(20) NOT NULL,
    M_ID NUMBER(20) NOT NULL,
    MONAT Number(2) NOT NULL,
    J_ID NUMBER (20) NOT NULL,
    JAHR NUMBER(4) NOT NULL,
    PRIMARY KEY (T_ID)
);

CREATE TABLE star_Kunde
(
    K_ID NUMBER(20) NOT NULL,
    K_Name VARCHAR(50) NOT NULL,
    K_Vorname VARCHAR(50) NOT NULL,
    S_ID NUMBER(20) NOT NULL,
    S_Name VARCHAR(50) NOT NULL,
    B_ID NUMBER(20) NOT NULL,
    B_Name VARCHAR(20) NOT NULL,
    PRIMARY KEY (K_ID)
);

CREATE TABLE star_Werkstatt
(
    W_ID NUMBER(20) NOT NULL,
    W_Name VARCHAR(20) NOT NULL,
    W_Typ VARCHAR(9) NOT NULL,
    W_MAZahl INTEGER NOT NULL,
    B_ID NUMBER(20) NOT NULL,
    B_Name VARCHAR(20) NOT NULL,
    PRIMARY KEY (W_ID)
);

CREATE TABLE star_Reparaturen
(
    REP_Nummer NUMBER(20) NOT NULL,
    REP_Kosten DECIMAL NOT NULL,
    F_ID NUMBER(20),
    K_ID NUMBER(20),
    W_ID NUMBER(20),
    T_ID_Anfang NUMBER(20),
    T_ID_Ende NUMBER(20),
    PRIMARY KEY(REP_Nummer),
    FOREIGN KEY (F_ID) REFERENCES star_Fahrzeug(F_ID),
    FOREIGN KEY (K_ID) REFERENCES star_Kunde(K_ID),
    FOREIGN KEY (W_ID) REFERENCES star_Werkstatt(W_ID),
    FOREIGN KEY (T_ID_Anfang) REFERENCES star_Zeit(T_ID),
    FOREIGN KEY (T_ID_Ende) REFERENCES star_Zeit(T_ID)
);

CREATE TABLE star_Mitarbeiter
(
    MA_ID NUMBER(20) NOT NULL,
    MA_Name VARCHAR(50) NOT NULL,
    PRIMARY KEY (MA_ID)
);

CREATE TABLE star_Lohnkosten
(
    L_Kosten DECIMAL NOT NULL,
    REP_Nummer NUMBER(20),
    MA_ID NUMBER(20),
    FOREIGN KEY (REP_Nummer) REFERENCES star_Reparaturen(REP_Nummer),
    FOREIGN KEY (MA_ID) REFERENCES star_Mitarbeiter(MA_ID)
);