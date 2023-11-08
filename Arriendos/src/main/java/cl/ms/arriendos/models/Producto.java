package cl.ms.arriendos.models;

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



@Data
public class Producto  {


    private Long id;

    private String nombreProducto;
    private Double precioProducto;
    private Date createAt;
    private String numeroSerie;
    private String patente;
    private String observacion;
    //private Categoria categoria;


    //private  Stock stock;


    //private Estado estado;





}
