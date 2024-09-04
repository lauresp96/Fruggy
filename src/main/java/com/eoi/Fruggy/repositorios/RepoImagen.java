package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Imagen;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Supermercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RepoImagen extends JpaRepository<Imagen, Long> {
    Set<Imagen> findBySupermercado(Supermercado supermercado);
    Set<Imagen> findByProductos(Producto productos);
}
