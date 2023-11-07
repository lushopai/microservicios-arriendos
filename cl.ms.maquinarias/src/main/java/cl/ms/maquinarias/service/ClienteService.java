package cl.ms.maquinarias.service;

import java.util.List;

import cl.ms.maquinarias.dto.ClienteDTO;
import cl.ms.maquinarias.models.Cliente;
import cl.ms.maquinarias.response.GenericResponse;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.response.ResponseList;

public interface ClienteService {

	public GenericResponse guardarCliente(ClienteDTO cliente);
	public GenericResponse actualizarCliente(ClienteDTO request,Long id);
	public GenericResponse eliminarCliente(Long id);
	public ResponseData<?> buscarPorId(Long id);
	public ResponseList listarClientes(int page);
	public List<Cliente> listado();
	public ResponseList listarPorNombre(String term);
	

}
