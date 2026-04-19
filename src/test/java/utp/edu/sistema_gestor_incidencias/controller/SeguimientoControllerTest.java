package utp.edu.sistema_gestor_incidencias.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Pruebas de integracion para {@link SeguimientoController}.
 *
 * <p>Esta clase puede usarse como modelo para otros tests de controladores
 * porque muestra la estructura base recomendada:
 * <p>1. levantar el contexto completo con {@code @SpringBootTest}
 * <p>2. habilitar {@code MockMvc} para simular peticiones HTTP
 * <p>3. preparar datos previos en {@code @BeforeEach}
 * <p>4. validar tanto el codigo HTTP como el contenido de la respuesta
 *
 * @author Steph
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SeguimientoControllerTest {

	/**
	 * Cliente de pruebas HTTP provisto por Spring.
	 *
	 * <p>Permite ejecutar solicitudes contra los endpoints sin iniciar un
	 * servidor real, lo que hace que las pruebas sean mas rapidas y controladas.
	 *
	 * @see MockMvc
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * Prepara el escenario comun para cada prueba.
	 *
	 * <p>Se registra un usuario, un tecnico y una incidencia base para que los
	 * casos de prueba puedan enfocarse solo en el comportamiento del seguimiento.
	 * El uso de {@code @DirtiesContext} garantiza que cada test empiece con un
	 * estado limpio e independiente.
	 *
	 * @throws Exception si ocurre un error al registrar los datos base del escenario
	 */
	@BeforeEach
	void setUp() throws Exception {
		mockMvc.perform(post("/api/usuario")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "nombre": "Usuario Base",
					  "correo": "usuario@empresa.com",
					  "estado": "activo",
					  "rol": "empleado",
					  "area": "Administracion"
					}
					"""));
		
		mockMvc.perform(post("/api/usuario")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "nombre": "Tecnico Base",
					  "correo": "tecnico@empresa.com",
					  "estado": "activo",
					  "rol": "tecnicoNivel1",
					  "area": "Logistica"
					}
					"""));
		
		mockMvc.perform(post("/api/incidencia")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "titulo": "Incidencia base",
					  "descripcion": "Descripcion base",
					  "estado": "abierto",
					  "usuario": {
					    "id": 1
					  },
					  "tecnico": {
					    "id": 2
					  }
					}
					"""));
	}
	
	/**
	 * Verifica el flujo exitoso de registro de seguimiento.
	 *
	 * <p>Se espera respuesta 200 OK cuando la incidencia asociada existe y que
	 * el JSON retornado contenga los datos persistidos correctamente.
	 *
	 * @throws Exception si ocurre un error al ejecutar la peticion o las validaciones
	 */
	@Test
	void crearSeguimientoDebeRetornar200CuandoLaIncidenciaExiste() throws Exception {
		mockMvc.perform(post("/api/seguimiento")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Primer seguimiento",
					  "fecha": "2026-04-17T10:00:00.000+00:00",
					  "estado": "activo",
					  "incidencia": {
					    "id": 1
					  }
					}
					"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idSeguimiento").value(1))
				.andExpect(jsonPath("$.comentario").value("Primer seguimiento"))
				.andExpect(jsonPath("$.estado").value("activo"))
				.andExpect(jsonPath("$.incidencia.id").value(1));
	}
	
	/**
	 * Verifica la validacion de regla de negocio cuando la incidencia no existe.
	 *
	 * <p>El controlador debe responder 404 Not Found y devolver el mensaje
	 * funcional esperado por el sistema.
	 *
	 * @throws Exception si ocurre un error al ejecutar la peticion o las validaciones
	 */
	@Test
	void crearSeguimientoDebeRetornar404CuandoLaIncidenciaNoExiste() throws Exception {
		mockMvc.perform(post("/api/seguimiento")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Seguimiento invalido",
					  "fecha": "2026-04-17T10:00:00.000+00:00",
					  "estado": "activo",
					  "incidencia": {
					    "id": 99
					  }
					}
					"""))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Incidencia no encontrada con ese ID"));
	}
	
	/**
	 * Verifica la validacion de datos obligatorios en la solicitud.
	 *
	 * <p>Si no se envia la incidencia relacionada, el sistema debe responder
	 * 400 Bad Request con un mensaje claro para el cliente.
	 *
	 * @throws Exception si ocurre un error al ejecutar la peticion o las validaciones
	 */
	@Test
	void crearSeguimientoDebeRetornar400CuandoNoSeEnvieLaIncidencia() throws Exception {
		mockMvc.perform(post("/api/seguimiento")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Seguimiento sin incidencia",
					  "fecha": "2026-04-17T10:00:00.000+00:00",
					  "estado": "activo"
					}
					"""))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Debe enviar el ID de la incidencia"));
	}
	
	/**
	 * Verifica la modificacion exitosa de un seguimiento existente.
	 *
	 * <p>Primero se crea el seguimiento y luego se actualiza. Finalmente se
	 * valida que la respuesta conserve el mismo identificador y refleje los
	 * nuevos valores enviados.
	 *
	 * @throws Exception si ocurre un error al ejecutar la peticion o las validaciones
	 */
	@Test
	void modificarSeguimientoDebeRetornar200CuandoExisteElSeguimientoYLaIncidencia() throws Exception {
		mockMvc.perform(post("/api/seguimiento")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Seguimiento inicial",
					  "fecha": "2026-04-17T10:00:00.000+00:00",
					  "estado": "activo",
					  "incidencia": {
					    "id": 1
					  }
					}
					"""))
				.andExpect(status().isOk());
		
		mockMvc.perform(post("/api/seguimiento/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Seguimiento actualizado",
					  "fecha": "2026-04-17T11:00:00.000+00:00",
					  "estado": "inactivo",
					  "incidencia": {
					    "id": 1
					  }
					}
					"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idSeguimiento").value(1))
				.andExpect(jsonPath("$.comentario").value("Seguimiento actualizado"))
				.andExpect(jsonPath("$.estado").value("inactivo"))
				.andExpect(jsonPath("$.incidencia.id").value(1));
	}
	
	/**
	 * Verifica el comportamiento cuando se intenta modificar un seguimiento
	 * inexistente.
	 *
	 * <p>El resultado esperado es 404 Not Found con el mensaje funcional
	 * correspondiente.
	 *
	 * @throws Exception si ocurre un error al ejecutar la peticion o las validaciones
	 */
	@Test
	void modificarSeguimientoDebeRetornar404CuandoElSeguimientoNoExiste() throws Exception {
		mockMvc.perform(post("/api/seguimiento/99")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Seguimiento inexistente",
					  "fecha": "2026-04-17T11:00:00.000+00:00",
					  "estado": "activo",
					  "incidencia": {
					    "id": 1
					  }
					}
					"""))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Seguimiento no encontrado con ese ID"));
	}
	
	/**
	 * Verifica la validacion de incidencia asociada durante la actualizacion.
	 *
	 * <p>Aunque el endpoint de modificacion exista, si la nueva incidencia no
	 * esta registrada en el sistema se debe devolver 404 Not Found.
	 *
	 * @throws Exception si ocurre un error al ejecutar la peticion o las validaciones
	 */
	@Test
	void modificarSeguimientoDebeRetornar404CuandoLaIncidenciaNoExiste() throws Exception {
		mockMvc.perform(post("/api/seguimiento/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "comentario": "Seguimiento con incidencia invalida",
					  "fecha": "2026-04-17T11:00:00.000+00:00",
					  "estado": "activo",
					  "incidencia": {
					    "id": 999
					  }
					}
					"""))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Incidencia no encontrada con ese ID"));
	}
}
