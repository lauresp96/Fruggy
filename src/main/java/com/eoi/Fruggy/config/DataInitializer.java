package com.eoi.Fruggy.config;

import com.eoi.Fruggy.entidades.*;
import com.eoi.Fruggy.repositorios.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class DataInitializer {

    private final RepoRol repoRol;
    private final RepoGenero repoGenero;
    private final RepoCategoria repoCategoria;
    private final RepoSubcategoria repoSubcategoria;
    private final RepoTipoDescuento repoTipoDescuento;
    private final RepoDescuento repoDescuento;
    private final MessageSource messageSource;


    @Autowired
    public DataInitializer(RepoRol repoRol, RepoGenero repoGenero, RepoCategoria repoCategoria,
                           RepoSubcategoria repoSubcategoria, RepoTipoDescuento repoTipoDescuento, RepoDescuento repoDescuento, MessageSource messageSource) {
        this.repoRol = repoRol;
        this.repoGenero = repoGenero;
        this.repoCategoria = repoCategoria;
        this.repoSubcategoria = repoSubcategoria;
        this.repoTipoDescuento = repoTipoDescuento;
        this.repoDescuento = repoDescuento;
        this.messageSource = messageSource;
    }

    //Este método inicializa datos básicos en la base de datos si no existen.
    @PostConstruct
    public void init() {
        Locale locale = new Locale("es");
        // Inicialización de roles
        if (repoRol.count() == 0) {
            repoRol.save(new Rol("ROLE_" + messageSource.getMessage("role.admin", null, locale),
                            messageSource.getMessage("role.admin.es.desc", null, locale),
                            messageSource.getMessage("role.admin.en.desc", null, locale)
                    )
            );
            repoRol.save(new Rol("ROLE_" + messageSource.getMessage("role.user", null, locale),
                            messageSource.getMessage("role.user.es.desc", null, locale),
                            messageSource.getMessage("role.user.en.desc", null, locale)
                    )
            );

        }

        // Inicialización de géneros
        if (repoGenero.count() == 0) {
            repoGenero.save(new Genero(
                    messageSource.getMessage("gender.es.male", null, locale),
                    messageSource.getMessage("gender.en.male", null, locale)));
            repoGenero.save(new Genero(
                    messageSource.getMessage("gender.es.female", null, locale),
                    messageSource.getMessage("gender.en.female", null, locale)));
            repoGenero.save(new Genero(
                    messageSource.getMessage("gender.es.other", null, locale),
                    messageSource.getMessage("gender.en.other", null, locale)));
        }

        try {
            // Cargar categorías
            if (repoCategoria.count() == 0) {
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.snacks", null, locale),
                        messageSource.getMessage("category.en.snacks", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.household", null, locale),
                        messageSource.getMessage("category.en.household", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.babies_kids", null, locale),
                        messageSource.getMessage("category.en.babies_kids", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.drinks", null, locale),
                        messageSource.getMessage("category.en.drinks", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.coffee_tea", null, locale),
                        messageSource.getMessage("category.en.coffee_tea", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.chocolates_sweets", null, locale),
                        messageSource.getMessage("category.en.chocolates_sweets", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.canned_goods", null, locale),
                        messageSource.getMessage("category.en.canned_goods", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.dietary", null, locale),
                        messageSource.getMessage("category.en.dietary", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.drugstore", null, locale),
                        messageSource.getMessage("category.en.drugstore", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.fresh_meats", null, locale),
                        messageSource.getMessage("category.en.fresh_meats", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.dairy_eggs", null, locale),
                        messageSource.getMessage("category.en.dairy_eggs", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.pets", null, locale),
                        messageSource.getMessage("category.en.pets", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.leisure_culture", null, locale),
                        messageSource.getMessage("category.en.leisure_culture", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.bakery", null, locale),
                        messageSource.getMessage("category.en.bakery", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.pasta_rice_legumes", null, locale),
                        messageSource.getMessage("category.en.pasta_rice_legumes", null, locale)));
                repoCategoria.save(new Categoria(
                        messageSource.getMessage("category.es.perfumery_pharmacy", null, locale),
                        messageSource.getMessage("category.en.perfumery_pharmacy", null, locale)));
            }
            List<Categoria> categorias = repoCategoria.findAll();

            // Cargar subcategorías
            if (repoSubcategoria.count() == 0) {
                for (Categoria categoria : categorias) {
                    switch (categoria.getTipo_es()) {
                        case "Aperitivos":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.chips", null, locale),
                                    messageSource.getMessage("subcategory.en.chips", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.snacks", null, locale),
                                    messageSource.getMessage("subcategory.en.snacks", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.nuts", null, locale),
                                    messageSource.getMessage("subcategory.en.nuts", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.chocolates", null, locale),
                                    messageSource.getMessage("subcategory.en.chocolates", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.salty_cookies", null, locale),
                                    messageSource.getMessage("subcategory.en.salty_cookies", null, locale),
                                    categoria));
                            break;
                        case "Bazar & Casa":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.basket", null, locale),
                                    messageSource.getMessage("subcategory.en.basket", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.kitchen_utensils", null, locale),
                                    messageSource.getMessage("subcategory.en.kitchen_utensils", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.storage", null, locale),
                                    messageSource.getMessage("subcategory.en.storage", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.decoration", null, locale),
                                    messageSource.getMessage("subcategory.en.decoration", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cleaning", null, locale),
                                    messageSource.getMessage("subcategory.en.cleaning", null, locale),
                                    categoria));
                            break;
                        case "Bebés & Niños":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.diapers", null, locale),
                                    messageSource.getMessage("subcategory.en.diapers", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.wipes", null, locale),
                                    messageSource.getMessage("subcategory.en.wipes", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.baby_clothes", null, locale),
                                    messageSource.getMessage("subcategory.en.baby_clothes", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.toys", null, locale),
                                    messageSource.getMessage("subcategory.en.toys", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.infant_food", null, locale),
                                    messageSource.getMessage("subcategory.en.infant_food", null, locale),
                                    categoria));
                            break;
                        case "Bebidas":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.soda", null, locale),
                                    messageSource.getMessage("subcategory.en.soda", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.juice", null, locale),
                                    messageSource.getMessage("subcategory.en.juice", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.energy_drink", null, locale),
                                    messageSource.getMessage("subcategory.en.energy_drink", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.sports_drink", null, locale),
                                    messageSource.getMessage("subcategory.en.sports_drink", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.mineral_water", null, locale),
                                    messageSource.getMessage("subcategory.en.mineral_water", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.vegetable_drink", null, locale),
                                    messageSource.getMessage("subcategory.en.vegetable_drink", null, locale),
                                    categoria));
                            break;
                        case "Café e Infusiones":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.coffee_beans", null, locale),
                                    messageSource.getMessage("subcategory.en.coffee_beans", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.ground_coffee", null, locale),
                                    messageSource.getMessage("subcategory.en.ground_coffee", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.relaxing_tea", null, locale),
                                    messageSource.getMessage("subcategory.en.relaxing_tea", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.fruit_tea", null, locale),
                                    messageSource.getMessage("subcategory.en.fruit_tea", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.instant_coffee", null, locale),
                                    messageSource.getMessage("subcategory.en.instant_coffee", null, locale),
                                    categoria));
                            break;
                        case "Chocolates & Dulces":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.milk_chocolate", null, locale),
                                    messageSource.getMessage("subcategory.en.milk_chocolate", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.dark_chocolate", null, locale),
                                    messageSource.getMessage("subcategory.en.dark_chocolate", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.bonbons", null, locale),
                                    messageSource.getMessage("subcategory.en.bonbons", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.sugar_free_chocolates", null, locale),
                                    messageSource.getMessage("subcategory.en.sugar_free_chocolates", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.candies", null, locale),
                                    messageSource.getMessage("subcategory.en.candies", null, locale),
                                    categoria));
                            break;
                        case "Conservas, Aceite & Condimentos":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.olive_oil", null, locale),
                                    messageSource.getMessage("subcategory.en.olive_oil", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.sauces", null, locale),
                                    messageSource.getMessage("subcategory.en.sauces", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.canned_fish", null, locale),
                                    messageSource.getMessage("subcategory.en.canned_fish", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.spices", null, locale),
                                    messageSource.getMessage("subcategory.en.spices", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.vinegar", null, locale),
                                    messageSource.getMessage("subcategory.en.vinegar", null, locale),
                                    categoria));
                            break;
                        case "Dietéticos":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cereals", null, locale),
                                    messageSource.getMessage("subcategory.en.cereals", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.gluten_free_products", null, locale),
                                    messageSource.getMessage("subcategory.en.gluten_free_products", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.nutritional_supplements", null, locale),
                                    messageSource.getMessage("subcategory.en.nutritional_supplements", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.healthy_snacks", null, locale),
                                    messageSource.getMessage("subcategory.en.healthy_snacks", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.energy_bars", null, locale),
                                    messageSource.getMessage("subcategory.en.energy_bars", null, locale),
                                    categoria));
                            break;
                        case "Droguería":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.multi_use_cleaners", null, locale),
                                    messageSource.getMessage("subcategory.en.multi_use_cleaners", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.detergent", null, locale),
                                    messageSource.getMessage("subcategory.en.detergent", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.bathroom_cleaners", null, locale),
                                    messageSource.getMessage("subcategory.en.bathroom_cleaners", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.sponges", null, locale),
                                    messageSource.getMessage("subcategory.en.sponges", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.broom", null, locale),
                                    messageSource.getMessage("subcategory.en.broom", null, locale),
                                    categoria));
                            break;
                        case "Frescos & Charcutería":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.pork_sausage", null, locale),
                                    messageSource.getMessage("subcategory.en.pork_sausage", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cooked_ham", null, locale),
                                    messageSource.getMessage("subcategory.en.cooked_ham", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cooked_chicken", null, locale),
                                    messageSource.getMessage("subcategory.en.cooked_chicken", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.bacon", null, locale),
                                    messageSource.getMessage("subcategory.en.bacon", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cheese", null, locale),
                                    messageSource.getMessage("subcategory.en.cheese", null, locale),
                                    categoria));
                            break;
                        case "Lácteos & Huevos":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.lactose_free_milk", null, locale),
                                    messageSource.getMessage("subcategory.en.lactose_free_milk", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.fresh_milk", null, locale),
                                    messageSource.getMessage("subcategory.en.fresh_milk", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.eggs", null, locale),
                                    messageSource.getMessage("subcategory.en.eggs", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.yogurt", null, locale),
                                    messageSource.getMessage("subcategory.en.yogurt", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.butter", null, locale),
                                    messageSource.getMessage("subcategory.en.butter", null, locale),
                                    categoria));
                            break;
                        case "Mascotas":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.dog_food", null, locale),
                                    messageSource.getMessage("subcategory.en.dog_food", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cat_food", null, locale),
                                    messageSource.getMessage("subcategory.en.cat_food", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.bird_food", null, locale),
                                    messageSource.getMessage("subcategory.en.bird_food", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.fish_food", null, locale),
                                    messageSource.getMessage("subcategory.en.fish_food", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.dog_toys", null, locale),
                                    messageSource.getMessage("subcategory.en.dog_toys", null, locale),
                                    categoria));
                            break;
                        case "Ocio y Cultura":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.books", null, locale),
                                    messageSource.getMessage("subcategory.en.books", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.magazines", null, locale),
                                    messageSource.getMessage("subcategory.en.magazines", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.video_games", null, locale),
                                    messageSource.getMessage("subcategory.en.video_games", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.movies", null, locale),
                                    messageSource.getMessage("subcategory.en.movies", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.toys", null, locale),
                                    messageSource.getMessage("subcategory.en.toys", null, locale),
                                    categoria));
                            break;
                        case "Panadería, Bollería y Pastelería":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.bread", null, locale),
                                    messageSource.getMessage("subcategory.en.bread", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.croissants", null, locale),
                                    messageSource.getMessage("subcategory.en.croissants", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cakes", null, locale),
                                    messageSource.getMessage("subcategory.en.cakes", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cupcakes", null, locale),
                                    messageSource.getMessage("subcategory.en.cupcakes", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.cookies", null, locale),
                                    messageSource.getMessage("subcategory.en.cookies", null, locale),
                                    categoria));
                            break;
                        case "Pasta, Arroz & Legumbres":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.rice", null, locale),
                                    messageSource.getMessage("subcategory.en.rice", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.beans", null, locale),
                                    messageSource.getMessage("subcategory.en.beans", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.pasta", null, locale),
                                    messageSource.getMessage("subcategory.en.pasta", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.lentils", null, locale),
                                    messageSource.getMessage("subcategory.en.lentils", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.chickpeas", null, locale),
                                    messageSource.getMessage("subcategory.en.chickpeas", null, locale),
                                    categoria));
                            break;
                        case "Perfumería & Parafarmacia":
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.perfumes", null, locale),
                                    messageSource.getMessage("subcategory.en.perfumes", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.vitamins", null, locale),
                                    messageSource.getMessage("subcategory.en.vitamins", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.medicines", null, locale),
                                    messageSource.getMessage("subcategory.en.medicines", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.hair_care", null, locale),
                                    messageSource.getMessage("subcategory.en.hair_care", null, locale),
                                    categoria));
                            repoSubcategoria.save(new Subcategoria(
                                    messageSource.getMessage("subcategory.es.skincare", null, locale),
                                    messageSource.getMessage("subcategory.en.skincare", null, locale),
                                    categoria));
                            break;
                        default:
                            break;
                    }
                }
            }
            // Iniciar tipos de descuento
            try {
                if (repoTipoDescuento.count() == 0) {
                    TipoDescuento descuentoPorVolumen = new TipoDescuento(
                            messageSource.getMessage("discount.type.volume.es", null, locale),
                            messageSource.getMessage("discount.type.volume.en", null, locale),
                            true,
                            LocalDate.now(),
                            LocalDate.now().plusYears(1));

                    TipoDescuento descuentoPorTemporada = new TipoDescuento(
                            messageSource.getMessage("discount.type.seasonal.es", null, locale),
                            messageSource.getMessage("discount.type.seasonal.en", null, locale),
                            true,
                            LocalDate.now(),
                            LocalDate.now().plusMonths(3));

                    TipoDescuento descuentoFidelidad = new TipoDescuento(
                            messageSource.getMessage("discount.type.loyalty.es", null, locale),
                            messageSource.getMessage("discount.type.loyalty.en", null, locale),
                            true,
                            LocalDate.now(),
                            LocalDate.now().plusMonths(6));

                    TipoDescuento descuentoLanzamiento = new TipoDescuento(
                            messageSource.getMessage("discount.type.launch.es", null, locale),
                            messageSource.getMessage("discount.type.launch.en", null, locale),
                            true,
                            LocalDate.now(),
                            LocalDate.now().plusMonths(1));

                    TipoDescuento descuentoLiquidacion = new TipoDescuento(
                            messageSource.getMessage("discount.type.clearance.es", null, locale),
                            messageSource.getMessage("discount.type.clearance.en", null, locale),
                            true,
                            LocalDate.now(),
                            LocalDate.now().plusMonths(2));

                    TipoDescuento descuentoEspecial = new TipoDescuento(
                            messageSource.getMessage("discount.type.special.es", null, locale),
                            messageSource.getMessage("discount.type.special.en", null, locale),
                            true,
                            LocalDate.now(),
                            LocalDate.now().plusMonths(4));

                    repoTipoDescuento.save(descuentoPorVolumen);
                    repoTipoDescuento.save(descuentoPorTemporada);
                    repoTipoDescuento.save(descuentoFidelidad);
                    repoTipoDescuento.save(descuentoLanzamiento);
                    repoTipoDescuento.save(descuentoLiquidacion);
                    repoTipoDescuento.save(descuentoEspecial);
                }

                // Crear descuentos
                if (repoDescuento.count() == 0) {
                    TipoDescuento descuentoPorVolumen = repoTipoDescuento.findByTipo_esJPQL(messageSource.getMessage("discount.type.volume.es", null, LocaleContextHolder.getLocale()));
                    TipoDescuento descuentoPorTemporada = repoTipoDescuento.findByTipo_esJPQL(messageSource.getMessage("discount.type.seasonal.es", null, LocaleContextHolder.getLocale()));
                    TipoDescuento descuentoFidelidad = repoTipoDescuento.findByTipo_esJPQL(messageSource.getMessage("discount.type.loyalty.es", null, LocaleContextHolder.getLocale()));
                    TipoDescuento descuentoLanzamiento = repoTipoDescuento.findByTipo_esJPQL(messageSource.getMessage("discount.type.launch.es", null, LocaleContextHolder.getLocale()));
                    TipoDescuento descuentoLiquidacion = repoTipoDescuento.findByTipo_esJPQL(messageSource.getMessage("discount.type.clearance.es", null, LocaleContextHolder.getLocale()));
                    TipoDescuento descuentoEspecial = repoTipoDescuento.findByTipo_esJPQL(messageSource.getMessage("discount.type.special.es", null, LocaleContextHolder.getLocale()));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchMessageException e) {
            throw new RuntimeException(e);
        }
    }
}