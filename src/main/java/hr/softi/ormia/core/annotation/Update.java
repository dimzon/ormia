package hr.softi.ormia.core.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 20.11.12.
 * Time: 08:11
 * To change this template use File | Settings | File Templates.
 */
public @interface Update {
    public abstract String value(); //SQL
    public abstract String cursorName() default ""; //Positioned Cursor name
}
