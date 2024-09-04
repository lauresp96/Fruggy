package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.entidades.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public abstract class AbstractSrvc<E, ID, REPO extends JpaRepository<E, ID>> {

    private final REPO repo;

    protected AbstractSrvc(REPO repo) {
        this.repo = repo;
    }

    public List<E> buscarEntidades() {
        return this.repo.findAll();
    }

    public Set<E> buscarEntidadesSet() {
        Set<E> eSet = new HashSet<E>(this.repo.findAll());
        return eSet;
    }

    public Optional<E> encuentraPorId(ID id) {
        return this.repo.findById(id);
    }

    public Page<E> buscarTodos(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Set<E> buscarTodosSet() {
        Set<E> eSet = new HashSet<E>(this.repo.findAll());
        return eSet;
    }

    //Guardar
    public E guardar(E entidad) throws Exception {
        // Guardo en la base de datos
        E entidadGuardada = repo.save(entidad);
        // Traducir la entidad a DTO para devolver el DTO
        return entidadGuardada;
    }

    public void guardar(List<E> ents) throws Exception {
        Iterator<E> it = ents.iterator();

        // Mientras al iterador queda proximo juego
        while (it.hasNext()) {
            // Obtenemos la password de la entidad
            // Datos del usuario
            E e = it.next();
            repo.save(e);
        }
    }

    // Eliminar un registro
    public void eliminarPorId(ID id) {
        this.repo.deleteById(id);
    }

    // Obtener el repositorio
    public REPO getRepo() {
        return repo;
    }


}

