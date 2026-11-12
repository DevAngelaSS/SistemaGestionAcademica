package com.utp.gestionacademica.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EstudianteValidacionTest {

    private static Validator validador;

    @BeforeAll
    static void iniciar() {
        validador = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Estudiante crearEstudianteValido() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("U23225202");
        estudiante.setNombres("Angela Katerine");
        estudiante.setApellidos("Suclupe Santamaría");
        estudiante.setCorreo("angela@utp.edu.pe");
        estudiante.setCarrera("Ingeniería de Sistemas");
        estudiante.setSemestre(5);
        return estudiante;
    }

    @Test
    void estudianteValidoNoGeneraViolaciones() {
        Set<ConstraintViolation<Estudiante>> violaciones = validador.validate(crearEstudianteValido());
        assertTrue(violaciones.isEmpty());
    }

    @Test
    void correoInvalidoGeneraUnaViolacion() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setCorreo("correo-sin-arroba");
        Set<ConstraintViolation<Estudiante>> violaciones = validador.validate(estudiante);
        assertEquals(1, violaciones.size());
    }

    @Test
    void nombresVaciosGeneranUnaViolacion() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setNombres(" ");
        Set<ConstraintViolation<Estudiante>> violaciones = validador.validate(estudiante);
        assertEquals(1, violaciones.size());
    }

    @Test
    void semestreFueraDeRangoGeneraUnaViolacion() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setSemestre(15);
        Set<ConstraintViolation<Estudiante>> violaciones = validador.validate(estudiante);
        assertEquals(1, violaciones.size());
    }

    @Test
    void semestreNuloGeneraUnaViolacion() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setSemestre(null);
        Set<ConstraintViolation<Estudiante>> violaciones = validador.validate(estudiante);
        assertEquals(1, violaciones.size());
    }
}
