package cl.ms.maquinarias.response;

import java.util.List;

import cl.ms.maquinarias.dto.StockDTO;
import cl.ms.maquinarias.models.Stock;
import lombok.Data;

@Data
public class ResponseStock {
	
	GenericResponse response;
	List<StockDTO> listado;

}
