package cl.ms.maquinarias.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int cantidad;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;
	
	
	public void addQuantity(int quantityAdd) {
		this.cantidad += quantityAdd;
	}
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5154109212590968682L;




}
