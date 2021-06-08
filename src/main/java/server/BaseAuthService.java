package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class BaseAuthService implements AuthService {
  private class Entry {
    private String login;
    private String pass;
    private String nick;

    public Entry(String login, String pass, String nick) {
      this.login = login;
      this.pass = pass;
      this.nick = nick;
    }
  }

  private List<Entry> entries;

  @Override
  public void start() {
    System.out.println("Сервис аутентификации запущен");
  }

  @Override
  public void stop() {
    System.out.println("Сервис аутентификации остановлен");
  }


  public BaseAuthService() {
    entries = new ArrayList<>();
    readEntriesFromDb();
  }

  private void readEntriesFromDb() {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection connection = DriverManager.getConnection("jdbc:sqlite:client.db");
      Statement stmt = connection.createStatement();
      try (ResultSet rs = stmt.executeQuery("SELECT login, pass, nick FROM client")) {
        while (rs.next()) {
          entries.add(new Entry(rs.getString("login"), rs.getString("pass"), rs.getString("nick")));
        }
      }
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getNickByLoginPass(String login, String pass) {
    for (Entry o : entries) {
      if (o.login.equals(login) && o.pass.equals(pass)) return o.nick;
    }
    return null;
  }
}