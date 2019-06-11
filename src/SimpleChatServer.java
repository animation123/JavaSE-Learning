import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleChatServer {

    ArrayList clientOutputSreams;

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {

                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch(Exception ex) {ex.printStackTrace();}
        } // 关闭构造函数

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEveryone(message);
                } // 结束while循环
            } catch(Exception ex) {ex.printStackTrace();}
        } // 关闭run()
    } // 关闭内部类

    public static void main(String[] args) {
        new SimpleChatServer().go();
    }

    public void go() {
        clientOutputSreams = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(5000);

            while(true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputSreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    } // 关闭go

    public void tellEveryone(String message) {

        Iterator it = clientOutputSreams.iterator();
        while(it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } // 结束while

    } // 关闭tellEveryone
} // 关闭类