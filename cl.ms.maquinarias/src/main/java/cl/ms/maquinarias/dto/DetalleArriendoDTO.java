package cl.ms.maquinarias.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetalleArriendoDTO {
	
	private Double importe;
	private String numeroSerie;
	private String nombreProducto;
	private LocalDateTime fechaEntrega;
	private LocalDateTime fechaPosibleEntrega;
	private Integer cantidadProductos;
	private Integer diasArriendo;
	

}
