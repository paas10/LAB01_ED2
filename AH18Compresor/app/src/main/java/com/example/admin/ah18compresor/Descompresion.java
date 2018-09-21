package com.example.admin.ah18compresor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;


public class Descompresion extends Fragment {

    //Ruta del Archivo
    static String Ruta;
    TextView Texto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_descompresion, container, false);

        Texto = (TextView) view.findViewById(R.id.txtResultado);
        String ArchivoLeido = LeerArchivo();
        Texto.setMovementMethod(new ScrollingMovementMethod());
        String ArchivoDescompreso = "";
        ArchivoDescompreso = descompresión(ArchivoLeido);
        Texto.setText(ArchivoDescompreso);

        return view;
    }

    //Metodo que recibe la ruta para escribir el archivo
    public void RecibirRuta(String ruta)
    {
        Ruta = ruta;
    }

    //Metodo donde se lee el Archivo
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
                    char[] FiltroSaltoLinea = Linea.toCharArray();

                    for (char caracter : FiltroSaltoLinea)
                    {
                        if (caracter == 'λ')
                            Texto += Character.toString('\n');
                        else
                            Texto += Character.toString(caracter);
                    }

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

    //Metodo donde se descomprime el Archivo
    public String descompresión(String Texto)
    {
        char[] texto = Texto.toCharArray();

        LinkedList<Node> Caracteres = new LinkedList<>();
        int cerosAgregados = Integer.parseInt(Character.toString(texto[0]));

        int contCaracter = 2;
        int contRepeticion = 4;
        while (texto[contRepeticion] != 'θ' && texto[contCaracter] != 'θ')
        {
            Node nuevo = new Node();
            nuevo.setCaracter(texto[contCaracter]);

            int aux = contRepeticion;
            String num = "";
            while (texto[aux] != '_' && texto[aux] != 'θ')
            {
                num += Character.toString(texto[aux]);
                aux++;
            }

            nuevo.setNumero(Integer.parseInt(num));

            Caracteres.offer(nuevo);

            contCaracter = aux + 1;

            if (texto[aux] == 'θ')
                break;

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

        // contCaracter está sobre la diagonal por lo que le sumamos 1
        contCaracter++;

        int [] numeros = new int[texto.length-contCaracter];
        int cont = 0;
        for (int a = contCaracter; a < texto.length; a++)
        {
            numeros[cont] = texto[a];
            cont++;
        }

        String[] binario = new String[numeros.length];
        for (int a = 0; a < numeros.length; a++)
        {
                binario[a] = Integer.toBinaryString(numeros[a]);
        }

        String ResultanteCerosUnos = "";
        for (String numbers : binario)
        {
            if (numbers.length() != 8)
            {
                for (int a = 0; a < 8-numbers.length(); a++)
                    ResultanteCerosUnos += "0";

                ResultanteCerosUnos += numbers;
            }
            else
            {
                ResultanteCerosUnos += numbers;
            }
        }


        char[] Binario = ResultanteCerosUnos.toCharArray();

        String Resultante = "";
        String temporal = "";
        for (int a = 0; a < Binario.length-cerosAgregados; a++)
        {
            temporal += Character.toString(Binario[a]);
            for (Estructura celda : tabla)
            {
                if (celda.getCod().equals(temporal))
                {
                    Resultante += celda.getCaracter();
                    temporal = "";
                }
            }
        }
        Escribir(Resultante);
        return Resultante;
    }

    //Metodo donde se escriben los resultados del arbol en Inorden
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

    //Metodo donde se escribe el texto
    private void Escribir(String Cadena)
    {

        String Formato ="Descompreso";
        File Temporal = new File(Ruta);
        Ruta =Temporal.getPath().replace(".huff",Formato)+".huff";

            File Archivo = new File(Ruta);
            try {
                FileWriter Escribir = new FileWriter(Archivo);
                BufferedWriter bw = new BufferedWriter(Escribir);
                bw.write(Cadena);
                bw.close();
                Escribir.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
    }
}


















