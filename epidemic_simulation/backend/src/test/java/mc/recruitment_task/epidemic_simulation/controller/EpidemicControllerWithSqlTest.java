package mc.recruitment_task.epidemic_simulation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsPageRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicParamsRes;
import mc.recruitment_task.epidemic_simulation.dto.response.EpidemicSimRes;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicParamsRepository;
import mc.recruitment_task.epidemic_simulation.repository.EpidemicSimRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
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
@Sql(value = {"/sql/create-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/delete-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EpidemicControllerWithSqlTest {
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
    void EpidemicController_getEpidemicsPage_returnsEpidemicParamsPageRes() throws Exception {
        // Given
        int page = 0;
        int pageSize = 10;

        // When
        MvcResult mvcResult = mockMvc.perform(
                get("/epidemic")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        EpidemicParamsPageRes response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EpidemicParamsPageRes.class);

        Optional<EpidemicParamsRes> paramsRes = response.getParams().stream().findFirst();

        assertEquals(1, response.getCount());
        assertEquals(1, response.getParams().size());
        assertTrue(paramsRes.isPresent());
        assertEquals(1, paramsRes.get().getId());
        assertEquals("test epidemic", paramsRes.get().getName());
        assertEquals(1, paramsRes.get().getPopulation());
        assertEquals(1, paramsRes.get().getInfected());
        assertEquals(1, paramsRes.get().getR());
        assertEquals(1, paramsRes.get().getMortality());
        assertEquals(1, paramsRes.get().getRecoveryTime());
        assertEquals(1, paramsRes.get().getDeathTime());
        assertEquals(1, paramsRes.get().getSimulationTime());
    }

    @Test
    void EpidemicController_getEpidemic_returnsEpidemicParamsRes() throws Exception {
        long id = 1;

        MvcResult mvcResult = mockMvc.perform(
                get("/epidemic/" + id))
                .andExpect(status().isOk())
                .andReturn();

        EpidemicParamsRes response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EpidemicParamsRes.class);

        assertEquals(1, response.getId());
        assertEquals("test epidemic", response.getName());
        assertEquals(1, response.getPopulation());
        assertEquals(1, response.getInfected());
        assertEquals(1, response.getR());
        assertEquals(1, response.getMortality());
        assertEquals(1, response.getRecoveryTime());
        assertEquals(1, response.getDeathTime());
        assertEquals(1, response.getSimulationTime());
    }

    @Test
    void EpidemicController_searchEpidemic_returnsEpidemicParamsPageRes() throws Exception {
        int page = 0;
        int pageSize = 10;
        String name = "test epidemic";

        MvcResult mvcResult = mockMvc.perform(
                get("/epidemic/search")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("name", name))
                .andExpect(status().isOk())
                .andReturn();

        EpidemicParamsPageRes response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                EpidemicParamsPageRes.class);

        Optional<EpidemicParamsRes> paramsRes = response.getParams().stream().findFirst();

        assertEquals(1, response.getCount());
        assertEquals(1, response.getParams().size());
        assertTrue(paramsRes.isPresent());
        assertEquals(1, paramsRes.get().getId());
        assertEquals("test epidemic", paramsRes.get().getName());
        assertEquals(1, paramsRes.get().getPopulation());
        assertEquals(1, paramsRes.get().getInfected());
        assertEquals(1, paramsRes.get().getR());
        assertEquals(1, paramsRes.get().getMortality());
        assertEquals(1, paramsRes.get().getRecoveryTime());
        assertEquals(1, paramsRes.get().getDeathTime());
        assertEquals(1, paramsRes.get().getSimulationTime());
    }

    @Test
    void EpidemicController_getEpidemicSim_returnsListEpidemicSimRes() throws Exception {
        long id = 1;

        MvcResult mvcResult = mockMvc.perform(
                get("/epidemic/simulation/" + id))
                .andExpect(status().isOk())
                .andReturn();

        List<EpidemicSimRes> response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<EpidemicSimRes>>() {});

        assertEquals(1, response.size());

        EpidemicSimRes sim = response.get(0);

        assertEquals(1, sim.getDay());
        assertEquals(1, sim.getInfected());
        assertEquals(0, sim.getHealthy());
        assertEquals(0, sim.getDead());
        assertEquals(0, sim.getImmune());
    }

    @Test
    void EpidemicController_deleteEpidemic_returnsVoid() throws Exception {
        long id = 1;

        MvcResult mvcResult = mockMvc.perform(
                delete("/epidemic/" + id))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(epidemicParamsRepository.existsById(id));
    }

    @Test
    void EpidemicController_getAlgorithms_returnsListEpidemicAlgorithmType() throws Exception {
        List<String> expectedResult = List.of("SIMPLE", "ADVANCED");

        MvcResult mvcResult = mockMvc.perform(
                        get("/epidemic/algorithm"))
                .andExpect(status().isOk())
                .andReturn();

        List<String> response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<String>>() {});

        assertEquals(expectedResult, response);
    }
}