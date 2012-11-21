package hr.softi.ormia.lang;

/**
 * Created with IntelliJ IDEA.
 * User: sbuljat
 * Date: 11/21/12
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 * TODO Add other methodes like inList, equals, ...
 */
public class OrmiaLang {

    public static boolean $empty(Object arg){
      if(arg == null){
          return true;
      }

      return false;
    }
}
