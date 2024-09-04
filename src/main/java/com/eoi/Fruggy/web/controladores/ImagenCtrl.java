package com.eoi.Fruggy.web.controladores;


import com.eoi.Fruggy.entidades.Imagen;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Supermercado;
import com.eoi.Fruggy.servicios.SrvcImagen;
import com.eoi.Fruggy.servicios.SrvcProducto;
import com.eoi.Fruggy.servicios.SrvcSupermercado;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/imagenes")
public class ImagenCtrl {
    private final SrvcImagen imagenSrvc;
    private final SrvcSupermercado supermercadoSrvc;
    private final SrvcProducto productoSrvc;

    public ImagenCtrl(SrvcImagen imagenSrvc, SrvcSupermercado supermercadoSrvc, SrvcProducto productoSrvc) {
        this.imagenSrvc = imagenSrvc;
        this.supermercadoSrvc = supermercadoSrvc;
        this.productoSrvc = productoSrvc;
    }

    // obtener una imagen por su ruta
    @GetMapping("/{rutaImagen:.+}")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> obtenerImagen(@PathVariable String rutaImagen) throws MalformedURLException {
        System.out.println("Ruta de la imagen solicitada: " + rutaImagen);
        Path path = Paths.get("D:/ficheros/" + rutaImagen);

        // Verifica si el archivo existe y es legible
        if (!Files.exists(path) || !Files.isReadable(path)) {
            System.out.println("Archivo no encontrado o no legible.");
            return ResponseEntity.notFound().build();
        }

        try {
            org.springframework.core.io.Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Img en formati JPG
                    .body(resource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //guardar una imagen asociada a un supermercado
    @PostMapping("/guardar/{supermercadoId}")
    public ResponseEntity<Imagen> guardarImagen(@RequestParam("file") MultipartFile file, @PathVariable Long supermercadoId) throws Throwable {
        Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(supermercadoId)
                .orElseThrow(() -> new RuntimeException("Supermercado no encontrado"));

        // Genera un nombre único para la imagen
        String nombreArchivoUnico = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rutaArchivo = Paths.get("D:/ficheros/" + nombreArchivoUnico);

        try {
            Files.copy(file.getInputStream(), rutaArchivo);
            Imagen imagen = new Imagen();
            imagen.setNombreArchivo(file.getOriginalFilename()); // Guarda el nombre original
            imagen.setRutaImagen(nombreArchivoUnico); // Guarda el nombre único para acceder a la imagen
            imagen.setSupermercado(supermercado);
            imagen = imagenSrvc.guardar(imagen); // Guarda la imagen en la base de datos

            return ResponseEntity.status(HttpStatus.CREATED).body(imagen);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    //guardar una imagen asociada a un producto
    @PostMapping("/guardarProducto/{productoId}")
    public ResponseEntity<Imagen> guardarImagenProducto(@RequestParam("file") MultipartFile file, @PathVariable Long productoId) throws Throwable {
        Producto producto = productoSrvc.encuentraPorId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        String nombreArchivoUnico = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rutaArchivo = Paths.get("D:/ficheros/" + nombreArchivoUnico);

        try {
            Files.copy(file.getInputStream(), rutaArchivo);
            Imagen imagen = new Imagen();
            imagen.setNombreArchivo(file.getOriginalFilename());
            imagen.setRutaImagen(nombreArchivoUnico);
            imagen.setProductos(producto);
            imagen = imagenSrvc.guardar(imagen);

            return ResponseEntity.status(HttpStatus.CREATED).body(imagen);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }
}



