package cl.ms.maquinarias.service;

import java.util.List;

import cl.ms.maquinarias.dto.ArriendosAtrasadosDTO;
import cl.ms.maquinarias.models.Arriendo;
import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseArriendoAtrasados;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;

public interface ArriendoService {
	
	public GenericResponse guardarArriendo(Arriendo arriendo);
	public ResponseData<Arriendo> buscarArriendoPorId(Long id);
	public GenericResponse finalizarArriendo(Long arriendoId,List<Long> productosaFinalizar);
	public ResponseArriendoAtrasados arriendosAtrasados();
	public GenericResponse eliminarArriendoById(Long id);
	public boolean validateStockAvalability(Arriendo arriendo);
	public void updateStockAndSaveArriendo(Arriendo request,Usuarios usuario,GenericResponse response);
	public void calcularImporteAndTotal(Arriendo request);
}
