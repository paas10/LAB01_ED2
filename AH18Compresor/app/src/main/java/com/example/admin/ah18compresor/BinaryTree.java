package com.example.admin.ah18compresor;

import java.nio.file.FileAlreadyExistsException;
import java.util.Comparator;
import java.util.Iterator;

public class BinaryTree
{
    Node nRaiz;

    public BinaryTree()
    {
        nRaiz = null;
    }

    private Node insertar(Node n1, Node n2)
    {
        nRaiz.setNumero(n1.getNumero() + n2.getNumero());

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
}
