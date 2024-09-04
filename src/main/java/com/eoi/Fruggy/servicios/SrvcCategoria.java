package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Categoria;
import com.eoi.Fruggy.entidades.Rol;
import com.eoi.Fruggy.repositorios.RepoCategoria;
import com.eoi.Fruggy.repositorios.RepoProducto;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@Service
public class SrvcCategoria extends AbstractSrvc<Categoria, Long, RepoCategoria> {

    @Autowired
    protected SrvcCategoria(RepoCategoria repoCategoria) {
        super(repoCategoria);
    }
    /**
     * Obtiene todas las categorías disponibles.
     * @param idioma El código del idioma para la localización.
     * @return Lista de categorías.
     */
    @Autowired
    private LocaleResolver localeResolver;
    public List<Categoria> getCategorias(String idioma) {
        Locale locale = new Locale(idioma);
        return getRepo().findAll();
    }

}
