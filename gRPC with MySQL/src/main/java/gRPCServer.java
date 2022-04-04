import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class gRPCServer {
    public static void main(String[] args){

        Server server = ServerBuilder.forPort(2022).addService(new UserService()).build();

        try {
            server.start();
            System.out.println("Server Started at:" + server.getPort() + "\n");
            server.awaitTermination();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
