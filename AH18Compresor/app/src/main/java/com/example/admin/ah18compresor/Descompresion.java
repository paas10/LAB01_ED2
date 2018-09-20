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

        String ArchivoDescompreso = "";
        ArchivoDescompreso = descompresión(ArchivoLeido);
        Texto.setText(ArchivoDescompreso);

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
            while (texto[aux] != '_' && texto[aux] != '/')
            {
                num += Character.toString(texto[aux]);
                aux++;
            }

            nuevo.setNumero(Integer.parseInt(num));

            Caracteres.offer(nuevo);

            contCaracter = aux + 1;

            if (texto[aux] == '/')
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


        int [] numeros = new int[texto.length-contCaracter];
        int cont = 0;
        String NumerodeCeros = "";
        for (int a = contCaracter; a < texto.length; a++)
        {
            if(Character.toString(texto[a]) != "_")
            {
                if(Character.toString(texto[a]).contains("/") == false)
                {
                    numeros[cont] = texto[a];
                    cont++;
                }
            }
        }
        NumerodeCeros = Character.toString(texto[0]);

        String[] binario = new String[numeros.length];
        for (int a = 0; a < numeros.length; a++)
        {
                binario[a] = Integer.toBinaryString(numeros[a]);
        }


        String ResultanteCerosUnos = "";
        for (String numbers : binario)
        {
            ResultanteCerosUnos += numbers;
        }

        // Guardo todos los posibles conbinacines de ceros y unos.
        String[] PosiblesCodificaciones = new String[tabla.length];
        int n = 0;
        for (Estructura celda : tabla)
        {
            PosiblesCodificaciones[n] = celda.getCod();
            n++;
        }


        //YO ME HABIA EQUIVOCADO, AHORITA LO QUE HAY QUE HACER ES CONVERTIR EL STRING RESULTANTECEROSUNOS A UN charArray(); PARA
        //IR SACANDO CARACTER POR CARACTER, EN CADA ITERACION SE TIENE QUE VERIFICAR SI LA CADENA CONCATENADA EXISTE EN LA CADENA DE
        //STRING POSIBLESCODIFICACIONES, si existe en la cadena hay que ir a buscar a tabla para extraer el respectivo caracter.

        char[] Binario = ResultanteCerosUnos.toCharArray();

        // ESTE ES UN EJEMPLO DE COMO IR A BUSCAR A TABLA EL CODIGO BINARIO PARA EXTRAER EL CARACTER CORRESPONDIENTE.

            for (String numbers : binario)
            {
                for(int i = 0;i<= PosiblesCodificaciones.length; i++)
                {
                    if (PosiblesCodificaciones[i] == numbers)
                    {
                        for (int a= 0; a<= tabla.length; a++)
                        {
                            if (numbers == tabla[a].getCod()) {
                                CerosUnos.append(tabla[a].getCaracter());
                            }
                        }
                    }
                }
            }


        return CerosUnos.toString();
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


















