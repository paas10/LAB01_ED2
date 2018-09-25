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

        Escribir("");
        return "";
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
