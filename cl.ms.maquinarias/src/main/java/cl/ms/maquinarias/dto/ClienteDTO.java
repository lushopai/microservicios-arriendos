package cl.ms.maquinarias.dto;

import lombok.Data;

@Data
public class ClienteDTO {
	
	private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String rut;
    private String nombreRegion;

}
