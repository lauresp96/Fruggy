package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa un tipo de descuento aplicable a productos en el sistema.
 * Cada tipo de descuento tiene un nombre en ambos idiomas, un periodo de validez y una serie de descuentos asociados.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipoDescuento")

public class TipoDescuento implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tipo_es", length = 255)
    private String tipo_es;

    @Column(name = "tipo_en", length = 255)
    private String tipo_en;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoDescuento")
    private Set<Descuento> descuentos;


    /**
     * Constructor para crear un nuevo tipo de descuento con valores iniciales.
     * @param tipoDescuento_es Descripción en español del tipo de descuento.
     * @param tipoDescuento_en Descripción en inglés del tipo de descuento.
     * @param activo Estado activo/inactivo del tipo de descuento.
     * @param fechaInicio Fecha de inicio del periodo de validez.
     * @param fechaFin Fecha de fin del periodo de validez.
     */
    public TipoDescuento(String tipoDescuento_es, String tipoDescuento_en, boolean b, LocalDate now, LocalDate localDate) {
        this.tipo_es = tipoDescuento_es;
        this.tipo_en = tipoDescuento_en;
        this.fechaInicio = now;
        this.fechaFin = localDate;
        this.activo = b;
    }
}
