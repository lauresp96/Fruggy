package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoCategoria extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c WHERE c.tipo_es = ?1")
    Optional<Categoria> findByTipoes(String tipoes);
}
