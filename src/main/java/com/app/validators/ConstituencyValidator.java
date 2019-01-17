package com.app.validators;

import com.app.dto.ConstituencyDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ConstituencyValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ConstituencyDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            if (o == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }

            ConstituencyDto constituencyDto = (ConstituencyDto) o;

            String name = constituencyDto.getName();
            if (name == null || name.equals("")) {
                errors.rejectValue("name", "CONSTITUENCY IS EMPTY");
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.VALIDATION, "CONSTITUENCY VALIDATION: " + e);
        }
    }
}