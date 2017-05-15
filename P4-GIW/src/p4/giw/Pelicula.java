package p4.giw;

import java.util.ArrayList;

/**
 *
 * @author manuelbr
 */
public class Pelicula {
    private int id;
    private String titulo;
    private ArrayList<Integer> generos;
    //El resto de campos como: la fecha de estreno o la url de la película son irrelevantes para el proceso que realizamos.
    
    public Pelicula(){
    
    }
    
    public Pelicula(int i, String title, ArrayList<Integer> gen){
        id = i;
        titulo = title;
        generos = gen;
    }
    
    //Setters/////////////////////////////////////////////////////
    public void setId(int i){
        id = i;
    }
    
    public void setTitulo(String title){
        titulo = title;
    }
    
    public void setGeneros(ArrayList<Integer> gens){
        generos = gens;
    }
    /////////////////////////////////////////////////////////////
    
    //Getters////////////////////////////////////////////////////
    public int getId(){
        return id;
    }
    
    public String getTitulo(){
        return titulo;
    }
    
    public ArrayList<Integer> getGeneros(){
        return generos;
    }
    /////////////////////////////////////////////////////////////
}
