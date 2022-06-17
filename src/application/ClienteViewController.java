package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClienteViewController implements Initializable {
	
    @FXML
    private Button btConectar;

    @FXML
    private Button btEnviar;

    @FXML
    private Button btSair;

    @FXML
    private TextArea txtAreaHistorico;

    @FXML
    private ListView<?> lvUsuarios;

    @FXML
    private TextField txtApelido;

    @FXML
    private TextField txtIP;

    @FXML
    private TextField txtMensagem;

    @FXML
    private TextField txtPorta;

    private Stage dialogStage;

    private Socket socket;
    private String remetente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.txtIP.setText("127.0.0.1");
        this.txtPorta.setText("2001");
    }

    public void onBtSairAction() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto("Saiu do Chat!");
            mensagem.setAction(Mensagem.Action.DISCONNECT);

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(mensagem);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getDialogStage().close();
    }

    public void onBtConectarAction() {
        this.dialogStage.setTitle(this.txtApelido.getText());
        this.remetente = txtApelido.getText();

        try {
            socket = new Socket(txtIP.getText(), Integer.valueOf(txtPorta.getText()));
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(remetente);
            mensagem.setTexto("Entrou no Chat!");
            mensagem.setAction(Mensagem.Action.CONNECT);

            ThreadCliente thread = new ThreadCliente(remetente, socket, txtAreaHistorico, lvUsuarios);
            thread.setName("Thread Cliente " + remetente);
            thread.start();

            saida.writeObject(mensagem); 

        } catch (IOException e) {
        	e.printStackTrace();
        }
        desabilitarTextFields();
    }

    public void onBtEnviarAction() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto(this.txtMensagem.getText());
            mensagem.setAction(Mensagem.Action.SEND);

            if (lvUsuarios.getSelectionModel().getSelectedItem() != null) {
                mensagem.setAction(Mensagem.Action.SEND_ONE);
                mensagem.setDestinatario((String)lvUsuarios.getSelectionModel().getSelectedItem());
            }

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(mensagem);

            this.txtMensagem.setText("");
            this.lvUsuarios.getSelectionModel().clearSelection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void desabilitarTextFields() {
        this.txtIP.setEditable(false);
        this.txtApelido.setEditable(false);
        this.txtPorta.setEditable(false);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
