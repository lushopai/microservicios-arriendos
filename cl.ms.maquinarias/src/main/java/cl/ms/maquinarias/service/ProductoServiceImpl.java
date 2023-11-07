package cl.ms.maquinarias.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.camel.tooling.model.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.ms.maquinarias.dto.FiltroProductosDTO;
import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.Stock;
import cl.ms.maquinarias.repository.ProductoRepository;
import cl.ms.maquinarias.repository.StockRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseList;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRepository pdao;

	@Autowired
	private StockService stockService;

	@Autowired
	private StockRepository sdao;

	@Override
	public GenericResponse añadirStock(Long productoId, int cantidad) {
		GenericResponse response = new GenericResponse();

		try {
			GenericResponse r = stockService.añadirStock(productoId, cantidad);
			log.info("response : " + r);
			if (r.getCode().equals("00")) {
				response.setCode(ResponseCodes.OK_CODE);
				response.setMessage(ResponseCodes.OK_MESSAGE);
			} else {
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(r.getMessage()));
			}

		} catch (Exception e) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.getMessage()));
		}

		return response;
	}

	@Override
	public GenericResponse validarStock(Long productoId, int cantidad) {
		GenericResponse response = new GenericResponse();
		Producto producto = pdao.findById(productoId).orElse(null);
		if (producto == null) {
			response.setCode("01");
			response.setMessage("Producto no existe");
			return response;
		}
		return null;
	}

	@Override
	public ResponseList listadoPorNombreProducto(String term) {
		GenericResponse response = new GenericResponse();
		ResponseList rp = new ResponseList();

		List<FiltroProductosDTO> productoFiltrado = pdao.findByNombreProductoContainingIgnoreCase(term)
				.stream()
				.filter(producto -> {
					Stock stock = Objects.requireNonNullElse(producto.getStock(), new Stock()); //Si el producto tiene stock null, lo inicializa como 0 automaticamente
					return stock.getCantidad() > 0;
				})
				.map(this::convertir)
				.collect(Collectors.toList());
		log.info("P "+ productoFiltrado.toString());

		if (productoFiltrado.isEmpty()) {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			rp.setResponse(response);
			return rp;
		}
	
		
		response.setCode(ResponseCodes.OK_CODE);
		response.setMessage(ResponseCodes.OK_MESSAGE);
		rp.setResponse(response);
		rp.setListado(productoFiltrado);
		return rp;
	}

	private FiltroProductosDTO convertir(Producto producto) {
		FiltroProductosDTO filtro = new FiltroProductosDTO();
		filtro.setId(producto.getId());
		filtro.setNombreProducto(producto.getNombreProducto());
		return filtro;
	}

	@Override
	public GenericResponse guardarProducto(Producto producto) {
		GenericResponse response = new GenericResponse();

		if (Strings.isNullOrEmpty(producto.getNombreProducto()) || Strings.isNullOrEmpty(producto.getNumeroSerie())
				|| Strings.isNullOrEmpty(producto.getObservacion()) || Strings.isNullOrEmpty(producto.getPatente())) {
			response.setCode(ResponseCodes.ERRORREQUIREDDATA);
			response.setMessage(ResponseCodes.ERRORREQUIREDDATAEMSG);
			return response;

		}
		Producto p = pdao.save(producto);

		Stock stock = new Stock();
		stock.setProducto(p);
		stock.setCantidad(0);

		GenericResponse rstock = stockService.guardarStock(stock);
		log.info("response save stock :{}", rstock);

		if (rstock.getCode().equals("00")) {
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			return response;
		} else {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(rstock.getMessage()));
			return response;

		}

	}

	@Override
	public GenericResponse actualizarProducto(Producto producto, Long idProducto) {
		GenericResponse response = new GenericResponse();

		Producto productoEncontrado = pdao.findById(idProducto).orElse(null);

		if (productoEncontrado == null) {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		}
		try {
			productoEncontrado.setNombreProducto(producto.getNombreProducto());
			productoEncontrado.setPatente(producto.getPatente());
			productoEncontrado.setObservacion(producto.getObservacion());
			productoEncontrado.setNumeroSerie(producto.getNumeroSerie());
			pdao.save(productoEncontrado);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			return response;

		} catch (Exception e) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.getMessage()));
			return response;
		}

	}

	@Override
	public GenericResponse eliminarProducto(Long id) {
		GenericResponse response = new GenericResponse();

		Producto p = pdao.findById(id).orElse(null);

		if (p == null) {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		}
		try {
			pdao.deleteById(id);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			return response;

		} catch (Exception e) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.getMessage()));
			return response;
		}

	}

	@Override
	public boolean hasEnoughStock(Long productoId, int requestedQuantity) {
		Producto producto = pdao.findById(productoId).orElse(null);
		if(producto != null && producto.getStock().getCantidad() >= requestedQuantity) {
			return true;
		}
		return false;
	}

}
