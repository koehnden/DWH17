-- 1 Anzahl der Mitarbeiter -> 438
CREATE VIEW anzahl_mitarbeiter AS
SELECT COUNT(*) anzahl_mitaribeiter FROM mitarbeiter;

-- 2 Unterschiedliche Räume in der Falkultät -> 262
CREATE VIEW anzahl_raeume AS
SELECT COUNT(DISTINCT(raum)) anzahl_raeume
FROM mitarbeiter;

-- 3. alle mitarbeiter die sich ein raum teilen -> 188 rows selected.
CREATE VIEW teilt_rufnummer AS
SELECT M.vorname, M.nachname, M.email, M.telefon
FROM mitarbeiter M,
     (SELECT telefon, COUNT(*)
      FROM mitarbeiter
      GROUP BY telefon
      HAVING COUNT(*)>1) CT
WHERE M.telefon = CT.telefon
ORDER BY M.telefon;

-- 4 alle mitarbeiter die sich einen Raum, aber unterschidliche Telefonnummber haben -> 88 rows selected
-- hier alle ausgewählt die sich einen Raum teilen und eine eigene Telefonnummer haben (richtig?)
CREATE VIEW teilt_raum_eigene_rufnummer AS
SELECT M.vorname, M.nachname, M.email, M.raum, M.telefon
FROM mitarbeiter M,
     (SELECT raum, COUNT(*)
      FROM mitarbeiter
      GROUP BY raum
      HAVING COUNT(*)>1) RT
WHERE M.raum = RT.raum AND M.telefon NOT IN (
        SELECT telefon
        FROM teilt_rufnummer)
ORDER BY M.raum;

-- TODO: PL/SQL function schreiben und das 5. lösen
