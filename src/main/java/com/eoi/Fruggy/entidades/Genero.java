package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "generos")

public class Genero implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "es_desc", nullable = false)
    private String esDesc;

    @Column(name = "en_desc", nullable = false)
    private String enDesc;

    @OneToMany(mappedBy = "genero")
    private Set<Detalle> detalles;

    /**
     * Constructor para crear un nuevo género con descripciones en ambos idiomas.
     * @param es Descripción en español.
     * @param en Descripción en inglés.
     */
    public Genero(String es, String en) {
        this.esDesc = es;
        this.enDesc = en;
        this.detalles = new HashSet<>();
    }
}
