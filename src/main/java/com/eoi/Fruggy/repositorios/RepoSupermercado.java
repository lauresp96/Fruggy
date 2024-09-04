package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Supermercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoSupermercado extends JpaRepository<Supermercado, Integer> {
}
