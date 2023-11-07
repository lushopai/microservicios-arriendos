package cl.ms.maquinarias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@PostMapping("/{productoId}/añadir-stock")
	public GenericResponse añadirStock(@PathVariable Long productoId, @RequestParam int cantidad) {
		return productoService.añadirStock(productoId, cantidad);
	}

	@GetMapping("/buscarPorNombre")
	public ResponseList buscador(@RequestParam String term) {
		return productoService.listadoPorNombreProducto(term);
	}

	@PostMapping("/guardarProducto")
	public GenericResponse guardarProducto(@RequestBody Producto producto) {
		return productoService.guardarProducto(producto);
	}

	@PutMapping("/actualizarProducto/{id}")
	public GenericResponse actualizarProducto(@RequestBody Producto producto, @PathVariable Long idProducto) {
		return productoService.actualizarProducto(producto, idProducto);
	}

	@DeleteMapping("/eliminarProducto")
	public GenericResponse eliminarProducto(@RequestParam Long idProducto) {
		return productoService.eliminarProducto(idProducto);
	}

}
