package p4.giw;

import java.util.ArrayList;

/**
 *
 * @author manuelbr
 */
public class Pelicula {
    private int id;
    private String titulo;
    //El resto de campos como: la fecha de estreno o la url de la película son irrelevantes para el proceso que realizamos.
    
    public Pelicula(){
    
    }
    
    public Pelicula(int i, String title){
        id = i;
        titulo = title;
    }
    
    //Setters/////////////////////////////////////////////////////
    public void setId(int i){
        id = i;
    }
    
    public void setTitulo(String title){
        titulo = title;
    }
    /////////////////////////////////////////////////////////////
    
    //Getters////////////////////////////////////////////////////
    public int getId(){
        return id;
    }
    
    public String getTitulo(){
        return titulo;
    }
    /////////////////////////////////////////////////////////////
}
