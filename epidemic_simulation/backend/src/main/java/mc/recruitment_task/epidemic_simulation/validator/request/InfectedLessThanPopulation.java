package mc.recruitment_task.epidemic_simulation.validator.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mc.recruitment_task.epidemic_simulation.validator.impl.InfectedLessThanPopulationValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InfectedLessThanPopulationValidator.class)
public @interface InfectedLessThanPopulation {
    String message() default "Infected people cannot be greater than population size.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
