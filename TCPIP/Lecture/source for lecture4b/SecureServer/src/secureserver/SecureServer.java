/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secureserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.security.Security;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import com.sun.net.ssl.internal.ssl.Provider;


import com.jamesooi.geometry.Circle;

/**
 *
 * @author James Ooi
 */
public class SecureServer extends Thread {

    public SecureServer(int port) {
        // Registering the JSSE provider
        Security.addProvider(new Provider());

        /*
         * The following KEY STORE must be created first and export to certiticate
         * before creating TRUST STORE for the client
         *
         * Creating KEY STORE:
         * keytool -genkey -alias testkey -keystore testkeystore.ks
         *
         * Export to CERTIFICATE:
         * keytool -export -alias testkey -keystore testkeystore.ks -file testkey.cer
        */
        
        //Specifying the Keystore details
        System.setProperty("javax.net.ssl.keyStore", "D:\\jsecure\\server\\pink.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "rosemary");

        try {
            ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
            ServerSocket serverSocket = factory.createServerSocket(port);

            System.out.println("Server started at " + new Date() + "\n");

            // Client number
            int clientNo = 1;

            while (true) {
                // Listen for a connection request
                Socket socket = serverSocket.accept();

                // Display client number, hostname & IP address
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("Client No. " + clientNo + " ["
                        + inetAddress.getHostName() + "] [" + inetAddress.getHostAddress()
                        + "] connected at " + new Date() + "\n");

                // Create & start new thread to handle this connection
                HandleClient clientThread = new HandleClient(socket);
                clientThread.start();

                // Increment client number
                clientNo++;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    // Inner class: Thread class for handling connection
    class HandleClient extends Thread {

        private Socket socket;

        public HandleClient(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                // Serve the client continuously
                while (true) {
                    // Receive radius from the client
                    double radius = inputFromClient.readDouble();

                    //Instantiate a Circle object
                    Circle circle = new Circle(radius);

                    // Get the circumference & area
                    double circumference = circle.getCircumference();
                    double area = circle.getArea();

                    // Send area to the client
                    outputToClient.writeDouble(circumference);
                    outputToClient.writeDouble(area);
                    outputToClient.flush();

                    // Server Logging
                    System.out.println("Radius received from client: " + radius + "\n");
                    System.out.println("Circumference found: " + circumference + "\n");
                    System.out.println("Area found: " + area + "\n\n");
                }
            } catch (SocketException ex) {
                System.out.println("Client is disconnected.\n");
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port;
        if (args.length == 0) {
            // if port is not specified in parameter, defaults to 8000
            port = 8000;
            new SecureServer(port);
        } else if (args.length == 1) {
            // if port is specified in parameter, use it.
            port = Integer.parseInt(args[0]);
            new SecureServer(port);
        } else {
            System.out.println("Invalid number of arguments!");
        }
    }

}
