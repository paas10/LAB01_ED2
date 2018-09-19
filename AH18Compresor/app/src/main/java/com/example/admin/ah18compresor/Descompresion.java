package com.example.admin.ah18compresor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class Descompresion extends Fragment {

    static String Ruta;
    TextView Texto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_descompresion, container, false);

        Texto = (TextView) view.findViewById(R.id.txtResultado);
        String ArchivoLeido = LeerArchivo();
        Texto.setText(ArchivoLeido);

        String ArchivoDescompreso = "";
        ArchivoDescompreso = descompresión(ArchivoLeido);

        return view;
    }

    public void RecibirRuta(String ruta)
    {
        Ruta = ruta;
    }

    public String LeerArchivo()
    {

        if(Ruta == null)
        {
            Toast.makeText(getActivity(), "No Hay Ningun Archivo Compreso .Huff para Leer",Toast.LENGTH_SHORT).show();
        }
        else
        {
        File Archivo = new File(Ruta);

        String Texto = "";
        if(Archivo.exists()==true)
        {
            FileReader LecturaArchivo;
            try {
                LecturaArchivo = new FileReader(Archivo);
                BufferedReader LeerArchivo = new BufferedReader(LecturaArchivo);
                String Linea="";
                Linea = LeerArchivo.readLine();
                while(Linea != null)
                {
                    Texto = Linea;
                    Linea= LeerArchivo.readLine();
                }
                LecturaArchivo.close();
                LeerArchivo.close();

                return Texto;
            } catch (IOException e) {
                Toast.makeText(getActivity(), "ERROR El Archivo no se puede Leer!",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "ERROR El Archivo no Existe!",Toast.LENGTH_SHORT).show();
        }
        }
        return "-1";
    }

    public String descompresión(String Texto)
    {
        char[] texto = Texto.toCharArray();

        StringBuilder CerosUnos = new StringBuilder();

        LinkedList<Node> Caracteres = new LinkedList<>();
        int cerosAgregados = texto[0];

        int flag = 0;
        while (texto[flag] != '|')
        {
            flag++;
        }

        int contCaracter = 2;
        int contRepeticion = 4;

        while (texto[contRepeticion] != '/' && texto[contCaracter] != '/')
        {
            Node nuevo = new Node();
            nuevo.setCaracter(texto[contCaracter]);

            int aux = contRepeticion;
            String num = "";
            while (texto[aux] != '_')
            {
                num += Character.toString(texto[aux]);
                aux++;
            }

            nuevo.setNumero(Integer.parseInt(num));

            Caracteres.offer(nuevo);

            contCaracter = aux + 1;
            contRepeticion = contCaracter + 2;
        }

        // HASTA ACÁ HE METIDO TODOS LOS CARACTERES CON SUS REPETICIONES A Caracteres

        Procesos procesos = new Procesos();
        BinaryTree Arbol = new BinaryTree();

        do{
            Node n1 = new Node();
            Node n2 = new Node();

            n1 = Caracteres.poll();
            n2 = Caracteres.poll();

            Caracteres.offer(Arbol.Insertar(n1, n2));

            Caracteres = procesos.OrdenarLinkedList(Caracteres);

        }while(Caracteres.size() != 1);

        // SE HA CREADO EL ARBOL

        Node raiz = new Node();
        raiz = Caracteres.poll();
        String rutas = "";

        InOrdenEscritura(raiz, rutas);

        Arbol.InOrdenEscritura(raiz);

        Estructura [] tabla = new Estructura[Arbol.Tabla.size()];
        int contador = 0;

        while(Arbol.Tabla.size() != 0)
        {
            tabla[contador] = Arbol.Tabla.poll();
            contador++;
        }
        // Hasta el momento la tabla con las codificaciones


        // Le sumo otro para dejar atras la diagnoal y tomar en cuenta el primer caracter compreso.
        contRepeticion++;

        int [] numeros = new int[texto.length-contRepeticion];
        int cont = 0;
        for (int a = contRepeticion; a < texto.length; a++)
        {
            numeros[cont] = Integer.parseInt(Character.toString(texto[a]));
        }

        String[] binario = new String[numeros.length];
        for (int a = 0; a < numeros.length; a++)
        {
            binario[a] = Integer.toString(numeros[a]);
        }


        for (String numbers : binario)
        {
            boolean encontrado = false;

            int conta = 0;
            while (encontrado != true)
            {
                if(numbers == tabla[conta].getCod())
                {
                    CerosUnos.append(tabla[conta].getCaracter());
                    encontrado = true;
                }
                conta++;
            }
        }



        return "";
    }

    private void InOrdenEscritura (Node nodoAuxiliar, String codi)
    {
        if (nodoAuxiliar != null)
        {
            if(nodoAuxiliar.isLeaf() == true)
                nodoAuxiliar.coding = codi;

            InOrdenEscritura(nodoAuxiliar.left, codi + "0");
            InOrdenEscritura(nodoAuxiliar.right, codi + "1");
        }
    }
}


















