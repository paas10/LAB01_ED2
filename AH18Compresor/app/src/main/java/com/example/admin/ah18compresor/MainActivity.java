package com.example.admin.ah18compresor;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Ruta del Archivo
    String Ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        //Se Instancia el Fragment Manager
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {

            final Huffman ExtraerDatos = new Huffman();
            final LZW LExtraerDatos = new LZW();
            //Se Extraen los datos Leidos de la Estructura para fijarlos en la Actividad
            List<String> Lista = ExtraerDatos.EnviarNombres();

                final String[] ListaNombres = new String[Lista.size()];
                int contador = 0;
                for(String i: Lista)
                {
                    ListaNombres[contador]= i;
                    contador++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Selecciona la Ruta para Guardar tus Compresiones");

                int checkedItem = 1; // cow
                builder.setSingleChoiceItems(ListaNombres, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });

                    builder.setItems(ListaNombres,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int item)
                        {
                            Toast.makeText(getApplicationContext(),"Has Elegido Guardar tus Compresiones en: " + ListaNombres[item], Toast.LENGTH_SHORT).show();
                           //Se envian los datos a los Fragments
                            ExtraerDatos.RecibirRuta(ListaNombres[item]);
                            LExtraerDatos.RecibirRuta(ListaNombres[item]);
                            Ruta = ListaNombres[item];
                        }
                    });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Cancel", null);


                AlertDialog dialog = builder.create();
                dialog.show();

        } else if (id == R.id.DescompresionLZW)
        {
            final Descompresionlzw LExtraerDatos = new Descompresionlzw();
            if(Ruta == null)
            {
                Toast.makeText(getApplicationContext(),"No Hay Ningun Archivo Para Mostrar Aún",Toast.LENGTH_LONG).show();
            }
            else if(LExtraerDatos.Ruta == null)
            {
                Toast.makeText(getApplicationContext(),"No Hay Ningun Archivo Para Mostrar Aún",Toast.LENGTH_LONG).show();
            }
            else {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Descompresionlzw()).commit();
            }

        }
        else if (id == R.id.Descompresion)
        {
            final Descompresion ExtraerDatos = new Descompresion();
            if(Ruta == null)
            {
                Toast.makeText(getApplicationContext(),"No Hay Ningun Archivo Para Mostrar Aún",Toast.LENGTH_LONG).show();
            }
            else if(ExtraerDatos.Ruta == null)
            {
                Toast.makeText(getApplicationContext(),"No Hay Ningun Archivo Para Mostrar Aún",Toast.LENGTH_LONG).show();
            }
            else {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Descompresion()).commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Huffman()).commit();

        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new LZW()).commit();
        } else if (id == R.id.nav_slideshow) {
            final Bitacora Recuperar = new Bitacora();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Bitacora()).commit();

        } else if (id == R.id.nav_manage) {

            final Mis_Compresiones Recuperar = new Mis_Compresiones();
            if(Recuperar.ListadeArchivosCompresos == null)
            {
                Toast.makeText(getApplicationContext(),"No Hay Ningun Archivo Para Mostrar Aún",Toast.LENGTH_LONG).show();
            }
            else {
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Mis_Compresiones()).commit();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
