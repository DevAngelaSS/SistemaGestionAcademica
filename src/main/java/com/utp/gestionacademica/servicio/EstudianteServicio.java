package com.utp.gestionacademica.servicio;

import com.utp.gestionacademica.modelo.Estudiante;
import com.utp.gestionacademica.repositorio.EstudianteRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EstudianteServicio {

    private final EstudianteRepositorio repositorio;

    public EstudianteServicio(EstudianteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Estudiante> listar() {
        return repositorio.findAll(Sort.by("apellidos", "nombres"));
    }

    public List<Estudiante> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        return repositorio.buscar(texto.trim());
    }

    public Estudiante obtenerPorId(Integer id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El estudiante solicitado no existe"));
    }

    public Estudiante guardar(Estudiante estudiante) {
        Optional<Estudiante> existente = repositorio.findByCodigo(estudiante.getCodigo());
        boolean codigoUsadoPorOtro = existente.isPresent()
                && !existente.get().getId().equals(estudiante.getId());
        if (codigoUsadoPorOtro) {
            throw new IllegalArgumentException("Ya existe un estudiante con el código " + estudiante.getCodigo());
        }
        return repositorio.save(estudiante);
    }

    public void eliminar(Integer id) {
        if (!repositorio.existsById(id)) {
            throw new IllegalArgumentException("El estudiante que intenta eliminar no existe");
        }
        repositorio.deleteById(id);
    }
}
