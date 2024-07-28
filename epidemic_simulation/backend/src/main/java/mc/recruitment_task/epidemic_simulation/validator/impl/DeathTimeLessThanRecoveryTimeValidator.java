package mc.recruitment_task.epidemic_simulation.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mc.recruitment_task.epidemic_simulation.validator.object.DeathTimeAndRecoveryTimeInterface;
import mc.recruitment_task.epidemic_simulation.validator.request.DeathTimeLessThanRecoveryTime;

public class DeathTimeLessThanRecoveryTimeValidator implements ConstraintValidator<DeathTimeLessThanRecoveryTime, DeathTimeAndRecoveryTimeInterface> {
    @Override
    public boolean isValid(DeathTimeAndRecoveryTimeInterface request, ConstraintValidatorContext context) {
        if (request == null) return true;

        return request.getDeathTime() <= request.getRecoveryTime();
    }
}
