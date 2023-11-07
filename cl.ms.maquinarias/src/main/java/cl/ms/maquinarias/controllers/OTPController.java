package cl.ms.maquinarias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ms.maquinarias.service.otp.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OTPController {

	@Autowired
	private OtpService otpService;

	@PostMapping("/generate")
	public ResponseEntity<String> generateOtp(@RequestParam String userId) {
		String otpToken = otpService.generateOTP(userId);
		return ResponseEntity.ok(otpToken);
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOtp( @RequestParam String otpToken) {

		if (otpService.verifyOtp(otpToken)) {
			return ResponseEntity.ok("OTP Verified");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP Token");
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<String> validateOtp(@RequestParam Long userId, @RequestParam String enteredOtp) {

		Boolean isvalid = otpService.validateOTP(userId, enteredOtp);

		if (isvalid) {
			return ResponseEntity.ok("OTP Validated");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP Token");
		}

	}

	@PostMapping("/send-email")
	public void sendOtp(@RequestParam Long userId) {

		otpService.sendOTPUsuario(userId);

	}

}
