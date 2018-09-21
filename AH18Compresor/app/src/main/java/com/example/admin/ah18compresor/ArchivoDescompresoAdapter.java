package com.example.admin.ah18compresor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class ArchivoDescompresoAdapter extends BaseAdapter
{

    //Se crean objetos de tipo ArrayList y Activity
    protected Activity activity;
    protected ArrayList<Archivo> items;

    //Se inicializa el constructor
    public ArchivoDescompresoAdapter(Activity activity, ArrayList<Archivo> items) {
        this.activity = activity;
        this.items = items;
    }


    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Archivo> Archivo) {
        for (int i =0; i < Archivo.size(); i++) {
            items.add(Archivo.get(i));
        }
    }

    //Funcion para obtener el numero
    @Override
    public int getCount() {
        return items.size();
    }

    //Funcion para obtener un elemento especifico o su id
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    //Se modifican los valores para mostrarlos correctamente en la view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.formato_miscompresiones, null);
        }

        Archivo dir = items.get(position);
        String Aux = "";

        TextView Nombre = (TextView) v.findViewById(R.id.Nombre);
        Nombre.setText(dir.getNombre());
        TextView Ruta = (TextView) v.findViewById(R.id.txtRuta);
        Aux = "La Ruta del Archivo es: "+dir.getRuta();
        Ruta.setText(Aux);
        TextView Razon = (TextView) v.findViewById(R.id.txtRazondeCompresion);
        Aux = "La Razón de Compresión es: "+dir.getRazon();
        Razon.setText(Aux);
        TextView Factor = (TextView) v.findViewById(R.id.txtFactordeCompresion);
        Aux = "El Factor de Compresión es: "+dir.getFactor();
        Factor.setText(Aux);
        TextView Porcentaje = (TextView) v.findViewById(R.id.txtPorcentaje);
        Aux = "El Porcentaje de Reducción es: "+dir.getPorcentaje()+"%";
        Porcentaje.setText(Aux);

        return v;
    }

}
