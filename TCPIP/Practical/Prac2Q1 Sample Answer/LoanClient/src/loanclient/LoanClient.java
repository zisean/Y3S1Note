/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanclient;

/**
 *
 * @author Administrator
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class LoanClient {
    
    public LoanClient(String host) {
        try {
            Socket socket = new Socket(host, 8000);
            
            DataInputStream input = new DataInputStream(
                    socket.getInputStream());
            DataOutputStream output = new DataOutputStream(
                    socket.getOutputStream());
            
            Scanner in = new Scanner(System.in);
            
            System.out.print("Annual Interest Rate: ");
            double interest = in.nextDouble();
            System.out.print("Loan Tenure in Years: ");
            int years = in.nextInt();
            System.out.print("Loan Amount: ");
            double amount = in.nextDouble();
            
            output.writeDouble(interest);
            output.writeInt(years);
            output.writeDouble(amount);
            output.flush();
            
            double monthlyPayment = input.readDouble();
            double totalPayment = input.readDouble();
            
            System.out.println("Monthly Payment: " + monthlyPayment);
            System.out.println("Total Payment: " + totalPayment);
            
        }
        catch(SocketException ex) {
            
        }
        catch(IOException ex) {
            
        }
    }
       

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length == 1) {
            LoanClient client = new LoanClient(args[0]);
        }
        else {
            System.out.println("Invalid number of arguments");
        }
    }
    
}
