package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "tbl_clientes")
@Data
public class Cliente implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private LocalDateTime createAt;
	private String email;
	private String rut;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JoinColumn(name = "region_id")
	private Region region;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cliente",cascade = CascadeType.ALL)
	private List<Arriendo> arriendos;
	
	
	@PrePersist
	private void init() {
		this.createAt = LocalDateTime.now();
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1636331619665496626L;

}
