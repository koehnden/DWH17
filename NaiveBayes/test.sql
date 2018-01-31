--SELECT label, count(*) / sum(count(*)) over () as percent
--FROM CRUISE_TRAIN
--GROUP BY label;


SELECT 
        label, 
        sex,
--        count(*) as num_sex,
--        sum(count(*)) over (PARTITION BY label) max_num_sex,
        (count(*) / sum(count(*)) over (PARTITION BY label)) 
        * (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
    FROM CRUISE_TRAIN
    GROUP BY label, sex
    ORDER BY per desc;