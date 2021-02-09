package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="pagos")
public class Pago extends BaseEntity{
	
	@ManyToOne(optional=false)
	private TipoPago tipo;
	
	@Column(name="concepto")
	@NotEmpty(message = "Required field")
	private String concepto;
		   
    @ManyToOne(optional=false)
    private Alumno alumnos;
    
    @Column(name="fecha")
	private LocalDate fecha;
    
    public String toJson() {
    	TipoPago copyTipoPago = tipo;
    	LocalDate copyFecha = fecha;
    	this.setFecha(null);
    	this.setTipo(null);
    	System.out.println(copyFecha.toString());
    	Gson json = new Gson();
    	String jsonString = json.toJson(this);
    	String result = jsonString.substring(0, jsonString.length()-1) + ",\"fecha\":\""
				+ copyFecha.toString() + "\"" + ",\"tipo\""+":"+"{" + "\"tipo\""+":"+"\"Cash\""+"}"+ "}"; 
    	this.setFecha(copyFecha);
    	this.setTipo(copyTipoPago);
    	return result;
    }
	

}
