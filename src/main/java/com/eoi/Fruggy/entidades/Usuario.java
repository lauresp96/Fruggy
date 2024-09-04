package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))

public class Usuario implements Serializable, UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Size(min = 5, max = 250)
    @Column(name = "email", length = 250)
    private String email;

    private Boolean active;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*-]).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula y un signo especial (!@#$&*-).")
    @Column(name = "password", length = 250)
    private String password;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres.")
    @Column(name = "telefono", length = 15)
    private String telefono;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_id", nullable = false)
    private Detalle detalle;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    // Relación con Imagen
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Imagen> imagenes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Cesta> cestas;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Rol rol : roles) {
            authorities.add(new SimpleGrantedAuthority(rol.getRolNombre()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Obtiene la cesta principal del usuario.
     * @return La cesta principal del usuario, o null si no existe.
     */
    public Cesta getCesta() {
        return cestas.stream()
                .filter(Cesta::getEsPrincipal) // Filtra la cesta que es principal
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todas las cestas del usuario.
     * @return Un conjunto de cestas del usuario.
     */
    public Set<Cesta> getCestas() { // todas las cestas
        return cestas;
    }

    /**
     * Busca una cesta por su ID.
     * @param cestaId El ID de la cesta a buscar.
     * @return La cesta correspondiente al ID, o null si no se encuentra.
     */
    public Cesta findCestaById(Long cestaId) {
        return cestas.stream()
                .filter(c -> c.getId().equals(cestaId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Añade una cesta al usuario si no supera el límite de 10 cestas.
     * @param cesta La cesta a añadir.
     * @throws IllegalStateException Si el usuario ya tiene el máximo de 10 cestas.
     */
    public void addCesta(Cesta cesta) {
        if (cestas.size() < 10) {
            cesta.setUsuario(this);
            cestas.add(cesta);
        } else {
            throw new IllegalStateException("El usuario ya tiene el máximo de 10 cestas.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id); // Usa el ID para comparar usuarios
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Usa el ID para calcular el hash
    }
}
