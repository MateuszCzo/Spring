package mc.recruitment_task.epidemic_simulation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReq;
import mc.recruitment_task.epidemic_simulation.dto.request.EpidemicParamsReqPut;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsRes;
import mc.recruitment_task.epidemic_simulation.enums.EpidemicAlgorithmType;
import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicParamsRepository;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicSimRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class EpidemicControllerWithoutSqlTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.0");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EpidemicParamsRepository epidemicParamsRepository;
    @Autowired
    private EpidemicSimRepository epidemicSimRepository;

    @Test
    void EpidemicController_simEpidemic_returnsEpidemicParamsRes() throws Exception {
        int simulationTime = 2;

        EpidemicParamsReq request = new EpidemicParamsReq();
        request.setName("test epidemic 2");
        request.setPopulation(10);
        request.setInfected(2);
        request.setR(2);
        request.setMortality(0.5f);
        request.setRecoveryTime(5);
        request.setDeathTime(2);
        request.setSimulationTime(simulationTime);
        request.setAlgorithmType(EpidemicAlgorithmType.SIMPLE);

        MvcResult mvcResult = mockMvc.perform(
                        post("/epidemic/simulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        EpidemicParamsRes result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EpidemicParamsRes.class);

        assertNotEquals(0, result.getId());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getPopulation(), result.getPopulation());
        assertEquals(request.getInfected(), result.getInfected());
        assertEquals(request.getR(), result.getR());
        assertEquals(request.getMortality(), result.getMortality());
        assertEquals(request.getRecoveryTime(), result.getRecoveryTime());
        assertEquals(request.getDeathTime(), result.getDeathTime());
        assertEquals(request.getSimulationTime(), result.getSimulationTime());

        Optional<EpidemicParameters> epidemicParameters = epidemicParamsRepository.findById(result.getId());

        assertTrue(epidemicParameters.isPresent());

        List<EpidemicSimulation> epidemicSim = epidemicSimRepository.findAllByEpidemicParameters(epidemicParameters.get());

        assertEquals(simulationTime, epidemicSim.size());
    }

    @Test
    void EpidemicController_putEpidemic_returnsEpidemicParamsRes() throws Exception {
        EpidemicParameters params = EpidemicParameters
                .builder()
                .name("test epidemic")
                .population(1)
                .infected(1)
                .r(1)
                .mortality(1)
                .recoveryTime(1)
                .deathTime(1)
                .simulationTime(1)
                .build();

        epidemicParamsRepository.save(params);

        int simulationTime = 2;

        EpidemicParamsReqPut request = new EpidemicParamsReqPut();
        request.setId(params.getId());
        request.setName("test epidemic");
        request.setPopulation(10);
        request.setInfected(2);
        request.setR(2);
        request.setMortality(0.5f);
        request.setRecoveryTime(5);
        request.setDeathTime(2);
        request.setSimulationTime(simulationTime);
        request.setAlgorithmType(EpidemicAlgorithmType.SIMPLE);

        MvcResult mvcResult = mockMvc.perform(
                        put("/epidemic/" + params.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        EpidemicParamsRes result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EpidemicParamsRes.class);

        assertEquals(params.getId(), result.getId());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getPopulation(), result.getPopulation());
        assertEquals(request.getInfected(), result.getInfected());
        assertEquals(request.getR(), result.getR());
        assertEquals(request.getMortality(), result.getMortality());
        assertEquals(request.getRecoveryTime(), result.getRecoveryTime());
        assertEquals(request.getDeathTime(), result.getDeathTime());
        assertEquals(request.getSimulationTime(), result.getSimulationTime());

        Optional<EpidemicParameters> epidemicParameters = epidemicParamsRepository.findById(result.getId());

        assertTrue(epidemicParameters.isPresent());

        List<EpidemicSimulation> epidemicSim = epidemicSimRepository.findAllByEpidemicParameters(epidemicParameters.get());

        assertEquals(simulationTime, epidemicSim.size());
    }
}