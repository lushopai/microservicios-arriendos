package cl.ms.maquinarias.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/buscarPorUsername")
	private ResponseData<?> buscarUsuario(@RequestParam String username) {
		return usuarioService.buscarUsuarioPorUsername(username);

	}
	@GetMapping("/buscarPorTerm")
	private ResponseList buscarUsuarioPorTerm(@RequestParam String term) {
		return usuarioService.buscarPorTerm(term);

	}

	@PostMapping("/guardarUsuario")
	private GenericResponse guardarUsuario(@Valid @RequestBody Usuarios usuarios, BindingResult result) {
		GenericResponse response = new GenericResponse();

		List<String> errors = result.getFieldErrors().stream().map(err -> {
			return "El campo '" + err.getField() + "' " + err.getDefaultMessage();

		}).collect(Collectors.toList());
		

		if (result.hasErrors()) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(errors.toString());
			return response;
		}
		response = usuarioService.guardaarUsuario(usuarios);
		LOGGER.info("RESPONSE SERVICE :{}", response.toString());
		return response;
	}
	
	@GetMapping("/listarUsuarios")
	private ResponseList listarUsuarios(@RequestParam(name = "page",defaultValue = "0") int page) {
		LOGGER.info("CANTIDAD PAG ENVIADOS : " + page);
		return usuarioService.listarUsuarios(page);
	}
	
	@DeleteMapping("/eliminarUsuario")
	public GenericResponse eliminarUsuario(@RequestParam Long id) {
		return usuarioService.eliminarUsuario(id);
	}

}
