package cl.ms.maquinarias.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.repository.UsuarioRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseCodes;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.otp.OtpService;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private OtpService otpService;

	@Override
	public ResponseData<Usuarios> buscarUsuarioPorUsername(String username) {

		ResponseData<Usuarios> response = new ResponseData<>();
		GenericResponse gResponse = new GenericResponse();

		Usuarios usuario = repository.findByUsername(username);
		if (usuario == null) {
			gResponse.setCode(ResponseCodes.ERROR_FIND);
			gResponse.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			response.setResponse(gResponse);
			return response;
		}

		gResponse.setCode(ResponseCodes.OK_CODE);
		gResponse.setMessage(ResponseCodes.OK_MESSAGE);
		response.setData(usuario);
		response.setResponse(gResponse);
		return response;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuarios usuario = repository.findByUsername(username);

		if (usuario == null) {
			LOGGER.error("Error en el login: no existe el usuario '" + username + "'  en el sistema");
			throw new UsernameNotFoundException(
					"Error  en el login: no existe el usuario '" + username + "'  en el sistema");
		}

		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> LOGGER.info("Role: " + authority.getAuthority())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(),
				usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	public ResponseData<?> buscarUsuarioPorId(Long id) {
		ResponseData<Usuarios> response = new ResponseData<>();
		GenericResponse gResponse = new GenericResponse();

		Usuarios usuario = repository.findById(id).orElse(null);
		if (usuario == null) {
			gResponse.setCode(ResponseCodes.ERROR_FIND);
			gResponse.setMessage(ResponseCodes.ERROR_FIND_MESSAGE);
			response.setResponse(gResponse);
			return response;
		}

		gResponse.setCode(ResponseCodes.OK_CODE);
		gResponse.setMessage(ResponseCodes.OK_MESSAGE);
		response.setData(usuario);
		response.setResponse(gResponse);
		return response;
	}

	@Override
	public GenericResponse guardaarUsuario(Usuarios usuarios) {
		GenericResponse response = new GenericResponse();

		Usuarios newUser = null;

		if (repository.findByUsername(usuarios.getUsername()) != null) {
			LOGGER.info("USERNAME YA EXISTE");
			response.setCode("99");
			response.setMessage("Username ya existe");
			return response;

		}
		List<Usuarios> usuariosConMismoEmail = repository.findByEmail(usuarios.getEmail());
		if (!usuariosConMismoEmail.isEmpty()) {
			response.setCode("99");
			response.setMessage("Email ya existe");
			return response;
		}

		try {
			usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
			usuarios.setEnabled(false);
			newUser = repository.save(usuarios);
			
			try {
				LOGGER.info("INICIO SERVICIO ENVIO OTP");
				otpService.sendOTPUsuario(newUser.getId());
				LOGGER.info("FIN SERVICIO ENVIO OTP");
			} catch (Exception e) {
				LOGGER.warn("ERROR AL ENVIAR OTP");
			}
			
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
	public ResponseList listarUsuarios(int page) {
		GenericResponse response = new GenericResponse();
		ResponseList res = new ResponseList();
		try {
			Pageable pageable = PageRequest.of(page,5);
			Page<Usuarios> usuariosPage = repository.findAll(pageable);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			res.setResponse(response);
			res.setTotalRecords(usuariosPage.getTotalElements());
			res.setTotalPages(usuariosPage.getTotalPages());
			res.setListado(usuariosPage.getContent());
			

			
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
	public ResponseList buscarPorTerm(String term) {
		GenericResponse response = new GenericResponse();
		ResponseList res = new ResponseList();
		try {
			List<Usuarios> lista = repository.findByUsernameContainingIgnoreCase(term);
			response.setCode(ResponseCodes.OK_CODE);
			response.setMessage(ResponseCodes.OK_MESSAGE);
			res.setResponse(response);
			res.setListado(lista);
			
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
	public GenericResponse eliminarUsuario(Long id) {
		GenericResponse response = new GenericResponse();
		try {
			repository.deleteById(id);
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

}
