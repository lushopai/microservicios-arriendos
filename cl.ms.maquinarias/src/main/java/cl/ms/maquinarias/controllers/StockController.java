package cl.ms.maquinarias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseStock;
import cl.ms.maquinarias.service.MovimientoService;
import cl.ms.maquinarias.service.StockService;

@RestController
@RequestMapping("/api/stock")
public class StockController {

	@Autowired
	private StockService stockService;


	@PostMapping("/remover")
	public GenericResponse removerStock(@RequestParam Long productoId,
			@RequestParam Integer cantidad) {
		return stockService.removerStock(productoId, cantidad);
	}
	
	@GetMapping("/listado")
	public ResponseStock listado() {
		return stockService.listado();
	}
}
