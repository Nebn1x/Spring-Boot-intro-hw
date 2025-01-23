package springboot.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String password;
    private String repeatedPassword;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.repeatedPassword = constraintAnnotation.repeatedPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object repeatedPasswordValue = new BeanWrapperImpl(value).getPropertyValue(repeatedPassword);

        if (passwordValue == null || repeatedPasswordValue == null) {
            return false;
        }

        return passwordValue.equals(repeatedPasswordValue);
    }
}