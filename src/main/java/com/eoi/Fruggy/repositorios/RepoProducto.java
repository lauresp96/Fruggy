package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoProducto extends JpaRepository<Producto, Long> {
    Page<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.subcategoria.categoria.id = :categoriaId")
    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.marca LIKE %:marca%")
    Page<Producto> findByMarcaContainingIgnoreCase(String marca, Pageable pageable);

    List<Producto> findBySubcategoriaIdAndIdNot(Long subcategoriaId, Long productoId);
}
