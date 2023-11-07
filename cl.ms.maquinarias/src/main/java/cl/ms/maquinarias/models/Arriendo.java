package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id",referencedColumnName = "id")
	private Cliente cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
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
