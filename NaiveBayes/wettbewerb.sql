-- Predicts outcome of the naive bayes classifier 
-- for a concrete instance of sex, ticket class
-- fare and age. Age and fare of all considerable guests
-- has to be lower than the age and fare of the guest
-- to be predicted
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
                              WHERE t.age <= (m_age + 4) or (m_age is null and t.age is null)
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

-- Computes the overall accuracy
create or replace FUNCTION eval_wettbewerb
RETURN NUMBER 
AS 
  accuracy NUMBER := 0;
  TP NUMBER := 0;
  FP NUMBER := 0;
  TN NUMBER := 0;
  FN NUMBER := 0;
  res NUMBER := 0;
BEGIN
	FOR r in (SELECT sex, label, pclass, age, fare
			  FROM CRUISE_TRAIN)
	LOOP
		SELECT wettbewerb(r.sex, r.pclass, r.age, r.fare) into res
		FROM DUAL;
		IF res = 1 AND r.label = 1 THEN
			TP := TP + 1;
		ELSIF res = 1 AND r.label = 0 THEN
			FP := FP + 1;
		ELSIF res = 0 AND r.label = 0 THEN
			TN := TN + 1;
		ELSIF res = 0 AND r.label = 1 THEN
			FN := FN + 1;
		END IF;
	END LOOP;
	accuracy := (TP + TN)/(TP + TN + FP + FN);
    DBMS_OUTPUT.PUT_LINE('accuracy = ' || accuracy);    
    RETURN accuracy;
END;

-- Creates a view containing the prediction for all test data
CREATE OR REPLACE VIEW wettbewerb_calc AS
SELECT wettbewerb(sex, pclass, age, fare) as label
FROM CRUISE_TEST;