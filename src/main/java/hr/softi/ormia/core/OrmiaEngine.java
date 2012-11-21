package hr.softi.ormia.core;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 16.11.12.
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class OrmiaEngine {
    private Connection connection;

    public OrmiaEngine(Connection connection){
        this.connection = connection;
    }

    public <T> T getData(Class<T> iface) {
        OrmiaProxy ormiaProxy = new OrmiaProxy(connection);
        Object proxy = java.lang.reflect.Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class[] {iface},
                ormiaProxy);
        return (T)proxy;
    }

}
