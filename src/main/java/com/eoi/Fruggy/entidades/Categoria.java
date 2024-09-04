package com.eoi.Fruggy.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "categorias")
public class Categoria implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_es", length = 255)
    private String tipo_es;

    @Column(name = "tipo_en", length = 255)
    private String tipo_en;

    @JsonBackReference
    @OneToMany(mappedBy = "categoria")
    private Set<Subcategoria> subcategorias;

         //Constructor de la categoría con nombre en ambos idiomas.
    public Categoria(String categorias_es, String categorias_en) {
        this.tipo_es = categorias_es;
        this.tipo_en = categorias_en;
    }

    public String getTipo(String idioma) {
        switch (idioma.toLowerCase()) {
            case "en":
                return tipo_en;
            case "es":
            default:
                return tipo_es;
        }
    }
}
