package hr.softi.ormia.core.exception;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 16.11.12.
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
public class OrmiaException extends RuntimeException {
    private String uniqueId;

    public OrmiaException(){
        super();
    }

    public OrmiaException(String message){
        super(message);
    }

    public OrmiaException(Throwable t){
        super(t);
    }

    public OrmiaException(String message, Throwable t){
        super(message, t);
    }

    public OrmiaException(String message, String uniqueId){
        this(message);
        this.uniqueId = uniqueId;
    }

    public OrmiaException(Throwable t, String uniqueId){
        this(t);
        this.uniqueId = uniqueId;
    }

    public OrmiaException(String message, Throwable t, String uniqueId){
        this(message, t);
        this.uniqueId = uniqueId;
    }

    // Getters&Setters
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

}
