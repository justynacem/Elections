package com.app.validators;

import com.app.dto.CandidateDto;
import com.app.dto.ConstituencyDto;
import com.app.dto.PoliticalPartyDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CandidateValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CandidateDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            if (o == null) {
                throw new NullPointerException("OBJECT IS NULL");
            }

            CandidateDto candidateDto = (CandidateDto) o;

            String name = candidateDto.getName();
            if (name == null || name.equals("")) {
                errors.rejectValue("name", "NAME IS EMPTY");
            }

            String surname = candidateDto.getSurname();
            if (surname == null || surname.equals("")) {
                errors.rejectValue("surname", "SURNAME IS EMPTY");
            }

            if (candidateDto.getAge() < 18) {
                errors.rejectValue("age", "AGE < 18 YEARS");
            }

            if (candidateDto.getConstituencyDto() == null) {
                errors.rejectValue("constituencyDto", "CONSTITUENCY IS NULL");
            }

            if (candidateDto.getPoliticalPartyDto() == null) {
                errors.rejectValue("politicalPartyDto", "POLITICAL PARTY IS NULL");
            }

            if (candidateDto.getFile() == null || candidateDto.getFile().getBytes().length <= 0) {
                errors.rejectValue("file", "PHOTO FILE IS EMPTY");
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.VALIDATION, "CANDIDATE:" + e);
        }
    }
}