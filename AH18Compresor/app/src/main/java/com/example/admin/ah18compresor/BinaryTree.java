package com.example.admin.ah18compresor;

import java.nio.file.FileAlreadyExistsException;
import java.util.Comparator;
import java.util.Iterator;

public class BinaryTree<T>{

     /*   public Node<T> nRoot;
        int iElements;

        public BinaryTree()
        {
            this.nRoot = null;
            this.iElements = 0;
        }


        public void Insert(Node<T> nNew)
        {
            if (nRoot == null)
            {
                nRoot = nNew;
            }
            else
            {
                Node<T> AuxiliarNode = nRoot;
                Node<T> FatherNode = nRoot;
                boolean bleft = false;

                while (AuxiliarNode != null)
                {
                    FatherNode = AuxiliarNode;

                    if (nNew.value.CompareTo(AuxiliarNode.value) < 0)
                    {
                        AuxiliarNode = AuxiliarNode.left;
                        bleft = true;
                    }
                    else if (nNew.value.CompareTo(AuxiliarNode.value) > 0)
                    {
                        AuxiliarNode = AuxiliarNode.right;
                        bleft = false;
                    }
                    else
                        return;
                }

                if (bleft
                        )
                    FatherNode.left = nNew;
                else
                    FatherNode.right = nNew;

            }
        }


        /// <summary>
        /// Eliminar la primera apracición de un valor en el Arbol
        /// </summary>
        /// <param name="valor">Valor a Eliminar</param>
        /// <returns>Nodo Eliminado</returns>
        public Node<T> Eliminar(T valor)
        {
            Node<T> Auxiliar = nRoot;
            Node<T> Father = nRoot;
            boolean itsLeftSon = true;

            while (valor.CompareTo(Auxiliar.value) != 0)
            {
                Father = Auxiliar;

                if (valor.CompareTo(Auxiliar.value) < 0)
                {
                    itsLeftSon = true;
                    Auxiliar = Auxiliar.left;
                }
                else
                {
                    itsLeftSon = false;
                    Auxiliar = Auxiliar.right;
                }
                if (Auxiliar == null)
                {
                    return null;
                }
            }// Fin ciclo inicial

            if (Auxiliar.isLeaf())
            {
                if (Auxiliar == nRoot)
                {
                    nRoot = null;
                }
                else if (itsLeftSon)
                {
                    Father.left = null;
                }
                else
                {
                    Father.right = null;
                }
            }
            else if (Auxiliar.right == null)
            {
                Node<T> temp = Auxiliar.left;
                if (Auxiliar == nRoot)
                {
                    nRoot = temp;
                }
                else if (itsLeftSon)
                {
                    Father.left = temp;
                }
                else
                {
                    Father.right = temp;
                }
            }
            else if (Auxiliar.left == null)
            {
                Node<T> temp = Auxiliar.right;
                if (Auxiliar == nRoot)
                {
                    nRoot = temp;
                }
                else if (itsLeftSon)
                {
                    Father.left = temp;
                }
                else
                {
                    Father.right = temp;
                }
            }
            else
            {
                Node<T> reemplazo = Reemplazar(Auxiliar);
                if (Auxiliar == nRoot)
                {
                    nRoot = reemplazo;
                }
                else if (itsLeftSon)
                {
                    Father.left = reemplazo;
                }
                else
                {
                    Father.right = reemplazo;
                }
                reemplazo.left = Auxiliar.left;

            }
            return Auxiliar;
        }

        /// <summary>
        /// Elimina un Nodo mediante sustitucion
        /// </summary>
        /// <param name="NodoAEliminar">Nodo a Eliminar </param>
        /// <returns>Nodo de Reemplazo</returns>
        private Node<T> Reemplazar(Node<T> NodoAEliminar)
        {
            Node<T> remplazoPadre = NodoAEliminar;
            Node<T> reemplazo = NodoAEliminar;
            Node<T> auxiliar = NodoAEliminar.right;
            while (auxiliar != null)
            {
                remplazoPadre = reemplazo;
                reemplazo = auxiliar;
                auxiliar = auxiliar.left;
            }
            if (reemplazo != NodoAEliminar.right)
            {
                remplazoPadre.left = reemplazo.right;
                reemplazo.right = NodoAEliminar.right;
            }
            return reemplazo;
        }

        public String Balanceado()
        {
            Node<T> auxiliar = nRoot;
            Node<T> NodoNoBalanceadoI = nRoot;
            Node<T> NodoNoBalanceadoD = nRoot;
            int ContadorIzquierda = 0;
            int ContadorDerecha = 0;

            while (auxiliar != null)
            {
                if (nRoot.left == null || nRoot.right == null)
                {
                    ContadorIzquierda = -1;
                    break;
                }
                if (auxiliar.left == null)
                {
                    if (auxiliar.right != null)
                    {
                        auxiliar = auxiliar.right;
                        NodoNoBalanceadoI = auxiliar;
                        ContadorIzquierda++;
                    }
                    else
                    {
                        auxiliar = auxiliar.right;
                    }
                }
                else
                {
                    auxiliar = auxiliar.left;
                    if(auxiliar != null)
                    {
                        NodoNoBalanceadoI = auxiliar;
                    }
                    ContadorIzquierda++;
                }
            }

            auxiliar = nRoot;
            while (auxiliar != null)
            {
                if(nRoot.left == null || nRoot.right == null)
                {
                    ContadorDerecha = -1;
                    break;
                }
                if (auxiliar.right == null)
                {
                    if (auxiliar.left != null)
                    {
                        auxiliar = auxiliar.left;
                        NodoNoBalanceadoD = auxiliar;
                        ContadorDerecha++;
                    }
                    else
                    {
                        auxiliar = auxiliar.left;
                    }
                }
                else
                {
                    auxiliar = auxiliar.right;
                    if(auxiliar != null)
                    {
                        NodoNoBalanceadoD = auxiliar;
                    }
                    ContadorDerecha++;
                }
            }

            if ((ContadorDerecha == ContadorIzquierda && (ContadorDerecha !=-1 || ContadorIzquierda != -1)))
            {
                return "Balanceado";
            }
            else if(ContadorDerecha == -1 || ContadorIzquierda == -1)
            {
                return "No Balanceado, el Árbol no se encuentra balanceado porque el nodo raiz, solo posee un nodo, ya sea izquierdo o Derecho";
            }
            else if (ContadorDerecha > ContadorIzquierda)
            {
                if ((ContadorDerecha - ContadorIzquierda) <= 1)
                {
                    return "Balanceado";
                }
                else
                {
                    return "No Balanceado, El nodo que no se encuentra balanceado es: "+NodoNoBalanceadoI.value;
                }

            }
            else if (ContadorIzquierda > ContadorDerecha)
            {
                if ((ContadorIzquierda - ContadorDerecha) <= 1)
                {
                    return "Balanceado";
                }
                else
                {
                    return "No Balanceado, El nodo que no se encuentra balanceado es: " + NodoNoBalanceadoD.value;
                }

            }

            return "Error";

        }*/

}
