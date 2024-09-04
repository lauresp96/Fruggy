package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Detalle;
import com.eoi.Fruggy.repositorios.RepoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SrvcDetalle extends AbstractSrvc<Detalle, Long, RepoDetalle> {
    @Autowired
    private RepoDetalle repoDetalle;

    protected SrvcDetalle(RepoDetalle repoDetalle) {
        super(repoDetalle);
    }


    /**
     * Guarda o actualiza un Detalle en la base de datos.
     * @param detalle El detalle a guardar o actualizar.
     * @return El detalle guardado.
     * @throws IllegalStateException Si el nombre de usuario ya está en uso.
     */
    public Detalle merge(Detalle detalle) {
        try {
            return repoDetalle.save(detalle);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("El nombre de usuario ya está en uso.");
        }
    }

    /**
     * Busca un Detalle por nombre de usuario excluyendo el ID especificado.
     * @param nombreUsuario El nombre de usuario a buscar.
     * @param id El ID que se debe excluir de la búsqueda.
     * @return Un Optional que contiene el Detalle si se encuentra, o vacío si no.
     */
    public Optional<Detalle> findByNombreUsuarioExcludingId(String nombreUsuario, Long id) {
        return repoDetalle.findByNombreUsuarioAndIdNot(nombreUsuario, id);
    }
}
