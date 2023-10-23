package project2;
import javax.swing.*;
import java.awt.*;

public class GUI1024 {
    public static void main(String arg[]){
        JMenu fileMenu;
        JMenuItem quitItem;
        JMenuItem resetItem;
        JMenuItem resizeItem;
        JMenuItem winningScore;
        JMenuItem resetStatistics;
        JMenuBar menus;

        fileMenu = new JMenu("Options");
        quitItem = new JMenuItem("Exit");
        resetItem = new JMenuItem ("Reset");
        resizeItem = new JMenuItem ("Resize");
        winningScore = new JMenuItem ("Set Winning goal");
        resetStatistics = new JMenuItem("Reset Statistics");

        fileMenu.add(winningScore);
        fileMenu.add(resetItem);
        fileMenu.add(resizeItem);
        fileMenu.add(resetStatistics);
        fileMenu.add(quitItem);

        menus = new JMenuBar();
        menus.add(fileMenu);

        JFrame g = new JFrame();
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g.setTitle("1024");
        g.setResizable(true);
        g.add(new GUI1024Panel(quitItem,resetItem,
                resizeItem, winningScore, resetStatistics), BorderLayout.CENTER);
        g.setLocationRelativeTo(null);
        g.setJMenuBar(menus);
        g.pack();
        g.setVisible(true);
    }
}
