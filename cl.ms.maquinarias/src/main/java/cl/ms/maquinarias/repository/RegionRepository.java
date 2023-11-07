package cl.ms.maquinarias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
	
	Region findByNombreRegion(String term);

}
