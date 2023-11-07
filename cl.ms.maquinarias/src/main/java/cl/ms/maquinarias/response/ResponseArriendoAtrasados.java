package cl.ms.maquinarias.response;

import java.util.LinkedList;

import cl.ms.maquinarias.dto.ArriendosAtrasadosDTO;
import cl.ms.maquinarias.dto.DetalleArriendoDTO;
import lombok.Data;

@Data
public class ResponseArriendoAtrasados {
	
	GenericResponse response;
	ArriendosAtrasadosDTO arriendo;


}
