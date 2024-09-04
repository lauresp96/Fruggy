package com.eoi.Fruggy.web.controladores.admin;

import com.eoi.Fruggy.entidades.*;
import com.eoi.Fruggy.servicios.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/usuarios")
public class ADMINUsuarioCtrl {

    private static final Logger log = LoggerFactory.getLogger(ADMINUsuarioCtrl.class);
    private final SrvcUsuario usuarioSrvc;
    private final SrvcDetalle detalleSrvc;
    private final SrvcRol rolSrvc;
    private final SrvcImagen imagenSrvc;
    private final SrvcGenero generoSrvc;

    public ADMINUsuarioCtrl(SrvcUsuario usuarioSrvc, SrvcDetalle detalleSrvc, SrvcRol rolSrvc, SrvcImagen imagenSrvc, SrvcGenero generoSrvc) {
        this.usuarioSrvc = usuarioSrvc;
        this.detalleSrvc = detalleSrvc;
        this.rolSrvc = rolSrvc;
        this.imagenSrvc = imagenSrvc;
        this.generoSrvc = generoSrvc;
    }

    // Método para listar usuarios con paginación y ordenación
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping
    public String listarUsuarios(@RequestParam(value = "email", required = false, defaultValue = "") String email, @RequestParam(value = "activo", required = false) Boolean activo, @RequestParam(value = "sortField", defaultValue = "email") String sortField, @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size, Model model) {

        // Determina la columna y dirección de ordenación
        String sortColumn;
        switch (sortField) {
            case "id_asc":
                sortColumn = "id";
                sortDirection = "asc";
                break;
            case "id_desc":
                sortColumn = "id";
                sortDirection = "desc";
                break;
            case "roles":
                sortColumn = "roles";
                break;
            case "email_asc":
                sortColumn = "email";
                sortDirection = "asc";
                break;
            case "email_desc":
                sortColumn = "email";
                sortDirection = "desc";
                break;
            default:
                sortColumn = "email";
                sortDirection = "asc";
                break;
        }

        Page<Usuario> usuarios = usuarioSrvc.obtenerUsuariosPaginados(page, size, sortColumn, sortDirection, email);
        // Obtiene el idioma actual del contexto
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuarios.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("email", email);
        model.addAttribute("activo", activo);
        model.addAttribute("idioma", idioma);

        return "admin/CRUD-Usuarios";
    }

    // Método para mostrar el formulario de creación de usuario
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/agregar")
    public String agregarUsuario(Model model) {
        Usuario usuario = new Usuario();
        Detalle detalle = new Detalle();
        detalle.setGenero(new Genero());
        usuario.setDetalle(detalle);
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
        model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
        model.addAttribute("idioma", idioma);

        log.info("Usuario agregado. {}", usuario);
        return "admin/crear-usuario";
    }

    // Método para guardar un nuevo usuario
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            return "admin/crear-usuario";
        }

        // Verificar si el email ya está en uso (solo para nuevos usuarios)
        if (usuario.getId() == null) { // New user
            if (usuarioSrvc.findByEmail(usuario.getEmail()).isPresent()) {
                model.addAttribute("error", "El correo electrónico ya está en uso.");
                model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                return "admin/crear-usuario";
            }
        }

        // Validaciones y guardado del usuario
        try {
            Detalle detalle = usuario.getDetalle();
            if (detalle != null && detalle.getGenero() != null && detalle.getGenero().getId() != null) {
                Optional<Genero> generoOptional = generoSrvc.encuentraPorId(detalle.getGenero().getId());
                if (generoOptional.isPresent()) {
                    detalle.setGenero(generoOptional.get());
                } else {
                    model.addAttribute("error", "Género no encontrado");
                    model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                    model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                    return "admin/crear-usuario";
                }
            } else {
                model.addAttribute("error", "Género no seleccionado");
                model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                return "admin/crear-usuario";
            }

            if (detalle != null) {
                detalle = detalleSrvc.guardar(detalle);
            } else {
                model.addAttribute("error", "Detalles del usuario no proporcionados");
                return "admin/crear-usuario";
            }

            usuario.setDetalle(detalle);
            usuarioSrvc.guardar(usuario);
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el usuario. Inténtalo de nuevo.");
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            return "admin/crear-usuario";
        }
        return "redirect:/admin/usuarios";
    }

    // Método para mostrar el formulario de edición de usuario
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioSrvc.encuentraPorId(id);
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("idioma", idioma);

            return "admin/modificar-usuario";
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "error";
        }
    }

    // Método para guardar los cambios de un usuario editado
    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, @Valid @ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            return "admin/modificar-usuario";
        }

        try {
            Optional<Usuario> usuarioExistente = usuarioSrvc.encuentraPorId(id);
            if (usuarioExistente.isPresent()) {
                Usuario usuarioOriginal = usuarioExistente.get();
                Detalle detalle = usuario.getDetalle();

                // Verificar si el nuevo email está en uso, excluyendo el ID actual
                Optional<Usuario> usuarioConEmailExistente = usuarioSrvc.findByEmail(usuario.getEmail());
                if (usuarioConEmailExistente.isPresent() && !usuarioConEmailExistente.get().getId().equals(id)) {
                    model.addAttribute("error", "El correo electrónico ya está en uso.");
                    model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                    model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                    return "admin/modificar-usuario";
                }

                // Si el nombre de usuario no ha cambiado, mantenerlo igual
                if (usuarioOriginal.getDetalle().getNombreUsuario().equals(detalle.getNombreUsuario())) {
                    detalle.setNombreUsuario(usuarioOriginal.getDetalle().getNombreUsuario());
                }

                // Actualizar otros campos del usuario
                usuarioOriginal.setEmail(usuario.getEmail());
                usuarioOriginal.setPassword(usuario.getPassword());
                usuarioOriginal.setTelefono(usuario.getTelefono());
                usuarioOriginal.setActive(usuario.getActive());
                usuarioOriginal.setDetalle(detalle);

                usuarioSrvc.guardar(usuarioOriginal);
            } else {
                model.addAttribute("error", "Usuario no encontrado");
                model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                return "admin/modificar-usuario";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la edición del usuario. Inténtalo de nuevo.");
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            return "admin/modificar-usuario";
        }

        return "redirect:/admin/usuarios";
    }

    // Método para listar las cestas de un usuario
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/{id}/cestas")
    public String listarCestas(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioSrvc.encuentraPorId(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            model.addAttribute("cestas", usuarioOptional.get().getCestas());
            return "usuarios/cestas";
        }
        return "redirect:/usuarios";
    }

    // Método para eliminar un usuario
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioSrvc.eliminarPorId(id);
        return "redirect:/admin/usuarios";
    }
}
