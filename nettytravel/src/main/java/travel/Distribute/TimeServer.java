package travel.Distribute;

import travel.BIO.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;

            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                System.out.println("waiting client connect..." );
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
                System.out.println("end client connect..." );

            }
        } finally {
            if (server != null) {
                System.out.println("The timer server close");
                server.close();
                server = null;
            }
        }
    }

}
