package cl.ms.maquinarias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.RegionService;
@RestController
@RequestMapping("/api/regiones")
public class RegionController {
	
	@Autowired
	private RegionService regionService;
	
	
	
	@GetMapping("/listarRegiones")
	private ResponseList listarRegiones() {
		return regionService.listarRegiones();
	}

}
