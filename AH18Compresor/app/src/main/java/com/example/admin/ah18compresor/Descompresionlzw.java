package com.example.admin.ah18compresor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;


public class Descompresionlzw extends Fragment {

    //Ruta del Archivo
    public static String Ruta;
    TextView Texto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_descompresionlzw, container, false);

        Texto = (TextView) view.findViewById(R.id.txtResultadolzw);
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
            Toast.makeText(getActivity(), "No Hay Ningun Archivo Compreso .LZW para Leer",Toast.LENGTH_SHORT).show();
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
    public String descompresión(String Texto) {

        char[] texto = Texto.toCharArray();

        HashMap CaracteresL = new HashMap();
        HashMap CaracteresN = new HashMap();

        int ContadorLimite = 0;
        while (texto[ContadorLimite] != 'θ')
            ContadorLimite++;

        for (int i = 1; i < ContadorLimite; i += 4)
        {
            int j = i+3;
            int extension = 0;
            while(texto[j] != '|' && texto[j] != 'θ')
            {
                j++;
                extension++;
            }

            String concat = "";
            for(int a = i+2; a < j; a++)
                concat += Character.toString(texto[a]);

            CaracteresN.put(Integer.parseInt(concat), String.valueOf(texto[i]));
            CaracteresL.put(String.valueOf(texto[i]), Integer.parseInt(concat));

            i += extension;
        }

        int CaracteresOriginales = CaracteresL.size();
        int contKey = CaracteresL.size() + 161;

        // Codificacion separada del encabezado
        String TextoSeparado = Texto.substring(ContadorLimite + 1, Texto.length());

        // cadena con cada caracter codificado
        char[] TextoSeccionado = TextoSeparado.toCharArray();

        int [] Numeros = new int[TextoSeccionado.length];

        // Se convierte de Unicode a int
        for (int i = 0; i < TextoSeccionado.length; i++)
        {
            Numeros[i] = (int)TextoSeccionado[i];
        }

        String Original = "";

        // Se decodifican los primeros dos caracteres para poder comenzar a agregar al diccionario
        for (int i = 0; i < 2; i++)
        {
            Original += (String) CaracteresN.get(Numeros[i]);
        }

        // VARIABLE QUE LLEVA EL CONTROL DE QUE NUMERO DEBE SER DESCOMPRESO
        int TurnoDecodificar = 2;

        // Se decodifica y a la vez se amplia el diccionario
        for (int i = 0; TurnoDecodificar < Numeros.length; i++)
        {
            char[] TextoExistente = Original.toCharArray();
            String TextoDecodificar = Character.toString(TextoExistente[i]);

            // SE VERIFICA HASTA QUE CARACTER SE TIENE QUE AGREGAR AL DICCIONARIO
            int aux = i;

            try
            {
                while (CaracteresL.containsKey(TextoDecodificar))
                {
                    aux++;
                    TextoDecodificar += Character.toString(TextoExistente[aux]);
                }

                CaracteresN.put(contKey, TextoDecodificar);
                CaracteresL.put(TextoDecodificar, contKey);
                contKey++;
            }
            catch (Exception e)
            {

            }

            // SE DECODIFICA EL CARACTER CORRESPONDIENTE AL TEXTO ORIGINAL.
            if (TurnoDecodificar < Numeros.length)
                Original += (String) CaracteresN.get(Numeros[TurnoDecodificar]);

            i = aux-1;
            TurnoDecodificar++;
        }


        Escribir(Original);
        return Original;
    }

    //Metodo donde se escribe el texto
    private void Escribir(String Cadena)
    {

        String Formato ="Descompreso";
        File Temporal = new File(Ruta);
        Ruta =Temporal.getPath().replace(".LZW",Formato)+".LZW";

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
