package hr.softi.ormia.core;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 16.11.12.
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class OrmiaEngine {

    public static <T> T getData(Class<T> iface) {
        OrmiaProxy ormiaProxy = new OrmiaProxy();
        Object proxy = java.lang.reflect.Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class[] {iface},
                ormiaProxy);
        return (T)proxy;
    }

    public String getName(){
        return "";
    }
}
