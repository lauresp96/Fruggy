package com.eoi.Fruggy.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos_descuentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDescuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "descuento_id")
    private Descuento descuento;

}
