package com.eoi.Fruggy.web.controladores;

import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.repositorios.RepoUsuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginCtrl {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RepoUsuario repoUsuario;

    public LoginCtrl(BCryptPasswordEncoder bCryptPasswordEncoder, RepoUsuario repoUsuario) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.repoUsuario = repoUsuario;
    }

    // Método para mostrar formulario incio sesión
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        }
        return "/login/login";
    }
}
