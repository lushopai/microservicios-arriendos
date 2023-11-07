package cl.ms.maquinarias.utils;

import java.util.regex.Pattern;

public class Utils {

	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

	public static boolean isValid(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		return pattern.matcher(email).matches();
	}
	
	public static boolean isValidRut(String rut) {
	    String rutPattern = "^(\\d{1,3}(\\.\\d{3})*-\\d|\\d{7,8}-[Kk])$";
	    return rut.matches(rutPattern);
	}

}
