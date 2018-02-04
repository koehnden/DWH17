CREATE VIEW SNOW_ERSATZTEILKOSTEN AS
SELECT b.B_Name, h.H_Name, SUM(r.REP_Ersatzteilkosten) AS Ersatzteilkosten
FROM snow_Reparaturen r, snow_Fahrzeug f, snow_Hersteller h, 
     snow_Werkstatt w, snow_Bundesland b
WHERE r.F_ID = f.ID
AND f.H_ID = h.ID
AND r.W_ID = w.ID
AND w.B_ID = b.ID
GROUP BY CUBE(B_Name, H_Name)
ORDER BY B_NAME, H_Name;
