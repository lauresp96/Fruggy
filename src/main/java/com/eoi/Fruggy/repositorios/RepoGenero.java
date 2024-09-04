package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoGenero extends JpaRepository<Genero, Integer> {
}
