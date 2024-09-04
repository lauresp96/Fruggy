package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoDetalle extends JpaRepository<Detalle, Long> {
    Optional<Detalle> findByNombreUsuario(String nombreUsuario);
    Optional<Detalle> findByNombreUsuarioAndIdNot(String nombreUsuario, Long id);
}
