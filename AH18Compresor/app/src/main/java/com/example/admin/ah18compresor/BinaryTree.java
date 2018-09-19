package com.example.admin.ah18compresor;

import java.nio.file.FileAlreadyExistsException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class BinaryTree
{
    Node nRaiz;
    LinkedList<Estructura> Tabla = new LinkedList<>();

    public BinaryTree()
    {
        nRaiz = null;
    }

    private Node insertar(Node n1, Node n2)
    {
        nRaiz = new Node();
        nRaiz.setNumero(n1.getNumero() + n2.getNumero());

        Node relleno1 = new Node();
        nRaiz.left = n1;
        nRaiz.right = n2;

        return nRaiz;
    }

    public Node Insertar (Node n1, Node n2)
    {
        if(n1.getNumero() <= n2.getNumero())
        {
            return insertar(n1, n2);
        }
        else
        {
            return insertar(n2, n1);
        }
    }

    public void InOrdenEscritura (Node nodoAuxiliar)
    {
        if (nodoAuxiliar != null)
        {
            if(nodoAuxiliar.isLeaf() == true)
            {
                Estructura nuevo = new Estructura();
                nuevo.setCod(nodoAuxiliar.coding);
                nuevo.setCaracter(nodoAuxiliar.getCaracter());

                Tabla.add(nuevo);
            }

            InOrdenEscritura(nodoAuxiliar.left);
            InOrdenEscritura(nodoAuxiliar.right);
        }
    }
}
