package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.ProductoEnCesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoProductoEnCesta extends JpaRepository<ProductoEnCesta, Long> {
    Optional<ProductoEnCesta> findByCestaIdAndProductoId(Long cestaId, Long productoId);
}
