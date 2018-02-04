CREATE VIEW SNOW_PERSONALKOSTEN AS
SELECT b.B_Name, j.Jahr, m.Monat, SUM(Personalkosten) AS Personalkosten
FROM snow_Reparaturen r, snow_Werkstatt w, snow_Bundesland b, snow_Tag t, 
     snow_Monat m, snow_Jahr j, 
     ( SELECT REP_Nummer AS Reparaturnummer, SUM(L_Kosten) AS PERSONALKOSTEN
       FROM snow_Lohnkosten
       GROUP BY REP_Nummer
     )
WHERE r.T_ID_ENDE = t.ID
AND t.M_ID = m.ID
AND m.J_ID = j.ID
AND r.W_ID = w.ID
AND w.B_ID = b.ID
AND r.REP_Nummer = Reparaturnummer
GROUP BY b.B_Name, ROLLUP(j.Jahr, m.Monat)
HAVING j.JAHR IS NOT NULL

UNION

SELECT NULL, NULL, NULL, SUM(L_Kosten)
FROM snow_Lohnkosten r;