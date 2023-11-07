package cl.ms.maquinarias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.ms.maquinarias.models.Arriendo;

public interface ArriendoRepository extends JpaRepository<Arriendo, Long> {

	
	@Query("SELECT DISTINCT a FROM Arriendo a " +
            "JOIN FETCH a.items item " +
            "JOIN FETCH item.producto " +
            "WHERE a.estadoArriendo = 'PENDIENTE' " +
            "AND item.finalizado = false " +
            "AND item.fechaEntrega < current_date")
    List<Arriendo> arriendosAtrasados();
	
}
