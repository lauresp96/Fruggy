package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Precio;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.repositorios.RepoPrecio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SrvcPrecio extends AbstractSrvc<Precio, Long, RepoPrecio> {

    protected SrvcPrecio(RepoPrecio repoPrecio) {
        super(repoPrecio);
    }

    public Precio findActivePriceByProduct(Long productoId) {
        return (Precio) getRepo().findFirstByProductoIdAndActivoTrue(productoId);
    }
}
