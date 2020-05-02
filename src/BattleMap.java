import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class BattleMap extends JPanel {
    private GameWindow gameWindow;

    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;
    public static final int margin = 10;

    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;

    private int cellHeight;
    private int cellWidth;
    private int humanNo = 0;

    private boolean isInit = false;
    private boolean isHumVsAi = true;
    private boolean humanXTurn = true;
    private boolean xIsFirst = true;


    public BattleMap(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setBackground(Color.ORANGE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });

    }

    private void update(MouseEvent e) {
        if(Logic.gameFinished) {
            return;
        }
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;

        if(isHumVsAi){
            if(!Logic.gameFinished){
                Logic.setHumanXY(cellX, cellY);
            }
        }
        else {
            Logic.setHumanNoXY(humanNo, cellX, cellY);
            if(humanNo == 0){
                humanNo = 1;
                gameWindow.setTurnInfo("Ход игрока \"O\"");
            }
            else {
                humanNo = 0;
                gameWindow.setTurnInfo("Ход игрока \"X\"");
            }
        }
        repaint();
        if(Logic.gameFinished){
            if(!isHumVsAi) {
                gameWindow.setTurnInfo(Logic.gameFinishStatus);
            }
            JOptionPane.showMessageDialog(gameWindow,Logic.gameFinishStatus);
        }

        System.out.println(cellX + " " + cellY);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!isInit) {
            return;
        }

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;

        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int i = 0; i < Logic.SIZE ; i++) {
            for (int j = 0; j < Logic.SIZE; j++) {
                if(Logic.map[i][j]==Logic.DOT_O){
                    drawO(g,j,i);
                }
                if(Logic.map[i][j]==Logic.DOT_X){
                    drawX(g,j,i);
                }
            }
        }
    }

    private void drawO(Graphics g, int cellX, int cellY){
        Graphics2D g2D = (Graphics2D)g;

        g.setColor(new Color(0,0,255));

        //g.drawOval(cellX*cellWidth + 5 , cellY*cellHeight + 5, cellWidth - 10,cellHeight - 10);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.drawOval(cellX*cellWidth + margin , cellY*cellHeight + margin, cellWidth - margin*2,cellHeight - margin*2);
    }
    private void drawX(Graphics g, int cellX, int cellY){
        g.setColor(new Color(255, 3, 0));
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));

        g2.drawLine(cellX*cellWidth + margin , cellY*cellHeight + margin,
                (cellX+1)*cellWidth - margin , (cellY+1)*cellHeight - margin);
        g2.drawLine(cellX*cellWidth + margin , (cellY+1)*cellHeight - margin,
                (cellX+1)*cellWidth - margin, cellY*cellHeight + margin);



    }


    void startNewGame(int gameMode, int fieldSizeX, int fieldSizeY, int winLength,boolean isHumVsAi,boolean xIsFirst) {
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLength = winLength;
        this.isHumVsAi = isHumVsAi;
        this.xIsFirst = xIsFirst;
        isInit = true;
        if(!isHumVsAi){
            if(xIsFirst)
                humanNo = 0;
            else
                humanNo = 1;
        }
        repaint();
    }
}
