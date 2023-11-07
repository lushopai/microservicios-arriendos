package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import lombok.Data;


@Entity
@Table(name = "productos")
@Data
public class Producto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombreProducto;
	private Double precioProducto;
	@Temporal(TemporalType.DATE)
	private Date createAt;
	private String numeroSerie;
	private String patente;
	private String observacion;
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
	
    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL)
	private  Stock stock;
    
    @ManyToOne
    private Estado estado;
    
 

	/**
	 * 
	 */
	private static final long serialVersionUID = 7401558243029244461L;

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombreProducto=" + nombreProducto + ", precioProducto=" + precioProducto
				+ ", createAt=" + createAt + ", numeroSerie=" + numeroSerie + ", patente=" + patente + ", observacion="
				+ observacion + "]";
	}

}
