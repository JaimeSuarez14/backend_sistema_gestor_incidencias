package utp.edu.sistema_gestor_incidencias;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import utp.edu.sistema_gestor_incidencias.controller.SeguimientoController;

@WebMvcTest(SeguimientoController.class)
class SeguimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void debeListarSeguimientos() throws Exception {
        mockMvc.perform(get("/api/seguimiento"))
                .andExpect(status().isOk());
    }

    @Test
    void debeObtenerSeguimientoPorId() throws Exception {
        mockMvc.perform(get("/api/seguimiento/1"))
                .andExpect(status().isOk());
    }
    
}