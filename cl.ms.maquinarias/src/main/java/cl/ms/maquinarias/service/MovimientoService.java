package cl.ms.maquinarias.service;

import cl.ms.maquinarias.enums.TipoMovimiento;
import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.response.GenericResponse;

public interface MovimientoService {
	
	GenericResponse createStockMovement(Producto producto,Integer cantidad,TipoMovimiento type);

}
