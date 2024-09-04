package com.eoi.Fruggy.servicios;


import com.eoi.Fruggy.entidades.TipoDescuento;
import com.eoi.Fruggy.repositorios.RepoTipoDescuento;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SrvcTipodescuento extends AbstractSrvc {

    private final RepoTipoDescuento repoTipoDescuento;

    public SrvcTipodescuento(RepoTipoDescuento repoTipoDescuento) {
        super(repoTipoDescuento);
        this.repoTipoDescuento = repoTipoDescuento;
    }

    public Optional<TipoDescuento> encuentraPorId(Long id) {
        return repoTipoDescuento.findById(id);
    }
}
