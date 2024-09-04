package com.eoi.Fruggy.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "precios")

public class Precio implements Serializable {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name ="fechaInicio")
    private LocalDateTime fechaInicio;

    @Column (name ="fechaFin")
    private LocalDateTime fechaFin;

    @Column (name ="activo" , nullable = false )
    private Boolean activo;

    @DecimalMin(value = "0.1", message = "El valor del precio debe ser mayor que 0.1.")
    @DecimalMax(value = "1000", message = "El valor del precio no puede ser mayor que 1000.")
    @Column(name = "valor")
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "productos_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "supermercados_id")
    private Supermercado supermercado;

    @OneToMany(mappedBy = "precio", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Cesta> cestaPrecios;



}
