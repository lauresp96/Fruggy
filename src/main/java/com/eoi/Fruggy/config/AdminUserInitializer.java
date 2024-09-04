package com.eoi.Fruggy.config;

import com.eoi.Fruggy.entidades.Detalle;
import com.eoi.Fruggy.entidades.Genero;
import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.repositorios.RepoDetalle;
import com.eoi.Fruggy.repositorios.RepoGenero;
import com.eoi.Fruggy.repositorios.RepoRol;
import com.eoi.Fruggy.repositorios.RepoUsuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final RepoUsuario repoUsuario;
    private final RepoRol repoRol;
    private final RepoGenero repoGenero;
    private final RepoDetalle repoDetalle;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final DataInitializer dataInitializer;

    public AdminUserInitializer(RepoUsuario repoUsuario, RepoRol repoRol, RepoGenero repoGenero, RepoDetalle repoDetalle, PasswordEncoder passwordEncoder, MessageSource messageSource, DataInitializer dataInitializer) {
        this.repoUsuario = repoUsuario;
        this.repoRol = repoRol;
        this.repoGenero = repoGenero;
        this.repoDetalle = repoDetalle;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
        this.dataInitializer = dataInitializer;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        dataInitializer.init();
        if (repoUsuario.count() == 0) {
            // Crear y guardar Detalle
            Detalle detalle = new Detalle();
            detalle.setNombreUsuario("admin");
            detalle.setNombre("Admin");
            detalle.setApellido("User");
            detalle.setEdad(30);
            detalle.setCalle("Calle Admin");
            detalle.setMunicipio("Municipio Admin");
            detalle.setPais("País Admin");
            detalle.setCodigopostal(12345);

            // Asignar un género al detalle
            Genero genero = repoGenero.findById(1).orElseThrow(() -> new RuntimeException("Género no encontrado"));
            detalle.setGenero(genero);

            Detalle savedDetalle = repoDetalle.save(detalle);

            // Crear Usuario y asignar el Detalle guardado
            Usuario admin = new Usuario();
            admin.setEmail("admin@example.com");
            admin.setActive(true);
            admin.setPassword(passwordEncoder.encode("Password-123"));
            admin.setDetalle(savedDetalle);

            // Añadir el rol al usuario
            Locale locale = new Locale("es");
            String adminRoleName = "ROLE_" + messageSource.getMessage("role.admin", null, locale);
            String adminRoleDescEs = messageSource.getMessage("role.admin.es.desc", null, locale);
            String adminRoleDescEn = messageSource.getMessage("role.admin.en.desc", null, locale);

            Optional<Rol> adminRoleopt = repoRol.findByRolNombre(adminRoleName);
            Rol adminRolefinal = new Rol();
            if (adminRoleopt.isPresent()) {
                adminRolefinal = adminRoleopt.get();
            }
            else
            {
                Rol adminRole = new Rol();
                adminRole.setRolNombre(adminRoleName);
                adminRole.setEsDesc(adminRoleDescEs);
                adminRole.setEnDesc(adminRoleDescEn);
                adminRole = repoRol.save(adminRole); // Guardar el rol si no existe
                adminRolefinal = adminRole;
            }

            Set<Rol> roles = new HashSet<>();
            roles.add(adminRolefinal);
            admin.setRoles(roles);

            // Guardar el Usuario
            repoUsuario.save(admin);
        }
    }
}
