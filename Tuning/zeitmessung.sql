set timing on;
set termout off;
spool messungen.log;
set serveroutput on;
DECLARE
    n number:=dbms_utility.get_time;
    CURSOR query1 is WITH nicht_berliner AS(
      SELECT vaterid, mutterid
      FROM einwohner
      WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           wohnort != 'Berlin'),
     berliner AS(
        SELECT id, vaterid, mutterid
        FROM einwohner
        WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
               wohnort = 'Berlin')
    SELECT COUNT(b.id)
    FROM nicht_berliner n, berliner b
    WHERE (b.vaterid = n.vaterid OR b.mutterid = n.mutterid);
    
    CURSOR query2 is SELECT COUNT(*)
    FROM einwohner
    WHERE vaterid IS NULL AND
          mutterid IS NULL AND
          extract(month from geburtsdatum) IN (12,1,2);
    
    CURSOR query3 IS SELECT COUNT(id)
    FROM einwohner
    WHERE regexp_replace(adresse, '[^0-9]', '') = 23;
    
    CURSOR query4 is SELECT path
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
    
    result1 query1%ROWTYPE;
    result2 query2%ROWTYPE;
    result3 query3%ROWTYPE;
    result4 query4%ROWTYPE;
    
Begin
    --n := dbms_utility.get_time;
    dbms_output.put_line('Erzeuge Index.');
    BEGIN
      EXECUTE IMMEDIATE 'CREATE BITMAP INDEX bit_map_vater_mutter ON einwohner (vaterid, mutterid, 1)';
    EXCEPTION
      WHEN OTHERS THEN
        dbms_output.put_line('BITMAP INDEX ON (vaterid, mutterid, 1)  existiert bereits');
    END;
    BEGIN
      EXECUTE IMMEDIATE 'CREATE BITMAP INDEX idx_wohnort ON einwohner (wohnort)';
    EXCEPTION
      WHEN OTHERS THEN
        dbms_output.put_line('BITMAP INDEX ON (wohnort)  existiert bereits');
    END;
    -- Anfrage 1-4 ausführen    
    
    dbms_output.put_line('Führe Anfrage 1 aus.');
    OPEN query1;
    fetch query1 into result1;
    CLOSE query1;
    
    dbms_output.put_line('Führe Anfrage 2 aus.');
    OPEN query2;
    fetch query2 into result2;
    CLOSE query2;
    
    dbms_output.put_line('Führe Anfrage 3 aus.');
    OPEN query3;
    fetch query3 into result3;
    CLOSE query3;
    
    dbms_output.put_line('Führe Anfrage 4 aus.');
    OPEN query4;
    fetch query4 into result4;
    CLOSE query4;
    -- (Korrekte) Ausführung von Anfrage 1-4 beendet
    
    dbms_output.put_line('Done.');
    dbms_output.put_line('Elapsed: '|| ((dbms_utility.get_time-n))*10||' ms');
END;
/
SPOOL OFF;
