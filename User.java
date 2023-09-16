package main;

import java.awt.event.KeyEvent;

class User extends Particle{

  InputHandler in;
  int inputTimer = 0;
  
  int numCollisions = 0; //tracks the user's collision count
  final int acceleration = 1;
  
  public User(InputHandler in){
    this.in = in;
    setMass(15);
    setPosX(0);
  }
  public void update(){

    inputUpdate();
    super.update();
    inputTimer--;
  }
  private void inputUpdate(){

    if(inputTimer > 0) return;
    
    //user input
    if(in.key[KeyEvent.VK_W])
      addVelY(-acceleration);
    if(in.key[KeyEvent.VK_A])
      addVelX(-acceleration);
    if(in.key[KeyEvent.VK_S])
      addVelY(acceleration);
    if(in.key[KeyEvent.VK_D])
      addVelX(acceleration);

    //stabilize
    if(in.key[KeyEvent.VK_SPACE]){
      if(getVelX() > 0)
        setVelX(getVelX()/2);
      else if(getVelX() < 0)
        setVelX(getVelX()/2);
      if(getVelY() > 0)
        setVelY(getVelY()/2);
      else if(getVelY() < 0)
        setVelY(getVelY()/2);
    }

    inputTimer = 6;
  }
}