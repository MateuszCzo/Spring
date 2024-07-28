package mc.recruitment_task.epidemic_simulation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReq;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReqPut;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsPageRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicSimRes;
import mc.recruitment_task.epidemic_simulation.enums.EpidemicAlgorithmType;
import mc.recruitment_task.epidemic_simulation.service.EpidemicServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/epidemic")
@RequiredArgsConstructor
public class EpidemicController {
    private final EpidemicServiceInterface epidemicService;

    @GetMapping()
    public ResponseEntity<EpidemicParamsPageRes> getEpidemicsPage(@RequestParam(name = "page", defaultValue = "0")
                                                                      @Min(0) int page,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10")
                                                                      @Min(1) @Max(100) int pageSize) {
        EpidemicParamsPageRes response = epidemicService.getEpidemicPage(page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpidemicParamsRes> getEpidemic(@PathVariable(name = "id") long id) {
        EpidemicParamsRes response = epidemicService.getEpidemic(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<EpidemicParamsPageRes> searchEpidemic(@RequestParam(name = "page", defaultValue = "0")
                                                                    @Min(0) int page,
                                                                @RequestParam(name = "pageSize", defaultValue = "10")
                                                                    @Min(1) @Max(100) int pageSize,
                                                                @RequestParam(name = "name")
                                                                    @Size(min = 1, max = 256) String name) {
        EpidemicParamsPageRes response = epidemicService.searchEpidemic(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/simulation/{id}")
    public ResponseEntity<List<EpidemicSimRes>> getEpidemicSim(@PathVariable(name = "id") long id) {
        List<EpidemicSimRes> response = epidemicService.getEpidemicSim(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulation")
    public ResponseEntity<EpidemicParamsRes> simEpidemic(@RequestBody @Valid EpidemicParamsReq request) {
        EpidemicParamsRes response = epidemicService.simEpidemic(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void deleteEpidemic(@PathVariable long id) {
        epidemicService.deleteEpidemic(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpidemicParamsRes> putEpidemic(@PathVariable long id,
                                                         @RequestBody @Valid EpidemicParamsReqPut request) {
        EpidemicParamsRes response = epidemicService.putEpidemic(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/algorithm")
    public ResponseEntity<List<EpidemicAlgorithmType>> getAlgorithms() {
        List<EpidemicAlgorithmType> result = epidemicService.getAlgorithms();

        return ResponseEntity.ok(result);
    }
}
