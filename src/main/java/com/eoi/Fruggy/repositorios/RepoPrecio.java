package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Precio;
import com.eoi.Fruggy.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoPrecio extends JpaRepository<Precio, Long> {
   List <Precio>  findFirstByProductoIdAndActivoTrue(Long productoId);
}
