package cl.ms.maquinarias.response;

import lombok.Data;

@Data
public class ResponseCodes {

	public static final String OK_CODE = "00";
	public static final String OK_MESSAGE = "CONTINUE";

	public static final String ERROR_FIND = "98";
	public static final String ERROR_FIND_MESSAGE = "DATA EMPTY";

	public static final String ERROREXECSERVICE = "9999";
	public static final String ERROREXECSERVICEMSG = "ERROR EXECUTE SERVICE";

	public static final String ERRORSTOCK = "10";
	public static final String ERRORSTOCKEMSG = "PRODUCTO SIN TOCK";

	public static final String ERRORREQUIREDDATA = "11";
	public static final String ERRORREQUIREDDATAEMSG = "DATA-INCOMPLETE";
	public static final String ERRORRUTFORMAT = "12";
	public static final String ERRORRUTFORMATMSG = "RUT FORMAT ERROR";
	public static final String ERROREMAILFORMAT = "13";
	public static final String ERROREMAILFORMATMSG = "EMAIL FORMAT ERROR";

}
