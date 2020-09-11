package com.cg.banking.entity;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

@Converter(autoApply=true)
public class LDTConverter implements AttributeConverter<LocalDate,Date>{

	@Override
	public Date convertToDatabaseColumn(LocalDate ldt) {
		if(ldt!=null)
			return java.sql.Date.valueOf(ldt);//returns date in a format suitable for SQL
		return null;
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqldt) {
		if(sqldt!=null)
			return sqldt.toLocalDate(); //returns the date in a format suitable for JAVA
		return null;
	}

}
