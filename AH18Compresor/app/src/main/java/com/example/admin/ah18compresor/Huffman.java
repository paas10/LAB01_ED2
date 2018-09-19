package com.example.admin.ah18compresor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.Environment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class Huffman extends Fragment implements OnItemClickListener {

    private List<String> NombresArchivos;
    private List<String> RutasArchivos;
    private List<String> RutasArchivosNoOrdenados;
    private ArrayAdapter<String> Adaptador;
    private String DirectorioRaiz;
    private TextView CarpetaActual;
    ListView Lista;
    static String Carpeta;
    static String ArchivoT;

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

        //Adaptador = new ArrayAdapter<String>(getActivity(),R.layout.fragment_huffman,R.id.RutaActual,NombresArchivos);
        final ArchivoAdapter Adaptador = new ArchivoAdapter(getActivity(), (ArrayList<String>) NombresArchivos);
        Lista.setAdapter(Adaptador);
    }

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

        return "-1";
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
                Dialogo.setMessage("¿Desea Aplicar El Metodo de Compresión Huffman a este Archivo?");
                Dialogo.setCancelable(false);
                Dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {
                        ConfirmarHuffman(archivo);
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


    public void ConfirmarHuffman(File Archivo) {
        Toast t=Toast.makeText(getActivity(),"Dentro de un momento el archivo: "+Archivo.getName()+" Sera enviando al método de compresion de Huffman y sera mostrado", Toast.LENGTH_SHORT);
        t.show();

        ArchivoT = Archivo.toString();

        String Texto = LeerArchivo(Archivo);
        //String Texto = "Tres tristes tigres";

        StringBuilder CodArchivo = new StringBuilder();
        StringBuilder CerosUnos = new StringBuilder();

        LinkedList<Node> Caracteres = new LinkedList<>();
        Caracteres = ObtenerCaracteresRepeticiones(Texto);

        for (Node i : Caracteres)
        {
            CodArchivo.append(Character.toString(i.getCaracter())+i.getNumero());
        }

        CodArchivo.append("/");

        //YA TENGO UNICAMENTE LOS CARACTERES CON LAS REPETICIONES

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

        // YA SE ARMO EL ARBOL

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

        char [] TextoOriginal = Texto.toCharArray();
        for (char caracter : TextoOriginal)
        {
            boolean encontrado = false;

            int cont = 0;
            while (encontrado != true)
            {
                if(caracter == tabla[cont].getCaracter())
                {
                    CerosUnos.append(tabla[cont].getCod());
                    encontrado = true;
                }
                cont++;
            }
        }

        String Variable = CerosUnos.toString();
        StringBuilder Prueba = CodArchivo.append(Codificar(Variable));
        Escribir(Prueba);
    }


    private static String Codificar(String Texto)
    {
        List<String> ListadeBytes = new ArrayList<>();
        List<Character> ListadeBytesChar = new ArrayList<>();
        List<Character> ListadeBytesExportados = new ArrayList<>();
        String Byte = "";
        char [] Cadena = Texto.toCharArray();
        int ContadorExterno = 0;
        int ContadorInterno = 0;
        int CantidadDeCaracteres = Cadena.length;

        while(ContadorExterno != Cadena.length)
        {
            if(ContadorInterno !=  8)
            {
                Byte = Byte + Character.toString(Cadena[ContadorExterno]);
                ContadorInterno++;
                ContadorExterno++;
                if(ContadorExterno == Cadena.length)
                {
                    ListadeBytes.add(Byte);
                }

            }
            else
            {
                ListadeBytes.add(Byte);
                ContadorInterno = 0;
                Byte = "";
            }
        }

        String Complemento = "";
        int Posicion = 0;
        for (String item: ListadeBytes)
        {
            if(item.length() != 8)
            {
                int caracteres = item.length();
                int Iteraciones = 8-caracteres;
                for(int i = 1; i<= Iteraciones;i++)
                {
                    Complemento = Complemento + "0";
                }
                item = item+Complemento;
                ListadeBytes.set(Posicion, item);
            }
            Posicion++;
        }

        String ListaRetorno = "";
        for (String item: ListadeBytes)
        {
            char Char = (char)(Integer.parseInt(item));
            ListaRetorno = ListaRetorno+Character.toString(Char);
        }

        return ListaRetorno;
    }

    public List<String> EnviarNombres()
    {
            DirectorioRaiz = Environment.getExternalStorageDirectory().getPath();
            NombresArchivos = new ArrayList<String>();
            RutasArchivos = new ArrayList<String>();
            int count = 0;
            File directorioactual = new File(DirectorioRaiz);
            File[] ListadeArchivos = directorioactual.listFiles();

            if (!DirectorioRaiz.equals(DirectorioRaiz)) {
                NombresArchivos.add("../");
                RutasArchivos.add(directorioactual.getParent());
                count = 1;
            }

            for (File archivo : ListadeArchivos) {
                RutasArchivos.add(archivo.getPath());
            }
            Collections.sort(RutasArchivos, String.CASE_INSENSITIVE_ORDER);

            for (int i = count; i < RutasArchivos.size(); i++) {
                File archivo = new File(RutasArchivos.get(i));

                if (archivo.isFile()) {
                    NombresArchivos.add(archivo.getName());
                } else {
                    NombresArchivos.add("/" + archivo.getName());
                }
            }

            if (ListadeArchivos.length < 1) {
                NombresArchivos.add("No hay ningun archivo");
                RutasArchivos.add(DirectorioRaiz);
            }

            return NombresArchivos;
    }

    public String RecibirRuta(String carpeta)
    {
        Carpeta = carpeta;
        return Carpeta;
    }


    public void CancelarHuffman() {
        Toast.makeText(getActivity(), "Selecciona Otro Archivo para la Compresión Huffman",Toast.LENGTH_SHORT).show();
    }


    // Retorna una tabla con cada caracter y sus repeticiones en el texto.
    public LinkedList<Node> ObtenerCaracteresRepeticiones (String texto)
    {
        Procesos procesos = new Procesos();

        char[] fragmentado = texto.toCharArray();

        int cont = fragmentado.length;
        char [] Filtro = new char[cont];

        // Ciclo exteriror recorre la cadena de caracteres completa
        // Ciclo interior unicamente registra los datos nuevos (1 vez cada caracter)

        Filtro = procesos.CadenaSinRepeticion(fragmentado);

        // HASTA EL MOMENTO SE COLOCARON TODOS LOS CARACTERES EN UNA CADENA DE CHAR.

        int cantCaracteres = Filtro.length;

        LinkedList<Node> tabla = new LinkedList<>();

        for (int i = 0; i < cantCaracteres; i++)
        {
            char symbol = Filtro[i];
            int contador = 0;
            for (int n = 0; n < cont; n++)
            {
                if (fragmentado[n] == symbol)
                    contador++;
            }

            Node nTemp = new Node(Filtro[i], contador);

            tabla.add(nTemp);
        }

        // YA SE CREÓ LA TABLA CON LA CANTIDAD DE REPETICIONES DE CADA CARACTER

        tabla = procesos.OrdenarLinkedList(tabla);

        return tabla;
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

    private void Escribir(StringBuilder Cadena)
    {
            MainActivity P = new MainActivity();
            File directorioactual = new File(DirectorioRaiz);
            File[] ListadeArchivos = directorioactual.listFiles();
            String Ruta = "";
            File ArchivoNuevo = new File(ArchivoT);
            String Formato = "/"+ArchivoNuevo.getName().replace(".txt",".huff");

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
                try {
                    FileWriter Escribir = new FileWriter(Archivo, true);
                    BufferedWriter bw = new BufferedWriter(Escribir);
                    bw.write(Cadena.toString());
                    bw.close();
                    Escribir.close();
                } catch (IOException ex) {
                    Toast.makeText(getActivity(), "No se Ha podido leer el archivo", Toast.LENGTH_SHORT).show();
                }
            }
    }

}
