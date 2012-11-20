package hr.softi.ormia.core.util;

import hr.softi.ormia.core.exception.OrmiaException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 16.11.12.
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class BeanUtility {

    private BeanUtility(){ }

    public static final <T> T populate(Class<T> klazz, ResultSet row){
        T instance = newInstance(klazz);
        populate(instance, row);
        return instance;
    }

    public static final void populate(Object bean, ResultSet row){
        if(bean==null || row==null){
            throw new OrmiaException("Unable to populate bean from result set - input arguments must be not null!", "O-BU-002");
        }

        try {
            HashMap<String, Object> rowAsMap = new HashMap<String, Object>();
            ResultSetMetaData meta = row.getMetaData();

            for (int i = 1; i <= meta.getColumnCount(); i++){
                Object value = row.getObject(meta.getColumnLabel(i));
                if (value instanceof String){
                    value = ((String)value).trim();
                }
                rowAsMap.put(meta.getColumnLabel(i).toUpperCase(), value);
            }

            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);

            for(PropertyDescriptor propDesc: beanInfo.getPropertyDescriptors()){

                Method writeMethod = propDesc.getWriteMethod();
                if(writeMethod!=null && writeMethod.getParameterTypes().length==1 && writeMethod.getModifiers()== Modifier.PUBLIC) {
                    String propertyName = propDesc.getName().toUpperCase();

                    if(rowAsMap.containsKey(propertyName)){
                        Object[] args = new Object[1];
                        args[0] = rowAsMap.get(propertyName);

                        Class<?>[] argumentTypes = writeMethod.getParameterTypes();
                        if(argumentTypes.length == args.length){
                            for(int i=0; i<argumentTypes.length; i++){
                                Class<?> type = argumentTypes[i];
                                Object value = args[i];

                                if(type.isPrimitive() && value == null){
                                    throw new OrmiaException("Unable to associate null value to a primitive type on setter " + writeMethod.getName(),"O-BU-004");
                                }

                                args[i] = Conversion.exe(value, type);
                            }

                            Object result = writeMethod.invoke(bean, args);
                        }
                    }
                }
            }

        }catch (Exception e){
            throw new OrmiaException("Unable to populate bean from result set - " + e.getMessage(), e, "O-BU-003");
        }
    }

    public static final <T> T newInstance(Class<T> klazz){
        T instance = null;
        if(klazz!=null){
            try {
                if(klazz.isMemberClass()){
                    if(!Modifier.isStatic(klazz.getModifiers())){
                        instance = klazz.getDeclaredConstructor(new Class[]{ klazz.getEnclosingClass()}).newInstance(klazz.getEnclosingClass().newInstance());
                    }else{
                        instance = klazz.getDeclaredConstructor(new Class[] {}).newInstance(new Object[] {});
                    }
                }else{
                    instance = klazz.newInstance();
                }
            }catch (Exception e){
                throw new OrmiaException("Unable to instantiate class " + klazz.getName() + " - " + e.getMessage(), e, "O-BU-001");
            }
        }

        return instance;
    }

    private static class Conversion{
        // Converts given value to a given target type - if possible
        public static Object exe(Object value, Class<?> targetType) throws ClassCastException{
            if(targetType.equals(String.class)){
                value = makeString(value);
            }else if(targetType.equals(java.util.Date.class) || targetType.equals(java.sql.Date.class)){
                value = makeDate(value);
            }else if(targetType.equals(java.sql.Time.class)){
                value = makeTime(value);
            }else if(targetType.equals(java.sql.Timestamp.class)){
                value = makeTimestamp(value);
            }else if(targetType.equals(Short.class)){
                value = makeShort(value);
            }else if(targetType.equals(Byte.class)){
                value = makeByte(value);
            }else if(targetType.equals(Integer.class)){
                value = makeInteger(value);
            }else if(targetType.equals(Long.class)){
                value = makeLong(value);
            }else if(targetType.equals(BigDecimal.class)){
                value = makeBigDecimal(value);
            }else if(value == null){
                // doesn't need conversion
            }else if(targetType.isInstance(value)){
                // doesn't need conversion
            }else if(targetType.equals(Boolean.class) || (targetType.isPrimitive() && "boolean".equals(targetType.getName()))){
                value = new Boolean(value.toString());
            }else if(targetType.isPrimitive() && "int".equals(targetType.getName())){
                value = makeInteger(value);
            }else if(targetType.isPrimitive() && "short".equals(targetType.getName())){
                value = makeShort(value);
            }else{
                throw new OrmiaException("Unable to convert " + ((value != null) ? value.getClass().getName() : "null") + " to target type " + targetType.getName() + " - no match!", "U-C-001");
            }

            return value;
        }

        private static Integer makeInteger(Object o){
            Integer result = null;

            if(o instanceof Integer) result = (Integer)o;
            else if(o instanceof Short) result = new Integer(((Short) o).intValue());
            else if(o instanceof Long) result = new Integer(((Long) o).intValue());
            else if(o instanceof Byte) result = new Integer(((Byte) o).intValue());
            else if(o instanceof String) try{result = new Integer((String)o);}catch(NumberFormatException nfe){}

            return result;
        }

        private static Short makeShort(Object o){
            Short result = null;

            if(o instanceof Integer) result = new Short(((Integer) o).shortValue());
            else if(o instanceof Short) result = (Short)o;
            else if(o instanceof Long) result = new Short(((Long) o).shortValue());
            else if(o instanceof Byte) result = new Short(((Byte) o).shortValue());
            else if(o instanceof String) try{result = new Short((String)o);}catch(NumberFormatException nfe){}

            return result;
        }

        private static Byte makeByte(Object o){
            Byte result = null;

            if(o instanceof Byte) result = (Byte)o;
            else if(o instanceof Integer) result = ((Integer)o).byteValue();
            else if(o instanceof Short) result = ((Short)o).byteValue();
            else if(o instanceof Long) result = ((Long)o).byteValue();
            else if(o instanceof String) try{result = new Byte((String)o);}catch(NumberFormatException nfe){}

            return result;
        }

        private static Long makeLong(Object o){
            Long result = null;

            if(o instanceof Integer) result = ((Integer)o).longValue();
            else if(o instanceof Short) result = ((Short)o).longValue();
            else if(o instanceof Long) result = (Long)o;
            else if(o instanceof Byte) result = ((Byte)o).longValue();
            else if(o instanceof String) try{result = new Long((String)o);}catch(NumberFormatException nfe){}

            return result;
        }

        private static BigDecimal makeBigDecimal(Object o){
            BigDecimal result = null;

            if(o instanceof BigDecimal) result = (BigDecimal) o;
            else if(o instanceof String) result =  new BigDecimal((String)o);
            else if(o instanceof Integer) result = new BigDecimal((Integer) o);
            else if(o instanceof Short) result = new BigDecimal((Short) o);
            else if(o instanceof Long) result = new BigDecimal((Long) o);
            else if(o instanceof Byte) result = new BigDecimal((Byte) o);

            return result;
        }

        private static String makeString(Object o){
            String result = null;

            if(o != null) result = o.toString();

            return result;
        }

        private static java.sql.Date makeDate(Object o){
            java.sql.Date result = null;

            if(o instanceof java.util.Date) result = new java.sql.Date(((java.util.Date)o).getTime());
            else if(o instanceof Long) result = new java.sql.Date((Long)o);

            return result;
        }

        private static java.sql.Time makeTime(Object o){
            java.sql.Time result = null;

            if(o instanceof java.util.Date) result = new java.sql.Time(((java.util.Date)o).getTime());
            else if(o instanceof Long) result = new java.sql.Time((Long)o);

            return result;
        }

        private static java.sql.Timestamp makeTimestamp(Object o){
            java.sql.Timestamp result = null;

            if(o instanceof java.sql.Timestamp){result = (java.sql.Timestamp)o;}
            else if(o instanceof java.util.Date) result = new java.sql.Timestamp(((java.util.Date)o).getTime());
            else if(o instanceof Long) result = new java.sql.Timestamp((Long)o);

            return result;
        }

    }
}
