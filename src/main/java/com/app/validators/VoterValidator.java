package com.app.validators;

import com.app.dto.ConstituencyDto;
import com.app.dto.VoterDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Education;
import com.app.model.Gender;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VoterValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(VoterDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            if (o == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }
            VoterDto voterDto = (VoterDto) o;

            if (voterDto.getGender() == null) {
                errors.rejectValue("gender", "GENDER IS EMPTY");
            }

            if (voterDto.getAge() < 18) {
                errors.rejectValue("age", "AGE < 18 YEARS");
            }

            if (voterDto.getEducation() == null) {
                errors.rejectValue("education", "EDUCATION IS EMPTY");
            }

            if (voterDto.getConstituencyDto() == null) {
                errors.rejectValue("constituencyDTO", "CONSTITUENCY IS NULL");
            }

        } catch (Exception e) {
            throw new MyException(ExceptionCode.VALIDATION, "VOTER: " + e);
        }
    }
}