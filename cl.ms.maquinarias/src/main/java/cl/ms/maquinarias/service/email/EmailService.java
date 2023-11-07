package cl.ms.maquinarias.service.email;

public interface EmailService {
	
    public void sendEmailHTML(String to,String subject,String otp,String userId);
    public String generateOtpLink(String userId,String enteredOtp);


}
