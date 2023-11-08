package cl.ms.arriendos.entity;

import cl.ms.arriendos.models.Producto;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

	@Transient
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
