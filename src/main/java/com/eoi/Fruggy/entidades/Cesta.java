package com.eoi.Fruggy.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cestas")

public class Cesta implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    @Column(name = "nombre", length = 100)

    private String nombre;

    @Column(name = "fecha")
    private LocalDateTime fecha;
    @Transient
    private String fechaFormateada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "precio_id")
    private Precio precio;

    @OneToMany(mappedBy = "cesta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoEnCesta> productosEnCesta = new ArrayList<>();

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    // Método para agregar productos
    public void addProducto(Producto producto, Integer cantidad, String comentario) {
        // Busca el producto en la cesta existente
        ProductoEnCesta productoEnCesta = productosEnCesta.stream()
                .filter(p -> p.getProducto().equals(producto))
                .findFirst()
                .orElse(null);

        if (productoEnCesta != null) {
            // Actualiza la cantidad si el producto ya está en la cesta
            productoEnCesta.setCantidad(productoEnCesta.getCantidad() + cantidad);
        } else {
            // Añade el nuevo producto a la cesta
            productoEnCesta = new ProductoEnCesta();
            productoEnCesta.setProducto(producto);
            productoEnCesta.setCantidad(cantidad);
            productoEnCesta.setCesta(this);
            productosEnCesta.add(productoEnCesta);
        }
    }

    /**
     * Obtiene el conjunto de productos únicos en la cesta.
     * @return Un conjunto de productos únicos.
     */
    public Set<Producto> getProductos() {
        return productosEnCesta.stream()
                .map(ProductoEnCesta::getProducto)
                .collect(Collectors.toSet());
    }

    /**
     * Calcula el total de la cesta en función de los precios de los productos y sus cantidades.
     * @return El total calculado de la cesta.
     */
    @Transient // No se almacena en la base de datos
    public Double getTotal() {
        return productosEnCesta.stream()
                .mapToDouble(p -> p.getProducto().getPrecios().stream().findFirst()
                        .map(precio -> precio.getValor() * p.getCantidad())
                        .orElse(0.0))
                .sum();
    }
}
