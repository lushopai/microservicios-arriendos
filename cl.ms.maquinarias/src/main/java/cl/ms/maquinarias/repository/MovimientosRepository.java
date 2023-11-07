package cl.ms.maquinarias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Movimientos;

public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {

}
