package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Table(name = "tbl_detalleArriendos")
@Data
public class DetalleArriendo implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer cantidad;
	private Integer dias;
	private LocalDateTime fechaPosibleEntrega;
	private LocalDateTime fechaEntrega;
	private boolean finalizado;
	private Double importe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_id", referencedColumnName = "id")
	private Producto producto;

	@PrePersist
	private void init() {
		fechaPosibleEntrega = LocalDateTime.now().plusDays(dias);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
