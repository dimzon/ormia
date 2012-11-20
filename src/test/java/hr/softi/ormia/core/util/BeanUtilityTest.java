package hr.softi.ormia.core.util;

import junit.framework.Assert;
import org.h2.tools.Csv;
import org.h2.tools.SimpleResultSet;
import org.junit.Test;

import java.sql.Types;

/**
 * Created with IntelliJ IDEA.
 * User: stipe
 * Date: 20.11.12.
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class BeanUtilityTest {

    @Test
    public void testNewInstance() throws Exception {
        Assert.assertNotNull(BeanUtility.newInstance(Player.class));
    }

    @Test
    public void testPopulateByClass() throws Exception {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("NAME", Types.VARCHAR, 255, 0);
        rs.addColumn("NUMBER", Types.INTEGER, 0, 0);
        rs.addRow("Michael Jordan", 23);
        rs.next();

        Player mj23 = BeanUtility.populate(Player.class,rs);
        Assert.assertEquals("Michael Jordan", mj23.getName());
        Assert.assertEquals(23, mj23.getNumber());
    }

    @Test
    public void testPopulateByObject() throws Exception {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("NAME", Types.VARCHAR, 255, 0);
        rs.addColumn("NUMBER", Types.INTEGER, 0, 0);
        rs.addRow("Michael Jordan", 23);
        rs.next();

        Player mj23 = new Player();
        BeanUtility.populate(mj23, rs);
        Assert.assertEquals("Michael Jordan", mj23.getName());
        Assert.assertEquals(23, mj23.getNumber());
    }

    public class Player{
        private String name;
        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

}
