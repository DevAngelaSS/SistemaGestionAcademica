package com.utp.gestionacademica.controlador;

import com.utp.gestionacademica.modelo.Estudiante;
import com.utp.gestionacademica.servicio.EstudianteServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EstudianteControlador {

    private final EstudianteServicio servicio;

    public EstudianteControlador(EstudianteServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/estudiantes";
    }

    @GetMapping("/estudiantes")
    public String listar(@RequestParam(required = false) String texto, Model modelo) {
        modelo.addAttribute("estudiantes", servicio.buscar(texto));
        modelo.addAttribute("texto", texto);
        return "lista";
    }

    @GetMapping("/estudiantes/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("estudiante", new Estudiante());
        modelo.addAttribute("titulo", "Registrar estudiante");
        return "formulario";
    }

    @GetMapping("/estudiantes/{id}/editar")
    public String editar(@PathVariable Integer id, Model modelo, RedirectAttributes mensajes) {
        try {
            modelo.addAttribute("estudiante", servicio.obtenerPorId(id));
            modelo.addAttribute("titulo", "Actualizar estudiante");
            return "formulario";
        } catch (IllegalArgumentException excepcion) {
            mensajes.addFlashAttribute("mensajeError", excepcion.getMessage());
            return "redirect:/estudiantes";
        }
    }

    @PostMapping("/estudiantes/guardar")
    public String guardar(@Valid @ModelAttribute("estudiante") Estudiante estudiante,
                          BindingResult resultado, Model modelo, RedirectAttributes mensajes) {
        boolean esNuevo = estudiante.getId() == null;
        if (!resultado.hasErrors()) {
            try {
                servicio.guardar(estudiante);
                mensajes.addFlashAttribute("mensajeExito",
                        esNuevo ? "Estudiante registrado correctamente" : "Estudiante actualizado correctamente");
                return "redirect:/estudiantes";
            } catch (IllegalArgumentException excepcion) {
                resultado.rejectValue("codigo", "codigoDuplicado", excepcion.getMessage());
            }
        }
        modelo.addAttribute("titulo", esNuevo ? "Registrar estudiante" : "Actualizar estudiante");
        modelo.addAttribute("mensajeError", "Revise los datos ingresados");
        return "formulario";
    }

    @PostMapping("/estudiantes/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes mensajes) {
        try {
            servicio.eliminar(id);
            mensajes.addFlashAttribute("mensajeExito", "Estudiante eliminado correctamente");
        } catch (IllegalArgumentException excepcion) {
            mensajes.addFlashAttribute("mensajeError", excepcion.getMessage());
        }
        return "redirect:/estudiantes";
    }
}
