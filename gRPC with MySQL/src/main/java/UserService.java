import com.shakil.MySQLgRPC.User;
import com.shakil.MySQLgRPC.userGrpc;

import  java.sql.*;
public class UserService extends userGrpc.userImplBase {

    String url = "jdbc:mysql://localhost:3306/grpc connection";
    String username = "root";
    String pass = "";

    //Class.forName("com.mysql.jdbc.Driver");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement  statement = null;

    //for registration: checks if the user's entered user_name exists in the database
    boolean isUserNameExists(String userName) throws SQLException {
        connection = DriverManager.getConnection(url, username, pass);
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = statement.executeQuery("SELECT * from users");

        while (resultSet.next()){
            if(resultSet.getString("UserName").equals(userName)){
                return true;
            }
        }
        return false;
    }

    //for registration: checks if the email address exists in the database
    boolean isEmailExists(String email) throws SQLException {
        connection = DriverManager.getConnection(url, username, pass);
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = statement.executeQuery("SELECT * from users");

        while (resultSet.next()){
            if(resultSet.getString("Email").equals(email)){
                return true;
            }
        }
        return false;
    }

    //for login: checks if the user's entered user_name and password exists in the database
    boolean isUserExists(String userName, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, pass);

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = statement.executeQuery("SELECT * from users");

        while (resultSet.next()) {
            if (resultSet.getString("UserName").equals(userName) && resultSet.getString("Password").equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void register(User.Registration request, io.grpc.stub.StreamObserver<User.APIResponse> responseObserver) {

        System.out.println("A new Client is registering...");

        String email = request.getEmail();
        String userName = request.getUsername();
        String password = request.getPassword();

        User.APIResponse.Builder response = User.APIResponse.newBuilder();

        try {
            if(isEmailExists(email)) {
                response.setResponseCode(101).setResponseMessage("You Already have a account...");
                System.out.println("email already exists...");
            }
            else if(isUserNameExists(userName)){
                System.out.println("user name already exits...");
                response.setResponseMessage("This user name already exits. Try a different user name...").setResponseCode(102);
            }
            else{
                statement.executeUpdate("INSERT INTO users VALUES ('"+userName+"','"+email+"','"+password+"')");
                System.out.println("registration successful");
                response.setResponseMessage("You Have been registered successfully. Please log in with you username and password...").setResponseCode(100);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(User.LoginRequest request, io.grpc.stub.StreamObserver<User.APIResponse> responseObserver) {

        System.out.println("A user is logging in...");

        String username = request.getUsername();
        String password = request.getPassword();

        User.APIResponse.Builder response = User.APIResponse.newBuilder();

        System.out.println("["+ username + ", "+ password + "]");

        try {
            if(isUserExists(username, password)){
                response.setResponseCode(200).setResponseMessage("Login Successful...");
                System.out.println("successfully logged in....");
            }
            else{
                response.setResponseCode(201).setResponseMessage("Wrong Username or Password...");
                System.out.println("user login failed...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.Empty request, io.grpc.stub.StreamObserver<User.APIResponse> responseObserver) {

        System.out.println("A user has logged out...");

        User.APIResponse.Builder response = User.APIResponse.newBuilder();
        response.setResponseMessage("You Have been logged out...\n").setResponseCode(000);

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }
}
