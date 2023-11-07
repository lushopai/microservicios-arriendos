package cl.ms.maquinarias.service.otp;

import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;

public interface OtpService {
	
	public String generateOTP(String subject);
	public Boolean verifyOtp(String token);
	public void sendOTPUsuario(Long userId);
	public Boolean validateOTP(Long userId,String enteredOTP);

	
}
