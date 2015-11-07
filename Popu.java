import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by wuxt on 2015/11/5.
 */
public class Popu extends JFrame{
    public Popu(){
        PopuOval a = new PopuOval();
        addKeyListener(a);
        add(a);
    }
    public static void main(String args[]){
        Popu frame = new Popu();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(500,180,250,450);
       // frame.setSize(250,430);
        frame.setTitle("aiwuxt");
        frame.setVisible(true);
        frame.setResizable(false);
    }
}

class PopuOval extends JPanel implements KeyListener{
    private int score = 0;
    private int i = 0;
    private int j = 0;
    private int fX;
    private int fY;
    private int sX;
    private int sY;
    private int fColor;
    private int sColor;
    private boolean fDown = true;
    private boolean sDown = true;
    private boolean left = true;
    private boolean right = true;
    private final int RGB[][] = new int[][]{
            {0,153,204}, {153,51,204}, {204,0,0}, {255,136,0}, {102,153,0}};
    private int turnState;
    private int nCount = 0;
    private boolean isRelated[][] = new boolean[12][20];
    public void setisRelated(){
        for(i = 1; i <= 10; i++){
            for(j = 0; j <= 18; j++)
                isRelated[i][j] = false;
        }
    }
    int[][] map = new int[12][20];
    private final int oval1[] = new int[]{0,1,2,3,4};
    private final int oval2[] = new int[]{0,1,2,3,4};

    public void newOval(){
        fColor = (int)(Math.random() * 1000) % 5;
        sColor = (int)(Math.random() * 1000) % 5;
        turnState = 0;
        fX = 5;
        fY = 0;
        sX = 6;
        sY = 0;
        if(gameOver()==1){
            newMap();
            drawWall();
            score = 0;
            JOptionPane.showMessageDialog(null, "GAME OVER");
        }
    }

    public void drawWall(){
        for(i = 0; i < 12; i++){
            map[i][19] = 6;
        }
        for(j = 0; j < 20; j++){
            map[0][j] = 6;
            map[11][j] = 6;
        }
    }

    public void newMap(){
        for(i = 0; i < 12; i++){
            for(j = 0; j < 20; j++){
                map[i][j] = 5;
            }
        }
    }

    PopuOval(){
        newMap();
        drawWall();
        newOval();
        Timer timer = new Timer(1000, new TimerListener());
        timer.start();
    }

    public void turn(){
        blow();
        switch (turnState){
            case 0:
                if(fDown&&sDown){
                    sY+=1;sX=fX;
                    turnState = 1;
                }
                break;
            case 1:
                if(fDown&&sDown&&left){
                    sX-=1;sY=fY;
                    turnState = 2;
                }
                break;
            case 2:
                if(fDown&&sDown&&sY!=0){
                    sY-=1;sX=fX;
                    turnState = 3;
                }
                break;
            case 3:
                if(fDown&&sDown&&right){
                    sX+=1;sY=fY;
                    turnState = 0;
                }
                break;
        }
        repaint();
    }
    public void left(){
        blow();
        if(fDown&&sDown&&left){fX -= 1;sX -= 1;}
        repaint();
    }
    public void right(){
        blow();
        if(fDown&&sDown&&right){fX += 1;sX += 1;}
        repaint();
    }
    public void down(){
        blow();
        if(fDown){
            fY += 1;
        }
        if(sDown){
            sY += 1;
        }
        addOval();
        repaint();
    }

    public void blow(){
        if(fY<18&&map[fX][fY+1]==5) fDown=true;
        else fDown=false;
        if(sY<18&&map[sX][sY+1]==5) sDown=true;
        else sDown=false;
        if(fX>0&&fX<11&&sX>0&&sX<11&&map[fX-1][fY]==5&&map[sX-1][sY]==5) left=true;
        else left=false;
        if(fX>0&&fX<11&&sX>0&&sX<11&&map[fX+1][fY]==5&&map[sX+1][sY]==5) right=true;
        else right=false;
    }

    public void checkOval(){
        for(j=18;j>=0;j--){
            for(i=1;i<=10;i++){
                if(map[i][j]<5){
                    if(!isRelated[i][j]){
                        groupOval(i,j,map[i][j]);
                        if(nCount<4){
                            nCount = 0;
                        }
                    }
                }
            }
        }
    }

    public void groupOval(int a, int b, int value){
        if(a>=1 && a<=11 && b<=18 && b>=0){
            if(map[a][b] == value && isRelated[a][b] == false){
                nCount++;
                isRelated[a][b] = true;
                groupOval(a, b + 1, value);
                groupOval(a + 1, b, value);
                groupOval(a, b - 1, value);
                groupOval(a - 1, b, value);
            }
        }
    }

    public void deOval(){
        for(j = 18; j >= 0; j--) {
            for (i = 1; i <= 10; i++) {
                if (isRelated[i][j]){
                    map[i][j] = 5;
                }
            }
        }
        setisRelated();
        dropOval();
    }

    public void dropOval(){
        for(j = 18; j >= 0; j--){
            for(i = 1; i <= 10; i++){
                if(map[i][j]==5){
                    int n = j;
                    while(n>0){
                        if(map[i][n]<5){
                            map[i][j] = map[i][n];
                            map[i][n] = 5;
                            break;
                        }
                        n--;
                    }
                }
            }
        }
    }

    public int gameOver(){
        blow();
        if(fDown||sDown||left||right||fY!=0||sY!=0) return 0;
        else return 1;
    }

    public void addOval(){
        blow();
        if(fDown==false) map[fX][fY] = oval1[fColor];
        if(sDown==false) map[sX][sY] = oval2[sColor];
        if(fDown==false&&sDown==false){
            /*nCount = 0;setisRelated();
            groupOval(fX,fY,map[fX][fY]);
            if(nCount>=4) deOval();
            nCount = 0;setisRelated();
            groupOval(sX,sY,map[sX][sY]);
            if(nCount>=4) deOval();*/
            checkOval();
            newOval();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(RGB[fColor][0],RGB[fColor][1],RGB[fColor][2]));
        g.fillOval(fX * 20, fY * 20, 20, 20);
        g.setColor(new Color(RGB[sColor][0],RGB[sColor][1],RGB[sColor][2]));
        g.fillOval(sX * 20, sY * 20, 20, 20);

        for(j = 0; j < 20; j++){
            for(i = 0; i < 12; i++){
                if(map[i][j] < 5){
                    g.setColor(new Color(RGB[map[i][j]][0],RGB[map[i][j]][1],RGB[map[i][j]][2]));
                    g.fillOval(i*20, j*20, 20, 20);
                }
                if(map[i][j] == 6){
                    g.setColor(Color.BLACK);
                    g.drawRect(i * 20, j * 20, 20, 20);
                }
            }
        }
        g.drawString("score=" + score, 0, 450);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                down();
                break;
            case KeyEvent.VK_UP:
                turn();
                break;
            case KeyEvent.VK_RIGHT:
                right();
                break;
            case KeyEvent.VK_LEFT:
                left();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class TimerListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
        repaint();
            blow();
            if(fDown) fY += 1;
            if(sDown) sY += 1;
            addOval();
        }
    }
}
