package cl.ms.maquinarias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	List<Producto> findByNombreProductoContainingIgnoreCase(String term);

}
