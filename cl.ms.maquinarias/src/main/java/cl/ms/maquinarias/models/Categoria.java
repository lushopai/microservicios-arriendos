package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categoria implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	
	@OneToMany(mappedBy = "categoria",cascade = CascadeType.ALL)
	private List<Producto> productos;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6000155438483661951L;

}
