import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    public static void main(String[] args){
        String dbHost = System.getenv().getOrDefault("POSTGRES_HOST", "localhost");
        String dbPort = System.getenv().getOrDefault("POSTGRES_PORT", "5432");
        String dbUser = System.getenv().getOrDefault("POSTGRES_USER", "admin");
        String dbPass = System.getenv().getOrDefault("POSTGRES_PASSWORD", "secure_pass");
        String dbName = System.getenv().getOrDefault("POSTGRES_DB", "system_db");

        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);

        System.out.println("[INFO] Bootstrapping Application...");
        try(Connection ignored = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)){
            System.out.println("[Success] Database connected.");
            try(ServerSocket server = new ServerSocket(8080)){
                System.out.println("[INFO] TCP Server listening on port 8080...");
                while(true){
                    try(Socket client = server.accept()){
                        System.out.println("[INFO] Incoming connection from proxy");
                        String response = "HTTP/1.1 200 OK\r\n\r\nSystemOperation\n";
                        client.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
        }catch (Exception e){
           System.err.println("[FATAL] Initialization failed: " + e.getMessage());
           System.exit(1);
        }
    }
}
