--Erzeugte Indexe/MV für Einwohner------------------------------------------------------------------------
CREATE BITMAP INDEX bitmap_vater_mutter ON einwohner (vaterid, mutterid, 1);

CREATE MATERIALIZED VIEW vater_mutter_not_null
BUILD IMMEDIATE
ENABLE QUERY REWRITE
AS
SELECT id, vaterid, mutterid, bundesland
FROM einwohner
WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL);

CREATE INDEX idx_month ON einwohner (extract(month from geburtsdatum));


----------------------------------------------------- Aufgabe 1 (ergebniss = 36 beim testdatensatz)
--View: Aufgabe4_1
WITH nicht_berliner AS
(
    SELECT vaterid, mutterid
    FROM einwohner
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           bundesland != 'Berlin'
),
 berliner AS
(
    SELECT id, vaterid, mutterid
    FROM einwohner
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
          bundesland = 'Berlin'
)
SELECT COUNT(b.id)
FROM nicht_berliner n, berliner b
WHERE (b.vaterid = n.vaterid OR b.mutterid = n.mutterid);

-- Mit Bitmap vater_mutter und Bitmap wohnort
'PLAN_TABLE_OUTPUT
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Plan hash value: 1313664913

--------------------------------------------------------------------------------------------------------
| Id  | Operation                      | Name                  | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT               |                       |     1 |    22 |     0   (0)|          |
|   1 |  SORT AGGREGATE                |                       |     1 |    22 |            |          |
|*  2 |   FILTER                       |                       |       |       |            |          |
|*  3 |    MAT_VIEW REWRITE ACCESS FULL| VATER_MUTTER_NOT_NULL |     1 |    22 |     7   (0)| 00:00:01 |
--------------------------------------------------------------------------------------------------------
'

---------------------------------------------------------- Aufgabe 2 (ergebniss = 1649 @10K DS)
--View: Aufgabe4_2

SELECT COUNT(*)
FROM einwohner
WHERE vaterid IS NULL AND
      mutterid IS NULL AND
      extract(month from geburtsdatum) IN (12,1,2);

-- ohne zusätzlichen indx
'------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Plan hash value: 2647946901

---------------------------------------------------------------------------------------------------
| Id  | Operation                             | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                      |           |     1 |    17 |     9   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                       |           |     1 |    17 |            |          |
|   2 |   INLIST ITERATOR                     |           |       |       |            |          |
|*  3 |    TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER |    57 |   969 |     9   (0)| 00:00:01 |
|*  4 |     INDEX RANGE SCAN                  | IDX_MONTH |    40 |       |     3   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------
'

------------------------------------------------ Aufgabe 3 (ergebniss = 86)
--View: Aufgabe4_3
SELECT COUNT(id)
FROM einwohner
WHERE regexp_replace(adresse, '[^0-9]', '') = 23;

-- ohne weiteren index
'Plan hash value: 1293750363

--------------------------------------------------------------------------------
| Id  | Operation          | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |           |     1 |    20 |    35   (3)| 00:00:01 |
|   1 |  SORT AGGREGATE    |           |     1 |    20 |            |          |
|*  2 |   TABLE ACCESS FULL| EINWOHNER |   100 |  2000 |    35   (3)| 00:00:01 |
--------------------------------------------------------------------------------
'

------------------------------------------- Aufgabe 4 (ergebniss = /74/251/494/771/1000)
--View: Aufgabe4_4
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
WHERE nodes = maxl

'PLAN_TABLE_OUTPUT
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Plan hash value: 3840475631

--------------------------------------------------------------------------------------------------------
| Id  | Operation                                  | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                           |           | 27958 |    54M|    36   (6)| 00:00:01 |
|*  1 |  VIEW                                      |           | 27958 |    54M|    36   (6)| 00:00:01 |
|   2 |   WINDOW BUFFER                            |           | 27958 |    53M|    36   (6)| 00:00:01 |
|   3 |    VIEW                                    |           | 27958 |    53M|    36   (6)| 00:00:01 |
|*  4 |     CONNECT BY NO FILTERING WITH START-WITH|           |       |       |            |          |
|   5 |      TABLE ACCESS FULL                     | EINWOHNER | 10000 | 80000 |    34   (0)| 00:00:01 |
'

'
with rec_path as (SELECT id,
                  SYS_CONNECT_BY_PATH( id, '/' ) path,
                  LEVEL recLevel
                  FROM einwohner START WITH vaterid IS NULL AND mutterid IS NULL
                  CONNECT BY vaterid = PRIOR id OR mutterid = PRIOR id),
maxLevel      as (select max(recLevel) maxRecLevel
                  from rec_path)
SELECT        r.path,
              r.recLevel LEN
              from rec_path r, maxLevel ml
              where r.recLevel=ml.maxRecLevel


-----------------------------------------------------------------------------------------------------------------------
| Id  | Operation                                 | Name                      | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                          |                           |   280 |   554K|   121   (3)| 00:00:01 |
|   1 |  TEMP TABLE TRANSFORMATION                |                           |       |       |            |          |
|   2 |   LOAD AS SELECT                          | SYS_TEMP_0FD9D6C40_19C23E |       |       |            |          |
|*  3 |    CONNECT BY NO FILTERING WITH START-WITH|                           |       |       |            |          |
|   4 |     TABLE ACCESS FULL                     | EINWOHNER                 | 10000 | 80000 |    34   (0)| 00:00:01 |
|   5 |   NESTED LOOPS                            |                           |   280 |   554K|    85   (2)| 00:00:01 |
|   6 |    VIEW                                   |                           |     1 |    13 |    42   (0)| 00:00:01 |
|   7 |     SORT AGGREGATE                        |                           |     1 |    13 |            |          |
|   8 |      VIEW                                 |                           | 27958 |   354K|    42   (0)| 00:00:01 |
|   9 |       TABLE ACCESS FULL                   | SYS_TEMP_0FD9D6C40_19C23E | 27958 |  1064K|    42   (0)| 00:00:01 |
|* 10 |    VIEW                                   |                           |   280 |   550K|    42   (0)| 00:00:01 |
|  11 |     TABLE ACCESS FULL                     | SYS_TEMP_0FD9D6C40_19C23E | 27958 |  1064K|    42   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - filter(("VATERID"=PRIOR "ID" OR "MUTTERID"=PRIOR "ID") AND "VATERID" IS NULL AND "MUTTERID" IS NULL)
  10 - filter("R"."RECLEVEL"="ML"."MAXRECLEVEL")'
