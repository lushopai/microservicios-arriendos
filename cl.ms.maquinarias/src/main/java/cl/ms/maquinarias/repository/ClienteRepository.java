package cl.ms.maquinarias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	List<Cliente> findByNombreContainingIgnoreCase(String term);
	boolean existsByRut(String rut);
}
