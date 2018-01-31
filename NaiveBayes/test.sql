--SELECT label, count(*) / sum(count(*)) over () as percent
--FROM CRUISE_TRAIN
--GROUP BY label;


SELECT 
    label, 
    sex,
--    count(*) as num_sex,
--    sum(count(*)) over (PARTITION BY label) max_num_sex,
    (count(*) / sum(count(*)) over (PARTITION BY label)) 
    * (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
FROM CRUISE_TRAIN
GROUP BY label, sex
ORDER BY per desc;


----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------


SELECT 
    label, 
    sex,
    pclass,
--    count(*) as num_sex,
--    sum(count(*)) over (PARTITION BY sex,label) as n_sex,
--    sum(count(*)) over (PARTITION BY pclass, label) as num_sex_class,
--    sum(count(*)) over (PARTITION BY label) max_num_sex,
    (sum(count(*)) over (PARTITION BY pclass, label) / sum(count(*)) over (PARTITION BY label))
    * (sum(count(*)) over (PARTITION BY sex, label) / sum(count(*)) over (PARTITION BY label)) 
    * (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
FROM CRUISE_TRAIN
GROUP BY label, sex, pclass
ORDER BY per desc;