CREATE OR REPLACE VIEW star_Gesamtkosten AS
with ma_gk as 
(
    select sum(l_kosten) as summe, rep_nummer 
    from STAR_LOHNKOSTEN l 
    group by l.rep_nummer 
    having count(l.rep_nummer)>1 
)

select (sum(r.rep_ersatzteilkosten)+sum(ma_gk.summe)) as GESAMTKOSTEN
from STAR_REPARATUREN r, ma_gk
where r.rep_nummer = ma_gk.rep_nummer;