package cl.ms.maquinarias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.Producto;
import cl.ms.maquinarias.models.Usuarios;

public interface UsuarioRepository extends JpaRepository<Usuarios,Long> {
    
    public Usuarios findByUsername(String username);
    
    List<Usuarios> findByEmail(String email);
    
    List<Usuarios> findByUsernameContainingIgnoreCase(String term);
    
}
