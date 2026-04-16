package utp.edu.sistema_gestor_incidencias.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.edu.sistema_gestor_incidencias.model.Incidencia;

@RestController
@RequestMapping("/api/incidencia")
public class IncidenciaController {
	private List<Incidencia> incidencia = new ArrayList<>();
}
