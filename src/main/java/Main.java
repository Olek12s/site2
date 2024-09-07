import auth.Account;
import auth.AccountManager;
import database.DataBaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main
{
    public static void main(String[] args) throws SQLException
    {
        String currentPath = System.getProperty("user.dir");
        //System.out.println(currentPath);

        DataBaseConnection db = new DataBaseConnection();
        AccountManager accountManager = new AccountManager(db);

        db.connect(currentPath + "\\test.db");  // currentPath nie jest niezbędny, ale nie jest to błędem
        db.executeUpdate("DROP TABLE IF EXISTS person");
        db.executeUpdate("CREATE TABLE PERSON (id INTEGER, name STRING, password STRING)");
        //db.executeUpdate("INSERT INTO PERSON VALUES (1, 'leo', '')"); doda razem z hasłem, jest to ręczne. Lepiej używać .register(?, ?)
        //db.executeUpdate("INSERT INTO PERSON VALUES (2, 'yuai', '')");

        ResultSet rs = db.executeQuery("SELECT * FROM PERSON");

        while(rs.next())
        {
            System.out.print(rs.getInt("id"));
            System.out.println(" " + rs.getString("name"));
        }

        accountManager.register("leon", "c");
        accountManager.register("Alice", "d");
        System.out.println("Authentication: " + accountManager.authenticate("leon", "c"));

        Account myAccount1 = accountManager.getAccount("leon");
        System.out.println(myAccount1.id() + " " + myAccount1.name());

        Account myAccount2 = accountManager.getAccount(1);
        System.out.println(myAccount2.id() + " " + myAccount2.name());

    }
}
