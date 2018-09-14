package com.example.admin.ah18compresor;

public class Node<T> {

    public T value;
    public Node<T> left;
    public Node<T> right;

    public Node(T Valor, Node<T> left, Node<T> right)
    {
        this.value = Valor;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf()
    {
            if (right == null && left == null)
            {
                return true;
            }
            else
            {
                return false;
            }
    }
}
