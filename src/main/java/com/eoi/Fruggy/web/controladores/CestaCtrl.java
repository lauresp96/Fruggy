package com.eoi.Fruggy.web.controladores;


import com.eoi.Fruggy.entidades.Cesta;
import com.eoi.Fruggy.entidades.Precio;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.servicios.SrvcCesta;
import com.eoi.Fruggy.servicios.SrvcPrecio;
import com.eoi.Fruggy.servicios.SrvcProducto;
import com.eoi.Fruggy.servicios.SrvcUsuario;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cestas")
public class CestaCtrl {

    private final SrvcCesta cestaSrvc;
    private final SrvcUsuario usuarioSrvc;
    private final SrvcProducto productoSrvc;
    private final SrvcPrecio precioSrvc;


    public CestaCtrl(SrvcCesta cestaSrvc, SrvcUsuario usuarioSrvc, SrvcProducto productoSrvc, SrvcPrecio precioSrvc) {
        this.cestaSrvc = cestaSrvc;
        this.usuarioSrvc = usuarioSrvc;
        this.productoSrvc = productoSrvc;
        this.precioSrvc = precioSrvc;
    }

    // Método para listar cestas del usuario autenticado
    @GetMapping
    public String listarCestas(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<Cesta> cestas = cestaSrvc.findByUsuario(usuario);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Cesta> cestasFormateadas = cestas.stream()
                .map(cesta -> {
                    cesta.setFechaFormateada(cesta.getFecha().format(formatter));
                    return cesta;
                })
                .collect(Collectors.toList());

        model.addAttribute("cestas", cestasFormateadas);
        model.addAttribute("usuario", usuario);
        return "cestas/cesta-lista";
    }

    // Método para mostrar el formulario de creación de cesta
    @GetMapping("/crear")
    public String mostrarFormularioCrearCesta(Model model) {
        Cesta cesta = new Cesta();
        List<Cesta> cestas = cestaSrvc.buscarEntidades();
        model.addAttribute("cesta", cesta);
        return "cestas/crear-cesta";
    }

    // Método para guardar una nueva cesta
    @PostMapping("/guardar")
    public String guardarCesta(@Valid @ModelAttribute Cesta cesta,
                               BindingResult result,
                               @AuthenticationPrincipal Usuario usuario,
                               RedirectAttributes redirectAttributes,
                               @RequestParam(value = "productoId", required = false) Long productoId) throws Exception {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Errores en la creación de la cesta.");
            return "redirect:/cestas/crear";
        }

        // Verifica el límite de cestas por usuario
        List<Cesta> cestasUsuario = cestaSrvc.findByUsuario(usuario);
        if (cestasUsuario.size() >= 10) {
            redirectAttributes.addFlashAttribute("error", "No puedes tener más de 10 cestas.");
            return "redirect:/cestas";
        }

        cesta.setUsuario(usuario);
        cesta.setFecha(LocalDateTime.now());

        cestaSrvc.guardar(cesta);
        redirectAttributes.addFlashAttribute("success", "Cesta creada con éxito.");

        if (productoId != null) {
            return "redirect:/productos?agregarACesta=true&cestaId=" + cesta.getId();
        }

        return "redirect:/cestas";
    }


    // Método para obtener los detalles de una cesta por ID
    @GetMapping("/{id}")
    public String obtenerCesta(@PathVariable Long id, Model model) throws Throwable {
        Cesta cesta = cestaSrvc.encuentraPorId(id)
                .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));

        // Calcular el total cesta
        double total = cesta.getProductosEnCesta().stream()
                .mapToDouble(pc -> {
                    double precioBase = pc.getProducto().getPrecios().stream()
                            .findFirst()
                            .map(Precio::getValor)
                            .orElse(0.0);

                    // Aplicar descuento si está activo
                    double descuento = pc.getProducto().getDescuentos().stream()
                            .filter(desc -> desc.getActivo())
                            .mapToDouble(desc -> desc.getPorcentaje() / 100.0)
                            .findFirst()
                            .orElse(0.0);

                    // Calcular el total por producto considerando el descuento
                    return (precioBase * (1 - descuento)) * pc.getCantidad();
                })
                .sum();

        model.addAttribute("cesta", cesta);
        model.addAttribute("total", total);
        return "cestas/cesta-detalle";
    }

    // Método para mostrar el formulario de edición de una cesta
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) throws Throwable {
        Cesta cesta = (Cesta) cestaSrvc.encuentraPorId(id)
                .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));
        model.addAttribute("cesta", cesta);
        return "/cestas/cesta-editar";
    }

    // Método para actualizar una cesta
    @PostMapping("/{id}")
    public String actualizarCesta(@PathVariable Long id,
                                  @Valid @ModelAttribute Cesta cestaActualizada,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal Usuario usuario) throws Throwable {
        // Validar el objeto Cesta
        if (bindingResult.hasErrors()) {
            return "/cestas/cesta-editar";
        }

        Cesta cestaExistente = (Cesta) cestaSrvc.encuentraPorId(id)
                .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));

        // Verificar si la cesta pertenece al usuario
        if (!cestaExistente.getUsuario().equals(usuario)) {
            throw new RuntimeException("No tienes permiso para editar esta cesta.");
        }

        cestaExistente.setNombre(cestaActualizada.getNombre());
        cestaSrvc.guardar(cestaExistente);
        return "redirect:/cestas";
    }

    // Método para eliminar una cesta
    @PostMapping("/{id}/eliminar")
    public String eliminarCesta(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            Cesta cestaExistente = cestaSrvc.encuentraPorId(id)
                    .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));

            cestaSrvc.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("success", "Cesta eliminada con éxito.");
        } catch (Exception e) {
            e.printStackTrace();  // Esto te ayudará a ver el stack trace completo en los logs
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cesta: " + e.getMessage());
        }
        return "redirect:/cestas";
    }

    // Método para agregar un producto a una cesta
    @PostMapping("/agregarProducto")
    public String agregarProductoACesta(@RequestParam Long productoId,
                                        @RequestParam(required = false) Long cestaId,
                                        @RequestParam(required = false) String nuevaCesta,
                                        @RequestParam Integer cantidad,
                                        @AuthenticationPrincipal Usuario usuario,
                                        RedirectAttributes redirectAttributes) throws Exception {
        Producto producto = productoSrvc.encuentraPorId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (cestaId != null) {
            Cesta cesta = cestaSrvc.encuentraPorId(cestaId)
                    .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));

            // Verifica si la cesta pertenece al usuario
            if (!cesta.getUsuario().equals(usuario)) {
                redirectAttributes.addFlashAttribute("mensajeError", "No tienes permiso para agregar productos a esta cesta.");
                return "redirect:/productos";
            }

            cestaSrvc.agregarProductoACesta(cestaId, productoId, cantidad, null);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto añadido a la cesta '" + cesta.getNombre() + "'.");
        } else if (nuevaCesta != null && !nuevaCesta.isEmpty()) {
            Cesta cesta = new Cesta();
            cesta.setNombre(nuevaCesta);
            cesta.setUsuario(usuario);
            cesta.setFecha(LocalDateTime.now());
            cesta.addProducto(producto, cantidad, null);
            cestaSrvc.guardar(cesta);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto añadido a la nueva cesta '" + cesta.getNombre() + "'.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes seleccionar una cesta existente o crear una nueva.");
            return "redirect:/productos";
        }

        return "redirect:/productos";
    }

    // Método para eliminar un producto de una cesta
    @PostMapping("/{cestaId}/eliminarProducto")
    public String eliminarProductoDeCesta(@PathVariable Long cestaId,
                                          @RequestParam Long productoId,
                                          RedirectAttributes redirectAttributes) {
        try {
            // Mensaje de confirmación antes de eliminar el producto
            boolean confirmacion = true;
            if (confirmacion) {
                cestaSrvc.eliminarProductoDeCesta(cestaId, productoId);
                redirectAttributes.addFlashAttribute("success", "Producto eliminado de la cesta con éxito.");
            } else {
                redirectAttributes.addFlashAttribute("info", "Eliminación de producto cancelada.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el producto de la cesta.");
        }
        return "redirect:/cestas/" + cestaId;
    }

    // Método para incrementar la cantidad de un producto en una cesta
    @PostMapping("/{cestaId}/incrementarProducto")
    public String incrementarProductoEnCesta(@PathVariable Long cestaId,
                                             @RequestParam Long productoId,
                                             RedirectAttributes redirectAttributes) {
        try {
            cestaSrvc.agregarProductoACesta(cestaId, productoId, 1, null);
            redirectAttributes.addFlashAttribute("success", "Producto incrementado en la cesta con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo incrementar el producto en la cesta.");
        }
        return "redirect:/cestas/" + cestaId;
    }

    // Método para decrementar la cantidad de un producto en una cesta
    @PostMapping("/{cestaId}/decrementarProducto")
    public String decrementarProductoEnCesta(@PathVariable Long cestaId,
                                             @RequestParam Long productoId,
                                             RedirectAttributes redirectAttributes) {
        try {
            cestaSrvc.agregarProductoACesta(cestaId, productoId, -1, null);
            redirectAttributes.addFlashAttribute("success", "Producto decrementado en la cesta con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo decrementar el producto en la cesta.");
        }
        return "redirect:/cestas/" + cestaId;
    }

    // Método para actualizar la cantidad de un producto en una cesta
    @PostMapping("/{cestaId}/actualizarCantidad")
    public String actualizarCantidadProductoEnCesta(@PathVariable Long cestaId,
                                                    @RequestParam Long productoId,
                                                    @RequestParam Integer cantidad,
                                                    RedirectAttributes redirectAttributes) {
        try {
            if (cantidad <= 0) {
                // Mensaje de confirmación antes de eliminar el producto
                boolean confirmacion = true;
                if (confirmacion) {
                    cestaSrvc.eliminarProductoDeCesta(cestaId, productoId);
                    redirectAttributes.addFlashAttribute("success", "Producto eliminado de la cesta con éxito.");
                } else {
                    redirectAttributes.addFlashAttribute("info", "Eliminación de producto cancelada.");
                }
            } else {
                // Actualizar la cantidad del producto en la cesta
                cestaSrvc.actualizarCantidadProductoEnCesta(cestaId, productoId, cantidad);
                redirectAttributes.addFlashAttribute("success", "Cantidad del producto actualizada con éxito.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la cantidad del producto.");
        }
        return "redirect:/cestas/" + cestaId;
    }

}
