--Erzeugte Indexe für Einwohner------------------------------------------------------------------------

CREATE BITMAP INDEX bit_map_vater_mutter ON einwohner (vaterid, mutterid, 1)
CREATE BITMAP INDEX idx_wohnort ON einwohner (wohnort);


----------------------------------------------------- Aufgabe 1 (ergebniss = 36 beim testdatensatz)
--View: Aufgabe4_1
WITH nicht_berliner AS
(
    SELECT vaterid, mutterid /*+ index(bit_map_vater_mutter) */
    FROM einwohner
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           wohnort != 'Berlin'
),
 berliner AS
(
    SELECT id, vaterid, mutterid /*+ index(bit_map_vater_mutter) */
    FROM einwohner
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           wohnort = 'Berlin'
)
SELECT COUNT(b.id)
FROM nicht_berliner n, berliner b
WHERE (b.vaterid = n.vaterid OR b.mutterid = n.mutterid);

-- Mit Bitmap vater_mutter und Bitmap wohnort (Bundesland indexierung war immer teuerer)
'-----------------------------------------------------------------------------------------------------
| Id  | Operation                             | Name        | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                      |             |     1 |    28 |     6   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                       |             |     1 |    28 |            |          |
|   2 |   NESTED LOOPS                        |             |     1 |    28 |     6   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER   |     1 |    14 |     1   (0)| 00:00:01 |
|   4 |     BITMAP CONVERSION TO ROWIDS       |             |       |       |            |          |
|*  5 |      BITMAP INDEX SINGLE VALUE        | IDX_WOHNORT |       |       |            |          |

PLAN_TABLE_OUTPUT
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
|*  6 |    TABLE ACCESS FULL                  | EINWOHNER   |     1 |    14 |     5   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------------

-- Mit B-Baum Wohnort und bitmap vater_mutter
-------------------------------------------------------------------------------------------------------
| Id  | Operation                       | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                |                      |     1 |    28 |     7   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                 |                      |     1 |    28 |            |          |
|   2 |   NESTED LOOPS                  |                      |     1 |    28 |     7   (0)| 00:00:01 |
|*  3 |    VIEW                         | index$_join$_002     |     1 |    14 |     2   (0)| 00:00:01 |
|*  4 |     HASH JOIN                   |                      |       |       |            |          |
|*  5 |      INDEX RANGE SCAN           | IDX_WOHNORT          |     1 |    14 |     1   (0)| 00:00:01 |

PLAN_TABLE_OUTPUT
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
|   6 |      BITMAP CONVERSION TO ROWIDS|                      |     1 |    14 |     1   (0)| 00:00:01 |
|*  7 |       BITMAP INDEX FULL SCAN    | BIT_MAP_VATER_MUTTER |       |       |            |          |
|*  8 |    TABLE ACCESS FULL            | EINWOHNER            |     1 |    14 |     5   (0)| 00:00:01 |
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
'--------------------------------------------------------------------------------
| Id  | Operation          | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |           |     1 |    12 |     5   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE    |           |     1 |    12 |            |          |
|*  2 |   TABLE ACCESS FULL| EINWOHNER |   160 |  1920 |     5   (0)| 00:00:01 |
--------------------------------------------------------------------------------

-- mit function-based idx_month
---------------------------------------------------------------------------------------------------
| Id  | Operation                             | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                      |           |     1 |    17 |     3   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                       |           |     1 |    17 |            |          |
|   2 |   INLIST ITERATOR                     |           |       |       |            |          |
|*  3 |    TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER |     6 |   102 |     3   (0)| 00:00:01 |
|*  4 |     INDEX RANGE SCAN                  | IDX_MONTH |     4 |       |     2   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------
-- aber function-based index erstellung selber teuer -> cost plan für idx_month
------------------------------------------------------------------------------------
| Id  | Operation              | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------
|   0 | CREATE INDEX STATEMENT |           |  1001 |  8008 |     6   (0)| 00:00:01 |
|   1 |  INDEX BUILD NON UNIQUE| IDX_MONTH |       |       |            |          |
|   2 |   SORT CREATE INDEX    |           |  1001 |  8008 |            |          |
|   3 |    TABLE ACCESS FULL   | EINWOHNER |  1001 |  8008 |     5   (0)| 00:00:01 |
------------------------------------------------------------------------------------
'

------------------------------------------------ Aufgabe 3 (ergebniss = 86)
--View: Aufgabe4_3
SELECT COUNT(id)
FROM einwohner
WHERE regexp_replace(adresse, '[^0-9]', '') = 23;

-- ohne weiteren index
'--------------------------------------------------------------------------------
| Id  | Operation          | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |           |     1 |    32 |     5   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE    |           |     1 |    32 |            |          |
|*  2 |   TABLE ACCESS FULL| EINWOHNER |    12 |   384 |     5   (0)| 00:00:01 |
--------------------------------------------------------------------------------
-------------------------------------------------------------------------------
| Id  | Operation         | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |           |    50 |  4150 |     5   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| EINWOHNER |    50 |  4150 |     5   (0)| 00:00:01 |
-------------------------------------------------------------------------------

-- function-based index has no effect and is also expensive to create (cost 7)
'

------------------------------------------- Aufgabe 4 (ergebniss = /74/251/494/771/1000)
--View: Aufgabe4_4

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
  10 - filter("R"."RECLEVEL"="ML"."MAXRECLEVEL")
