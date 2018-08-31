/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientnetwork;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author DELL PC
 */
public class Client extends JFrame{
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
   
    public Client(String host){
       super("Client chat");
       serverIP = host;
       userText = new JTextField();
       userText.setEditable(false);
       userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText,BorderLayout.NORTH);   
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);        
        setSize(300,150);
        setVisible(true);
        }
        
        //connecting to server
        public void startRunning(){
            try{
                connectToServer();
                setupStreams();
                whileChatting();
            }catch(EOFException eofException){
                showMessage("\n Client terminated the connection");
            }catch(IOException ioException){
                ioException.printStackTrace();
            }finally{
                closeCrap();
            }
        }
        //connecting to the server 
        private void connectToServer() throws IOException{
            showMessage("Attempting connection... \n");
            connection = new Socket(InetAddress.getByName(serverIP),3487);
            showMessage("Connected to:" + connection.getInetAddress().getHostName());
        }
       //setting up the stream 
        private void setupStreams() throws IOException{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            showMessage("\n Streaming \n");
        }
        //while chatting with server 
        private void whileChatting() throws IOException{
            ableToType(true);
            do{
                try{
                    message = (String) input.readObject();
                    showMessage("\n" + message);
                }catch(ClassNotFoundException classNotFoundException){
                    showMessage("\n Can't understand the object type");
                }
            }while(!message.equals("SERVER - END"));
                  
        }
        //close crap method 
        private void closeCrap(){
            showMessage("\n Shutting down!");
            ableToType(false);
            try{
                output.close();
                input.close();
                connection.close();
            }catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
        //send message method
        private void sendMessage(String message){
            try{
                output.writeObject("CLIENT - " + message);
                output.flush();
                showMessage("\n CLIENT - " + message);
            }catch(IOException ioException){
                chatWindow.append("\n Something went wrong sending text !");
            }
        }
        //update chatwindow
        private void showMessage(final String s){
            SwingUtilities.invokeLater(
                    new Runnable(){
                        public void run(){
                            chatWindow.append(s);
                        }
                    }
            
            );
        }
        //abletotype method
        private void ableToType(final boolean tof){
            SwingUtilities.invokeLater(
                    new Runnable(){
                        public void run(){
                            userText.setEditable(tof);
                        }
                    }
            );
        }
}