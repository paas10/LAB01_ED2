package com.example.admin.ah18compresor;

public class Node
{
    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public char getCaracter() {
        return Caracter;
    }

    public void setCaracter(char caracter) {
        Caracter = caracter;
    }

    private int Numero;
    private char Caracter;

    Node left = null;
    Node right = null;

}
