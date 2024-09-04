package com.eoi.Fruggy.web.controladores.admin;

import com.eoi.Fruggy.entidades.Imagen;
import com.eoi.Fruggy.entidades.Supermercado;
import com.eoi.Fruggy.servicios.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin/supermercados")
public class ADMINSupermercadoCtrl {

    private final SrvcSupermercado supermercadoSrvc;
    private final SrvcUsuario usuarioSrvc;
    private final SrvcImagen imagenSrvc;
    private final SrvcPrecio precioSrvc;
    private final SrvcValSupermercado valSupermercadoSrvc;

    public ADMINSupermercadoCtrl(SrvcSupermercado supermercadoSrvc, SrvcUsuario usuarioSrvc, SrvcImagen imagenSrvc, SrvcPrecio precioSrvc, SrvcValSupermercado valSupermercadoSrvc) {
        this.supermercadoSrvc = supermercadoSrvc;
        this.usuarioSrvc = usuarioSrvc;
        this.imagenSrvc = imagenSrvc;
        this.precioSrvc = precioSrvc;
        this.valSupermercadoSrvc = valSupermercadoSrvc;
    }

    // Método para listar los supermercados con paginación y ordenación
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping
    public String listarSupermercados(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "nombreSuper") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection,
                                      Model model) {
        // Obtiene la página de supermercados
        Page<Supermercado> paginaSupermercados = supermercadoSrvc.obtenerSupermercadosPaginados(page, size, sortField, sortDirection);

        model.addAttribute("supermercados", paginaSupermercados);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaSupermercados.getTotalPages());
        model.addAttribute("currentSortField", sortField);
        model.addAttribute("currentSortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "admin/CRUD-Supermercados";
    }

    // Método para mostrar el formulario de creación de un nuevo supermercado
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/agregar")
    public String agregarSupermercado(Model model) {
        model.addAttribute("supermercado", new Supermercado());
        return "admin/crear-supermercado";
    }

    // Método para guardar un nuevo supermercado en la base de datos
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/guardar")
    public String guardarSupermercado(@Valid @ModelAttribute Supermercado supermercado, BindingResult bindingResult, Model model) throws Exception {

        // Verifica si hay errores en la validación
        if (bindingResult.hasErrors()) {
            return "admin/crear-supermercado";
        }
        // Guarda el supermercado en la base de datos
        supermercadoSrvc.guardar(supermercado);

        // Procesa las imágenes
        if (supermercado.getImagenesArchivo() != null) {
            for (MultipartFile file : supermercado.getImagenesArchivo()) {
                if (!file.isEmpty()) {
                    Imagen imagen = imagenSrvc.guardarImagen(file, supermercado); // Aquí se llama al servicio de imagen
                    imagen.setSupermercado(supermercado); // Establece la relación
                    imagenSrvc.guardar(imagen); // Guarda la imagen en la base de datos
                }
            }
        }

        return "redirect:/admin/supermercados";
    }

    // Método para mostrar el formulario de edición de un supermercado existente
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/editar/{id}")
    public String editarSupermercado(@PathVariable("id") Long id, Model model) {
        Optional<Supermercado> supermercadoOptional = supermercadoSrvc.encuentraPorId(id);
        if (supermercadoOptional.isPresent()) {
            Supermercado supermercado = supermercadoOptional.get();
            model.addAttribute("supermercado", supermercado);
            model.addAttribute("usuarios", usuarioSrvc.buscarEntidades());
            model.addAttribute("imagenes", imagenSrvc.buscarEntidades());
            model.addAttribute("precios", precioSrvc.buscarEntidades());
            model.addAttribute("valoraciones", valSupermercadoSrvc.buscarEntidades());
            model.addAttribute("imagenes", imagenSrvc.buscarEntidades());
            return "admin/modificar-supermercado";
        } else {
            return "redirect:/admin/supermercados";
        }
    }

    //IMAGENES

    // Método para agregar una imagen a un supermercado
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/{id}/agregar-imagen")
    public String agregarImagen(@PathVariable("id") Long id, @RequestParam("imagen") MultipartFile file, Model model) {

        // Busca el supermercado por ID
        try {
            Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(id)
                    .orElseThrow(() -> new RuntimeException("Supermercado no encontrado"));

            // Guarda la imagen si no está vacía
            if (!file.isEmpty()) {
                Imagen imagen = imagenSrvc.guardarImagen(file, supermercado);
                imagen.setSupermercado(supermercado);
                imagenSrvc.guardar(imagen);
            } else {
                model.addAttribute("error", "El archivo no puede estar vacío.");
            }
        } catch (Throwable e) {
            model.addAttribute("error", "Error al agregar la imagen: " + e.getMessage());
        }
        return "redirect:/admin/supermercados/editar/" + id;
    }

    // Método para actualizar un supermercado existente
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/actualizar")
    public String actualizarSupermercado(@Valid @ModelAttribute Supermercado supermercado,
                                         BindingResult bindingResult,
                                         @RequestParam("imagenesArchivo") MultipartFile imagenesArchivo,
                                         Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "admin/modificar-supermercado";
        }

        // Guarda el supermercado
        supermercadoSrvc.guardar(supermercado);

        // Procesa la nueva imagen subida
        if (imagenesArchivo != null && !imagenesArchivo.isEmpty()) {
            // Eliminar la imagen existente si la hay
            Set<Imagen> imagenesExistentes = imagenSrvc.buscarPorSupermercado(supermercado);
            for (Imagen imagen : imagenesExistentes) {
                imagenSrvc.eliminarPorId(imagen.getId());
            }

            // Guardar la nueva imagen
            Imagen nuevaImagen = imagenSrvc.guardarImagen(imagenesArchivo, supermercado);
            nuevaImagen.setSupermercado(supermercado);
            imagenSrvc.guardar(nuevaImagen);
        }

        return "redirect:/admin/supermercados";
    }

    // Método para eliminar un supermercado
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/eliminar/{id}")
    public String eliminarSupermercado(@PathVariable("id") Long id, Model model) {
        supermercadoSrvc.eliminarPorId(id);
        return "redirect:/admin/supermercados";
    }
}

