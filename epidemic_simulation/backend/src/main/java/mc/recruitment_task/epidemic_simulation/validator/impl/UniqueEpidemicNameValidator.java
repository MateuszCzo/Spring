package mc.recruitment_task.epidemic_simulation.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicParamsRepository;
import mc.recruitment_task.epidemic_simulation.validator.request.UniqueEpidemicName;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEpidemicNameValidator implements ConstraintValidator<UniqueEpidemicName, String> {
    @Autowired
    private EpidemicParamsRepository epidemicParamsRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) return true;

        return !epidemicParamsRepository.existsByName(name);
    }
}
