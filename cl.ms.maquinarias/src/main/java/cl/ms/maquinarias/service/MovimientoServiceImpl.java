package cl.ms.maquinarias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.ms.maquinarias.enums.TipoMovimiento;
import cl.ms.maquinarias.models.Movimientos;
import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.Stock;
import cl.ms.maquinarias.repository.MovimientosRepository;
import cl.ms.maquinarias.repository.ProductoRepository;
import cl.ms.maquinarias.repository.StockRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MovimientoServiceImpl implements MovimientoService {

	@Autowired
	private MovimientosRepository mdao;

	@Autowired
	private StockRepository sdao;

	@Autowired
	private ProductoRepository pdao;

	@Override
	public GenericResponse createStockMovement(Producto producto, Integer cantidad, TipoMovimiento type) {
		GenericResponse response = new GenericResponse();

		Stock p = sdao.findByProductoId(producto.getId());

		if (p == null) {
			log.info("producto para crear movimiento no existe :{}", producto);
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		} else if (cantidad <= 0) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage("Cantidad ingresada debe ser mayor a 0");
			return response;
		} else if (type == null) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage("Tipo de movimiento no válido");
			return response;
		}
		Movimientos m = new Movimientos();
		m.setProducto(producto);
		m.setCantidad(cantidad);
		m.setTipoMovimiento(type);
		mdao.save(m);

		response.setCode(ResponseCodes.OK_CODE);
		response.setMessage(ResponseCodes.OK_MESSAGE);
		return response;
	}

}
