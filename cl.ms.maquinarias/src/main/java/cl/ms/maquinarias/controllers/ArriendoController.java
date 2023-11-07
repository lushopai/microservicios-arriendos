package cl.ms.maquinarias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.models.Arriendo;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseArriendoAtrasados;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.ArriendoService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/arriendos")
public class ArriendoController {
	
	@Autowired
	ArriendoService arriendoService;
	
	@GetMapping("/")
	public ResponseData<Arriendo> listarArriendoPorId(@RequestParam Long id) {
		return arriendoService.buscarArriendoPorId(id);
	}
	
	@PostMapping("/")
	public GenericResponse crearArriendo(@org.springframework.web.bind.annotation.RequestBody Arriendo arriendo) {
		return arriendoService.guardarArriendo(arriendo);
	}
	
	@GetMapping("/atrasados")
	public ResponseArriendoAtrasados listarAtrasados() {
		return arriendoService.arriendosAtrasados();
	}

}
