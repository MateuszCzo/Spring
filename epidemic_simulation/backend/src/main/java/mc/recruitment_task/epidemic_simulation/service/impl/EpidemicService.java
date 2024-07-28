package mc.recruitment_task.epidemic_simulation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReq;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReqPut;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsPageRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicSimRes;
import mc.recruitment_task.epidemic_simulation.enums.EpidemicAlgorithmType;
import mc.recruitment_task.epidemic_simulation.exception.EpidemicAlgorithmNotFoundException;
import mc.recruitment_task.epidemic_simulation.exception.EpidemicParamsNotFoundException;
import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicParamsRepository;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicSimRepository;
import mc.recruitment_task.epidemic_simulation.service.EpidemicAlgorithmInterface;
import mc.recruitment_task.epidemic_simulation.service.EpidemicServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service()
@RequiredArgsConstructor
public class EpidemicService implements EpidemicServiceInterface {
    private final Map<String, EpidemicAlgorithmInterface> epidemicAlgorithms;
    private final EpidemicParamsRepository epidemicParamsRepository;
    private final EpidemicSimRepository epidemicSimRepository;
    private final ObjectMapper objectMapper;

    @Override
    public EpidemicParamsPageRes getEpidemicPage(int page, int pageSize) {
        Page<EpidemicParameters> epidemicParameters = epidemicParamsRepository.findAll(
                PageRequest.of(page, pageSize)
        );

        long count = epidemicParamsRepository.count();

        Collection<EpidemicParamsRes> dtoParams = epidemicParameters.stream()
                .map(object -> objectMapper.convertValue(object, EpidemicParamsRes.class))
                .toList();

        EpidemicParamsPageRes response = new EpidemicParamsPageRes();
        response.setCount(count);
        response.setParams(dtoParams);

        return response;
    }

    @Override
    public EpidemicParamsRes getEpidemic(long id) {
        EpidemicParameters epidemicParameters = epidemicParamsRepository.findById(id)
                .orElseThrow(() -> new EpidemicParamsNotFoundException("Cannot find epidemic params"));

        return objectMapper.convertValue(epidemicParameters, EpidemicParamsRes.class);
    }

    @Override
    public EpidemicParamsPageRes searchEpidemic(String name, int page, int pageSize) {
        Page<EpidemicParameters> epidemicParameters = epidemicParamsRepository.findByNameContaining(
                name,
                PageRequest.of(page, pageSize)
        );

        long count = epidemicParamsRepository.countByNameContaining(name);

        Collection<EpidemicParamsRes> dtoParams = epidemicParameters.stream()
                .map(object -> objectMapper.convertValue(object, EpidemicParamsRes.class))
                .toList();

        EpidemicParamsPageRes response = new EpidemicParamsPageRes();
        response.setCount(count);
        response.setParams(dtoParams);

        return response;
    }

    @Override
    public List<EpidemicSimRes> getEpidemicSim(long id) {
        EpidemicParameters epidemicParameters = epidemicParamsRepository.findById(id)
                .orElseThrow(() -> new EpidemicParamsNotFoundException("Cannot find epidemic simulation"));

        Collection<EpidemicSimulation> simulation = epidemicSimRepository.findAllByEpidemicParameters(epidemicParameters);

        return simulation.stream()
                .map(object -> objectMapper.convertValue(object, EpidemicSimRes.class))
                .collect(Collectors.toList());
    }

    @Override
    public EpidemicParamsRes simEpidemic(EpidemicParamsReq request) {
        if (!epidemicAlgorithms.containsKey(request.getAlgorithmType().getServiceName())) {
            throw new EpidemicAlgorithmNotFoundException("Can not find epidemic algorithm");
        }
        EpidemicAlgorithmInterface algorithm = epidemicAlgorithms.get(request.getAlgorithmType().getServiceName());

        EpidemicParameters epidemicParameters = objectMapper.convertValue(request, EpidemicParameters.class);

        epidemicParamsRepository.save(epidemicParameters);

        List<EpidemicSimulation> epidemicSimulations = algorithm.simulate(epidemicParameters);

        epidemicSimRepository.saveAll(epidemicSimulations);

        return objectMapper.convertValue(epidemicParameters, EpidemicParamsRes.class);
    }

    @Override
    @Transactional
    public void deleteEpidemic(long id) {
        epidemicSimRepository.deleteByEpidemicParametersId(id);

        epidemicParamsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public EpidemicParamsRes putEpidemic(long id, EpidemicParamsReqPut request) {
        if (id != request.getId()) {
            throw new EpidemicAlgorithmNotFoundException("Invalid epidemic id");
        }
        if (!epidemicAlgorithms.containsKey(request.getAlgorithmType().getServiceName())) {
            throw new EpidemicAlgorithmNotFoundException("Can not find epidemic algorithm");
        }
        EpidemicAlgorithmInterface algorithm = epidemicAlgorithms.get(request.getAlgorithmType().getServiceName());

        EpidemicParameters params = epidemicParamsRepository.findById(id)
                .orElseThrow(() -> new EpidemicParamsNotFoundException("Cannot find epidemic simulation"));

        params.setName(request.getName());
        params.setPopulation(request.getPopulation());
        params.setInfected(request.getInfected());
        params.setR(request.getR());
        params.setMortality(request.getMortality());
        params.setRecoveryTime(request.getRecoveryTime());
        params.setDeathTime(request.getDeathTime());
        params.setSimulationTime(request.getSimulationTime());

        epidemicParamsRepository.save(params);

        epidemicSimRepository.deleteByEpidemicParametersId(id);

        Collection<EpidemicSimulation> epidemicSimulations = algorithm.simulate(params);

        epidemicSimRepository.saveAll(epidemicSimulations);

        return objectMapper.convertValue(params, EpidemicParamsRes.class);
    }

    @Override
    public List<EpidemicAlgorithmType> getAlgorithms() {
        return List.of(EpidemicAlgorithmType.class.getEnumConstants());
    }
}
