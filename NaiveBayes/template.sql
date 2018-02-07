-- Predicts outcome of the naive bayes classifier 
-- for a concrete instance of sex

----------- RESULT ----------------
--PREDICT_NAIVE_BAYES_SEX('FEMALE')
-----------------------------------
--                                1

CREATE OR REPLACE FUNCTION predict_naive_bayes_sex(
    m_sex IN cruise_train.sex%TYPE
)
RETURN NUMBER 
AS 
    prediction NUMBER := 0;
BEGIN           
	SELECT calc.label into prediction
	FROM (SELECT 
			label, 
			sex,
			(count(*) / sum(count(*)) over (PARTITION BY label)) 
			* (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
		FROM CRUISE_TRAIN
		GROUP BY label, sex
		ORDER BY per desc) calc
	WHERE calc.sex = m_sex and rownum = 1; 
	
    DBMS_OUTPUT.PUT_LINE('prediction = ' || prediction);    
    return prediction;
END;


-- Computes the overall accuracy

----------- RESULT ----------------
' EVAL_NAIVE_BAYES_SEX
--------------------
,783479349 '

CREATE OR REPLACE FUNCTION eval_naive_bayes_sex
RETURN NUMBER 
AS 
  accuracy NUMBER := 0;
  TP NUMBER := 0;
  FP NUMBER := 0;
  TN NUMBER := 0;
  FN NUMBER := 0;
  res NUMBER := 0;
BEGIN
	FOR r in (SELECT sex, label
			  FROM CRUISE_TRAIN)
	LOOP
		SELECT predict_naive_bayes_sex(r.sex) into res
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



-- Predicts outcome of the naive bayes classifier 
-- for a concrete instance of sex and ticket class

----------- RESULT ----------------
--PREDICT_NAIVE_BAYES_SEX_CLASS('FEMALE',1)
-------------------------------------------
--                                        1

CREATE OR REPLACE FUNCTION predict_naive_bayes_sex_class(
    m_sex IN cruise_train.sex%TYPE,
    m_class IN cruise_train.pclass%TYPE
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
			(sum(count(*)) over (PARTITION BY pclass, label) / sum(count(*)) over (PARTITION BY label))
			* (sum(count(*)) over (PARTITION BY sex, label) / sum(count(*)) over (PARTITION BY label)) 
			* (sum(count(*)) over  (PARTITION by (label)) /  sum(count(*)) over ()) as per
		FROM CRUISE_TRAIN
		GROUP BY label, sex, pclass
		ORDER BY per desc) calc
	WHERE calc.sex = m_sex and calc.pclass = m_class and rownum = 1; 
	
    DBMS_OUTPUT.PUT_LINE('prediction = ' || prediction);    
    return prediction;
END;


-- Computes the overall accuracy

----------- RESULT ----------------
'EVAL_NAIVE_BAYES_SEX_CLASS
--------------------------
,783479349 '

CREATE OR REPLACE FUNCTION eval_naive_bayes_sex_class
RETURN NUMBER 
AS 
  accuracy NUMBER := 0;
  TP NUMBER := 0;
  FP NUMBER := 0;
  TN NUMBER := 0;
  FN NUMBER := 0;
  res NUMBER := 0;
BEGIN
	FOR r in (SELECT sex, label, pclass
			  FROM CRUISE_TRAIN)
	LOOP
		SELECT predict_naive_bayes_sex_class(r.sex, r.pclass) into res
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