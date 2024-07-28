package mc.recruitment_task.epidemic_simulation.validator.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mc.recruitment_task.epidemic_simulation.validator.impl.UniqueNewEpidemicNameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueNewEpidemicNameValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNewEpidemicName {
    String message() default "Epidemic mame must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
