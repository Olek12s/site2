package database;

import java.sql.*;

public class DataBaseConnection // konieczny plugin SimpleSqliteBrowser w celu łatwego odczytu baz danych!!!
{
    private Connection connection;
    private Statement statement;

    public Statement getStatement()
    {
        if (statement == null)
        {
            createStatement();
        }
        return statement;
    }
    public Connection getConnection() {return connection;}

    public void connect(String dbPath)
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);  // jeśli plik nie istnieje pod ścieżką, zostanie utworzony plik .db
            System.out.println("Połaczenie z bazą danych sqlite nazwiązane");
        }
        catch (SQLException ex)
        {
            System.err.println("Nie udało się połączyć z bazą danych.");
            ex.printStackTrace();
        }
    }

    public void disconnect()
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
                System.out.println("rozlaczono");
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    private void createStatement()
    {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String query)
    {
        try
        {
            return getStatement().executeQuery(query);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String query)
    {
        try
        {
            getStatement().executeUpdate(query);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
