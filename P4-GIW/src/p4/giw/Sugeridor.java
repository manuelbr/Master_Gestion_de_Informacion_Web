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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author manuelbr
 */
public class Sugeridor {
    private static ArrayList<Object> peliculas;
    private static ArrayList<Object> valoraciones;
    private HashMap<Integer,HashMap<Integer,Integer>> usuarios;
    private HashMap<Integer,Double> medias;
    private HashMap<Integer,Integer> pelisSugeridas;
    double mediaUsuario;
    
    
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
    
    public HashMap<Integer,Integer> getPelisSugeridas(){
        return pelisSugeridas;
    }
    
    public static void loadData(String rutaValoraciones,String rutaPeliculas) throws FileNotFoundException, IOException {
        
        //Se cargan los archivos con los datos
        valoraciones = readData(rutaValoraciones);
        peliculas = readData(rutaPeliculas);
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
    
    public void calculaPeliculas(HashMap<Integer,Integer> valors,HashMap<Integer,Double> vecinos){
        Integer[] vecinosId = vecinos.keySet().toArray(new Integer[vecinos.keySet().size()]);
        pelisSugeridas = new HashMap<Integer,Integer>();
        ArrayList<Integer> pelis = new ArrayList<Integer>();
        
        double similitud = 0;
        double acumulador = 0;
        double acumulador2 = 0;
        
        //Se calculan las películas que van a ser evaluadas
        for(int i = (vecinosId.length-10); i<vecinosId.length; i++){
            similitud = vecinos.get(vecinosId[i]);
            acumulador += Math.abs(similitud);
            Set<Integer> keyMovies = usuarios.get(vecinosId[i]).keySet();
            for(int key : keyMovies){
                if(!valors.containsKey(key)){
                   pelis.add(key);
                }
            }
        }
        
        //Se calcula el rate predicho por el usuario para esas películas
        for(int i = 0; i<pelis.size(); i++){
            for(int k = (vecinosId.length-10); k<vecinosId.length; k++){
                if(usuarios.get(vecinosId[k]).containsKey(pelis.get(i))){
                    acumulador2 += similitud*(usuarios.get(vecinosId[k]).get(pelis.get(i)) - medias.get(vecinosId[k]));
                }
            }
            int calificacion = (int)(mediaUsuario*(acumulador2/acumulador));
            
            if(calificacion > 5){
                pelisSugeridas.put(pelis.get(i), 5);
            }else
                pelisSugeridas.put(pelis.get(i), calificacion);
            
        }
    }
    
    /*
    * Función que calcula lo vecinos de un usuario en función de las valoraciones que ha dado a sus películas
    * asignadas. Para el cálculo de los vecinos, se usa la correlación de Pearson, ya que también se ha 
    * probado con la función coseno, obteniendo resultados mediocres.
    */
    public HashMap<Integer,Double> calculaVecinos(HashMap<Integer,Integer> valors){
        int numVecinos = 0;
        int acumulacion1, acumulacion2, acumulacion3;
        double resultado;
        HashMap<Integer,Double> vecinos = new HashMap<Integer,Double>();
        medias = new HashMap<Integer,Double>();
        mediaUsuario = 0;
        
        Integer[] keys = (Integer[]) usuarios.keySet().toArray(new Integer[usuarios.keySet().size()]);
        Set<Integer> keysUsuario = valors.keySet();
        
        //Se calculan las medias de rate de los usuarios en función de los hash que se tienen.
        for(int k = 0; k<keys.length; k++){
            Set<Integer> keysMoviesUsuario = usuarios.get(keys[k]).keySet();
            int acumulador_Notas = 0;
            int acumulador_NumPelis = 0;
            for(int keyM : keysMoviesUsuario){
                acumulador_Notas += usuarios.get(keys[k]).get(keyM);
                acumulador_NumPelis++;
            }
            medias.put(keys[k], (acumulador_Notas+0.0)/(acumulador_NumPelis+0.0));
        }
        //Media de rate del usuario
        for(int keyM : keysUsuario){
            mediaUsuario += valors.get(keyM);
        }
        mediaUsuario = mediaUsuario/valors.size();
        
        for(int p = 0; p<keys.length; p++){
            acumulacion1 = 0;
            acumulacion2 = 0;
            acumulacion3 = 0;
            int aux = 0;
            for(int keyM : keysUsuario){
                if(usuarios.get(keys[p]).containsKey(keyM)){
                    aux++;
                    acumulacion2 += Math.pow((usuarios.get(keys[p]).get(keyM) - medias.get(keys[p])),2);
                    acumulacion1 += (usuarios.get(keys[p]).get(keyM) - medias.get(keys[p])) * (valors.get(keyM) - mediaUsuario);
                    acumulacion3 += Math.pow((valors.get(keyM) - mediaUsuario),2);
                    //System.out.println("El usuario: "+keys[p]+" para la peli: "+keyM+" tiene una valoracion de: "+usuarios.get(keys[p]).get(keyM)+" y el usuario ha dado una puntuación de: "+valors.get(keyM));
                }
            }
            
            if(acumulacion2 != 0 && acumulacion3 != 0){
                //System.out.println("El usuario: "+keys[p]+" ha coincidido en: "+aux);
                resultado = acumulacion1/(Math.sqrt(acumulacion2) * Math.sqrt(acumulacion3));
            }else
                resultado = 0;
               
            vecinos.put(keys[p], resultado);
        }
        
        //Se devuelve el vecindario ordenado
        Map<Integer, Double> map = sortByValues(vecinos); 
        HashMap<Integer,Double> vecinosFinales = new HashMap<Integer,Double>();
        
        Set<Integer> ks = map.keySet();
        for(int k: ks){
            if(map.get(k) != 0){
                //System.out.println(k + " " + map.get(k));
                vecinosFinales.put(k,map.get(k));
            }
        }
        
       
        return vecinosFinales;
    }
    
    /*
    * Clase de comparación para ordenar el vecindario de menor a mayor
    *
    */
    
    public static HashMap sortByValues(HashMap map) { 
       LinkedList list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       } 
       return sortedHashMap;
    }
}
