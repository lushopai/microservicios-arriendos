package cl.ms.maquinarias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

	Estado findByNombre(String term);
}
