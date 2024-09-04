package com.eoi.Fruggy.DTO;

public class SubcategoriaDTO {
    private Long id;
    private String tipo_es;
    private String tipo_en;

     // Utilizado para transferir datos de subcategorías entre capas de la aplicación.
    public SubcategoriaDTO() {
    }

    public SubcategoriaDTO(Long id, String tipo_es, String tipo_en) {
        this.id = id;
        this.tipo_es = tipo_es;
        this.tipo_en = tipo_en;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //Obtiene el nombre de la subcategoría en español.
    public String getTipo_es() {
        return tipo_es;
    }

    public void setTipo_es(String tipo_es) {
        this.tipo_es = tipo_es;
    }

    //Obtiene el nombre de la subcategoría en ingles.
    public String getTipo_en() {
        return tipo_en;
    }

    public void setTipo_en(String tipo_en) {
        this.tipo_en = tipo_en;
    }
}
