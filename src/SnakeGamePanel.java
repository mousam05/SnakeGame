import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGamePanel extends JPanel {
    
    
    Snake snake;
    Food food;
    Stone[] stones = new Stone[10];
    int score, lives;
    DisplayPanel display;
    boolean gameInProgress;
    
    /** DisplayPanel is a JPanel that displays the current score, the no. of lives left
     * and the time left to eat the food displayed, along with a relevant message describing
     * the state of the game. This panel is constantly updated (repainted)
     * by the timer that drives the game.
     */
    public class DisplayPanel extends JPanel{
        
        String message;
        
        public DisplayPanel(){
            setBackground(Color.CYAN);
            message = " ";
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            g.setColor(Color.BLACK);
            g.drawString("Score: " + score + " Lives left: " + lives, 20, 20);
            g.drawString(message, 20, 40);
            if(food != null)
                g.drawString("Time left to eat the food: " 
                    + (int)(5 - (System.currentTimeMillis() - food.startTime)/1000) , 20, 60);
            
        }
    }
    
    
    Timer timer = new Timer(50, new ActionListener(){
        public void actionPerformed(ActionEvent e){
            
            snake.update();
            
            for(int i=0; i<10; i++)
                if(snake.X[snake.length - 1] == stones[i].X && snake.Y[snake.length - 1] == stones[i].Y){
                    display.message = "You hit a stone! GAME OVER.";
                    gameInProgress = false;
                    timer.stop();
                    food = null;
                    display.repaint();
                    return;
                }
            
            for(int i=0; i<snake.length-1; i++){
                if(snake.X[i] == snake.X[snake.length - 1] && snake.Y[i] == snake.Y[snake.length - 1]){
                    display.message = "You bit yourself! GAME OVER.";
                    gameInProgress = false;
                    timer.stop();
                    food = null;
                    display.repaint();
                    return;
                }
            }
            
            repaint();
                    
            if(food == null)
                food = new Food();
            
            
            else if(snake.X[snake.length - 1] == food.X && snake.Y[snake.length - 1] == food.Y){
                food = null;
                score++;
                snake.increaseLength();
            }
            
            else if(System.currentTimeMillis() - food.startTime > 5000){
                lives--;
                food = null;
                if(lives == 0){
                    display.message = "You have no lives left! GAME OVER.";
                    gameInProgress = false;
                    timer.stop();
                }
            }
            
            display.repaint();    
        }
    });
    
    /** This class is used to create a food pellet as an object.
     */
    private class Food{
        private int X, Y;
        private long startTime;
        
        public Food(){
            
            boolean overlap = false;
            do{
            X = 10 * (int)(Math.random() * 40) + 50;
            Y = 10 * (int)(Math.random() * 40) + 50;
            for(int i=0; i<10; i++)
                if(X == stones[i].X && Y == stones[i].Y){
                    overlap = true;
                    break;
                }
            }while(overlap);            
            
            startTime = System.currentTimeMillis();
        }
        
        public void draw(Graphics g){
            g.setColor(Color.MAGENTA);
            g.fillRoundRect(X, Y, 10, 10, 4, 4);
        }
    }
    
    /** This class is used to create a stone as an object.
     */
    private class Stone{
        private int X, Y;
        
        public Stone(){
            
                X = 10* (int)(Math.random() * 40) + 50;
                Y = 10* (int)(Math.random() * 40) + 50;
           }
        
        public void draw(Graphics g){
            g.setColor(Color.RED);
            g.fillRoundRect(X, Y, 10, 10, 4, 4);
        }
        
            
        
    }
    /** This class is used to create a snake as an object.
     */
    private class Snake{
        
        private int length;
        private int[] X;    //X coordinates of all the units of the snake
        private int[] Y;    //Y coordinates of all the units of the snake
        //tail is and(X[0], Y[0]) and head is at (X[length-1], Y[length-1])
        
        private String moving;
        
        public Snake(){
            length = 10;
            X = new int[100];
            Y = new int[100];
            moving = "up";
            
            for(int i=0; i<10; i++){
                X[i] = 200;
                Y[i] = 200 - i*10;
            }
        }
        
        public void draw(Graphics g){
            for(int i=0; i<length; i++){
                g.setColor(Color.BLUE);
                g.fillRoundRect(X[i], Y[i], 10, 10, 4, 4);
            }
        }
        
        public void update(){
            
            for(int i=0; i<(length - 1); i++){
                X[i] = X[i+1];
                Y[i] = Y[i+1];
            } 
            
            if(moving.equals("up"))
                Y[length-1] -= 10;
            else if(moving.equals("down"))
                Y[length-1] += 10;
            else if(moving.equals("left"))
                X[length-1] -= 10;
            else
                X[length-1] += 10;
            
            if(X[length-1] >= 500)
                X[length-1] = 0;
            if(Y[length-1] >= 500)
                Y[length-1] = 0;
            
            if(X[length-1] < 0)
                X[length-1] = 490;
            if(Y[length-1] < 0)
                Y[length-1] = 490;  
             
        }
        
        public void increaseLength(){
            
            length++;
            X[length -1 ] = X[length -2];
            Y[length -1 ] = Y[length -2];
            
            if(moving.equals("up"))
                Y[length-1] = Y[length-2] - 10;
            else if(moving.equals("down"))
                Y[length-1] = Y[length-2] + 10;
            else if(moving.equals("left"))
                X[length-1] = X[length-2] - 10;
            else
                X[length-1] = X[length-2] + 10;
           
        }
        
        
        public void changeDir(KeyEvent e){
            int code = e.getKeyCode();
            if(moving.equals("up") || moving.equals("down"))
                if(code == KeyEvent.VK_RIGHT)
                    moving = "right";
                else if(code == KeyEvent.VK_LEFT)
                    moving = "left";
            
            if(moving.equals("right") || moving.equals("left"))
                if(code == KeyEvent.VK_UP)
                    moving = "up";
                else if(code == KeyEvent.VK_DOWN)
                    moving = "down";
        }
        
    }
    
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        if(gameInProgress){
            for(int i=0; i<10; i++)
                stones[i].draw(g);
            snake.draw(g);
            if(food != null)
            food.draw(g);
        }
    }
    
    public void doNewGame(){
        boolean overlap;
        for(int i=0; i<10; i++){
            
            do{
                overlap = false;
                stones[i]  = new Stone();
                for(int p=0; p<i; p++)
                    if(stones[p].X == stones[i].X && stones[p].Y == stones[i].Y){
                            overlap = true;
                            break;
                    }
            }while(overlap);
        }
        
        
        snake = new Snake();
        score = 0;
        lives = 5;
        display.message = "Welcome to snake game!";
        
        timer.start();
        gameInProgress = true;
        
    }
    public SnakeGamePanel(){
        
        
        setBackground(Color.GREEN);
        setLayout(null);
        display = new DisplayPanel();
        add(display);
        display.setBounds(0, 500, 500, 100);
        
        gameInProgress = false;
        //setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        
        addMouseListener( new MouseAdapter() { 
            public void mousePressed(MouseEvent evt) {
                if(hasFocus() && !gameInProgress)
                    doNewGame();
                else
                    requestFocus();
            }
        } );
        
        addFocusListener( new FocusAdapter() {    
            public void focusGained(FocusEvent evt) {
                
                doNewGame();
                repaint();
            }
        } );
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                snake.changeDir(e);    
            }
        });
    }
    
    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Game");
        window.setContentPane(new SnakeGamePanel());
        window.setSize(500, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
    }
    
}
