package auth;

import database.DataBaseConnection;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager
{
    DataBaseConnection connection;
    public static int IDENTIFICATOR = 0;

    public AccountManager(DataBaseConnection connection) {
        this.connection = connection;
    }

    public void register(String name, String password)
    {
        // String sql = "INSERT INTO PERSON VALUES (" + id + ", " + "'" + name + "'" + ")";
        //connection.executeUpdate(sql);    // można tak zrobić i to zadziała, ale wrażliwe jest to na SQL Injection
        try
        {
            //Sprawdzanie, czy czasem użytkownik o takim samym imieniu istnieje
            String checkSql = "SELECT * FROM PERSON WHERE name = ?";
            PreparedStatement chkStmt = connection.getConnection().prepareStatement(checkSql);
            chkStmt.setString(1, name);

            //checkStmt.execute();    // nic sie nie wstawi, to tylko wykonanie SELECT
            ResultSet rs = chkStmt.executeQuery();
            if (rs.next())    // sprawdza, czy zapytanie SQL zwróciło przynajmniej jeden wiersz danych, można też zapisać if (checkStmt.getResultSet().next())
            {
                throw new RuntimeException("User " + name + " already exists.");
            }


            //Wstawienie użytkownika do bazy danych
            String sql = "INSERT INTO PERSON (id, name, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            stmt.setInt(1, IDENTIFICATOR);
            stmt.setString(2, name);
            //stmt.setString(3, password);    // nieszyfrowane hasło
            stmt.setString(3, BCrypt.withDefaults().hashToString(8, password.toCharArray()));


            stmt.executeUpdate();
            IDENTIFICATOR++;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean authenticate(String name, String password)
    {
        try
        {
            String sql = "SELECT * FROM PERSON WHERE name = ? AND password = ?";
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, password);

            //stmt.execute();
            ResultSet rs = stmt.executeQuery();

            return rs.next();  // Zwraca true, jeśli istnieje wiersz, czyli użytkownik i hasło są prawidłowe
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;   // false w przypadku błędu
    }

    public Account getAccount(String name)
    {
        try
        {
            String sql = "SELECT ID, NAME FROM PERSON WHERE name = ?;";
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            //stmt.execute();                           // Można też tak, ale trzeba zapisać te dwie zakomentowane linijki.
            //ResultSet rs = stmt.getResultSet();
            if (rs.next())
            {
                int id = rs.getInt("id");
                String userName = rs.getString("name");
                return new Account(id, userName);
            }
            else
            {
                System.err.println("Użytkownik o nazwie " + name + " nie istnieje");
                return null;
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public Account getAccount(int id)
    {
        String sql = "SELECT ID, NAME FROM PERSON WHERE ID = ?";

        try
        {
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);

           stmt.setInt(1, id);

           ResultSet rs = stmt.executeQuery();
           //stmt.execute();
           //ResultSet rs = stmt.getResultSet();
           if (rs.next())
           {
               int userID = rs.getInt("id");
               String userName = rs.getString("name");

               return new Account(userID, userName);
           }
           else
           {
               System.err.println("Użytkownik o ID " + id + " nie istnieje");
               return null;
           }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
