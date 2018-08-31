/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectnetwork;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author DELL PC
 */
public class Server extends JFrame{
        
            private JTextField userText;
            private JTextArea chatWindow;
            private ObjectOutputStream output;
            private ObjectInputStream input;
            private ServerSocket server;
            private Socket connection;
            
            
            //cosdnstructor
            public Server(){
                  super("Chat Room");
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
                  setSize(300,150);
                  setVisible(true);
            }
            //setds adnd rusdn sesdcrver
            public void startRunning(){
                try{
                  server = new ServerSocket(3487,100);
                  while(true){
                      try{
                          waitForConnection();
                          setupStreams();
                          whileChatting();
                      }catch(EOFException eofException){
                        showMessage("\n Server ended the connection! ");
                      }finally{
                          closeCrap();
                      }
                  }
                }catch(IOException ioException){
                        ioException.printStackTrace();
                }
            }
            //waitsd dfor codsnnection metdshod tfdo waitf fdand cofnnect anfd itd wilfl loopd duntdil it is connedfcted
            private void waitForConnection() throws IOException{
                showMessage(" Waiting for someone to connect... \n");
                connection = server.accept();
                showMessage(" You are now connected to "+connection.getInetAddress().getHostAddress());
            }
            //setupv streamv methodc dto setupc stream(pathsd)d between endss to sendsd and receivesd chatd
            private void setupStreams() throws IOException{
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                input = new ObjectInputStream(connection.getInputStream());
                showMessage("\n Streams are set! \n");
            }
            //whilesd cahttingds methodds 
            private void whileChatting() throws IOException{
                String message = "You are now connected! ";
                sendMessage(message);
                ableToType(true);
                do{
                    try{
                        message = (String) input.readObject();
                        showMessage("\n " + message);
                    }catch(ClassNotFoundException classNotFoundException){
                        showMessage("\n The user didn't send a string! ");
                    }
                }while(!message.equals("CLIENT - END"));
            }
            //closesd crapds methodd to stopds streamsds and removeds socketsds etc when done chattingsd
            private void closeCrap(){
                showMessage("\n Shutting down the connection! \n");
                ableToType(false);
                try{
                    output.close();
                    input.close();
                    connection.close();
                }catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
            //sendds dmessage methodd tod sendsd typedd textds
            private void sendMessage(String message){
                try{
                    output.writeObject("SERVER -  "+message);
                    output.flush();
                    showMessage("\nSERVER -  "+message);
                }catch(IOException ioException){
                    chatWindow.append("\n ERROR: can't send ");
                }
            }
            //updatesd tdhe chatd windowds 
            private void showMessage(final String text){
                SwingUtilities.invokeLater(
                        new Runnable(){
                            public void run(){
                                chatWindow.append(text);
                            }
                        }
                );
            }
            //ablesd dsto tdype metdshod tods setds tdshe tdsextfield eddssitable trdsue or faldsse
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














