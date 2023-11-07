package cl.ms.maquinarias.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
	
	Stock findByProductoId(Long producto);

}
