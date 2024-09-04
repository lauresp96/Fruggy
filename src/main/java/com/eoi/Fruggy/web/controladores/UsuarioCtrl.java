package com.eoi.Fruggy.web.controladores;

import com.eoi.Fruggy.entidades.Detalle;
import com.eoi.Fruggy.entidades.Genero;
import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.servicios.SrvcDetalle;
import com.eoi.Fruggy.servicios.SrvcGenero;
import com.eoi.Fruggy.servicios.SrvcRol;
import com.eoi.Fruggy.servicios.SrvcUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Controller
public class UsuarioCtrl {

    private final SrvcUsuario usuarioSrvc;
    private final SrvcDetalle detalleSrvc;
    private final SrvcRol rolSrvc;
    private final SrvcGenero generoSrvc;

    public UsuarioCtrl(SrvcUsuario usuarioSrvc, SrvcDetalle detalleSrvc, SrvcRol rolSrvc, SrvcGenero generoSrvc) {
        this.usuarioSrvc = usuarioSrvc;
        this.detalleSrvc = detalleSrvc;
        this.rolSrvc = rolSrvc;
        this.generoSrvc = generoSrvc;
    }


    // Método para mostrar el formulario de registro de usuario
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        Usuario usuario = new Usuario();
        Detalle detalle = new Detalle();
        detalle.setGenero(new Genero());
        usuario.setDetalle(detalle);

        // Obtener el idioma actual del contexto
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        model.addAttribute("usuario", usuario);
        model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
        model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
        model.addAttribute("idioma", idioma);
        return "usuarios/registroUsuario";
    }

    // Método para guardar un nuevo usuario después de la validación del formulario
    @PostMapping("/registro/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            return "usuarios/registroUsuario";
        }

        // Validación del email
        if (usuarioSrvc.getRepo().findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            return "usuarios/registroUsuario";
        }

        // Validaciones adicionales detalle
        Detalle detalle = usuario.getDetalle();
        if (detalle != null) {
            if (detalle.getGenero() == null || detalle.getGenero().getId() == null) {
                model.addAttribute("error", "Género no seleccionado");
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                return "usuarios/registroUsuario";
            }

            Optional<Genero> generoOptional = generoSrvc.encuentraPorId(detalle.getGenero().getId());
            if (generoOptional.isPresent()) {
                detalle.setGenero(generoOptional.get());
            } else {
                model.addAttribute("error", "Género no encontrado");
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
                return "usuarios/registroUsuario";
            }
        }

        try {
            detalle = detalleSrvc.guardar(detalle);

            // Asignar un rol de usuario por defecto = USER
            Set<Rol> roles = new HashSet<>();
            Optional<Rol> rolUser = rolSrvc.buscarRolPorNombre("ROLE_USER");
            if (rolUser.isPresent()) {
                roles.add(rolUser.get());
                usuario.setRoles(roles);
            } else {
                model.addAttribute("error", "Rol de usuario no encontrado");
                return "usuarios/registroUsuario";
            }

            usuario.setActive(true);
            usuario.setDetalle(detalle);
            usuarioSrvc.guardar(usuario);
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el usuario. Intenta con otro nombre de usuario.");
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            return "usuarios/registroUsuario";
        }

        return "redirect:/login";
    }

    // Método para mostrar la administración de la cuenta del usuario
    @GetMapping("usuario/administracion")
    public String administracionCuenta(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioOpt = usuarioSrvc.getRepo().findByEmail(userDetails.getUsername());
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("roles", rolSrvc.buscarEntidadesSet());
            model.addAttribute("idioma", idioma);
            return "usuarios/usuario-administracion-cuenta";
        }
        return "redirect:/login";
    }

    // Método para mostrar el formulario de edición del usuario
    @GetMapping("usuario/administracion/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioOpt = usuarioSrvc.encuentraPorId(id);
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verificar si el usuario autenticado es el mismo que está intentando editar
            if (!usuario.getEmail().equals(userDetails.getUsername())) {
                return "redirect:/login";
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("idioma", idioma);
            return "usuarios/editarUsuario";
        }
        return "redirect:/login";
    }

    // Método para procesar la edición del usuario
    @PostMapping("usuario/administracion/editar/{id}")
    public String editarUsuario(@PathVariable Long id, @Valid @ModelAttribute("usuario") Usuario usuario,
                                BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        if (bindingResult.hasErrors()) {
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            model.addAttribute("idioma", idioma);
            return "usuarios/editarUsuario";
        }

        try {
            Detalle detalle = usuario.getDetalle();
            if (detalle != null && detalle.getGenero() != null && detalle.getGenero().getId() != null) {
                Optional<Genero> generoOptional = generoSrvc.encuentraPorId(detalle.getGenero().getId());
                if (generoOptional.isPresent()) {
                    detalle.setGenero(generoOptional.get());
                } else {
                    model.addAttribute("error", "Género no encontrado");
                    model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                    model.addAttribute("idioma", idioma);
                    return "usuarios/editarUsuario";
                }
            } else {
                model.addAttribute("error", "Género no seleccionado");
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                model.addAttribute("idioma", idioma);
                return "usuarios/editarUsuario";
            }

            // Verificar si el nombre de usuario ya está en uso, excluyendo el usuario actual
            Optional<Detalle> existingDetalle = detalleSrvc.findByNombreUsuarioExcludingId(usuario.getDetalle().getNombreUsuario(), usuario.getDetalle().getId());
            if (existingDetalle.isPresent()) {
                model.addAttribute("error", "El nombre de usuario ya está en uso.");
                model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
                model.addAttribute("idioma", idioma);
                return "usuarios/editarUsuario";
            }

            usuario.setId(id);
            usuarioSrvc.guardar(usuario);
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la edición del usuario. Inténtalo de nuevo.");
            model.addAttribute("generos", generoSrvc.buscarEntidadesSet());
            return "usuarios/editarUsuario";
        }

        return "redirect:/usuario/administracion";
    }

    // Método para dar de baja al usuario
    @PostMapping("usuario/administracion/baja")
    public String darDeBaja(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioSrvc.getRepo().findByEmail(userDetails.getUsername());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuarioSrvc.eliminarPorId(usuario.getId());
            // Invalida la sesión actual
            request.getSession().invalidate();
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    // Método para manejar el logout del usuario
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
