package main;

import java.awt.*;
import java.awt.Color;

class Particle{

  private int radius = 25;
  private int particleSize = radius*2;
  
  
  private double posX;
  private double posY;
  private double velX;
  private double velY;
  private double mass = (int)(Math.random()*6)+1;
  private boolean sticky = (1 == (int)(Math.random()*0));
  private int popCount = 0;

  public Particle(){
    posX = 0;
    posY = 0;
    velX = 0;
    velY = 0;
  }
  public Particle(double posX, double posY){
    this.posX = posX;
    this.posY = posY;
    velX = 0;
    velY = 0;
  }
  public Particle(double posX, double posY, double velX, double velY){
    this.posX = posX;
    this.posY = posY;
    this.velX = velX;
    this.velY = velY;
  }

  public void update(){
    posX += velX;
    posY += velY;
  }
  public void render(Graphics g){
    //renders this particle

    //color setup
    
    Color col; 
    Color textCol = Color.WHITE;
    /*
    if(mass < 1)
      col = new Color(1,1,(int)(mass*255));
    else
      col = new Color((int)(1/mass*255),(int)(1/mass*255),(int)(1/mass*255));*/
    switch((int)(mass)){
      case 0:
        col = new Color(161,172,255);
        textCol = Color.black;
        break;
      case 1:
        col = new Color(119,133,242);
        textCol = Color.black;
        break;
      case 2:
        col = new Color(89,103,207);
        textCol = Color.black;
        break;
      case 3:
        col = new Color(66,78,173);
        break;
      case 4:
        col = new Color(37,51,156);
        break;
      case 5:
        col = new Color(12,25,120);
        break;
      default:
        col = Color.black;
        break;
    }
    
    g.setColor(col);
    g.fillOval((int)(posX-radius), (int)(posY-radius), particleSize, particleSize);
    g.setColor(textCol);
    g.drawString(""+ (int)(mass*100)/100.0, (int)posX-10, (int)posY);
    g.drawString(""+ popCount, (int)posX-10, (int)posY + 10);
  }
  private double distanceFrom(Particle p){
    //returns the distance between this particle and a given particle
    double xSquared = Math.pow(p.posX - this.posX, 2);
    double ySquared = Math.pow(p.posY - this.posY, 2);
    return Math.pow(xSquared + ySquared, 0.5);
  }
  public boolean isColliding(Particle p){
    //checks if the distance is less than the sum of the radii of this and the given particle
    return distanceFrom(p) < (this.radius + p.radius);
  }

  //accesors and mutators
  public double getPosX(){
    return posX;
  }
  public double getPosY(){
    return posY;
  }
  public double getVelX(){
    return velX;
  }
  public double getVelY(){
    return velY;
  }
  public void setPosX(double d){
    posX = d;
  }
  public void setPosY(double d){
    posY = d;
  }
  public void setPos(double x, double y){
    posX = x;
    posY = y;
  }
  public void setVelX(double d){
    velX = d;
  }
  public void setVelY(double d){
    velY = d;
  }
  public void setVel(double x, double y){
    velX = x;
    velY = y;
  }
  public void addPosX(double d){
    posX += d;
  }
  public void addPosY(double d){
    posY += d;
  }
  public void addPos(double x, double y){
    posX += x;
    posY += y;
  }
  public void addVelX(double d){
    velX += d;
  }
  public void addVelY(double d){
    velY += d;
  }
  public void addVel(double x, double y){
    velX += x;
    velY += y;
  }

  public void setMass(double m){
    mass = m;
  }
  public double getMass(){
    return mass;
  }
  public boolean isSticky(){
    return sticky;
  }
  public void setSize(int r){
    radius = r;
    particleSize = radius*2;
  }
  public int getSize(){
    return radius;
  }
  public int getCollisions(){
    return popCount;
  }
  public void addCollision(){
    popCount++;
  }

  
  public void borderCollision(){
    if(posX>=World.screenWidth-radius && velX > 0){
      velX*=-1;
    }
    if(posY>=World.screenHeight-radius && velY > 0){
      velY*=-1;
    }
    if(posX<=radius && velX < 0){
      velX*=-1;
    }
    if(posY<=radius && velY < 0){
      velY*=-1;
    }
  }
}