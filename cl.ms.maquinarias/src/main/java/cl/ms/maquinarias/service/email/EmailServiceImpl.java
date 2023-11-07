package cl.ms.maquinarias.service.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final   JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
	        this.javaMailSender = javaMailSender;
	 }

	@Override
	public void sendEmailHTML(String to, String subject, String otp, String userId) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);

			String otpButtonHtml = "<a href=\"" + generateOtpLink(userId, otp)
					+ "\" style=\"display: inline-block; background-color: #00466a; color: white; padding: 10px 20px; border-radius: 4px; text-decoration: none;\">Validar OTP</a>";

			String htmlContent = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">\n"
					+ "  <div style=\"margin:50px auto;width:70%;padding:20px 0\">\n"
					+ "    <div style=\"border-bottom:1px solid #eee\">\n"
					+ "      <a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">Logo</a>\n"
					+ "    </div>\n" + "    <p style=\"font-size:1.1em\">Hola,</p>\n"
					+ "    <p>Gracias por elegir registrarse. Utiliza el siguiente botón para completar tu registro. El OTP es válido por 10 minutos:</p>\n"
					+ "    <div style=\"text-align: center; margin-top: 20px;\">" + otpButtonHtml + "</div>\n"
					+ "    <p style=\"font-size:0.9em;\">Saludos,<br />Potato Tech</p>\n"
					+ "    <hr style=\"border:none;border-top:1px solid #eee\" />\n"
					+ "    <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">\n"
					+ "      <p>Potato Tech</p>\n" + "      <p>Calle falsa 123</p>\n" + "      <p>Talca</p>\n"
					+ "    </div>\n" + "  </div>\n" + "</div>";

			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
		}

	}

	@Override
	public String generateOtpLink(String userId, String enteredOtp) {
		String url = "http://localhost:8080/api/otp/validate/?userId=" + userId + "&enteredOtp=" + enteredOtp;
		LOGGER.info("URL OTP : " + url);
		return url;
	}

}
