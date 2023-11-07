package cl.ms.maquinarias.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDTO {
	
	private Long stockId;
	private int cantidad;
	private Long productoId;
	private String nombreProducto;
	private String numeroSerie;
	private String patente;
	private String observacion;

}
