set timing on;
set termout off;
spool messungen.log;
set serveroutput on;
Declare
    n number;
    type cv_type is ref cursor;
    cv cv_type;
Begin
    n := dbms_utility.get_time;

    -- Index anlegen
EXECUTE IMMEDIATE 'CREATE BITMAP INDEX bit_map_vater_mutter ON einwohner (vaterid, mutterid, 1);'
EXECUTE IMMEDIATE 'CREATE BITMAP INDEX idx_wohnort ON einwohner (wohnort);'

    -- Anfrage 1-4 ausführen
    open cv for
    -- Anfrage 1
WITH nicht_berliner AS
(
    SELECT vaterid, mutterid
    FROM einwohner
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           wohnort != 'Berlin'
),
 berliner AS
(
    SELECT id, vaterid, mutterid
    FROM einwohner
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           wohnort = 'Berlin'
)
SELECT COUNT(b.id)
FROM nicht_berliner n, berliner b
WHERE (b.vaterid = n.vaterid OR b.mutterid = n.mutterid);

    close cv;

    open cv for
        -- Anfrage 2
SELECT COUNT(*)
FROM einwohner
WHERE vaterid IS NULL AND
      mutterid IS NULL AND
      extract(month from geburtsdatum) IN (12,1,2);

    close cv;

    open cv for
        -- Anfrage 3
SELECT COUNT(id)
FROM einwohner
WHERE regexp_replace(adresse, '[^0-9]', '') = 23;
    close cv;

    open cv for
        -- Anfrage 4
SELECT path
FROM (
    SELECT id, path, LENGTH(path)-LENGTH(REPLACE(path,'/','')) nodes,
    max(LENGTH(path)-LENGTH(REPLACE(path,'/',''))) OVER () maxl
    FROM (
        SELECT id, SYS_CONNECT_BY_PATH( id, '/' ) path
        FROM einwohner
        START WITH vaterid IS NULL AND mutterid IS NULL
        CONNECT BY vaterid = PRIOR id OR mutterid = PRIOR id
    )
)
WHERE nodes = maxl;

    close cv;

    -- (Korrekte) Ausführung von Anfrage 1-4 beendet
    dbms_output.put_line( ((dbms_utility.get_time-n))||' ms');
End;
/
show errors;
spool off;
