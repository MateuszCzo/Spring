package mc.recruitment_task.epidemic_simulation.dto.response;

import lombok.Data;

import java.util.Collection;

@Data
public class EpidemicParamsPageRes {
    long count;
    Collection<EpidemicParamsRes> params;
}
