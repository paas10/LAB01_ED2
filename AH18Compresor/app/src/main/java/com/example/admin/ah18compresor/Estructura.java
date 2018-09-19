package com.example.admin.ah18compresor;

public class Estructura {

    public char getCaracter() {
        return caracter;
    }

    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    private char caracter;
    private String cod;

    public Estructura()
    {
        caracter = '\0';
        cod = "";
    }

}
