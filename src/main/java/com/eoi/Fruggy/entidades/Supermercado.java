package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "supermercados")

public class Supermercado implements Serializable {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank(message = "El nombre del supermercado es obligatorio")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    @Column (name ="nombreSuper",length = 255)
    private String nombreSuper;

    @URL(message = "La URL debe ser válida")
    @Size(max = 255, message = "La URL no puede tener más de 255 caracteres")
    @Column (name ="url",length = 255)
    private String url;

    @Size(max = 255, message = "El horario no puede tener más de 255 caracteres")
    @Column (name ="horario",length = 255)
    private String horario;

    // Campos de dirección

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 250, message = "La calle no puede tener más de 250 caracteres")
    @Column(name = "calle", length = 250)
    private String calle;

    @NotBlank(message = "El municipio es obligatorio")
    @Size(max = 250, message = "El municipio no puede tener más de 250 caracteres")
    @Column(name = "municipio", length = 250)
    private String municipio;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 250, message = "El país no puede tener más de 250 caracteres")
    @Column(name = "pais", length = 250)
    private String pais;

    @Min(value = 1000, message = "El código postal debe ser mayor o igual a 1000")
    @Max(value = 99999, message = "El código postal debe ser menor o igual a 99999")
    @Column(name = "codigoPostal")
    private Integer codigopostal;

    @OneToMany(mappedBy = "supermercado")
    private Set<Precio> precios;

    @OneToMany(mappedBy = "supermercado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ValoracionSupermercado> valoraciones;

    // Relación con Imagen
    @OneToMany(mappedBy = "supermercado", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes;

    @Transient // No se almacena en la base de datos
    private List<MultipartFile> imagenesArchivo;

    @Transient // Este campo no se guardará en la base de datos. Es para coger nota media de Valoraciones
    private Double notaMedia;
}
