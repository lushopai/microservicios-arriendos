package cl.ms.maquinarias.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.dto.ClienteDTO;
import cl.ms.maquinarias.models.Cliente;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private ClienteService clienteService;

	@PostMapping("/guardarCliente")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public GenericResponse guardarCliente(@Valid @RequestBody ClienteDTO cliente) {
		LOGGER.info("INICIO SERVICIO GUARDAR CLIENTE: "+ cliente.getRut());
		GenericResponse response = clienteService.guardarCliente(cliente);
		LOGGER.info("FIN SERVICIO GUARDAR CLIENTE: " + response.toString());
		return response;
	}

	@GetMapping("/buscarCliente")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public ResponseData<?> buscarCliente(@RequestParam Long id) {
		return clienteService.buscarPorId(id);
	}
	
	@PutMapping("/actualizarCliente")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public GenericResponse actualizarCliente(@RequestParam Long id,@RequestBody ClienteDTO cliente) {
		LOGGER.info("INICIO SERVICIO ACTUALIZAR CLIENTE: "+ cliente.getRut());
		GenericResponse response = clienteService.actualizarCliente(cliente, id);
		LOGGER.info("FIN SERVICIO GUARDAR CLIENTE: " + response.toString());

		return response;
	}
	
	@DeleteMapping("/eliminarCliente")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public GenericResponse eliminarCliente(@RequestParam Long id) {
		return clienteService.eliminarCliente(id);
	}
	

	@GetMapping("/listadoClientes")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public  ResponseList demo (@RequestParam(name = "page",defaultValue = "0") int page){
		return clienteService.listarClientes(page);
	}
	
	@GetMapping("/buscarPorTerm")
	public ResponseList buscarPorNombre(@RequestParam String term) {
		return clienteService.listarPorNombre(term);
	}
	

}
