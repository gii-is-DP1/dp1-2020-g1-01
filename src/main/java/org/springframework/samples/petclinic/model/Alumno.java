package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name="alumnos")
@Data
public class Alumno extends Usuario{
	
	@NotNull
	@Column(name="num_tareas_entregadas")
	private Integer numTareasEntregadas;
	
	@Column(name="fecha_matriculacion")
	private Date fechaMatriculacion;
	
	@NotNull
	@Column(name="fecha_solicitud")
	private Date fechaSolicitud;
	
	@Column(name="fecha_baja")
	private Date fechaBaja;
	
    @ManyToOne(optional=true)
    private Tutor tutores;
    
    @ManyToOne(optional=true)
    private Grupo grupos;
}