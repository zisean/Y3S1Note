/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secureclient;

/**
 *
 * @author James Ooi
 */
import com.sun.net.ssl.internal.ssl.Provider;
import java.io.*;
import java.net.*;
import java.security.Security;
import java.util.*;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class SecureClient {
    // IO streams

    private DataOutputStream toServer;
    private DataInputStream fromServer;

    public final static String PROMPT = "Enter radius or negative value to quit: ";

    public SecureClient(String serverHost, int serverPort) {
        // Registering the JSSE provider
        Security.addProvider(new Provider());
        
        /* The following TRUST STORE must be created using keytool
         *
         * keytool -importcert -keystore secure.ts -storepass testpwd -file testkey.cer
         *
        */
        System.setProperty("javax.net.ssl.trustStore", "D:\\jsecure\\client\\pink.ts");
        System.setProperty("javax.net.ssl.trustStorePassword", "rosemary");

        try {
            // Create a socket to connect to the server
            SocketFactory factory = SSLSocketFactory.getDefault();
            Socket socket = factory.createSocket(serverHost, serverPort);
            
            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            // Create Scanner object to read input
            Scanner in = new Scanner(System.in);

            try {
                // Prompt for input
                System.out.print(PROMPT);
                double radius = in.nextDouble();

                while ((int) radius >= 0) {
                    // Send the radius to the server
                    toServer.writeDouble(radius);
                    toServer.flush();

                    // Get circumference & area from the server
                    double circumference = fromServer.readDouble();
                    double area = fromServer.readDouble();

                    // Display result
                    System.out.println("Circumference: " + circumference + "\n");
                    System.out.println("Area: " + area + "\n\n");

                    // Prompt for next input
                    System.out.print(PROMPT);
                    radius = in.nextDouble();
                }
            } catch (InputMismatchException ex) {
                System.err.println(ex);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            /*
		args[0] => hostName
		args[1] => port
             */
            new SecureClient(args[0], Integer.parseInt(args[1]));
        } else {
            System.out.println("Invalid number of arguments!");
        }
    }
}
