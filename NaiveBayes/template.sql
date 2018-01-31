-- Predicts outcome of the naive bayes classifier 
-- for a concrete instance of sex
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
CREATE OR REPLACE FUNCTION eval_naive_bayes_sex
RETURN NUMBER 
AS 
  accuracy NUMBER := 0;
BEGIN         
    DBMS_OUTPUT.PUT_LINE('accuracy = ' || accuracy);    
    RETURN accuracy;
END;


-- Predicts outcome of the naive bayes classifier 
-- for a concrete instance of sex and ticket class
CREATE OR REPLACE FUNCTION predict_naive_bayes_sex_class(
    m_sex IN cruise_train.sex%TYPE,
    m_class IN cruise_train.pclass%TYPE
)
RETURN NUMBER 
AS 
    prediction NUMBER := 0;
BEGIN           
    DBMS_OUTPUT.PUT_LINE('prediction = ' || prediction);    
    return prediction;
END;


-- Computes the overall accuracy
CREATE OR REPLACE FUNCTION eval_naive_bayes_sex_class
RETURN NUMBER 
AS 
  accuracy NUMBER := 0;
BEGIN         
    DBMS_OUTPUT.PUT_LINE('accuracy = ' || accuracy);    
    RETURN accuracy;
END;