create or replace FUNCTION wettbewerb(
    m_sex IN cruise_train.sex%TYPE,
    m_class IN cruise_train.pclass%TYPE,
    m_age in cruise_train.age%TYPE,
    m_fare in cruise_train.fare%TYPE
)
RETURN NUMBER 
AS 
    prediction NUMBER := 0;
BEGIN 


	SELECT calc.label into prediction
	FROM (SELECT 
			label, 
			sex,
			pclass,
			(cnt.num / sum(count(*)) over (PARTITION BY label))
            * (cntf.num / sum(count(*)) over (PARTITION BY label))
			* (sum(count(*)) over (PARTITION BY pclass, label) / sum(count(*)) over (PARTITION BY label))
			* (sum(count(*)) over (PARTITION BY sex, label) / sum(count(*)) over (PARTITION BY label)) 
			* (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
          FROM CRUISE_TRAIN, (SELECT t.label as lab, count(*) as num
                              FROM CRUISE_TRAIN t
                              WHERE t.age <= (m_age +4) or ((m_age is null or m_age < 1) and t.age is not null)
                              GROUP BY t.label) cnt, (SELECT tf.label as lab, count(*) as num
                                                     FROM CRUISE_TRAIN tf
                                                     WHERE tf.fare <= m_fare * 1.05
                                                     GROUP BY tf.label) cntf
        WHERE label = cnt.lab and label = cntf.lab
		GROUP BY label, sex, pclass, cnt.num, cntf.num
		ORDER BY per desc) calc
	WHERE calc.sex = m_sex and calc.pclass = m_class and rownum = 1; 

    DBMS_OUTPUT.PUT_LINE('prediction = ' || prediction);    
    return prediction;
END;

----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------


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


----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------


SELECT 
    label, 
    sex,
    pclass,
    embarked,
--    age,
--    sum(count(*)) over (PARTITION BY label, age) as num_age,
    sum(count(*)) over (PARTITION BY label, embarked) as num_embarked,
    sum(count(*)) over (PARTITION BY sex,label) as n_sex,
    sum(count(*)) over (PARTITION BY pclass, label) as num_sex_class,
    sum(count(*)) over (PARTITION BY label) max_num_sex
--    ,
--    (sum(count(*)) over (PARTITION BY pclass, label) / sum(count(*)) over (PARTITION BY label))
--   * (sum(count(*)) over (PARTITION BY sex, label) / sum(count(*)) over (PARTITION BY label)) 
--    * (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
FROM CRUISE_TRAIN
--WHERE age IS NOT NULL
GROUP BY label, sex, pclass, embarked, age;
--ORDER BY per desc;