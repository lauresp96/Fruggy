package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.entidades.ValoracionProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoValProducto extends JpaRepository<ValoracionProducto, Long> {
    List<ValoracionProducto> findByProductoId(Long productoId);
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);

    @Modifying
    @Query("UPDATE ValoracionProducto v SET v.usuario.id = NULL WHERE v.usuario.id = :usuarioId")
    void updateUsuarioIdToNull(@Param("usuarioId") Long usuarioId);
}
