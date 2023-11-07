package cl.ms.maquinarias.service;

import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.Stock;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseStock;

public interface StockService {
	
	GenericResponse añadirStock(Long producto, int cantidad);
	GenericResponse validarStock(Producto producto,int cantidad);
	GenericResponse removerStock(Long productoId,Integer cantidad);
	ResponseStock listado();
	GenericResponse guardarStock(Stock stock);

}
