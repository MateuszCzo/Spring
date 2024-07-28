package mc.recruitment_task.epidemic_simulation.validator.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mc.recruitment_task.epidemic_simulation.validator.impl.UniqueEpidemicNameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEpidemicNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEpidemicName {
    String message() default "Epidemic mame must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
