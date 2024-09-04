package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Cesta;
import com.eoi.Fruggy.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RepoCesta extends JpaRepository<Cesta, Long> {
    List<Cesta> findByUsuario(Usuario usuario);
}
