package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Alumno;
import org.springframework.web.bind.annotation.RequestParam;


public interface AlumnoRepository extends CrudRepository<Alumno, String> {
	
	@Query(value = "Select * from alumnos where (alumnos.nick_usuario = :nickUsuario)", nativeQuery = true)
	public Alumno findByNick(@Param("nickUsuario") String nickUsuario);
	
	@Query(value = "Select * from alumnos where grupos_nombre_grupo= :nombreGrupo",nativeQuery = true)
    public List<Alumno> findByGroup(@Param("nombreGrupo") String nombreGrupo);
	
	@Query("SELECT alumno FROM Alumno alumno WHERE alumno.fechaMatriculacion IS not null")
	public List<Alumno>findStudents();

    @Query("SELECT alumno,grupo FROM Alumno alumno JOIN Grupo grupo WHERE grupo.cursos.cursoDeIngles = :cursoDeIngles")
    public List<Alumno> findStudentsByCourse(@Param("cursoDeIngles") String cursoDeIngles);
    
    @Query(value = "SELECT * FROM ALUMNOS where(tutores_nick_usuario=:nickTutor and FECHA_MATRICULACION IS NOT NULL)",nativeQuery = true)
    public List<Alumno> findStudentsByTutor(@Param("nickTutor")String nickTutor);
}
