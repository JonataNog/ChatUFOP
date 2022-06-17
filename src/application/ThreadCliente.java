package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import application.Mensagem.Action;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ThreadCliente extends Thread {

    private Socket socket;
    private TextArea textArea;
    private String remetente;
    private ListView<String> list;
    boolean sair = false;

    @SuppressWarnings("unchecked")
	public ThreadCliente(String r, Socket s, TextArea textArea, ListView<?> lvUsuarios) {
        this.remetente = r;
        this.socket = s;
        this.textArea = textArea;
        this.list = (ListView<String>) lvUsuarios;
    }

    @Override
    public void run() {
        try {
            while (!sair) {
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Mensagem mensagem = (Mensagem) entrada.readObject();
                Action action = mensagem.getAction();

                switch (action) {
                    case CONNECT:
                        conectar(mensagem);
                        break;
                    case DISCONNECT:
                        desconectar(mensagem);
                        break;
                    case SEND:
                        receberMensagem(mensagem);
                        break;
                    case SEND_ONE:
                        receberMensagem(mensagem);
                        break;
                    case USERS_ONLINE:
                        atualizarUsuarios(mensagem);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
           System.out.println("Erro ao criar a thread!");
        }
    }

    public void conectar(Mensagem mensagem) {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
    }

    public void desconectar(Mensagem mensagem) throws IOException {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));

        if (mensagem.getRemetente().equals(this.remetente)) {
            this.socket.close();
            this.sair = true;
        }
    }

    public void receberMensagem(Mensagem mensagem) throws IOException {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
    }

    public void atualizarUsuarios(Mensagem mensagem) {
        ArrayList<String> usuariosOnline = mensagem.getUsuariosOnline();

        Platform.runLater(() -> {
            list.getItems().clear();
            list.getItems().addAll(usuariosOnline);
        });

    }

}