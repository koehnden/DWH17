CREATE VIEW STAR_Ersatzteilkosten AS
SELECT w.B_Name, f.H_Name, SUM(r.REP_Ersatzteilkosten) AS Ersatzteilkosten
FROM star_Reparaturen r, star_Fahrzeug f, star_Werkstatt w
WHERE r.F_ID = f.F_ID
AND r.W_ID = w.W_ID
GROUP BY CUBE(B_Name, H_Name)
ORDER BY B_NAME, H_Name;