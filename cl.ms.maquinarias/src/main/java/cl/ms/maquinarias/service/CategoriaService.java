package cl.ms.maquinarias.service;

import cl.ms.maquinarias.models.Categoria;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseData;

public interface CategoriaService {
	
	GenericResponse guardarCategoria(Categoria categoria);
	ResponseData<?> buscarPorId(Long id);
	GenericResponse actualizarCategoria(Categoria categoria,Long id );
	GenericResponse eliminarCategoriaPorId(Long id);

}
