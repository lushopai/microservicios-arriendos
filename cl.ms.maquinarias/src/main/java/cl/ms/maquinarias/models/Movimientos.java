package cl.ms.maquinarias.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cl.ms.maquinarias.enums.TipoMovimiento;
import lombok.Data;

@Entity
@Table(name = "movimientos")
@Data
public class Movimientos implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int cantidad;
	
	@Enumerated(EnumType.STRING)
	private TipoMovimiento tipoMovimiento;
	
	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto  producto;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8573091771816376196L;

}
