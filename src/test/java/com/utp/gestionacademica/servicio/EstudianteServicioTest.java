package com.utp.gestionacademica.servicio;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.gestionacademica.modelo.Estudiante;
import com.utp.gestionacademica.repositorio.EstudianteRepositorio;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class EstudianteServicioTest {

    @Mock
    private EstudianteRepositorio repositorio;

    @InjectMocks
    private EstudianteServicio servicio;

    private Estudiante crearEstudiante(Integer id, String codigo) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(id);
        estudiante.setCodigo(codigo);
        estudiante.setNombres("Angela");
        estudiante.setApellidos("Suclupe");
        estudiante.setCorreo("angela@utp.edu.pe");
        estudiante.setCarrera("Ingeniería de Sistemas");
        estudiante.setSemestre(5);
        return estudiante;
    }

    @Test
    void guardaEstudianteNuevoCuandoElCodigoNoExiste() {
        Estudiante nuevo = crearEstudiante(null, "U001");
        when(repositorio.findByCodigo("U001")).thenReturn(Optional.empty());
        when(repositorio.save(nuevo)).thenReturn(nuevo);

        Estudiante guardado = servicio.guardar(nuevo);

        assertSame(nuevo, guardado);
    }

    @Test
    void rechazaEstudianteNuevoConCodigoDuplicado() {
        Estudiante existente = crearEstudiante(1, "U001");
        Estudiante nuevo = crearEstudiante(null, "U001");
        when(repositorio.findByCodigo("U001")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> servicio.guardar(nuevo));
        verify(repositorio, never()).save(any());
    }

    @Test
    void permiteActualizarConservandoSuPropioCodigo() {
        Estudiante existente = crearEstudiante(1, "U001");
        Estudiante actualizado = crearEstudiante(1, "U001");
        when(repositorio.findByCodigo("U001")).thenReturn(Optional.of(existente));
        when(repositorio.save(actualizado)).thenReturn(actualizado);

        Estudiante guardado = servicio.guardar(actualizado);

        assertSame(actualizado, guardado);
    }

    @Test
    void rechazaActualizarConCodigoDeOtroEstudiante() {
        Estudiante otro = crearEstudiante(2, "U002");
        Estudiante actualizado = crearEstudiante(1, "U002");
        when(repositorio.findByCodigo("U002")).thenReturn(Optional.of(otro));

        assertThrows(IllegalArgumentException.class, () -> servicio.guardar(actualizado));
        verify(repositorio, never()).save(any());
    }

    @Test
    void eliminaEstudianteExistente() {
        when(repositorio.existsById(1)).thenReturn(true);

        servicio.eliminar(1);

        verify(repositorio).deleteById(1);
    }

    @Test
    void rechazaEliminarEstudianteInexistente() {
        when(repositorio.existsById(5)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> servicio.eliminar(5));
        verify(repositorio, never()).deleteById(any());
    }

    @Test
    void busquedaVaciaListaTodosLosEstudiantes() {
        when(repositorio.findAll(any(Sort.class))).thenReturn(List.of());

        servicio.buscar("   ");

        verify(repositorio).findAll(any(Sort.class));
        verify(repositorio, never()).buscar(any());
    }

    @Test
    void busquedaConTextoUsaElRepositorio() {
        Estudiante estudiante = crearEstudiante(1, "U001");
        when(repositorio.buscar("angela")).thenReturn(List.of(estudiante));

        List<Estudiante> resultado = servicio.buscar("  angela ");

        assertSame(estudiante, resultado.get(0));
    }

    @Test
    void obtenerPorIdInexistenteLanzaExcepcion() {
        when(repositorio.findById(9)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> servicio.obtenerPorId(9));
    }
}
