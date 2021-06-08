package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Controller {
  public static Socket socket;
  public static DataInputStream in;
  public static DataOutputStream out;

  @FXML
  TextField messageField;
  @FXML
  TextArea mainChatArea;
  @FXML
  TextField loginField;
  @FXML
  TextField passField;

  public void sendMessage(ActionEvent actionEvent) {
    if (!messageField.getText().trim().isEmpty()) {
//      mainChatArea.appendText(messageField.getText().trim() + "\n");
      try {
        out.writeUTF(messageField.getText().trim());
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        messageField.clear();
        messageField.requestFocus();
      }
    } else {
      messageField.clear();
      messageField.requestFocus();
    }
  }

  public void onAuthClick() {
    if (socket == null || socket.isClosed()) {
      start();
    }
    try {
      out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
      loginField.clear();
      passField.clear();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start() {
    try {
      socket = new Socket("localhost", 8189);
      socket.setSoTimeout(120000);
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());
      Thread t = new Thread(() -> {
        try {
          while (true) {
            String str = in.readUTF();
            mainChatArea.appendText(str + "\n");
            if (str.startsWith("/authok")) {
              socket.setSoTimeout(0);
            }
          }
        } catch (SocketTimeoutException ex1) {
          mainChatArea.appendText("Socket timeout, connection closed." + "\n");
          try {
            socket.close();
          } catch (IOException ex2) {
            ex2.printStackTrace();
          }
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
      t.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

