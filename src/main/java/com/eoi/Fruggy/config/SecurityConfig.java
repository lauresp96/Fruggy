package com.eoi.Fruggy.config;

import com.eoi.Fruggy.servicios.UsuarioSecurityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

 // Esta clase define las reglas de acceso y autenticación para diferentes rutas de la aplicación.
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
// Activa la seguridad basada en anotaciones a nivel de método, permitiendo el uso de anotaciones como @PreAuthorize, @Secured, y @RolesAllowed.
@Configuration
public class SecurityConfig {

    @Autowired
    @Qualifier("usuarioSecurityImpl")
    private UsuarioSecurityImpl userDetailsService;// Servicio de usuario personalizado para la autenticación.

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    //Define la configuración de seguridad para el perfil de desarrollo.
    @Bean
    @Profile("desarrollo")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable()) // Desactiva la protección CSRF y CORS.
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/fonts/**").permitAll()
                        .requestMatchers("/lib/**").permitAll()
                        .requestMatchers("/scss/**").permitAll()
                        .requestMatchers("/imagenes/**").permitAll()
                        .requestMatchers("/language/**").permitAll()
                        .requestMatchers("/inicio").permitAll() // Permitir acceso a /inicio
                        .requestMatchers("/registro", "/registro/guardar", "/usuario/administracion/**").permitAll()
                        .requestMatchers("/productos/**").permitAll() // Permitir acceso a /productos
                        .requestMatchers("/supermercados/**").permitAll() // Permitir acceso a /supermercados
                        .requestMatchers("/admin/**").hasAuthority("ROLE_Administrador")
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated() // Asegura que cualquier otra petición requiera autenticación
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/accessDenied") // Página de acceso denegado
                );

        return http.build();
    }

    // Define la configuración de seguridad para el perfil local.
    @Bean
    @Profile("local")
    public SecurityFilterChain securityFilterChainLocal(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/fonts/**").permitAll()
                        .requestMatchers("/lib/**").permitAll()
                        .requestMatchers("/scss/**").permitAll()
                        .requestMatchers("/imagenes/**").permitAll()
                        .requestMatchers("/inicio").permitAll() // Permitir acceso a /inicio
                        .requestMatchers("/registro", "/registro/guardar", "/usuario/administracion/**").permitAll()
                        .requestMatchers("/productos").permitAll() // Permitir acceso a /productos
                        .requestMatchers("/supermercados").permitAll() // Permitir acceso a /supermercados
                        .requestMatchers("/").permitAll() // Permitir acceso a la raíz
                        .requestMatchers("/admin/**").hasRole("Administrator")
                        .anyRequest().authenticated() // Asegura que cualquier otra petición requiera autenticación
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/accessDenied") // Página de acceso denegado
                );

        return http.build();
    }

    // Para autenticar a los usuarios. Crea un proveedor de autenticación basado en el servicio de detalles de usuario.
    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("ROLE_"); // Incluye el prefijo "ROLE_" para los roles de usuario.
    }
}
