package com.olimpiadafdi.quicktest.data;

public class Badge {
    private int id;
    private String descCorta;
    private String descLarga;
    private int puntuacion;

    public Badge(int id, String descCorta, String descLarga, int puntuacion) {
        this.id = id;
        this.descCorta = descCorta;
        this.descLarga = descLarga;
        this.puntuacion = puntuacion;
    }

    public int getId() {
        return id;
    }

    public String getDescCorta() {
        return descCorta;
    }

    public String getDescLarga() {
        return descLarga;
    }

    public int getPuntuacion() {
        return puntuacion;
    }
}
