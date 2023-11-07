package cl.ms.maquinarias.service.otp;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cl.ms.maquinarias.config.Constants;
import cl.ms.maquinarias.models.Otp;
import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.repository.OtpRepository;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;
import cl.ms.maquinarias.service.UsuarioService;
import cl.ms.maquinarias.service.email.EmailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class OtpServiceImpl implements OtpService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Constants constants;
	
	@Autowired
	private UsuarioService service;

	
	@Autowired
	private OtpRepository otpRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public String generateOTP(String subject) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + Integer.parseInt(constants.getJwt().get("expirationTimeInMillis")));
		
		  return Jwts.builder()
	                .setSubject(subject)
	                .setIssuedAt(now)
	                .setExpiration(expiration)
	                .signWith(SignatureAlgorithm.HS256, constants.getJwt().get("secretKey"))
	                .compact();
	}

	@Override
	public Boolean verifyOtp(String token) {
		try {
            Jwts.parser().setSigningKey(constants.getJwt().get("secretKey")).parseClaimsJws(token);
            return true;
		} catch (Exception e) {
			LOGGER.warn("Error al validar OTP :{}",e.toString());
		}
		return false;
	}

	@Override
	@Async
	public void sendOTPUsuario(Long userId) {
		String otp = null;
		ResponseData<?> usuarioBuscado = service.buscarUsuarioPorId(userId);
		if(!usuarioBuscado.getResponse().getCode().equals("00")) {
			LOGGER.info("FALLO SERVICIO BUSCAR POR ID");
		}
		Usuarios usuario = (Usuarios) usuarioBuscado.getData();
		otp = generateOTP(String.valueOf(usuario.getId()));
		
		LOGGER.info("RESULT GENERATE OTP :{}",otp);
		try {
			LOGGER.info("INICIO SERVICIO EMAIL");
			emailService.sendEmailHTML(usuario.getEmail(), "OTP", otp, String.valueOf(usuario.getId()));
			LOGGER.info("FIN SERVICIO EMAIL");
		} catch (Exception e) {
			LOGGER.info("NO SE PUDO ENVIAR EMAIL :{}", e.toString());
		}
		Otp otpSave = new Otp();
		otpSave.setOtp(otp);
		otpSave.setUsuarios(usuario);
		otpRepository.save(otpSave);
		LOGGER.info("OTP GUARDADA ENLA BASE DE DATOS PARA EL USUARIO :{}", usuario.getUsername());
		
	}

	@Override
	public Boolean validateOTP(Long userId, String enteredOTP) {
		Otp lastOtpUser = otpRepository.findLastOtpForUser(userId);
		if(lastOtpUser == null) {
			LOGGER.info("NO EXISTE EL USUARIO");
			return false;
		}
		boolean isvalidOTP = lastOtpUser.getOtp().equals(enteredOTP);
		if(isvalidOTP) {
			LOGGER.info("OTP OK");
			Usuarios usuario = lastOtpUser.getUsuarios();
			usuario.setEnabled(true);
			GenericResponse response = usuarioService.guardaarUsuario(usuario);
			LOGGER.info("RESPONSE SAVE USER :{}", response.toString());
			
			
		}else {
			LOGGER.info("OTP INCORRECTO",enteredOTP);
		}
		return isvalidOTP;
	}


}
