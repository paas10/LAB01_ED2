package com.example.admin.ah18compresor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.List;


public class Huffman extends Fragment implements OnItemClickListener {

    private List<String> NombresArchivos;
    private List<String> RutasArchivos;
    private ArrayAdapter<String> Adaptador;
    private String DirectorioRaiz;
    private TextView CarpetaActual;
    ListView Lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_huffman, container, false);

        CarpetaActual = (TextView) view.findViewById(R.id.RutaActual);
        Lista = (ListView) view.findViewById(R.id.ListaVista);

        DirectorioRaiz = Environment.getExternalStorageDirectory().getPath();
        Lista.setOnItemClickListener(this);
        VerDirectorio(DirectorioRaiz);

        return view;
    }

    private void VerDirectorio (String rutadirectorio)
    {
        NombresArchivos = new ArrayList<String>();
        RutasArchivos = new ArrayList<String>();
        int count = 0;
        File directorioactual = new File(rutadirectorio);
        File[] ListadeArchivos = directorioactual.listFiles();

        if (!rutadirectorio.equals(DirectorioRaiz))
        {
            NombresArchivos.add("../");
            RutasArchivos.add(directorioactual.getParent());
            count = 1;
        }

        for (File archivo:ListadeArchivos){
            RutasArchivos.add(archivo.getPath());
        }

        Collections.sort(RutasArchivos,String.CASE_INSENSITIVE_ORDER);

        for (int i = count; i<RutasArchivos.size(); i++){
            File archivo = new File (RutasArchivos.get(i));

            if(archivo.isFile())
            {
                NombresArchivos.add(archivo.getName());
            }
            else
            {
                NombresArchivos.add("/" + archivo.getName());
            }
        }

        if(ListadeArchivos.length<1)
        {
            NombresArchivos.add("No hay ningun archivo");
            RutasArchivos.add(rutadirectorio);
        }

        Adaptador = new ArrayAdapter<String>(getActivity(),R.layout.fragment_huffman,R.id.RutaActual,NombresArchivos);
        Lista.setAdapter(Adaptador);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        File archivo = new File(RutasArchivos.get(i));
        if(archivo.isFile())
        {
            if (archivo.getName().endsWith(".txt"))
            {
                AlertDialog.Builder Dialogo = new AlertDialog.Builder(getActivity());
                Dialogo.setTitle("Importante");
                Dialogo.setMessage("¿Desea Aplicar El Metodo de Compresión Huffman a este Archivo?");
                Dialogo.setCancelable(false);
                Dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        ConfirmarHuffman();
                    }
                });
                Dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        CancelarHuffman();
                    }
                });
                Dialogo.show();

            }
            else
            {
                Toast.makeText(getActivity(), "Has Seleccionado El Archivo: "+archivo.getName(),Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            VerDirectorio(RutasArchivos.get(i));
        }
    }

    public void ConfirmarHuffman() {
        Toast t=Toast.makeText(getActivity(),"Dentro de un momento tu Archivo habrá sido compreso y se mostrará", Toast.LENGTH_SHORT);
        t.show();
    }

    public void CancelarHuffman() {
        Toast.makeText(getActivity(), "Selecciona Otro Archivo para la Compresión Huffman",Toast.LENGTH_SHORT).show();
    }



    // Retorna una tabla con cada caracter y sus repeticiones en el texto.
    public estrucutura[] obtenerCaracteresRepeticiones (String texto)
    {
        char[] fragmentado = texto.toCharArray();

        int cont = fragmentado.length;
        char [] Filtro = new char[cont];

        // Ciclo exteriror recorre la cadena de caracteres completa
        // Ciclo interior unicamente registra los datos nuevos (1 vez cada caracter)
        for(int a = 0; a < cont; a++)
        {
            int b = 0;
            boolean Existe = false;
            while(Filtro[b] != '\0')
            {
                if(Filtro[b] == fragmentado[a])
                {
                    Existe = true;
                    break;
                }
                b++;
            }

            if(Existe == false)
                Filtro[b] = fragmentado[a];
        }

        // HASTA EL MOMENTO SE COLOCARON TODOS LOS CARACTERES EN UNA CADENA DE CHAR.

        int cantCaracteres = Filtro.length;

        estrucutura[] tabla = new estrucutura[cantCaracteres];

        for (int i = 0; i < cantCaracteres; i++)
        {
            tabla[i].setCaracter(Filtro[i]);
            char symbol = tabla[i].getCaracter();

            int contador = 0;
            for (int n = 0; n < cont; n++)
            {
                if (fragmentado[n] == symbol)
                    contador++;
            }

            tabla[i].setRepeticiones(contador);
        }

        // YA SE CREÓ LA TABLA CON LA CANTIDAD DE REPETICIONES DE CADA CARACTER

        return tabla;
    }


    public class estrucutura
    {
        public char getCaracter() {
            return caracter;
        }

        public void setCaracter(char caracter) {
            this.caracter = caracter;
        }

        public int getRepeticiones() {
            return repeticiones;
        }

        public void setRepeticiones(int repeticiones) {
            this.repeticiones = repeticiones;
        }

        char caracter;
        int repeticiones;
    }
}
