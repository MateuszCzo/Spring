package mc.recruitment_task.epidemic_simulation.validator.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mc.recruitment_task.epidemic_simulation.validator.impl.DeathTimeLessThanRecoveryTimeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DeathTimeLessThanRecoveryTimeValidator.class)
public @interface DeathTimeLessThanRecoveryTime {
    String message() default "Death time must be less than recovery time.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
