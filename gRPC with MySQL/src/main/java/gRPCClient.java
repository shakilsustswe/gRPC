import com.shakil.MySQLgRPC.User;
import com.shakil.MySQLgRPC.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class gRPCClient {
    public static void main(String[] args){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 2022).usePlaintext().build();

        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);

        Scanner scr = new Scanner(System.in);
        while(true) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("If you want Registration, write Reg of Any Order");
            System.out.println("If you want LogIn, write LogIn of Any Order");
            System.out.println("If you want LogOut, write LogOut of Any Order");

            String command = scr.nextLine();
            if (command.equalsIgnoreCase("Logout")) {
                User.Empty emptyMessage = User.Empty.newBuilder().build();
                User.APIResponse logout = userStub.logout(emptyMessage);
                System.out.println(logout.getResponseMessage());
                break;
            }
            if (command.equalsIgnoreCase("reg")) {
                System.out.print("User Name: ");
                String userName = scr.nextLine();
                System.out.print("Email: ");
                String email = scr.nextLine();
                System.out.print("Password: ");
                String password = scr.nextLine();

                User.Registration registration = User.Registration.newBuilder().setEmail(email).setUsername(userName).setPassword(password).build();
                User.APIResponse regResponse = userStub.register(registration);
                System.out.println(regResponse.getResponseMessage());

            } else if (command.equalsIgnoreCase("login")) {

                System.out.print("User Name: ");
                String userName = scr.nextLine();
                System.out.print("Password: ");
                String password = scr.nextLine();

                User.LoginRequest loginRequest = User.LoginRequest.newBuilder().setUsername(userName).setPassword(password).build();
                User.APIResponse response = userStub.login(loginRequest);
                System.out.println(response.getResponseMessage());

            } else {
                System.out.println("[Invalid command. Try again...]");
            }

        }

    }

}
