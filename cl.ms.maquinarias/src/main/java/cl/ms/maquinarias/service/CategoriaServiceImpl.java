package cl.ms.maquinarias.service;

import javax.validation.ConstraintViolationException;

import org.apache.camel.tooling.model.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.ms.maquinarias.models.Categoria;
import cl.ms.maquinarias.repository.CategoriaRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseData;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	@Transactional
	public GenericResponse guardarCategoria(Categoria categoria) {
		GenericResponse response = new GenericResponse();
		LOGGER.info("SERVICIO GUARDAR CATEGORIA REQUEST :{}", categoria);

		try {
			categoriaRepository.save(categoria);
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
		ResponseData<Categoria> responseData = new ResponseData<>();
		GenericResponse response = new GenericResponse();
		LOGGER.info("SERVICIO BUSCAR CATEGORIA REQUEST :{}", id);
		try {
			Categoria categoria = categoriaRepository.findById(id).orElse(null);
			if (categoria == null) {
				response.setCode(ResponseCodes.ERROR_FIND);
				response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
				responseData.setResponse(response);
				return responseData;
			}
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			responseData.setData(categoria);
			responseData.setResponse(response);
		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(ex.toString()));
			return responseData;
		} catch (Exception e) {
			LOGGER.warn("EXCEPCION NO CONTROLADA");
			response.setCode(ResponseCodes.ERROREXECSERVICE);
			response.setMessage(ResponseCodes.ERROREXECSERVICEMSG.concat(" - ").concat(e.toString()));
			return responseData;
		}
		return responseData;
	}

	@Override
	@Transactional
	public GenericResponse actualizarCategoria(Categoria categoria, Long id) {
		GenericResponse response = new GenericResponse();
		LOGGER.info("SERVICIO ACTUALIZAR CATEGORIA REQUEST :{}", categoria);
		Categoria categoriaEncontrada = categoriaRepository.findById(id).orElse(null);
		if (categoriaEncontrada == null) {
			response.setCode(ResponseCodes.ERROR_FIND);
			response.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			return response;
		}
		try {
			if (Strings.isNullOrEmpty(categoria.getNombre())) {
				response.setCode(ResponseCodes.ERRORREQUIREDDATA);
				response.setMessage(ResponseCodes.ERRORREQUIREDDATAEMSG);
				return response;
			}
			categoriaEncontrada.setNombre(categoria.getNombre());
			categoriaRepository.save(categoriaEncontrada);

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
	@Transactional
	public GenericResponse eliminarCategoriaPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
