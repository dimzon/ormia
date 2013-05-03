### ORMia = Java O/R mapping framework inspired by IBM's pureQuery
--

O/R frameworks tend to treat SQL as a second-class citizen, this is not the case in "ORMia".
Why are we trying to hide SQL from the developer and then introduce some other languages like HQL, JPQL,...?
All we need is a simple way of translating relational data to Java objects and vice versa.
Who said that implies not writing sequel statements? 
Don't know, but this framework doesn't generate sequel statements for you, this is something that's still needed.

ORMia goals:
- simplify JDBC
- programming to interfaces
- use interfaces as a SQL statement containers
- allow reuse
- automatic object <-> relational translation

##### Usage
    // MyDAO.java
    public interface MyDAO {
      @Select("SELECT * FROM person WHERE id = ?)
      Person getPersonById(String id);
    }

    // MyAPP.java
    public class MyAPP {
      public static void main(String[] args) {
        Person p = ORMia.getData(MyDAO.class).getPersonById("1234");
      }
    }
As you can see all you need is to write an interface that declares a method with appropriate annotation which holds a SQL, that's it!


###### TODO
- build core engine based on DynamicProxy implementation
- add support for: @Select, @Update, @Call
- add suport for cursors (positioned update, hold cursors over commit): @Cursor
- add support for command line object generation from relational table (like TABLE PERSON -> Person.java & PersonData.java)
- add caching support for querying data (ability to clear cache, use global cache,..)