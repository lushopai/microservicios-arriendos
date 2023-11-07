package cl.ms.maquinarias.service;

import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;

public interface UsuarioService {

    ResponseData<Usuarios> buscarUsuarioPorUsername(String username);
    ResponseData<?> buscarUsuarioPorId(Long id);
    GenericResponse guardaarUsuario(Usuarios usuarios);
    ResponseList listarUsuarios(int page);
    
    ResponseList buscarPorTerm(String term);
    GenericResponse eliminarUsuario(Long id);


    
}
