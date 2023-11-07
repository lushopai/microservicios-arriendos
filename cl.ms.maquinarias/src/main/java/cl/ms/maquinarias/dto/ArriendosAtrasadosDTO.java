package cl.ms.maquinarias.dto;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class ArriendosAtrasadosDTO {

	private Long idArriendo;
	private String descripcionArriendo;
	private String estadoArriendo;
	private LocalDateTime fechaArriendo;
	private String observacion;
	private String patente;
	private Double totalArriendo;
	private LinkedList<DetalleArriendoDTO> detalle;

}
