package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detalles",
        uniqueConstraints = @UniqueConstraint(columnNames = "nombreUsuario")) // Asegura la unicidad del nombreUsuario

public class Detalle implements Serializable {

    @Id
    @Column(name = "detalles_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres.")
    @Column(name = "nombreUsuario", length = 50)
    private String nombreUsuario;

    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres.")
    @Column(name = "nombre", length = 255)
    private String nombre;


    @Column(name = "apellido", length = 255)
    private String apellido;

    @Max(value = 99, message = "La edad no puede superar los 99 años.")
    @Column(name = "edad")
    private Integer edad;

    // Campos de dirección
    @Size(max = 250, message = "La calle no puede tener más de 250 caracteres.")
    @Column(name = "calle", length = 250)
    private String calle;

    @Size(max = 250, message = "El municipio no puede tener más de 250 caracteres.")
    @Column(name = "municipio", length = 250)
    private String municipio;

    @Size(max = 250, message = "El país no puede tener más de 250 caracteres.")
    @Column(name = "pais", length = 250)
    private String pais;

    @Min(value = 10000, message = "El código postal debe tener al menos 5 dígitos.")
    @Max(value = 99999, message = "El código postal no puede tener más de 5 dígitos.")
    @Column(name = "codigoPostal")
    private Integer codigopostal;

    @OneToOne(mappedBy = "detalle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Usuario usuario;

    @NotNull(message = "El género es obligatorio")
    @ManyToOne
    @JoinColumn(name = "genero_id", nullable = false)
    private Genero genero;

}
