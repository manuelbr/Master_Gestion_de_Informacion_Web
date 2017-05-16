
package p4.giw;

/**
 *
 * @author manuelbr
 */
public class Valoracion {
    private int idUser;
    private int idMovie;
    private int valoracion;
    private int timeStamp;
    
    public Valoracion(){
    
    }
    
    public Valoracion(int idu, int idm, int val, int time){
        idUser = idu;
        idMovie = idm;
        valoracion = val;
        timeStamp = time;
    }
    
    //Setters/////////////////////////////////////////////////////
    public void setIdUser(int id){
        idUser = id;
    }
    
    public void setIdMovie(int id){
        idMovie = id;
    }
    
    public void setValoracion(int val){
        valoracion = val;
    }
    
    public void setTimeStamp(int time){
        timeStamp = time;
    }
    /////////////////////////////////////////////////////////////////
    
    //Getters////////////////////////////////////////////////////////
    public int getIdUser(){
        return idUser;
    }
    
    public int getIdMovie(){
        return idMovie;
    }
    
    public int getValoracion(){
        return valoracion;
    }
    
    public int getTimeStamp(){
        return timeStamp;
    }
    /////////////////////////////////////////////////////////////////
}
