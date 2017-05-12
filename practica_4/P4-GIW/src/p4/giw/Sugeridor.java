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
    private HashMap<Integer,HashMap<Integer,Integer>> usuarios;
    
    
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
    
    /*
    * Función para la acumulación de los perfiles de usuario 
    * en función de sus valoraciones
    *
    */
    
    public void mapReduce(){
        usuarios = new HashMap<Integer,HashMap<Integer,Integer>>();
        
        //Se inicializa el vector de usuarios
        for(int i = 0; i< valoraciones.size(); i++){
            usuarios.put(((Valoracion)valoraciones.get(i)).getIdUser(), new HashMap<Integer,Integer>());
        }
        
        //Se produce el mapeado-reducción
        for(int i = 0; i< valoraciones.size(); i++){
            usuarios.get(((Valoracion)valoraciones.get(i)).getIdUser()).put(((Valoracion)valoraciones.get(i)).getIdMovie(),((Valoracion)valoraciones.get(i)).getValoracion());
        }
    }
    
    /*
    * Función que calcula lo vecinos de un usuario en función de las valoraciones que ha dado a sus películas
    * asignadas.
    *
    */
    public ArrayList<Integer> calculaVecinos(HashMap<Integer,Integer> valors){
        int numVecinos = 0;
        int i = 0;
        int acumulacion1, acumulacion2, acumulacion3;
        double resultado;
        ArrayList<Integer> vecinos = new ArrayList<Integer>();
        
        
        Integer[] keys = (Integer[]) usuarios.keySet().toArray(new Integer[usuarios.keySet().size()]);
        Set<Integer> keysUsuario = valors.keySet();
        
        while(numVecinos < 10){
            acumulacion1 = 0;
            acumulacion2 = 0;
            acumulacion3 = 0;
            int aux = 0;
            for(int keyM : keysUsuario){
                if(usuarios.get(keys[i]).containsKey(keyM)){
                    aux++;
                    acumulacion2 += Math.pow(valors.get(keyM),2);
                    acumulacion1 += usuarios.get(keys[i]).get(keyM) * valors.get(keyM);
                    acumulacion3 += Math.pow(usuarios.get(keys[i]).get(keyM),2);
                }
            }
            
            resultado = acumulacion1/(Math.sqrt(acumulacion2) * Math.sqrt(acumulacion3));
            
            //System.out.println(keys[i] + " " + resultado);
            //System.out.println(acumulacion1 + " " + acumulacion2 + " " +acumulacion3);
            if(resultado > 0.5){
                numVecinos++;
                vecinos.add(keys[i]);
                System.out.println(keys[i] + " " + resultado);
                System.out.println("El numero de coincidencias en este vecino es: "+aux);
            }
            i++;
        }
        
        return vecinos;
    }
}
