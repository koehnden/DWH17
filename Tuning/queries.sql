
----------------------------------------------------- Aufgabe 1 (ergebniss = 0 beim testdatensatz)
WITH nicht_berliner AS 
(
    SELECT vaterid, mutterid
    FROM EINWOHNER 
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL)   
           bundesland != 'Berlin'      
),
 berliner AS 
(
    SELECT id, vaterid, mutterid
    FROM EINWOHNER 
    WHERE (vaterid IS NOT NULL OR mutterid IS NOT NULL) AND
           bundesland = 'BERLIN'
)
SELECT count(b.id) 
FROM nicht_berliner n, berliner b 
WHERE (b.vaterid = n.vaterid OR b.mutterid = n.mutterid); 


-- mit B-Baum idx_vater_mutter
---------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name             | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |                  |     1 |    35 |     3   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                      |                  |     1 |    35 |            |          |
|*  2 |   TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER        |   160 |  5600 |     3   (0)| 00:00:01 |
|*  3 |    INDEX RANGE SCAN                  | IDX_VATER_MUTTER |     3 |       |     2   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------------      


-- mit Bitmap vater mutter index 
------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name          | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |               |     1 |    35 |     2   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                      |               |     1 |    35 |            |          |
|*  2 |   TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER     |   160 |  5600 |     2   (0)| 00:00:01 |
|   3 |    BITMAP CONVERSION TO ROWIDS       |               |       |       |            |          |
|*  4 |     BITMAP INDEX RANGE SCAN          | BIT_MAP_VATER |       |       |            |          |
-----------------------------------------------------------------------------------------------------


---------------------------------------------------------- Aufgabe 2
SELECT COUNT(*)
FROM einwohner
WHERE vaterid IS NULL AND 
      mutterid IS NULL AND      
      extract(month from geburtsdatum) IN (12,1,2);

-- mit B-Baum vater_mutter index
---------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name             | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |                  |     1 |    35 |     3   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                      |                  |     1 |    35 |            |          |
|*  2 |   TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER        |   160 |  5600 |     3   (0)| 00:00:01 |
|*  3 |    INDEX RANGE SCAN                  | IDX_VATER_MUTTER |     3 |       |     2   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------------

-- mit Bitmap vater mutter index 
------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name          | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |               |     1 |    35 |     2   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                      |               |     1 |    35 |            |          |
|*  2 |   TABLE ACCESS BY INDEX ROWID BATCHED| EINWOHNER     |   160 |  5600 |     2   (0)| 00:00:01 |
|   3 |    BITMAP CONVERSION TO ROWIDS       |               |       |       |            |          |
|*  4 |     BITMAP INDEX RANGE SCAN          | BIT_MAP_VATER |       |       |            |          |
------------------------------------------------------------------------------------------------------


------------------------------------------------ Aufgabe 3
SELECT COUNT(id) 
FROM einwohner 
WHERE regexp_replace(adresse, '[^0-9]', '') = 23;

-- ohne index 
--------------------------------------------------------------------------------
| Id  | Operation          | Name      | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |           |     1 |    32 |     5   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE    |           |     1 |    32 |            |          |
|*  2 |   TABLE ACCESS FULL| EINWOHNER |    12 |   384 |     5   (0)| 00:00:01 |
--------------------------------------------------------------------------------

