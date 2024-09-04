package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.repositorios.RepoGenero;
import org.springframework.stereotype.Service;

@Service
public class SrvcGenero extends AbstractSrvc {
    protected SrvcGenero(RepoGenero repoGenero) {
        super(repoGenero);
    }
}
