package main;

import javax.swing.*;

public class Main {
  
  public static void main(String[] args) {
    JFrame jFrame = new JFrame("Entropy");
    World world = new World();
    jFrame.setSize(World.screenWidth, World.screenHeight);
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.setResizable(false);
    jFrame.add(world);
    jFrame.pack();
    
    jFrame.setLocationRelativeTo(null);
    jFrame.setVisible(true);
    
    world.start();
  }
  
}