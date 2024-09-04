package com.eoi.Fruggy.web.controladores.admin;

import com.eoi.Fruggy.entidades.*;
import com.eoi.Fruggy.servicios.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

//Controlador para la gestión de productos en la administración.
@Controller
@RequestMapping("/admin/productos")
public class ADMINProductoCtrl {

    private static final Logger log = LoggerFactory.getLogger(ADMINProductoCtrl.class);
    private final SrvcProducto productosSrvc;
    private final SrvcPrecio precioSrvc;
    private final SrvcImagen imagenSrvc;
    private final SrvcSubcategoria subcategoriasSrvc;
    private final SrvcCategoria categoriasSrvc;
    private final SrvcSupermercado supermercadoSrvc;
    private final SrvcDescuento descuentoSrvc;
    private final SrvcTipodescuento tipoDescuentoSrvc;

    public ADMINProductoCtrl(SrvcProducto productosSrvc, SrvcPrecio precioSrvc, SrvcImagen imagenSrvc, SrvcSubcategoria subcategoriasSrvc, SrvcCategoria categoriasSrvc, SrvcSupermercado supermercadoSrvc, SrvcDescuento descuentoSrvc, SrvcTipodescuento tipoDescuentoSrvc) {
        this.productosSrvc = productosSrvc;
        this.precioSrvc = precioSrvc;
        this.imagenSrvc = imagenSrvc;
        this.subcategoriasSrvc = subcategoriasSrvc;
        this.categoriasSrvc = categoriasSrvc;
        this.supermercadoSrvc = supermercadoSrvc;
        this.descuentoSrvc = descuentoSrvc;
        this.tipoDescuentoSrvc = tipoDescuentoSrvc;
    }


    //  Muestra la lista de productos con paginación y ordenación.
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping
    public String listarProductos(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "asc") String sortDirection,
                                  @RequestParam(defaultValue = "") String search,
                                  Model model) {

        // Campo de ordenación y la dirección
        if ("precios.valor_desc".equals(sortField)) {
            sortField = "precios.valor";
            sortDirection = "desc";
        } else if ("precios.valor".equals(sortField)) {
            sortDirection = "asc";
        } else if ("id".equals(sortField)) {
            sortField = "id";
        }

        // Obtener la página de productos ordenada
        Page<Producto> paginaProductos = productosSrvc.obtenerProductosPaginados(page, size, sortField, sortDirection);
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        model.addAttribute("productos", paginaProductos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaProductos.getTotalPages());
        model.addAttribute("currentSortField", sortField);
        model.addAttribute("currentSortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equalsIgnoreCase("asc") ? "desc" : "asc");
        model.addAttribute("idioma", idioma);

        return "/admin/CRUD-Productos";
    }

    //  Formulario de creación de producto
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/agregar")
    public String mostrarFormularioCreacion(Model model) {
        Producto producto = new Producto();
        List<Subcategoria> subcategorias = subcategoriasSrvc.buscarEntidades();
        List<Categoria> categorias = categoriasSrvc.buscarEntidades();
        List<Supermercado> supermercados = supermercadoSrvc.buscarEntidades();
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        model.addAttribute("producto", producto);
        model.addAttribute("subcategorias", subcategorias);
        model.addAttribute("categorias", categorias);
        model.addAttribute("supermercados", supermercados);
        model.addAttribute("idioma", idioma);
        return "/admin/crear-producto";
    }

    // Guarda un nuevo producto.
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto,
                                  BindingResult result,
                                  @RequestParam("precio") String precioValor,
                                  @RequestParam("subcategoria.id") Long subcategoriaId,
                                  @RequestParam("supermercado.id") Long supermercadoId
    ) throws Exception {
        if (result.hasErrors()) {
            return "/admin/crear-producto";
        }

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        Optional<Subcategoria> subcategoria = subcategoriasSrvc.encuentraPorId(subcategoriaId);
        if (subcategoria.isPresent()) {
            producto.setSubcategoria(subcategoria.get());
        }

        Producto productoGuardado = productosSrvc.guardar(producto);

        // Procesar imagenes
        if (producto.getImagenesArchivo() != null) {
            for (MultipartFile file : producto.getImagenesArchivo()) {
                if (!file.isEmpty()) {
                    Imagen imagen = imagenSrvc.guardarImagenProducto(file, producto);
                    imagen.setProductos(producto);
                    imagenSrvc.guardar(imagen);
                }
            }
        }
        // Guardar Precio
        Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(supermercadoId).orElse(null);
        Precio precio = new Precio();
        precio.setProducto(productoGuardado);
        precio.setValor(Double.parseDouble(precioValor.replace(",", ".")));
        precio.setSupermercado(supermercado);
        precio.setActivo(true);
        precioSrvc.guardar(precio);

        return "redirect:/admin/productos";
    }

    // Mostrar formulario para editar un producto
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Optional<Producto> producto = productosSrvc.encuentraPorId(id);
        List<Subcategoria> subcategorias = subcategoriasSrvc.buscarEntidades();
        List<Categoria> categorias = categoriasSrvc.buscarEntidades();
        List<Supermercado> supermercados = supermercadoSrvc.buscarEntidades();
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();

        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
        }
        model.addAttribute("subcategorias", subcategorias);
        model.addAttribute("categorias", categorias);
        model.addAttribute("supermercados", supermercados);
        model.addAttribute("imagenes", imagenSrvc.buscarEntidades());
        model.addAttribute("idioma", idioma);
        return "/admin/crear-producto";
    }

    // post de edición de producto
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/editar/{id}")
    public String guardarEdicionProducto(@PathVariable Long id,
                                         @Valid @ModelAttribute("producto") Producto producto,
                                         BindingResult result,
                                         @RequestParam("precio") String precioValor,
                                         @RequestParam("subcategoria.id") Long subcategoriaId,
                                         @RequestParam("supermercado.id") Long supermercadoId,
                                         @RequestParam(value = "imagenesArchivo", required = false) MultipartFile imagenesArchivo
    ) throws Exception {
        if (result.hasErrors()) {
            return "/admin/crear-producto";
        }

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        Optional<Subcategoria> subcategoria = subcategoriasSrvc.encuentraPorId(subcategoriaId);
        if (subcategoria.isPresent()) {
            producto.setSubcategoria(subcategoria.get());
        }

        Producto productoGuardado = productosSrvc.guardar(producto);

        // Eliminar la imagen existente si la hay
        Set<Imagen> imagenesExistentes = imagenSrvc.buscarPorProducto(productoGuardado);
        for (Imagen imagen : imagenesExistentes) {
            imagenSrvc.eliminarPorId(imagen.getId());
        }
        // Procesa la nueva imagen subida
        if (imagenesArchivo != null && !imagenesArchivo.isEmpty()) {
            Imagen nuevaImagen = imagenSrvc.guardarImagenProducto(imagenesArchivo, productoGuardado);
            nuevaImagen.setProductos(productoGuardado);
            imagenSrvc.guardar(nuevaImagen);
        }

        // Actualiza precio
        Supermercado supermercado = (Supermercado) supermercadoSrvc.encuentraPorId(supermercadoId).orElse(null);
        Optional<Precio> precioExistenteOpt = precioSrvc.encuentraPorId(productoGuardado.getId());
        if (precioExistenteOpt.isPresent()) {
            Precio precioExistente = precioExistenteOpt.get();
            precioExistente.setValor(Double.parseDouble(precioValor.replace(",", ".")));
            precioExistente.setSupermercado(supermercado);
            precioSrvc.guardar(precioExistente);
        } else {
            Precio nuevoPrecio = new Precio();
            nuevoPrecio.setProducto(productoGuardado);
            nuevoPrecio.setValor(Double.parseDouble(precioValor.replace(",", ".")));
            nuevoPrecio.setSupermercado(supermercado);
            nuevoPrecio.setActivo(true);
            precioSrvc.guardar(nuevoPrecio);
        }

        return "redirect:/admin/productos";
    }

    // Eliminar un producto
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        Set<Precio> precios = precioSrvc.buscarTodosSet();
        Optional<Precio> precioExistenteOpt = precios.stream()
                .filter(precio -> precio.getProducto() != null && precio.getProducto().getId() == id)
                .findFirst();
        // Si hay un precio existente, eliminarlo
        precioExistenteOpt.ifPresent(precio -> precioSrvc.eliminarPorId(precio.getId()));
        productosSrvc.eliminarPorId(id);
        return "redirect:/admin/productos";
    }

                                      // DESCUENTOS

    // Mostrar formulario para agregar descuento a un producto
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @GetMapping("/descuento/{id}")
    public String mostrarFormularioDescuento(@PathVariable Long id, Model model) {
        Optional<Producto> producto = productosSrvc.encuentraPorId(id);
        List<TipoDescuento> tiposDescuento = tipoDescuentoSrvc.buscarEntidades();
        Locale locale = LocaleContextHolder.getLocale();
        String idioma = locale.getLanguage();
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            model.addAttribute("tiposDescuento", tiposDescuento);
            model.addAttribute("idioma", idioma);
        }
        return "/admin/agregar-descuento";
    }

    // Post para agregar descuento a un producto
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @PostMapping("/descuento/{id}")
    public String agregarDescuento(@PathVariable Long id,
                                   @RequestParam("tipoDescuentoId") Long tipoDescuentoId,
                                   @RequestParam("porcentaje") Double porcentaje,
                                   @RequestParam("fechaInicio") String fechaInicio,
                                   @RequestParam("fechaFin") String fechaFin) throws Exception {


        Optional<Producto> productoOpt = productosSrvc.encuentraPorId(id);
        Optional<TipoDescuento> tipoDescuentoOpt = tipoDescuentoSrvc.encuentraPorId(tipoDescuentoId);

        if (productoOpt.isPresent() && tipoDescuentoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            TipoDescuento tipoDescuento = tipoDescuentoOpt.get();

            Descuento descuento = new Descuento();
            descuento.setProducto(producto);
            descuento.setTipoDescuento(tipoDescuento);
            descuento.setPorcentaje(porcentaje);
            descuento.setFechaInicio(LocalDate.parse(fechaInicio));
            descuento.setFechaFin(LocalDate.parse(fechaFin));
            descuento.setActivo(true);

            descuentoSrvc.guardar(descuento);

            // Aplicar el descuento al precio actual
            Set<Precio> precios = producto.getPrecios();
            for (Precio precio : precios) {
                Double nuevoValor = precio.getValor() * (1 - porcentaje / 100);
                precio.setValor(nuevoValor);
                precioSrvc.guardar(precio);
            }
        }
        return "redirect:/admin/productos";
    }
}

