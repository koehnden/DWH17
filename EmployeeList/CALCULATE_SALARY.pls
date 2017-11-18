create or replace FUNCTION CALCULATE_SALARY(mailacc varchar2) RETURN NUMBER AS   
  klasse varchar2(500);
  homepage varchar2(500);
  sal number;
BEGIN    
  begin
    select titel,hp into klasse, homepage from PERSONEN where mailacc like mail;
  Exception
    when no_data_found then
      --zur Adresse exisitert kein Datensatz
      return 0;
  end;
  klasse:=upper(klasse);
  
  case
    when REGEXP_LIKE (upper(klasse), upper('^Prof.')) then sal:=65000;
    when REGEXP_LIKE (upper(klasse), upper('^PD.')) then sal:=60000;
    when REGEXP_LIKE (upper(klasse), upper('Ph.')) or REGEXP_LIKE (upper(klasse), upper('^dr.')) then sal:=45000;
    else sal:=35000;    
  end case;
  if(homepage is null) then
    return sal-500;
  else
    return sal;
  end if;
  
END CALCULATE_SALARY;