package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.repositorios.RepoRol;
import com.eoi.Fruggy.repositorios.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SrvcRol extends AbstractSrvc<Rol, Long, RepoRol> {

    @Autowired
    private RepoRol repoRol;
    @Autowired
    private RepoUsuario repoUsuario;

    protected SrvcRol(RepoRol repoRol) {
        super(repoRol);
    }


    @Override
    public List<Rol> buscarEntidades() {
        List<Rol> roles = super.buscarEntidades();
        return roles;
    }

    public Optional<Rol> buscarRolPorNombre(String nombre) {
        return repoRol.findByRolNombre(nombre);
    }
}
