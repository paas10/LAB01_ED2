package com.example.admin.ah18compresor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class Mis_Compresiones extends Fragment {


    static List<Archivo> ListadeArchivosCompresos;
    static List<Archivo> ListadeArchivosCompresoshuff = new ArrayList<>();
    static List<Archivo> ListadeArchivosCompresoslzw = new ArrayList<>();
    ListView Lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mis__compresiones, container, false);

        Lista = (ListView) view.findViewById(R.id.ListaArchivosCompresos);
        final ArchivoDescompresoAdapter Adaptador = new ArchivoDescompresoAdapter(getActivity(), (ArrayList<Archivo>) ListadeArchivosCompresos);
        Lista.setAdapter(Adaptador);
        return view;
    }

    public void RecibirDatos(List<Archivo> ListadeArchivos)
    {

        List<Archivo> ListaAuxiliar = new ArrayList<>();
        if(ListadeArchivos.get(0).getRuta().contains(".huff") == true)
        {
                ListadeArchivosCompresoshuff = ListadeArchivos;
        }
        else if (ListadeArchivos.get(0).getRuta().contains(".LZW") == true)
        {
            ListadeArchivosCompresoslzw = ListadeArchivos;
        }

        if (ListadeArchivosCompresoshuff != null && ListadeArchivosCompresoslzw != null)
        {
            for (Archivo item: ListadeArchivosCompresoshuff)
            {
                ListaAuxiliar.add(item);
            }

            for (Archivo item: ListadeArchivosCompresoshuff)
            {
                ListaAuxiliar.add(item);
            }

        }
        else if (ListadeArchivosCompresoshuff == null)
        {
            ListadeArchivosCompresos = ListadeArchivosCompresoslzw;
        }
        else if (ListadeArchivosCompresoslzw == null)
        {
            ListadeArchivosCompresos = ListadeArchivosCompresoshuff;
        }

        ListadeArchivosCompresos = ListaAuxiliar;
        Bitacora Envio = new Bitacora();
        Envio.RecibirDatos(ListadeArchivosCompresos);

    }


}