package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoSubcategoria extends JpaRepository<Subcategoria, Long> {
    List<Subcategoria> findByCategoriaId(Long categoriaId);

}
