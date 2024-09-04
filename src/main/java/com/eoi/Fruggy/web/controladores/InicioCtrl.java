package com.eoi.Fruggy.web.controladores;

import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.servicios.SrvcProducto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class InicioCtrl {
    private final SrvcProducto productosSrvc;

    public InicioCtrl(SrvcProducto productosSrvc) {
        this.productosSrvc = productosSrvc;
    }


    // Método para mostrar página de inicio
    @GetMapping
    public String mostrarInicio(Model model) {
        // Obtener los productos nuevos
        Page<Producto> productosNuevos = productosSrvc.obtenerProductosPorNovedades(0, 10);
        // Obtener productos con el precio más bajo
        List<Producto> productosPrecioBajo = productosSrvc.obtenerProductosPorPrecioAscendente(0, 10).getContent();
        model.addAttribute("productosNuevos", productosNuevos.getContent());
        model.addAttribute("productosPrecioBajo", productosPrecioBajo);

        return "inicio";
    }
}

