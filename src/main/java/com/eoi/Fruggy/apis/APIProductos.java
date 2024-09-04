package com.eoi.Fruggy.apis;

import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.servicios.SrvcProducto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class APIProductos {

    private SrvcProducto productoSrvc;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoSrvc.buscarEntidades();
        return ResponseEntity.ok(productos);
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Producto>> obtenerProductoPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoSrvc.encuentraPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) throws Exception {
        Producto nuevoProducto = productoSrvc.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // Actualizar un producto existente
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoSrvc.encuentraPorId(id);
        if (productoExistente.isPresent()) {
            producto.setId(id);
            try {
                Producto productoActualizado = productoSrvc.guardar(producto);
                return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Optional <Producto> productoExistente = productoSrvc.encuentraPorId(id);
        if (productoExistente.isPresent()) {
         productoSrvc.eliminarPorId(id);
         return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
