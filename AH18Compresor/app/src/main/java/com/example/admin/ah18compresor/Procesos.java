package com.example.admin.ah18compresor;

import java.util.HashMap;
import java.util.LinkedList;

public class Procesos {

    public char[] CadenaSinRepeticion (char[] completo)
    {
        int cont = completo.length;
        String caracteres = "";

        for(int a = 0; a < cont; a++)
        {
            if(!caracteres.contains(Character.toString(completo[a])))
                caracteres += Character.toString(completo[a]);
        }

        char[] cadena = caracteres.toCharArray();

        return cadena;
    }

    public LinkedList<Node> OrdenarLinkedList(LinkedList<Node> Desordenada)
    {
        LinkedList<Node> Ordenada = new LinkedList<>();

        int conta = 0;
        do {
            int menor = 10000000;
            int contaMenor = 0;

            for (Node i:Desordenada)
            {
                if (i.getNumero() < menor)
                {
                    menor = Desordenada.peek().getNumero();
                    contaMenor = conta;
                }
                conta++;
            }

            Ordenada.offer(Desordenada.remove(contaMenor));

            conta = 0;
            contaMenor = 0;

        }while(Desordenada.size() != 0);

        return Ordenada;
    }


    // METODO QUE REVISA SI LA COMBINACION ENVIADA YA EXISTE EN EL DICCIONARIO
    public boolean Existe (HashMap Diccionario, String Combinacion)
    {
        boolean Existe = false;
        int limite = Diccionario.size();

        for (int i = 1; i <= limite; i++)
        {
            try
            {
                if (Diccionario.get(i).toString() == Combinacion)
                    Existe = true;
            }
            catch (Exception e)
            {

            }

        }

        return Existe;
    }


}
