package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Cesta;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.ProductoEnCesta;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.repositorios.RepoCesta;
import com.eoi.Fruggy.repositorios.RepoProducto;
import com.eoi.Fruggy.repositorios.RepoUsuario;
import com.eoi.Fruggy.repositorios.RepoValProducto;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
public class SrvcProducto extends AbstractSrvc<Producto, Long, RepoProducto> {

    @Autowired
    private RepoProducto repoProducto;
    @Autowired
    RepoUsuario repoUsuario;
    @Autowired
    RepoCesta repoCesta;

    private final SrvcValProducto valProductoSrvc;

    protected SrvcProducto(RepoProducto repoProducto, SrvcValProducto valProductoSrvc) {
        super(repoProducto);
        this.valProductoSrvc = valProductoSrvc;
    }

    public Page<Producto> obtenerProductosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repoProducto.findAll(pageable);
    }
    // FILTROS

    // Productos por precio menor
    public Page<Producto> obtenerProductosPorPrecioAscendente(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("precios.valor").ascending());
        return repoProducto.findAll(pageable);
    }

    // Productos por precio mayor
    public Page<Producto> obtenerProductosPorPrecioDescendente(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("precios.valor").descending());
        return repoProducto.findAll(pageable);
    }

    // Productos por fecha de creación (novedades)
    public Page<Producto> obtenerProductosPorNovedades(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repoProducto.findAll(pageable);
    }

    // Productos por mejor puntuación
    public Page<Producto> obtenerProductosPorMejorPuntuacion(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productosPaginados = repoProducto.findAll(pageable);

        // Calcular la nota media para cada producto
        productosPaginados.getContent().forEach(producto -> {
            Double notaMedia = valProductoSrvc.calcularNotaMedia(producto.getId());
            producto.setNotaMedia(notaMedia);
        });

        // Ordenar en memoria por notaMedia
        List<Producto> productosOrdenados = productosPaginados.getContent().stream()
                .sorted(Comparator.comparing(Producto::getNotaMedia, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        // Convertir la lista ordenada en una Page
        return new PageImpl<>(productosOrdenados, pageable, productosPaginados.getTotalElements());
    }

    /// PARA ADMIN PRODUCTOS
    public Page<Producto> obtenerProductosPaginados(int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        if ("precios.valor_desc".equals(sortField)) {
            sortField = "precios.valor";
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, direction, sortField);
        return repoProducto.findAll(pageable);
    }

    public Page<Producto> buscarProductosPorNombre(String nombreProducto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = repoProducto.findByNombreProductoContainingIgnoreCase(nombreProducto, pageable);

        // Si no hay productos, página vacía
        if (productos.isEmpty()) {
            return Page.empty(pageable); // Devolver una página vacía
        }

        return productos;
    }

    public Page<Producto> buscarProductosPorCategoria(Long categoriaId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repoProducto.findByCategoriaId(categoriaId, pageable);
    }

    public Page<Producto> buscarProductosPorMarca(String marca, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = repoProducto.findByMarcaContainingIgnoreCase(marca, pageable);
        return productos.isEmpty() ? Page.empty(pageable) : productos;
    }

    public List<Producto> buscarProductosSimilares(Long subcategoriaId, Long productoId) {
        // Lógica para buscar productos similares en la misma subcategoría, excluyendo el producto actual
        return repoProducto.findBySubcategoriaIdAndIdNot(subcategoriaId, productoId);
    }
}
