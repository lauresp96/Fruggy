package com.eoi.Fruggy.web.controladores;

import com.eoi.Fruggy.entidades.Imagen;
import com.eoi.Fruggy.entidades.Supermercado;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.entidades.ValoracionSupermercado;
import com.eoi.Fruggy.servicios.SrvcImagen;
import com.eoi.Fruggy.servicios.SrvcSupermercado;
import com.eoi.Fruggy.servicios.SrvcUsuario;
import com.eoi.Fruggy.servicios.SrvcValSupermercado;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/supermercados")
public class SupermercadoCtrl {

    private final SrvcSupermercado supermercadoSrvc;
    private final SrvcValSupermercado valSupermercadoSrvc;
    private final SrvcUsuario usuarioSrvc;
    private final SrvcImagen imagenSrvc;

    public SupermercadoCtrl(SrvcSupermercado supermercadoSrvc, SrvcValSupermercado valSupermercadoSrvc, SrvcUsuario usuarioSrvc, SrvcImagen imagenSrvc) {
        this.supermercadoSrvc = supermercadoSrvc;
        this.valSupermercadoSrvc = valSupermercadoSrvc;
        this.usuarioSrvc = usuarioSrvc;
        this.imagenSrvc = imagenSrvc;
    }

    // Método para listar supermercados con paginación y ordenación
    @GetMapping
    public String listarSupermercados(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "nombreSuper") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection,
                                      Model model) {

        // Obtener la página de supermercados según los parámetros de paginación y ordenación
        Page<Supermercado> paginaSupermercados = supermercadoSrvc.obtenerSupermercadosPaginados(page, size, sortField, sortDirection);

        // Crear una lista de números de página
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < paginaSupermercados.getTotalPages(); i++) {
            pageNumbers.add(i);
        }

        // Calcular la nota media para cada supermercado
        for (Supermercado supermercado : paginaSupermercados) {
            Double notaMedia = valSupermercadoSrvc.calcularNotaMedia(supermercado.getId());
            supermercado.setNotaMedia(notaMedia);
        }

        model.addAttribute("supermercados", paginaSupermercados);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaSupermercados.getTotalPages());
        model.addAttribute("currentSortField", sortField);
        model.addAttribute("currentSortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "/supermercados/lista-supermercados";
    }


    // Método para mostrar los detalles de un supermercado específico
    @GetMapping("/detalles/{id}")
    public String verDetallesSupermercado(@PathVariable("id") long id, Model model) throws Throwable {
        Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de supermercado inválido: " + id));

        // Obtener las valoraciones y calcular la nota media
        List<ValoracionSupermercado> valoraciones = valSupermercadoSrvc.obtenerValoracionesPorSupermercado(id);
        Double notaMedia = valSupermercadoSrvc.calcularNotaMedia(id);

        model.addAttribute("supermercado", supermercado);
        model.addAttribute("valoraciones", valoraciones);
        model.addAttribute("notaMedia", notaMedia);
        model.addAttribute("valoracion", new ValoracionSupermercado());
        model.addAttribute("imagenes", supermercado.getImagenes());

        return "supermercados/detalles-supermercado";
    }

    // Método para guardar una valoración de supermercado
    @PostMapping("/detalles/{supermercadoId}/guardar")
    public String guardarValoracion(@PathVariable Long supermercadoId,
                                    @Valid @ModelAttribute("valoracion") ValoracionSupermercado valoracion,
                                    BindingResult result,
                                    @AuthenticationPrincipal Usuario usuario,
                                    Model model) throws Throwable {
        if (result.hasErrors()) {
            Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(supermercadoId)
                    .orElseThrow(() -> new IllegalArgumentException("ID de supermercado inválido: " + supermercadoId));
            List<ValoracionSupermercado> valoraciones = valSupermercadoSrvc.obtenerValoracionesPorSupermercado(supermercadoId);
            model.addAttribute("supermercado", supermercado);
            model.addAttribute("valoraciones", valoraciones);
            model.addAttribute("error", "Error al guardar la valoración. Verifica los datos e intenta nuevamente.");
            return "supermercados/detalles-supermercado";
        }

        // Verificar si el usuario está autenticado
        Optional<Usuario> usuarioOptional = Optional.ofNullable(usuario);
        if (usuarioOptional.isEmpty()) {
            model.addAttribute("error", "Usuario no autenticado.");
            Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(supermercadoId)
                    .orElseThrow(() -> new IllegalArgumentException("ID de supermercado inválido: " + supermercadoId));
            List<ValoracionSupermercado> valoraciones = valSupermercadoSrvc.obtenerValoracionesPorSupermercado(supermercadoId);
            model.addAttribute("supermercado", supermercado);
            model.addAttribute("valoraciones", valoraciones);
            return "supermercados/detalles-supermercado";
        }

        // Asignar el usuario y supermercado a la valoración
        valoracion.setUsuario(usuarioOptional.get());
        Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(supermercadoId)
                .orElseThrow(() -> new IllegalArgumentException("ID de supermercado inválido: " + supermercadoId));
        valoracion.setSupermercado(supermercado);

        try {
            valSupermercadoSrvc.guardar(valoracion);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            List<ValoracionSupermercado> valoraciones = valSupermercadoSrvc.obtenerValoracionesPorSupermercado(supermercadoId);
            model.addAttribute("valoraciones", valoraciones);
            model.addAttribute("supermercado", supermercado);
            return "supermercados/detalles-supermercado";
        }

        return "redirect:/supermercados/detalles/" + supermercadoId;
    }
}
