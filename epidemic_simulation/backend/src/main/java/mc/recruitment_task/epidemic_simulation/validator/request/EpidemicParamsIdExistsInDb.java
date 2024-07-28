package mc.recruitment_task.epidemic_simulation.validator.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mc.recruitment_task.epidemic_simulation.validator.impl.EpidemicParamsIdExistsInDbValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EpidemicParamsIdExistsInDbValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EpidemicParamsIdExistsInDb {
    String message() default "Epidemic id must be valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
