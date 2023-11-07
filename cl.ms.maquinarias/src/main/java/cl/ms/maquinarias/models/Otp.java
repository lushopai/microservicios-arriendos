package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "otp")
@Data
public class Otp implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String otp;
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime fechaCreacion;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "u_id")
	private Usuarios usuarios;
	
	
	@PrePersist
	public void init() {
		this.fechaCreacion = LocalDateTime.now();
		
	}
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5501150101586989418L;

}
