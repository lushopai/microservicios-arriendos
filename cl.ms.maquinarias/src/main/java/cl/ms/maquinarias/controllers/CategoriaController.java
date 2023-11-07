package cl.ms.maquinarias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.models.Categoria;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
	
	
	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping("/buscarPorId")
	private ResponseData<?> listarPorId(@RequestParam Long id){
		return categoriaService.buscarPorId(id);
	}
	
	@PostMapping("/guardarCategoria")
	private GenericResponse guardarCategoria(@RequestBody Categoria categoria) {
		return categoriaService.guardarCategoria(categoria);
		
	}
	
	@PutMapping("/actualizarCategoria")
	private GenericResponse actualizarCategoria(@RequestParam Long id,@RequestBody Categoria categoria){
		return categoriaService.actualizarCategoria(categoria, id);
	}

}
