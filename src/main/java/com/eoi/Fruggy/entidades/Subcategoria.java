package com.eoi.Fruggy.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Representa una subcategoría de productos en el sistema.
 * Cada subcategoría está asociada a una categoría y puede tener múltiples productos.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subcategorias")

public class Subcategoria implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_es", length = 255)
    private String tipo_es;

    @Column(name = "tipo_en", length = 255)
    private String tipo_en;

    @JsonBackReference
    @OneToMany(mappedBy = "subcategoria")
    private Set<Producto> productos;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


    /**
     * Constructor para crear una nueva subcategoría con descripciones en ambos idiomas y una categoría asociada.
     * @param subcategorias_es Descripción en español.
     * @param subcategorias_en Descripción en inglés.
     * @param categoria Categoría asociada a la subcategoría.
     */
    public Subcategoria(String subcategorias_es, String subcategorias_en, Categoria categoria) {
        this.tipo_es = subcategorias_es;
        this.tipo_en = subcategorias_en;
        this.categoria = categoria;
    }


}
