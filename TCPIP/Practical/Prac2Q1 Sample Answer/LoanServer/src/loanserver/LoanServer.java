/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author Administrator
 */


public class LoanServer {
    
    public LoanServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            
            while(true) {
                Socket socket = serverSocket.accept();
                
                HandleClient client = new HandleClient(socket);
                client.start();
                
            }
        }
        catch(IOException ex) {
            
        }
    }
    
    class HandleClient extends Thread {
        private Socket socket;
        
        public HandleClient(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            try {
                DataInputStream input = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream output = new DataOutputStream(
                        socket.getOutputStream());
                
                double interest = input.readDouble();
                int years = input.readInt();
                double amount = input.readDouble();
                
                Loan loan = new Loan(interest, years, amount);
                
                double monthlyPayment = loan.getMonthlyPayment();
                double totalPayment = loan.getTotalPayment();
                
                output.writeDouble(monthlyPayment);
                output.writeDouble(totalPayment);
                output.flush();
                
            }
            catch(SocketException ex) {
                
            }
            catch(IOException ex) {
                
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new LoanServer();
    }
    
}
