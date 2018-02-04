CREATE VIEW star_PERSONALKOSTEN AS
SELECT w.B_Name, z.Jahr, z.Monat, SUM(Personalkosten) AS Personalkosten
FROM star_Reparaturen r, star_Werkstatt w, star_Zeit z, 
( SELECT REP_Nummer AS Reparaturnummer, SUM(L_Kosten) AS PERSONALKOSTEN
  FROM star_Lohnkosten
  GROUP BY REP_Nummer
)
WHERE r.T_ID_ENDE = z.T_ID
AND r.W_ID = w.W_ID
AND r.REP_Nummer = Reparaturnummer
GROUP BY w.B_Name, ROLLUP(z.Jahr, z.Monat)
HAVING z.JAHR IS NOT NULL

UNION

SELECT NULL, NULL, NULL, SUM(L_Kosten)
FROM star_Lohnkosten r;