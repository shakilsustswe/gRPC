package Client;

import com.shakilsustswe.grpc.User;
import com.shakilsustswe.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class Client {

    public static void main(String[] args){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 2022).usePlaintext().build();

        //stubs - generate from proto file

        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("If you want Registration, write Reg");
            System.out.println("If you want LogIn, write LogIn");
            ////System.out.println("If you want LogOut, write LogOut");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Reg")) {
                System.out.println("Enter Email : ");
                String email = scanner.nextLine();
                System.out.println("Enter UserName : ");
                String userName = scanner.nextLine();
                System.out.println("Enter Password : ");
                String password = scanner.nextLine();

                //registration-- a new user is registering
                User.Registration registration = User.Registration.newBuilder().setEmail(email).setUserName(userName).setPassword(password).build();
                User.ApiResponse regResponse = userStub.register(registration);
                System.out.println(regResponse.getResponseMessage());
            } else if (input.equalsIgnoreCase("LogIn")) {
                System.out.println("Enter UserName : ");
                String userName = scanner.nextLine();
                System.out.println("Enter Password : ");
                String password = scanner.nextLine();
                //login-- user did not registered before
                User.LoginRequest loginRequest2 = User.LoginRequest.newBuilder().setUserName(userName).setPassword(password).build();
                User.ApiResponse response2 = userStub.logIn(loginRequest2);
                System.out.println(response2.getResponseMessage());
            } else if (input.equalsIgnoreCase("LogOut")) {
                System.out.println("Enter UserName : ");
                String userName = scanner.nextLine();
                System.out.println("Enter Password : ");
                String password = scanner.nextLine();

                User.Empty emptyMessage = User.Empty.newBuilder().build();
                User.ApiResponse logout = userStub.logout(emptyMessage);
                System.out.println(logout.getResponseMessage());
            }else
            {
                System.out.println("If you want Registration, write Reg");
                System.out.println("If you want LogIn, write LogIn");
                System.out.println("If you want LogOut, write LogOut");

            }
            if(input.equals("stop"))break;
        }

    }


}