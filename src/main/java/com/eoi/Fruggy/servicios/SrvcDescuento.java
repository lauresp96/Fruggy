package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Descuento;
import com.eoi.Fruggy.repositorios.RepoDescuento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SrvcDescuento extends AbstractSrvc <Descuento, Long, RepoDescuento>{
   @Autowired
   private RepoDescuento repoDescuento;
    protected SrvcDescuento(RepoDescuento repoDescuento) {
        super(repoDescuento);
    }

}
