package main;

import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferStrategy;

class World extends Canvas implements Runnable{
    
  /**
	 * 
	 */
	private static final long serialVersionUID = -7619825478780549877L;
//Screen settings
  public static final int scale = 2;
  public static final int maxScreenCol = 24 * 16;
  public static final int maxScreenRow = 16 * 16;
  public static final int screenWidth = maxScreenCol * scale;
  public static final int screenHeight = maxScreenRow * scale;
  
  public static InputHandler in = new InputHandler();
  public static User user = new User(in);
  Thread thread;
  int fps = 0;
  boolean isRunning = false;

  ArrayList<Particle> particles = new ArrayList<Particle>();

  //settings
  public final boolean isIMF = true;
  public final double stickChance = 0.05;
  public final int popMult = 1;

  public World(){
    setPreferredSize(new Dimension(screenWidth, screenHeight));
    setBackground(Color.black);
    addKeyListener(in);
    addMouseListener(in);
    addMouseMotionListener(in);
    setFocusable(true);
  }
  public void start(){
    if(isRunning) return;
    isRunning = true;
    
    thread = new Thread(this);
    thread.start();

    System.out.println("Working!");
  }
  @Override
  public void run(){
    System.out.println("running");
    int timer = 0;
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    int tic = 0;

    //beginning setup
    user.setPosX(200);
    user.setPosY(200);
    particles.add(user);
    particles.add(new Particle(screenWidth/2+10, screenHeight/2));
    for(int i = 1; i < 6; i++){
      Particle a = new Particle((int)(Math.random()*screenWidth), (int)(Math.random()*screenHeight));
      particles.add(a);
    }
    
    
    //game loop
    while(isRunning) {
      long now = System.nanoTime();

      delta += (now - lastTime) / ns;
      timer += (now - lastTime);

      lastTime = now;

      if(delta >= 1) {
        //execute frame
        update();
        render();
        tic++;
        delta--;
      } 
      if(timer >= 1000000000){
        fps = tic;
        tic = 0;
        timer = 0;
      }
    }
  }

  private void update(){

    //updates all particles
    for(Particle p : particles)
      p.update();

    /*
    recomend checking for collisions at this step,
    rather than during the update phase.
    Might make collisions faster to proccess
    Or not idk
    */

    for(int i = 0; i < particles.size()-1; i++){
      for(int j = i+1; j < particles.size(); j++){
        Particle a = particles.get(i);
        Particle b = particles.get(j);
        if(a.isColliding(b)){
          //momentum collision
          collide(a,b);
        }
      }
    }
    
    for(Particle p : particles)
      p.borderCollision();
    
    for(int i = 1; i < particles.size(); i++){
      Particle a = particles.get(i);
      int numCollisions = popMult * (particles.size()-1);
      if(isIMF && a.getCollisions()>=numCollisions)
        kaboomboom(a);
    }
      
  }
  
  private void render(){
    BufferStrategy bs = this.getBufferStrategy();                  
    if(bs == null){
      createBufferStrategy(3);
      return;
    }
    
    Graphics g = bs.getDrawGraphics();

    g.fillRect(0, 0, screenWidth, screenHeight);
    
    //renders all particles
    for(Particle p : particles)
      p.render(g);

    
    //draw info
    g.setFont(new Font(Font.SERIF, Font.PLAIN, 10));
    g.setColor(Color.YELLOW);
    g.drawString("FPS: " + fps, screenWidth - 100, 10);
    g.drawString("pos: " + (int)(user.getPosX()*100)/100.0 + ", " + (int)(user.getPosY()*100)/100.0, screenWidth - 100, 20);
    g.drawString("vel: " + Math.round(user.getVelX()*100)/100.0 + ", " + Math.round(user.getVelY()*100)/100.0, screenWidth - 100, 30);
    g.drawString("num: " + particles.size(), screenWidth - 100, 40);
    g.drawString("pop: " + 4 * (particles.size()-1), screenWidth - 100, 50);
        
    g.dispose();
    bs.show();
    
  }

  private void collide(Particle a, Particle b){
    if(isIMF && Math.random() < stickChance && (a != particles.get(0) && b != particles.get(0)) ){
      conjoin(a,b);
      return;
    }
      //x collision
      double avelX = (2*b.getMass()*b.getVelX() + a.getMass()*a.getVelX() - b.getMass()*a.getVelX())/(a.getMass() + b.getMass());
      double bvelX = (2*a.getMass()*a.getVelX() + b.getMass()*b.getVelX() - a.getMass()*b.getVelX())/(b.getMass() + a.getMass());
      a.setVelX(avelX);
      b.setVelX(bvelX);
  
      //y collision
      double avelY = (2*b.getMass()*b.getVelY() + a.getMass()*a.getVelY() - b.getMass()*a.getVelY())/(a.getMass() + b.getMass());
      double bvelY = (2*a.getMass()*a.getVelY() + b.getMass()*b.getVelY() - a.getMass()*b.getVelY())/(b.getMass() + a.getMass());
      a.setVelY(avelY);
      b.setVelY(bvelY);

    //incremement collisions
      a.addCollision();
      b.addCollision();

      collisionOffset(a, b);
  }
  private void collisionOffset(Particle a, Particle b){
    //offset for collision
    a.addPos(a.getVelX(), a.getVelY());
    b.addPos(b.getVelX(), b.getVelY());
    if(a.isColliding(b)){
      collisionOffset(a, b);
    }
  }
  private void conjoin(Particle a, Particle b){
    //System.out.println("avel: " + a.getVelY());
    //System.out.println("bvel: " + b.getVelY());
    
    //a.setSize(a.getSize() + b.getSize());
    a.setSize(a.getSize() + 1);
    
    double velX = (a.getMass()*a.getVelX()+b.getMass()*b.getVelX())/((double)a.getMass()+b.getMass());
    
    a.setVelX(velX);
    
    double velY = (a.getMass()*a.getVelY()+b.getMass()*b.getVelY())/(a.getMass()+b.getMass());
      
    a.setVelY(velY);
    
    //System.out.println("velY: " + velY);
    
a.setMass(a.getMass()+b.getMass());

    //remove particle
    particles.remove(b);
  }
  private void kaboomboom(Particle a){
      Particle b = new Particle(a.getPosX()-2, a.getPosY()-2, a.getVelX()-2, a.getVelY()-2);
      Particle c = new Particle(a.getPosX()+2, a.getPosY()+2, a.getVelX()+2, a.getVelY()+2);

      double mass = a.getMass()/2;
      b.setMass(mass);
      c.setMass(mass);

    final int minSize = 1;
    int size = a.getSize()-1;
    if(size < minSize) size = minSize;
    b.setSize(size);
    c.setSize(size);

      b.addPosX(-60);
      c.addPosX(60);

      particles.remove(a);
      particles.add(b);
      particles.add(c);
  }
}