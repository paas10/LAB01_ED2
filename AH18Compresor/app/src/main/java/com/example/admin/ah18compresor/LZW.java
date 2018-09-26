package com.example.admin.ah18compresor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.UnicodeSet;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class LZW extends Fragment implements OnItemClickListener {


    private List<String> NombresArchivos;
    private List<String> RutasArchivos;
    private String DirectorioRaiz;
    private TextView CarpetaActual;
    ListView Lista;
    static String Carpeta;
    static String ArchivoT;
    static int Caracteres;
    static int Tamaño;
    static List<Archivo> ListadeArchivos = new ArrayList<>();
    String RutaAbsoluta;
    String RutaCompresa;

    //Cree el Metodo ConfirmarLZW donde se obtiene el String Leido y se Envia a tu Método, asi como en Huffman
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lzw, container, false);

        CarpetaActual = (TextView) view.findViewById(R.id.Ruta);
        Lista = (ListView) view.findViewById(R.id.ListaVistaLZW);

        DirectorioRaiz = Environment.getExternalStorageDirectory().getPath();
        Lista.setOnItemClickListener(this);
        VerDirectorio(DirectorioRaiz);

        return view;
    }

    //Metodo en donde se recuperan los Directorios validos para mostrarlos
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

        final ArchivoAdapter Adaptador = new ArchivoAdapter(getActivity(), (ArrayList<String>) NombresArchivos);
        Lista.setAdapter(Adaptador);
    }

    //Metodo en donde se lee el Archivo
    private String LeerArchivo (File Archivo)
    {
        String Texto = "";
        if(Archivo.exists()==true)
        {
            FileReader LecturaArchivo;
            try {
                LecturaArchivo = new FileReader(Archivo);
                BufferedReader LeerArchivo = new BufferedReader(LecturaArchivo);
                String Linea="";
                String SiguienteLinea="";
                Linea = LeerArchivo.readLine();
                while(Linea != null)
                {
                    SiguienteLinea = LeerArchivo.readLine();
                    Texto += Linea;

                    if (SiguienteLinea != null)
                        Texto += "λ";

                    Linea = SiguienteLinea;
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

        return "-1";
    }

    public void ConfirmarLZW(File Archivo) {
        Toast t = Toast.makeText(getActivity(), "Dentro de un momento el archivo: " + Archivo.getName() + " Sera enviando al método de compresion de LZW y Podra Verlo", Toast.LENGTH_SHORT);
        t.show();
        ArchivoT = Archivo.toString();
        String Texto = LeerArchivo(Archivo);
        String Compresion = CompresionLZW(Texto);

        StringBuilder texto = new StringBuilder();
        texto.append(Compresion);

        Escribir(texto);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final File archivo = new File(RutasArchivos.get(i));
        if(archivo.isFile())
        {
            if (archivo.getName().endsWith(".txt"))
            {
                AlertDialog.Builder Dialogo = new AlertDialog.Builder(getActivity());
                Dialogo.setTitle("Importante");
                Dialogo.setMessage("¿Desea Aplicar El Metodo de Compresión LZW a este Archivo?");
                Dialogo.setCancelable(false);
                Dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        RutaAbsoluta = archivo.toString();
                        ConfirmarLZW(archivo);
                    }
                });
                Dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //CancelarLZW();
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

    //Metodo que recibe la ruta para guardar el archivo.
    public String RecibirRuta(String carpeta)
    {
        Carpeta = carpeta;
        return Carpeta;
    }

    private String CompresionLZW (String Texto) {
        Procesos procesos = new Procesos();

        HashMap Caracteres = new HashMap();
        char[] TextoSeccionado = Texto.toCharArray();
        String Encabezado = "";
        String Codificacion = "";

        // SE OBTIENEN TODOS LOS CARACTERES DEL TEXTO UNA VEZ
        char[] CaracteresSinRepeticion = procesos.CadenaSinRepeticion(TextoSeccionado);
        int contKey = 161;

        // SE INGRESAN ESOS CARACTERES EN UN DICCIONARIO
        for (char caracter : CaracteresSinRepeticion)
        {
            Caracteres.put(String.valueOf(caracter), contKey);
            Encabezado += "|" + caracter + "," + contKey;
            contKey++;
        }
        Encabezado += "θ";

        for (int i = 0; i < TextoSeccionado.length; i++)
        {
            String TextoCodificar = Character.toString(TextoSeccionado[i]);

            // SE VERIFICA HASTA QUE CARACTER SE TIENE QUE AGREGAR AL DICCIONARIO
            int aux = i;

            try
            {
                while (Caracteres.containsKey(TextoCodificar))
                {
                    aux++;
                    TextoCodificar += Character.toString(TextoSeccionado[aux]);
                }

                Caracteres.put(TextoCodificar, contKey);
                contKey++;
            }
            catch (Exception e)
            {

            }

            // SE CONCATENAN TODOS LOS CHAR QUE SE VAN A CODIFICAR
            String concat = "";
            for (int j = i; j < aux; j++)
            {
                concat += Character.toString(TextoSeccionado[j]);
            }

            int toUnicode = (int) Caracteres.get(concat);
            char unicode = (char) toUnicode;

            Codificacion += unicode;

            i = aux-1;
        }

        return Encabezado+Codificacion;
    }

    //Metodo en donde se escribe el Archivo
    private void Escribir(StringBuilder Cadena)
    {
        MainActivity P = new MainActivity();
        File directorioactual = new File(DirectorioRaiz);
        File[] ListadeArchivos = directorioactual.listFiles();
        String Ruta = "";
        File ArchivoNuevo = new File(ArchivoT);
        String Formato = "/"+ArchivoNuevo.getName().replace(".txt",".LZW");

        if(Carpeta == null)
        {
            Toast.makeText(getActivity(), "No hay Ningun Archivo para Escribir Aún", Toast.LENGTH_SHORT).show();
        }
        else {
            for (File item : ListadeArchivos) {
                if (item.toString().contains(Carpeta) == true) {
                    Ruta = item.getAbsolutePath();
                    Ruta = Ruta + Formato;
                }
            }

            File Archivo = new File(Ruta);
            RutaCompresa = Archivo.toString();
            ListaDeArchivosCompresos();
            try {
                FileWriter Escribir = new FileWriter(Archivo);
                BufferedWriter bw = new BufferedWriter(Escribir);
                bw.write(Cadena.toString());
                bw.close();
                Escribir.close();
                Descompresionlzw Envio = new Descompresionlzw();
                Envio.RecibirRuta(Ruta);
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ListaDeArchivosCompresos()
    {
        File ArchivoOrginal = new File(RutaAbsoluta);
        File ArchivoCompreso = new File(RutaCompresa);
        Double RazondeCompresion;
        Double FactordeCompresion;
        Double PorcentajedeCompresion;

        RazondeCompresion = Double.longBitsToDouble(ArchivoCompreso.length())/Double.longBitsToDouble(ArchivoOrginal.length());
        FactordeCompresion = Double.longBitsToDouble(ArchivoOrginal.length())/Double.longBitsToDouble(ArchivoCompreso.length());
        PorcentajedeCompresion = (Double.longBitsToDouble(ArchivoCompreso.length())*100)/Double.longBitsToDouble(ArchivoOrginal.length());

        Archivo ArchivoNuevo = new Archivo(ArchivoOrginal.getName(),RutaCompresa,RazondeCompresion.toString(),FactordeCompresion.toString(),PorcentajedeCompresion.toString());
        ListadeArchivos.add(ArchivoNuevo);

        Mis_Compresiones miscompresiones = new Mis_Compresiones();
        miscompresiones.RecibirDatos(ListadeArchivos);
    }

}
