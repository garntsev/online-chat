package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ServerApp {
  public static void main(String[] args) {
    // TODO: run once
//    initDatabase();
    new MyServer();
  }

  private static void initDatabase() {
    Connection conn = null;
    try {
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:client.db");
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS client (" +
          "login TEXT NOT NULL, " +
          "pass TEXT NOT NULL, " +
          "nick TEXT NOT NULL);");
      stmt.executeUpdate("INSERT INTO client (login, pass, nick) VALUES " +
          "(\"login1\", \"pass1\", \"nick1\")," +
          "(\"login2\", \"pass2\", \"nick3\")," +
          "(\"login3\", \"pass3\", \"nick3\")");
    } catch (SQLException | ClassNotFoundException throwables) {
      throwables.printStackTrace();
    }
  }
}