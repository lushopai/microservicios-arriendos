package cl.ms.maquinarias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
