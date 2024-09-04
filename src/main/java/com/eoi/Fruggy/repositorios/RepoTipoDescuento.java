package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.TipoDescuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoTipoDescuento extends JpaRepository<TipoDescuento, Long> {
    @Query("SELECT t FROM TipoDescuento t WHERE t.tipo_es = :tipo_es")
    TipoDescuento findByTipo_esJPQL(@Param("tipo_es") String tipo_es);
}
