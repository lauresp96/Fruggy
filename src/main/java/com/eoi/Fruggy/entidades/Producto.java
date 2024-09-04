package com.eoi.Fruggy.entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Utiliza Long en lugar de long

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(max = 255, message = "El nombre del producto no puede tener más de 255 caracteres.")
    @Column(name = "nombreProducto", length = 255)
    private String nombreProducto;

    @NotBlank(message = "La marca es obligatoria.")
    @Size(max = 45, message = "La marca no puede tener más de 45 caracteres.")
    @Column(name = "marca", length = 45)
    private String marca;

    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres.")
    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @JsonManagedReference
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Precio> precios = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    private Subcategoria subcategoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Descuento> descuentos = new HashSet<>();

    @OneToMany(mappedBy = "productos", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Imagen> imagenes = new HashSet<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ValoracionProducto> valoraciones = new HashSet<>();

    @Transient // No se almacena en la base de datos
    private List<MultipartFile> imagenesArchivo;

    @Transient // Este campo no se guardará en la base de datos. Es para coger nota media de Valoraciones
    private Double notaMedia;

}
