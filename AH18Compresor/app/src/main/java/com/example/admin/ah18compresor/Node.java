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
    Node left;
    Node right;
    String coding;

    public boolean isLeaf ()
    {
        if(left == null && right == null)
            return true;
        else
            return false;
    }

    public Node()
    {
        Numero = 0;
        Caracter = '\0';
        left = null;
        right = null;
        coding = "";
    }

    public Node(char caracter, int repeticiones)
    {
        Numero = repeticiones;
        Caracter = caracter;
        left = null;
        right = null;
        coding = "";
    }

}
