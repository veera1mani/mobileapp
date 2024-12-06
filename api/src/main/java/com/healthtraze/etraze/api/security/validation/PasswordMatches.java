package com.healthtraze.etraze.api.security.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Target;

import com.healthtraze.etraze.api.base.constant.Constants;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {

    String message() default Constants.PASSWORDSDONOTMATCH;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
