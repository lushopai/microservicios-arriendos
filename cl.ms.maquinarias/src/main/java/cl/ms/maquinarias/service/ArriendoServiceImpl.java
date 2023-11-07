package cl.ms.maquinarias.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cl.ms.maquinarias.dto.ArriendosAtrasadosDTO;
import cl.ms.maquinarias.dto.DetalleArriendoDTO;
import cl.ms.maquinarias.models.Arriendo;
import cl.ms.maquinarias.models.DetalleArriendo;
import cl.ms.maquinarias.models.Estado;
import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.RollBackAudit;
import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.repository.ArriendoRepository;
import cl.ms.maquinarias.repository.AuditRepository;
import cl.ms.maquinarias.repository.EstadoRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseArriendoAtrasados;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseData;

@Service
public class ArriendoServiceImpl implements ArriendoService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ArriendoRepository arriendoRepository;

	@Autowired
	private UsuarioService service;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private StockService stockService;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private AuditRepository auditRepository;

	@Override
	@Transactional
	public GenericResponse guardarArriendo(Arriendo arriendo) {
		GenericResponse response = new GenericResponse();

		// VALIDACION DE CAMPOS VACIONS PENDIENTE

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			ResponseData<Usuarios> usuario = service.buscarUsuarioPorUsername(authentication.getName());
			if (usuario == null) {
				response.setCode(ResponseCodes.ERROR_FIND);
				response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
				return response;
			}
			Usuarios user = usuario.getData();
			LOGGER.info("USUARIO ENCONTRADO : " + user);
			// Validar si hay existencias de stock antes de actualizarlo
			if (!validateStockAvalability(arriendo)) {
				response.setCode(ResponseCodes.ERRORSTOCK);
				response.setMessage(ResponseCodes.ERRORSTOCKEMSG);
				return response;
			}
			LOGGER.info("INICIO GUARDADO ARRIENDO :{}", arriendo);
			try {
				updateStockAndSaveArriendo(arriendo, user, response);
			} catch (Exception e) {
				LOGGER.warn("FALLO EL SUPER SERVICIO : " + e.toString());
			}

		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY ACA 1 :{}", ex.getMessage());
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
			return response;
		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA :{} ", e.toString());
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			return response;
		}
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseData<Arriendo> buscarArriendoPorId(Long id) {
		ResponseData<Arriendo> response = new ResponseData<>();
		GenericResponse res = new GenericResponse();
		Arriendo arriendo = null;

		Optional<Arriendo> encontrado = arriendoRepository.findById(id);
		if (encontrado.isEmpty()) {
			res.setCode(ResponseCodes.ERROR_FIND);
			res.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			response.setResponse(res);
			return response;
		}
		try {
			arriendo = encontrado.get();
			res.setCode(ResponseCodes.OK_CODE);
			res.setMessage(ResponseCodes.OK_MESSAGE);
			response.setData(arriendo);
			response.setResponse(res);
		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY : {}", ex.getMessage());
			res.setCode(ResponseCodes.ERROREXECSERVICE);
			res.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
			response.setResponse(res);
			return response;
		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA");
			res.setCode(ResponseCodes.ERROREXECSERVICE);
			res.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			response.setResponse(res);
			return response;
		}

		return response;
	}

	@Override
	public GenericResponse finalizarArriendo(Long arriendoId, List<Long> productosaFinalizar) {
		GenericResponse response = new GenericResponse();
		Arriendo arriendo = arriendoRepository.findById(arriendoId).orElse(null);
		if (arriendo == null) {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		}
		if ("FINALIZADO".equals(arriendo.getEstadoArriendo())) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage("EL ARRIENDO YA HA SIDO FINALIZADO");
			return response;
		}
		List<Long> productosNoEncontrados = new ArrayList<>();
		for (long productoId : productosaFinalizar) {
			boolean productoEncontrado = arriendo.getItems().stream()
					.anyMatch(detalle -> detalle.getProducto().getId().equals(productoId));
			if (!productoEncontrado) { // Valida si existen los productos
				productosNoEncontrados.add(productoId); // Se añaden a la lista los productos no encontrados
			}
		}
		if (!productosNoEncontrados.isEmpty()) {
			response.setCode("99");
			response.setMessage("Productos no encontrados");
			return response;
		}
		for (DetalleArriendo detalle : arriendo.getItems()) {
			if (productosaFinalizar.contains(detalle.getProducto().getId())) {
				detalle.setFinalizado(true);
			}
		}
		boolean todosProductosFinalizados = arriendo.getItems().stream().allMatch(DetalleArriendo::isFinalizado);

		if (todosProductosFinalizados) {
			arriendo.setEstadoArriendo("FINALIZADO");
		}
		try {
			arriendoRepository.save(arriendo);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);

		} catch (Exception e) {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));

		}

		return response;
	}

	@Override
	public ResponseArriendoAtrasados arriendosAtrasados() {

		List<Arriendo> arriendos = arriendoRepository.arriendosAtrasados();
		
		ResponseArriendoAtrasados response = new ResponseArriendoAtrasados();
		GenericResponse res = new GenericResponse();
		
		
		ArriendosAtrasadosDTO dto = new ArriendosAtrasadosDTO();
		LinkedList<DetalleArriendoDTO> detalleArriendoDTO = new LinkedList<>();
		for (Arriendo arriendo : arriendos) {
			dto.setIdArriendo(arriendo.getId());
			dto.setDescripcionArriendo(arriendo.getDescripcion());
			dto.setEstadoArriendo(arriendo.getEstadoArriendo());
			dto.setFechaArriendo(arriendo.getFechaArriendo());
			dto.setObservacion(arriendo.getObservacion());
			dto.setPatente(arriendo.getPatente());
			dto.setTotalArriendo(arriendo.getTotal());
			for (DetalleArriendo item : arriendo.getItems()) {
				
				DetalleArriendoDTO d = new DetalleArriendoDTO();
				d.setCantidadProductos(item.getCantidad());
				d.setDiasArriendo(item.getDias());
				d.setFechaEntrega(item.getFechaEntrega());
				d.setFechaPosibleEntrega(item.getFechaPosibleEntrega());
				d.setImporte(item.getImporte());
				d.setNombreProducto(item.getProducto().getNombreProducto());
				d.setNumeroSerie(item.getProducto().getNumeroSerie());
				detalleArriendoDTO.add(d);

				}
			dto.setDetalle(detalleArriendoDTO);
		}
		res.setCode(ResponseCodes.OK_CODE);
		res.setMessage(ResponseCodes.OK_MESSAGE);
		response.setResponse(res);
		response.setArriendo(dto);
		return response;
	}

	@Override
	public GenericResponse eliminarArriendoById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateStockAvalability(Arriendo arriendo) {
		for (DetalleArriendo item : arriendo.getItems()) {
			if (!productoService.hasEnoughStock(item.getProducto().getId(), item.getCantidad())) {
				return false;

			}
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateStockAndSaveArriendo(Arriendo request, Usuarios usuario, GenericResponse response) {
		try {
			request.getItems().forEach(item -> {
				Producto producto = item.getProducto();
				LOGGER.info("INICIO ACTUALIZAR STOCK");
				GenericResponse res = stockService.removerStock(item.getProducto().getId(), item.getCantidad());
				if (!res.getCode().equals("00")) {
					LOGGER.info("FALLO SERVICIO ACTUALIZAR STOCK : " + res);
				}
				LOGGER.info("FIN ACTUALIZAR STOCK : " + res);
				try {
					calcularImporteAndTotal(request);
				} catch (Exception e) {
					LOGGER.info("FALLO CALCULAR IMPORTE Y TOTAL :{}", e.toString());
					response.setCode(ResponseCodes.ERROREXECSERVICE);
					response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
				}

				item.setFinalizado(false);
				Estado estado = estadoRepository.findByNombre("ARRENDADO");
				if (estado != null) {
					producto.setEstado(estado);
				} else {
					LOGGER.info("ESTADO NO EXISTE EN LA BD " + estado);
				}

			});
			request.setUsuario(usuario);
			try {
				arriendoRepository.save(request);
			} catch (Exception e2) {
				LOGGER.info("FALLO GUARDADO ARRIENDO :{}", e2.toString());
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e2.toString()));
			}

			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);

		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY ACA 2 " + ex.toString());
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));

		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA " + e.toString());
			RollBackAudit audit = new RollBackAudit();
			for (DetalleArriendo item : request.getItems()) {
				GenericResponse r = productoService.añadirStock(item.getProducto().getId(), item.getCantidad());
				if (r.getCode().equals("00")) {
					audit.setData(request.toString());
					audit.setErrors(r.toString());
					auditRepository.save(audit);
				} else {
					audit.setData(request.toString());
					audit.setErrors(r.toString());
					LOGGER.error("FALLO REVERSION STOCK : " + r);
					auditRepository.save(audit);
				}

			}
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
		}

	}

	@Override
	public void calcularImporteAndTotal(Arriendo request) {
		LOGGER.info("REQUEST FOR CALCULAR IMPORTE : " + request);
		Double total = 0.0;
		for (DetalleArriendo item : request.getItems()) {
			Double importe = item.getProducto().getPrecioProducto() * item.getCantidad() * item.getDias();
			LOGGER.info("importe calculado : " + importe);
			item.setImporte(importe);
			total += importe;
		}
		request.setTotal(total);
	}

}
