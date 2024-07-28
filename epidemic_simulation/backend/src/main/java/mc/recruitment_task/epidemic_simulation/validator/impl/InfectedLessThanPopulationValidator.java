package mc.recruitment_task.epidemic_simulation.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mc.recruitment_task.epidemic_simulation.validator.object.InfectedAndPopulationInterface;
import mc.recruitment_task.epidemic_simulation.validator.request.InfectedLessThanPopulation;

public class InfectedLessThanPopulationValidator implements ConstraintValidator<InfectedLessThanPopulation, InfectedAndPopulationInterface> {
    @Override
    public boolean isValid(InfectedAndPopulationInterface request, ConstraintValidatorContext context) {
        if (request == null) return true;

        return request.getInfected() <= request.getPopulation();
    }
}
