package com.healthtraze.etraze.api.security.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.security.model.User;


public class UserValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, Constants.FIRSTNAME, StringIteration.MESSAGEFIRSTNAME, StringIteration.FIRSTNAMEISREQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, Constants.LASTNAME, StringIteration.MESSAGELASTNAME, StringIteration.LASTNAMEISREQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, Constants.PASSWORD, StringIteration.MESSAGEPASSWORD, StringIteration.LASTNAMEISREQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, Constants.USERNAME, StringIteration.MESSAGEUSERNAME, StringIteration.USERNAMEISREQUIRED);
    }

}
