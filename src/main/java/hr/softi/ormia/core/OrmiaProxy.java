package hr.softi.ormia.core;

import hr.softi.ormia.core.annotation.Select;
import hr.softi.ormia.core.annotation.Update;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 16.11.12.
 * Time: 15:07
 *
 * Dynamic Proxy which processes given interface with SQL annotations (@Select, @Update,...).
 * This is the integral part of Ormia framework because it's responsible for execution and O/R mapping of all SQLs.
 *
 * TODO: Add caching support (per session, per application,...) see JSR??? for caching in JavaEE
 *
 */
public class OrmiaProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getAnnotation(Update.class)!=null){
            Object retUpdate = processUpdate(proxy,method,args);
            if(method.getAnnotation(Select.class)!=null){
                //We're allowing this construct @Update @Select
                return processSelect(proxy,method,args);
            }
        }
        if(method.getAnnotation(Select.class)!=null){
            return processSelect(proxy,method,args);
        }
        return null;
    }

    private Object processSelect(Object proxy, Method method, Object[] args) throws Throwable{
        return null;
    }

    private Object processUpdate(Object proxy, Method method, Object[] args) throws Throwable{
        return null;
    }
}
