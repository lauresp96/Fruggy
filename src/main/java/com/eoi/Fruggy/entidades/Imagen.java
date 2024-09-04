package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imagenes")
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    private String rutaImagen;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_id", unique = true)
    private Detalle detalle;

    @ManyToOne
    @JoinColumn(name = "supermercado_id")
    private Supermercado supermercado;

    @ManyToOne
    @JoinColumn(name = "productos_id")
    private Producto productos;
}
