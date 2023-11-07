package cl.ms.maquinarias.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "tbl_rollBackAudit")
@Data
public class RollBackAudit implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime timestamp;
	private String data;
	private String errors;
	
	/**
	 * 
	 */
	
	@PrePersist
	public void init() {
		timestamp = LocalDateTime.now();
	}
	private static final long serialVersionUID = 1281979979654343820L;

}
