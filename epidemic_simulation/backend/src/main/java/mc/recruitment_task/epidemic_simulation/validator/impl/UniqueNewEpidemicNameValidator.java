package mc.recruitment_task.epidemic_simulation.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReqPut;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicParamsRepository;
import mc.recruitment_task.epidemic_simulation.validator.request.UniqueNewEpidemicName;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueNewEpidemicNameValidator implements ConstraintValidator<UniqueNewEpidemicName, EpidemicParamsReqPut>{
    @Autowired
    EpidemicParamsRepository epidemicParamsRepository;

    @Override
    public boolean isValid(EpidemicParamsReqPut request, ConstraintValidatorContext context) {
        if (request == null) return true;

        return epidemicParamsRepository.findByNameAndIdNot(request.getName(), request.getId()).isEmpty();
    }
}
