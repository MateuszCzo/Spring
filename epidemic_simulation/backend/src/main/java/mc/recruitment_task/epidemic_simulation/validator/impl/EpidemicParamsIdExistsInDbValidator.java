package mc.recruitment_task.epidemic_simulation.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicParamsRepository;
import mc.recruitment_task.epidemic_simulation.validator.request.EpidemicParamsIdExistsInDb;
import org.springframework.beans.factory.annotation.Autowired;

public class EpidemicParamsIdExistsInDbValidator implements ConstraintValidator<EpidemicParamsIdExistsInDb, Long> {
    @Autowired
    private EpidemicParamsRepository epidemicParamsRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return epidemicParamsRepository.existsById(id);
    }
}
