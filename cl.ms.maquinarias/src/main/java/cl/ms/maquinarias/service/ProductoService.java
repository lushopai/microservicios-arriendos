package cl.ms.maquinarias.service;

import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseList;

public interface ProductoService {
	
	GenericResponse añadirStock(Long productoId,int cantidad);
	GenericResponse validarStock(Long productoId, int cantidad);
	ResponseList listadoPorNombreProducto(String term);
	GenericResponse guardarProducto(Producto producto);
	GenericResponse actualizarProducto(Producto producto,Long idProducto);
	GenericResponse eliminarProducto(Long id);
	public boolean hasEnoughStock(Long productoId,int requestedQuantity);
	
}
