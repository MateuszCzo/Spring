package mc.recruitment_task.epidemic_simulation.service;

import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReq;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReqPut;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsPageRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicSimRes;
import mc.recruitment_task.epidemic_simulation.enums.EpidemicAlgorithmType;

import java.util.List;

public interface EpidemicServiceInterface {
    EpidemicParamsPageRes getEpidemicPage(int page, int pageSize);

    EpidemicParamsRes getEpidemic(long id);

    EpidemicParamsPageRes searchEpidemic(String name, int page, int pageSize);

    List<EpidemicSimRes> getEpidemicSim(long id);

    EpidemicParamsRes simEpidemic(EpidemicParamsReq epidemicParametersRequest);

    void deleteEpidemic(long id);

    EpidemicParamsRes putEpidemic(long id, EpidemicParamsReqPut request);

    List<EpidemicAlgorithmType> getAlgorithms();
}
