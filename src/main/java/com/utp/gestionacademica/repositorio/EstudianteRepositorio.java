package com.utp.gestionacademica.repositorio;

import com.utp.gestionacademica.modelo.Estudiante;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstudianteRepositorio extends JpaRepository<Estudiante, Integer> {

    Optional<Estudiante> findByCodigo(String codigo);

    @Query("select e from Estudiante e where "
            + "lower(e.codigo) like lower(concat('%', :texto, '%')) or "
            + "lower(e.nombres) like lower(concat('%', :texto, '%')) or "
            + "lower(e.apellidos) like lower(concat('%', :texto, '%')) or "
            + "lower(e.correo) like lower(concat('%', :texto, '%')) or "
            + "lower(e.carrera) like lower(concat('%', :texto, '%')) "
            + "order by e.apellidos, e.nombres")
    List<Estudiante> buscar(@Param("texto") String texto);
}
