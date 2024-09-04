package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "valoracionesProductos")

public class ValoracionProducto implements Serializable {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "comentario", length = 255)
    private String comentario;

    @Min(value = 0, message = "La nota mínima es 0")
    @Max(value = 5, message = "La nota máxima es 5")
    @Column(name = "nota")
    private Double nota;


    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne // Hace falta para que cada usuario pueda dejar una valoracion de producto.
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
