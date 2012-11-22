package hr.softi.ormia.core;

import hr.softi.ormia.core.annotation.Select;
import hr.softi.ormia.core.annotation.Update;
import hr.softi.ormia.core.exception.OrmiaException;
import hr.softi.ormia.lang.OrmiaLang;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;

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
public class OrmiaProxy extends OrmiaLang implements InvocationHandler {
    private Connection conn;

    public OrmiaProxy(Connection conn){
        this.conn = conn;
    }

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
        Select select = method.getAnnotation(Select.class);
        if($empty(select.value())){
            throw new OrmiaException("Invalid @Select annotation: value must be defined!", "O-OP-001");
        }

        /*
         * TODO parse sql statement, allow 3 types of params passing:
         * (1) SELECT * FROM xxx WHERE one = ? AND two = ?
         * (2) SELECT * FROM xxx WHERE two = ?2 AND one = ?1
         * (3) SELECT * FROM xxx WHERE one = :one AND two = :two
         *
         * Use StatementHelper
         */

        return null;
    }

    private Object processUpdate(Object proxy, Method method, Object[] args) throws Throwable{
        return null;
    }

//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("--proxied");
//
//        Select selectAnon = method.getAnnotation(Select.class);
//        String sql = selectAnon.sql();
//        System.out.println("Executing SQL = " + sql);
//
//        PreparedStatement statement = getConnection().prepareStatement(sql);
//        ParameterMetaData paramsMeta = statement.getParameterMetaData();
//        for(int i=1; i<=paramsMeta.getParameterCount(); i++){
//            setPreparedStatementField(statement, i, args[i-1]);
//        }
//
//        ResultSet result = statement.executeQuery();
//        result.next();
//        Object outputBean = method.getReturnType().newInstance();
//        BeanUtilities.populate(outputBean, result);
//        return outputBean;
//    }

    private static Connection getConnection(String driverClass, String url, String user, String password) {
        try {
            Class.forName (driverClass).newInstance ();
        } catch (Exception e) {
            throw new RuntimeException ("Unable to load the driver. Message " + e.getMessage ());
        }

        java.util.Properties connectionProperties = new java.util.Properties();
        connectionProperties.put("user", user);
        connectionProperties.put("password", password);

        try {
            Connection connection = DriverManager.getConnection(url,connectionProperties);
            return connection;
        } catch (SQLException e) {
            throw new OrmiaException("Unable to obtain a connection. SqlCode " + e.getErrorCode() + " Message " + e.getMessage(), "O-OP-002");
        }
    }

    private static void setPreparedStatementField(PreparedStatement pStmt, int count, Object arg) throws SQLException {
        if (arg instanceof String) {
            pStmt.setString(count, (String) arg);
        } else if (arg instanceof BigDecimal) {
            pStmt.setBigDecimal(count, (BigDecimal) arg);
        } else if (arg instanceof java.sql.Date) {
            pStmt.setDate(count, (java.sql.Date) arg);
        } else if (arg instanceof java.lang.Long) {
            pStmt.setLong(count, ((java.lang.Long) arg).longValue());
        } else if (arg instanceof java.lang.Integer) {
            pStmt.setInt(count, ((java.lang.Integer) arg).intValue());
        } else if (arg instanceof Float)
            pStmt.setFloat(count, ((Float) arg).floatValue());
        else if (arg instanceof Double)
            pStmt.setDouble(count, ((Double) arg).doubleValue());
        else if (arg instanceof byte[])
            pStmt.setBytes(count, (byte[]) arg);
        else if (arg instanceof Short)
            pStmt.setShort(count, ((Short) arg).shortValue());
        else if (arg instanceof Timestamp)
            pStmt.setTimestamp(count, ((Timestamp) arg));
        else if (arg instanceof java.sql.Time) {
            pStmt.setTime(count, (java.sql.Time) arg);
        } else if (arg == null) {
            pStmt.setNull(count, Types.DECIMAL);
        } else {
            pStmt.setObject(count, arg);
        }
    }

}
