package com.example.admin.ah18compresor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bitacora extends Fragment {

    static List<Archivo> ListadeArchivosCompresos;
    static File Archivo;
    ListView Lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bitacora, container, false);

        Lista = (ListView) view.findViewById(R.id.ListaArchivosCompresosBitacora);
        try {
            GestionarArchivo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ArchivoDescompresoAdapter Adaptador = new ArchivoDescompresoAdapter(getActivity(), (ArrayList<Archivo>) ListadeArchivosCompresos);
        Lista.setAdapter(Adaptador);
        return view;
    }

    public void RecibirDatos(List<Archivo> ListadeArchivos)
    {
        ListadeArchivosCompresos = ListadeArchivos;
    }


    //Metodo en donde se lee el Archivo
    private void GestionarArchivo () throws IOException {

       SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
       String Ruta = myPreferences.getString("Ruta","unknown");
        if(Ruta == null)
        {
            String Nombre = ListadeArchivosCompresos.get(0).getNombre();
            String Rutai = ListadeArchivosCompresos.get(0).getRuta();

            if(Rutai.contains(".huff"))
            {
                Nombre = Nombre.replace(".txt",".huff");
            }
            else if (Rutai.contains(".LZW"))
            {
                Nombre = Nombre.replace(".txt", ".LZW");
            }

            Rutai = Rutai.replace(Nombre,"Bitacora.txt");

            Archivo = new File(Rutai);
            FileWriter Escribir = new FileWriter(Archivo, true);
            BufferedWriter bw = new BufferedWriter(Escribir);
            for (Archivo item: ListadeArchivosCompresos)
            {
                bw.write(item.getNombre()+"ƒ"+item.getRuta()+"ƒ"+item.getRazon()+"ƒ"+item.getFactor()+"ƒ"+item.getPorcentaje());
                bw.newLine();
            }
            bw.close();
            Escribir.close();

            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString("Ruta", Rutai);
            myEditor.commit();

        }
        else
        {
            if(ListadeArchivosCompresos == null)
            {
                List<String> Datos = new ArrayList<>();
                FileReader LecturaArchivo = new FileReader(Ruta);
                BufferedReader LeerArchivo = new BufferedReader(LecturaArchivo);
                String Linea="";
                Linea = LeerArchivo.readLine();
                while(Linea != null)
                {
                    Datos.add(Linea);
                    Linea = LeerArchivo.readLine();
                }
                LecturaArchivo.close();
                LeerArchivo.close();

                List<Archivo> ListaAux = new ArrayList<>();
                for (String item: Datos)
                {
                    String [] Auxiliar = item.split("ƒ");

                    Archivo AuxArchivo = new Archivo(Auxiliar[0],Auxiliar[1],Auxiliar[2],Auxiliar[3],Auxiliar[4]);
                    ListaAux.add(AuxArchivo);
                }

                ListadeArchivosCompresos = ListaAux;
            }
            else
            {
                Archivo = new File(Ruta);
                FileWriter Escribir = new FileWriter(Archivo, true);
                BufferedWriter bw = new BufferedWriter(Escribir);
                for (Archivo item: ListadeArchivosCompresos)
                {
                    bw.write(item.getNombre()+"ƒ"+item.getRuta()+"ƒ"+item.getRazon()+"ƒ"+item.getFactor()+"ƒ"+item.getPorcentaje());
                    bw.newLine();
                }
                bw.close();
                Escribir.close();

                List<String> Datos = new ArrayList<>();
                FileReader LecturaArchivo = new FileReader(Ruta);
                BufferedReader LeerArchivo = new BufferedReader(LecturaArchivo);
                String Linea="";
                Linea = LeerArchivo.readLine();
                while(Linea != null)
                {
                    Datos.add(Linea);
                    Linea = LeerArchivo.readLine();
                }
                LecturaArchivo.close();
                LeerArchivo.close();

                List<Archivo> ListaAux = new ArrayList<>();
                for (String item: Datos)
                {
                    String [] Auxiliar = item.split("ƒ");

                    Archivo AuxArchivo = new Archivo(Auxiliar[0],Auxiliar[1],Auxiliar[2],Auxiliar[3],Auxiliar[4]);
                    ListaAux.add(AuxArchivo);
                }

                ListadeArchivosCompresos = ListaAux;
            }
        }

    }


}
