package cl.ms.maquinarias.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import cl.ms.maquinarias.models.Region;
import cl.ms.maquinarias.repository.RegionRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseList;

@Service
public class RegionServiceImpl implements RegionService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private RegionRepository regionRepository;

	@Override
	public ResponseList listarRegiones() {
		ResponseList response = new ResponseList();
		GenericResponse res = new GenericResponse();
		
		try {
			List<Region> listado = regionRepository.findAll();
			response.setListado(listado);
			res.setCode(ResponseCodes.OK_CODE);
			res.setMessage(ResponseCodes.OK_MESSAGE);
			response.setResponse(res);
			
		} catch (DataIntegrityViolationException | ConstraintViolationException ex) {
			LOGGER.warn("ERROR ENTITY");
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

}
