import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements MouseListener, ActionListener {

    int menu = 0;

    int[] x = {150, 450, 650, 350};
    int[] y = {250, 150, 350, 380};

    int[] owner = {1, 2, 2, 2};
    int[] soldiers = {100, 12, 15, 8};

    int selected = -1;
    int target = -1;
    int army = 0;

    double armyX;
    double armyY;

    boolean victory = false;

    Timer timer = new Timer(20, this);

    public Main() {
        setPreferredSize(new Dimension(800, 500));
        addMouseListener(this);
        timer.start();
    }

    public void startGame() {
        menu = 1;

        owner = new int[]{1, 2, 2, 2};
        soldiers = new int[]{100, 12, 15, 8};

        selected = -1;
        target = -1;
        army = 0;
        victory = false;
    }

    public void button(Graphics g, String text, int x, int y,
                       int w, int h, Color c) {

        g.setColor(c);
        g.fillRect(x, y, w, h);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        FontMetrics fm = g.getFontMetrics();

        int textX = x + (w - fm.stringWidth(text)) / 2;
        int textY = y + (h + fm.getAscent() - fm.getDescent()) / 2;

        g.drawString(text, textX, textY);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (menu == 0) {

            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 800, 500);

            button(g, "ИГРАТЬ", 300, 160, 200, 50, Color.GREEN);
            button(g, "НАСТРОЙКИ", 300, 240, 200, 50, Color.ORANGE);
            button(g, "ВЫХОД", 300, 320, 200, 50, Color.RED);
        }

        if (menu == 1) {

            g.setColor(new Color(100, 180, 100));
            g.fillRect(0, 0, 800, 500);

            for (int i = 0; i < 4; i++) {

                if (owner[i] == 1) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.RED);
                }

                g.fillOval(x[i] - 30, y[i] - 30, 60, 60);

                g.setColor(Color.BLACK);
                g.drawOval(x[i] - 30, y[i] - 30, 60, 60);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));

                g.drawString("" + soldiers[i], x[i] - 15, y[i] + 7);
            }

            if (target != -1) {

                g.setColor(Color.BLUE);
                g.fillOval((int) armyX - 15, (int) armyY - 15, 30, 30);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 12));

                g.drawString("" + army, (int) armyX - 10, (int) armyY + 4);
            }

            button(g, "В МЕНЮ", 670, 10, 115, 40, Color.LIGHT_GRAY);

            if (victory) {

                g.setColor(Color.DARK_GRAY);
                g.fillRect(200, 180, 400, 140);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 36));

                g.drawString("ПОБЕДА!", 310, 235);

                button(g, "В МЕНЮ", 280, 260, 240, 40, Color.GREEN);
            }
        }

        if (menu == 2) {

            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 800, 500);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));

            g.drawString("НАСТРОЙКИ", 290, 100);

            button(g, "Жёлтый", 170, 200, 140, 60, Color.YELLOW);

            button(g, "Фиолетовый", 330, 200, 140, 60,
                    new Color(180, 100, 230));

            button(g, "Красный", 490, 200, 140, 60, Color.RED);

            button(g, "НАЗАД", 300, 340, 200, 50, Color.ORANGE);
        }
    }

    public boolean inside(int mx, int my, int x, int y, int w, int h) {

        return mx >= x && mx <= x + w
                && my >= y && my <= y + h;
    }

    public void mouseClicked(MouseEvent e) {

        int mx = e.getX();
        int my = e.getY();

        if (menu == 0) {

            if (inside(mx, my, 300, 160, 200, 50)) {
                startGame();
            }

            else if (inside(mx, my, 300, 240, 200, 50)) {
                menu = 2;
            }

            else if (inside(mx, my, 300, 320, 200, 50)) {
                System.exit(0);
            }
        }

        else if (menu == 2) {

            if (inside(mx, my, 300, 340, 200, 50)) {
                menu = 0;
            }
        }

        else if (menu == 1) {

            if (inside(mx, my, 670, 10, 115, 40)) {
                menu = 0;
                return;
            }

            if (victory) {

                if (inside(mx, my, 280, 260, 240, 40)) {
                    menu = 0;
                }

                return;
            }

            if (target != -1) {
                return;
            }

            for (int i = 0; i < 4; i++) {

                double dx = mx - x[i];
                double dy = my - y[i];

                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= 30) {

                    if (selected == -1) {

                        if (owner[i] == 1 && soldiers[i] > 0) {
                            selected = i;
                        }
                    }

                    else if (selected == i) {
                        selected = -1;
                    }

                    else {

                        army = soldiers[selected];

                        soldiers[selected] = 0;

                        armyX = x[selected];
                        armyY = y[selected];

                        target = i;
                        selected = -1;
                    }

                    return;
                }
            }

            selected = -1;
        }

        repaint();
    }

    public void battle() {

        if (owner[target] == 1) {

            soldiers[target] += army;
        }

        else if (army > soldiers[target]) {

            soldiers[target] = army - soldiers[target];

            owner[target] = 1;
        }

        else {

            soldiers[target] -= army;
        }

        army = 0;
        target = -1;

        if (owner[0] == 1 && owner[1] == 1
                && owner[2] == 1 && owner[3] == 1) {

            victory = true;
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (menu == 1 && target != -1 && !victory) {

            double dx = x[target] - armyX;
            double dy = y[target] - armyY;

            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= 3) {

                battle();
            }

            else {

                armyX += dx / distance * 3;
                armyY += dy / distance * 3;
            }
        }

        repaint();
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {

        JFrame window = new JFrame();

        window.add(new Main());
        window.pack();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }
}
