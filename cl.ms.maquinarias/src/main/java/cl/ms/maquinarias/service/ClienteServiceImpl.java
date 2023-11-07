package cl.ms.maquinarias.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.apache.camel.tooling.model.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ms.maquinarias.dto.ClienteDTO;
import cl.ms.maquinarias.models.Cliente;
import cl.ms.maquinarias.models.Region;
import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.repository.ClienteRepository;
import cl.ms.maquinarias.repository.RegionRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.utils.Utils;

@Service
public class ClienteServiceImpl implements ClienteService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Override
	@Transactional
	public GenericResponse guardarCliente(ClienteDTO cliente) {
		GenericResponse response = new GenericResponse();

		if (Strings.isNullOrEmpty(cliente.getNombre()) || Strings.isNullOrEmpty(cliente.getApellidoPaterno())
				|| Strings.isNullOrEmpty(cliente.getApellidoPaterno()) || Strings.isNullOrEmpty(cliente.getEmail())
				|| Strings.isNullOrEmpty(cliente.getRut()) || Strings.isNullOrEmpty(cliente.getNombreRegion())) {
			StringBuilder camposFaltantes = new StringBuilder();
			camposFaltantes.append("datos evaluados : ").append("Nombre : [").append(cliente.getNombre()).append("],")
					.append("Apellido Paterno : [").append(cliente.getApellidoPaterno()).append("],")
					.append("Apellido Materno : [").append(cliente.getApellidoMaterno()).append("],").append("Rut : [")
					.append(cliente.getRut()).append("],").append("Email : [").append(cliente.getEmail()).append("],");

			LOGGER.info("VALORES EVALUADOS : {} , rut :{}", camposFaltantes, cliente.getRut());
			;

			response.setCode(ResponseCodes.ERRORREQUIREDDATA);
			response.setMessage(ResponseCodes.ERRORREQUIREDDATAEMSG);
			return response;
		} else if (!Utils.isValid(cliente.getEmail())) {
			response.setCode(ResponseCodes.ERROREMAILFORMAT);
			response.setMessage(ResponseCodes.ERROREMAILFORMATMSG);
			return response;
		} else if (!Utils.isValidRut(cliente.getRut())) {
			response.setCode(ResponseCodes.ERRORRUTFORMAT);
			response.setMessage(ResponseCodes.ERRORRUTFORMATMSG);
			return response;
		}

		// Buscar la region por nombre
		Region region = regionRepository.findByNombreRegion(cliente.getNombreRegion().trim());
		if (region != null) {
			Cliente clientenew = new Cliente();
			clientenew.setNombre(cliente.getNombre());
			clientenew.setApellidoPaterno(cliente.getApellidoPaterno());
			clientenew.setApellidoMaterno(cliente.getApellidoMaterno());
			clientenew.setEmail(cliente.getEmail());
			clientenew.setRut(cliente.getRut());
			clientenew.setRegion(region);

			try {
				clienteRepository.save(clientenew);
				response.setCode(ResponseCodes.OK_CODE);
				response.setMessage(ResponseCodes.OK_MESSAGE);
			} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
				LOGGER.warn("ERROR ENTITY");
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
				return response;
			} catch (Exception e) {
				LOGGER.warn("EXCEPCION NO CONTROLADA");
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
				return response;
			}

		} else {
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage("No se encontró la región con el nombre proporcionado.");
		}

		return response;
	}

	@Override
	@Transactional
	public GenericResponse actualizarCliente(ClienteDTO request, Long id) {
		GenericResponse response = new GenericResponse();

		LOGGER.info("SERVICIO ACTUALIZAR CLIENTE REQUEST :{}", request);
		if (Strings.isNullOrEmpty(request.getNombre()) || Strings.isNullOrEmpty(request.getApellidoPaterno())
				|| Strings.isNullOrEmpty(request.getApellidoMaterno()) || Strings.isNullOrEmpty(request.getEmail())
				|| Strings.isNullOrEmpty(request.getRut()) || Strings.isNullOrEmpty(request.getNombreRegion())) {
			StringBuilder camposFaltantes = new StringBuilder();
			camposFaltantes.append("datos evaluados : ").append("Nombre : [").append(request.getNombre()).append("],")
					.append("Apellido Paterno : [").append(request.getApellidoPaterno()).append("],")
					.append("Apellido Materno : [").append(request.getApellidoMaterno()).append("],").append("Rut : [")
					.append(request.getRut()).append("],").append("Email : [").append(request.getEmail()).append("],");

			LOGGER.info("VALORES EVALUADOS : {} , rut :{}", camposFaltantes, request.getRut());

			response.setCode(ResponseCodes.ERRORREQUIREDDATA);
			response.setMessage(ResponseCodes.ERRORREQUIREDDATAEMSG);
			return response;
		}

		Optional<Cliente> clienteExistente = clienteRepository.findById(id);

		if (clienteExistente.isPresent()) {

			Cliente cliente = clienteExistente.get();
			cliente.setNombre(request.getNombre());
			cliente.setApellidoPaterno(request.getApellidoPaterno());
			cliente.setApellidoMaterno(request.getApellidoMaterno());
			cliente.setEmail(request.getEmail());

			Region region = regionRepository.findByNombreRegion(request.getNombreRegion());
			if (region != null) {
				cliente.setRegion(region);
			} else {
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage("No se encontró la región con el nombre proporcionado.");
				return response;
			}
			try {

				clienteRepository.save(cliente);

				response.setCode(ResponseCodes.OK_CODE);
				response.setMessage(ResponseCodes.OK_MESSAGE);

			} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
				LOGGER.warn("ERROR ENTITY");
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
				return response;
			} catch (Exception e) {
				LOGGER.warn("EXCEPCION NO CONTROLADA");
				response.setCode(ResponseCodes.ERROREXECSERVICE);
				response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
				return response;
			}

		} else {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		}

		return response;
	}

	@Override
	@Transactional
	public GenericResponse eliminarCliente(Long id) {
		GenericResponse response = new GenericResponse();
		try {
			clienteRepository.deleteById(id);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);

		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
			return response;
		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			return response;
		}
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseData<?> buscarPorId(Long id) {
		ResponseData<Cliente> response = new ResponseData<>();
		GenericResponse res = new GenericResponse();
		Optional<Cliente> buscar = null;

		try {
			buscar = clienteRepository.findById(id);
		} catch (Exception e) {
			res.setCode(ResponseCodes.ERROREXECSERVICE);
			res.setMessage(ResponseCodes.ERROREXECSERVICEMSG);
			response.setResponse(res);
			return response;
		}

		Cliente encontrado = null;
		if (buscar.isEmpty()) {
			res.setCode(ResponseCodes.ERROR_FIND);
			res.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			response.setResponse(res);
			return response;
		}
		encontrado = buscar.get();

		res.setCode(ResponseCodes.OK_CODE);
		res.setMessage(ResponseCodes.OK_MESSAGE);
		response.setData(encontrado);
		response.setResponse(res);
		return response;
	}

	@Override
	public ResponseList listarClientes(int page) {
		GenericResponse response = new GenericResponse();
		ResponseList res = new ResponseList();

		try {
			Pageable pageable = PageRequest.of(page, 5);
			Page<Cliente> clientePage = clienteRepository.findAll(pageable);
			List<ClienteDTO> clienteRegionDTOs = new ArrayList<>();

			for (Cliente cliente : clientePage) {
				ClienteDTO clienteRegionDTO = new ClienteDTO();
				clienteRegionDTO.setId(cliente.getId());
				clienteRegionDTO.setNombre(cliente.getNombre());
				clienteRegionDTO.setApellidoPaterno(cliente.getApellidoPaterno());
				clienteRegionDTO.setApellidoMaterno(cliente.getApellidoMaterno());
				clienteRegionDTO.setEmail(cliente.getEmail());
				clienteRegionDTO.setRut(cliente.getRut());
				clienteRegionDTO.setNombreRegion(cliente.getRegion().getNombreRegion());

				clienteRegionDTOs.add(clienteRegionDTO);
			}

			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			res.setTotalPages(clientePage.getTotalPages());
			res.setTotalRecords(clientePage.getTotalElements());
			res.setResponse(response);
			res.setListado(clienteRegionDTOs);

		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
			res.setResponse(response);
			return res;
		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			res.setResponse(response);
			return res;
		}

		return res;
	}

	@Override
	public List<Cliente> listado() {
		return clienteRepository.findAll();
	}

	@Override
	public ResponseList listarPorNombre(String term) {
		GenericResponse response = new GenericResponse();
		ResponseList res = new ResponseList();

		try {
			List<Cliente> listado = clienteRepository.findByNombreContainingIgnoreCase(term);
			List<ClienteDTO> list = new ArrayList<>();

			for (Cliente cliente : listado) {
				ClienteDTO clienteRegionDTO = new ClienteDTO();
				clienteRegionDTO.setId(cliente.getId());
				clienteRegionDTO.setNombre(cliente.getNombre());
				clienteRegionDTO.setApellidoPaterno(cliente.getApellidoPaterno());
				clienteRegionDTO.setApellidoMaterno(cliente.getApellidoMaterno());
				clienteRegionDTO.setEmail(cliente.getEmail());
				clienteRegionDTO.setRut(cliente.getRut());
				clienteRegionDTO.setNombreRegion(cliente.getRegion().getNombreRegion());

				list.add(clienteRegionDTO);
			}

			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);

			res.setListado(list);
			res.setResponse(response);

		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
			res.setResponse(response);
			return res;
		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			res.setResponse(response);
			return res;
		}

		return res;
	}

}
