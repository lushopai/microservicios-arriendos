package cl.ms.arriendos.models;

import cl.ms.arriendos.entity.Arriendo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class Cliente {
	

	private Long id;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private LocalDateTime createAt;
	private String email;
	private String rut;
	

	//private Region region;
	
	private List<Arriendo> arriendos;
	
	
	@PrePersist
	private void init() {

		this.createAt = LocalDateTime.now();
	}
	



}
