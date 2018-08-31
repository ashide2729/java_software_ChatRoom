/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package projectnetwork;

import javax.swing.JFrame;

/**
 *
 * @author DELL PC
 */
public class ProjectNetwork {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Server myside = new Server();
        myside.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myside.startRunning();
    }
    
}
