package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    
    private static int qtdClientes = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2001);
        System.out.println("A porta: 2001 foi aberta!");
        System.out.println("Servidor iniciado e esperando conexões...");
        while (qtdClientes < 5) {
            Socket socket;
            socket = serverSocket.accept();
            
            qtdClientes++;
            
            System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");

            ThreadServidor thread = new ThreadServidor(socket);
            thread.setName("Thread Servidor: " + String.valueOf(qtdClientes));
            thread.start();
        }
        serverSocket.close();
    }
}