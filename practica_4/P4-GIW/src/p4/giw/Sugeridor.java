/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p4.giw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author manuelbr
 */
public class Sugeridor {
    private static ArrayList<Object> peliculas;
    private static ArrayList<Object> valoraciones;
    
    
    /*
    * Función para la carga de los archivos de texto plano con los datos de películas y de opiniones de usuarios sobre ellas.
    * La función sirve para la carga de ambos archivos, pasándole como argumento la ruta donde se encuentran ambos ficheros.
    */

    public static ArrayList<Object>  readData(String route) throws FileNotFoundException, IOException{
        BufferedReader in = new BufferedReader(new FileReader(route));
        ArrayList<Object> lista = new ArrayList<Object>();
        
        String line;
        String[] lineSplited;
        ArrayList<Integer> generos;
        while((line = in.readLine()) != null){
            
            switch(route){
                case "data/u.data": lineSplited = line.replaceAll("[^\\S\\r\\n]+", " ").trim().split(" ");
                                    Valoracion v = new Valoracion(Integer.parseInt(lineSplited[0]),Integer.parseInt(lineSplited[1]), Integer.parseInt(lineSplited[2]), Integer.parseInt(lineSplited[3]));
                                    lista.add(v);
                                    break;
                                    
                case "data/u.item": line = new String(line.getBytes("UTF-8"), "UTF-8");
                                    lineSplited = line.split("\\|");
                                    generos = new ArrayList<Integer>();
                                    for(int i = 5; i<24; i++){
                                        generos.add(Integer.parseInt(lineSplited[i]));
                                    }
                                    Pelicula p = new Pelicula(Integer.parseInt(lineSplited[0]),lineSplited[1], generos);
                                    lista.add(p);
                                    break;
            }
        }
        in.close();
        
        return lista;
    }
    
    public static ArrayList<Object> getPeliculas(){
        return peliculas;
    }
    
    public static ArrayList<Object> getValoraciones(){
        return valoraciones;
    }
    
    public static void loadData() throws FileNotFoundException, IOException {
        
        //Se cargan los archivos con los datos
        valoraciones = readData("data/u.data");
        peliculas = readData("data/u.item");
    }
    
    public ArrayList<Integer> calculaVecinos(HashMap<Integer,Integer> valors){
        ArrayList<Valoracion> aComparar = new ArrayList<Valoracion>();
        
        for(int i = 0; i<valoraciones.size(); i++){
            //if(((Valoracion) valoraciones.get(i)).getIdMovie() == )
        }
    }
}
