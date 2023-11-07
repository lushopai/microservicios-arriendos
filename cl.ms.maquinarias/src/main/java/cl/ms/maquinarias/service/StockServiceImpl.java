package cl.ms.maquinarias.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.ms.maquinarias.dto.StockDTO;
import cl.ms.maquinarias.enums.TipoMovimiento;
import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.Stock;
import cl.ms.maquinarias.repository.ProductoRepository;
import cl.ms.maquinarias.repository.StockRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseStock;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

	@Autowired
	private StockRepository sdao;

	@Autowired
	private ProductoRepository pdao;

	@Autowired
	private MovimientoService mservice;

	@Override
	public GenericResponse añadirStock(Long producto, int cantidad) {
		GenericResponse response = new GenericResponse();

		log.info("producto recibido : {}", producto);
		Stock stockOptional = sdao.findByProductoId(producto);
		log.info("producto encontrado  : {}", stockOptional);

		if (stockOptional == null) {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		} else if (cantidad <= 0) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage("Cantidad ingresada debe ser mayor a 0");
			return response;
		} else {
			Stock stock = stockOptional;
			stock.addQuantity(cantidad);
			sdao.save(stock);

			GenericResponse responseM = mservice.createStockMovement(stock.getProducto(), cantidad, TipoMovimiento.IN);
			log.info("response Movimientos :{}", responseM);
			if (responseM.getCode().equals("00")) {
				response.setCode(ResponseCodes.OK_CODE);
				response.setMessage(ResponseCodes.OK_MESSAGE);
				return response;
			} else {
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(responseM.getMessage()));
				return response;
			}
		}

	}

	@Override
	public GenericResponse validarStock(Producto producto, int cantidad) {
		GenericResponse response = new GenericResponse();

		Stock stockOptional = sdao.findByProductoId(producto.getId());
		if (stockOptional == null) {
			response.setCode("01");
			response.setMessage("Producto no existe");
			return response;
		}

		return null;
	}

	@Override
	public GenericResponse removerStock(Long productoId, Integer cantidad) {
		GenericResponse response = new GenericResponse();

		if (cantidad == null || cantidad <= 0) {
			response.setCode(ResponseCodes.ERRORREQUIREDDATA);
			response.setMessage(ResponseCodes.ERRORREQUIREDDATAEMSG);
			return response;
		}
		Stock p = sdao.findByProductoId(productoId);
		if (p == null) {
			log.info("producto para remover stock no existe :{}", productoId);
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		}
		Stock stock = p;
		if (stock.getCantidad() < cantidad) {
			response.setCode(ResponseCodes.ERRORSTOCK);
			response.setMessage(ResponseCodes.ERRORSTOCKEMSG);
			return response;
		}
		stock.setCantidad(stock.getCantidad() - cantidad);
		sdao.save(stock);
		GenericResponse responseM = mservice.createStockMovement(p.getProducto(), cantidad, TipoMovimiento.OUT);
		if (responseM.getCode().equals("00")) {
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			return response;
		} else {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(responseM.getMessage()));
			return response;
		}

	}

	@Override
	public ResponseStock listado() {
		ResponseStock response = new ResponseStock();
		GenericResponse r = new GenericResponse();

		List<StockDTO> stockDTOList = new ArrayList<>();
		List<Stock> listado = sdao.findAll();
		for (Stock stock : listado) {
			StockDTO stockDTO = new StockDTO();
			stockDTO.setStockId(stock.getId());
			stockDTO.setCantidad(stock.getCantidad());
			stockDTO.setProductoId(stock.getProducto().getId());
			stockDTO.setNombreProducto(stock.getProducto().getNombreProducto());
			stockDTO.setPatente(stock.getProducto().getPatente());
			stockDTO.setNumeroSerie(stock.getProducto().getNumeroSerie());
			stockDTO.setObservacion(stock.getProducto().getObservacion());
			stockDTOList.add(stockDTO);
		}

		r.setCode(ResponseCodes.OK_CODE);
		r.setMessage(ResponseCodes.OK_MESSAGE);
		response.setResponse(r);
		response.setListado(stockDTOList);
		return response;
	}

	@Override
	public GenericResponse guardarStock(Stock stock) {

		GenericResponse response = new GenericResponse();

		try {
			sdao.save(stock);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			return response;
		} catch (Exception e) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			
			return response;
		}
	}

}
