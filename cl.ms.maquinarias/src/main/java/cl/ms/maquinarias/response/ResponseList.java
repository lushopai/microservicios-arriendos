package cl.ms.maquinarias.response;

import java.util.List;

import cl.ms.maquinarias.dto.FiltroProductosDTO;
import cl.ms.maquinarias.models.Producto;
import lombok.Data;

@Data
public class ResponseList {
	
	GenericResponse response;
	List<?> listado;
	long  totalRecords;
	long totalPages;

}
