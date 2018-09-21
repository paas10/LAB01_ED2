package com.example.admin.ah18compresor;

public class Archivo
{
    String Nombre;
    String Ruta;
    String Razon;
    String Factor;
    String Porcentaje;

    public Archivo(String nombre, String ruta, String razon, String factor, String porcentaje) {
        Nombre = nombre;
        Ruta = ruta;
        Razon = razon;
        Factor = factor;
        Porcentaje = porcentaje;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }

    public String getRazon() {
        return Razon;
    }

    public void setRazon(String razon) {
        Razon = razon;
    }

    public String getFactor() {
        return Factor;
    }

    public void setFactor(String factor) {
        Factor = factor;
    }

    public String getPorcentaje() {
        return Porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        Porcentaje = porcentaje;
    }


}
