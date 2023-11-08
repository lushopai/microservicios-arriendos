package cl.ms.arriendos.entity;

import cl.ms.arriendos.models.Cliente;
import cl.ms.arriendos.models.Usuarios;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "tbl_arriendos")
@Data
public class Arriendo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5812318363002754324L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime fechaArriendo;
	private String observacion;
	private String descripcion;
	private String patente;
	private String estadoArriendo;
	@CreatedDate
	private Date fechaCreacion;
	@LastModifiedDate
	private Date dateUpdate;
	private Double total;

	@Transient
	private Cliente cliente;
	@Transient
	private Usuarios usuario;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "arriendo_id")
	private List<DetalleArriendo> items;

	@PrePersist
	public void init() {
		fechaArriendo = LocalDateTime.now();
		
		if(estadoArriendo == null) {
			estadoArriendo = "PENDIENTE";
		}
	}
	
	

}
