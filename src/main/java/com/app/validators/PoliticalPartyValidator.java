package com.app.validators;

import com.app.dto.PoliticalPartyDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PoliticalPartyValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PoliticalPartyDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            if (o == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }

            PoliticalPartyDto politicalPartyDto = (PoliticalPartyDto) o;
            String name = politicalPartyDto.getName();
            if (name == null || name.equals("")) {
                errors.rejectValue("name", "NAME IS EMPTY");
            }
            String description = politicalPartyDto.getDescription();
            if (description == null || description.matches("")) {
                errors.rejectValue("description", "DESCRIPTION IS EMPTY");
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.VALIDATION, "POLITICAL PARTY VALIDATION: " + e);
        }
    }
}