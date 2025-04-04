package org.example.controller;

import org.example.config.ThymeleafConfig;
import org.example.model.AgendamentoDTO;
import org.example.service.CaronaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaginaController.class)
@Import({ThymeleafConfig.class})
@MockBean(CaronaService.class)
@TestPropertySource(properties = {
        "spring.thymeleaf.prefix=classpath:/templates/,classpath:/test/resources/templates/",
        "GOOGLE_MAPS_API_KEY=test-key"
})
@AutoConfigureMockMvc
public class PaginaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaronaService caronaService;

    @Test
    public void testMostrarPaginaTeste() throws Exception {
        AgendamentoDTO mockAgendamento = AgendamentoDTO.builder()
                .origem("Av. Presidente Dutra, Rondonópolis")
                .destino("Av. Rio Branco, Rondonópolis")
                .mapaUrl("http://mock-url.com/mapa")
                .tempoEstimado("15 mins")
                .build();

        when(caronaService.agendarCarona(any(AgendamentoDTO.class)))
                .thenReturn(mockAgendamento);

        mockMvc.perform(get("/teste"))
                .andExpect(status().isOk())
                .andExpect(view().name("teste"))
                .andExpect(model().attributeExists("agendamento"));
    }

    @Test
    public void testeEndpoint() throws Exception {
        // Configuração do mock e teste
    }
}