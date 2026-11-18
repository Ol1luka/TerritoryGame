
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JPanel implements MouseListener, ActionListener {

    int menu = 0;
    int color = 0;

    ArrayList<Point> bashni = new ArrayList<>();
    ArrayList<Point> voiska = new ArrayList<>();

    Point vibrana = null;
    Point kuda = null;

    javax.swing.Timer timer = new javax.swing.Timer(20, this);

    public Main() {
        setPreferredSize(new Dimension(800, 500));
        addMouseListener(this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        if (menu == 0) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 800, 500);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));


            g.setColor(Color.GREEN);
            g.fillRect(300, 160, 200, 50);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("ИГРАТЬ", 360, 192);

            g.setColor(Color.ORANGE);
            g.fillRect(300, 240, 200, 50);

            g.setColor(Color.BLACK);
            g.drawString("НАСТРОЙКИ", 335, 272);

            g.setColor(Color.RED);
            g.fillRect(300, 320, 200, 50);

            g.setColor(Color.BLACK);
            g.drawString("ВЫХОД", 365, 352);
        }


        if (menu == 1) {
            if (color == 0) {
                g.setColor(new Color(100, 180, 100));
            }
            if (color == 1) {
                g.setColor(Color.GRAY);
            }
            if (color == 2) {
                g.setColor(new Color(200, 180, 100));
            }

            g.fillRect(0, 0, 800, 500);


            for (int i = 0; i < bashni.size(); i++) {
                Point p = bashni.get(i);

                if (i == 0) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.RED);
                }

                g.fillOval(p.x - 30, p.y - 30, 60, 60);

                g.setColor(Color.BLACK);
                g.drawOval(p.x - 30, p.y - 30, 60, 60);
            }




            g.setColor(Color.BLUE);

            for (int i = 0; i < voiska.size(); i++) {
                Point p = voiska.get(i);
                g.fillOval(p.x - 5, p.y - 5, 10, 10);
            }


            g.setColor(Color.DARK_GRAY);
            g.fillRect(670, 10, 115, 40);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("В МЕНЮ", 690, 36);
        }

        // настройки
        if (menu == 2) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 800, 500);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("НАСТРОЙКИ", 300, 100);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Выбери цвет поля", 310, 160);

            g.setColor(Color.GREEN);
            g.fillRect(230, 200, 100, 60);

            g.setColor(Color.GRAY);
            g.fillRect(350, 200, 100, 60);

            g.setColor(new Color(200, 180, 100));
            g.fillRect(470, 200, 100, 60);

            g.setColor(Color.ORANGE);
            g.fillRect(300, 340, 200, 50);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("НАЗАД", 365, 372);
        }
    }


    public void startGame() {
        menu = 1;

        bashni.clear();
        voiska.clear();

        vibrana = null;
        kuda = null;

        bashni.add(new Point(150, 250));
        bashni.add(new Point(450, 150));
        bashni.add(new Point(650, 350));
        bashni.add(new Point(350, 380));
    }


    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // кнопки меню
        if (menu == 0) {
            if (x >= 300 && x <= 500) {
                if (y >= 160 && y <= 210) {
                    startGame();
                }

                if (y >= 240 && y <= 290) {
                    menu = 2;
                }

                if (y >= 320 && y <= 370) {
                    System.exit(0);
                }
            }
        }

        else if (menu == 1) {


            if (x >= 670 && x <= 785 && y >= 10 && y <= 50) {
                menu = 0;
                voiska.clear();
                vibrana = null;
                kuda = null;
                repaint();
                return;
            }


            for (int i = 0; i < bashni.size(); i++) {
                Point p = bashni.get(i);

                if (p.distance(x, y) < 30) {
                    if (i == 0) {
                        vibrana = p;
                    } else {
                        if (vibrana != null) {
                            kuda = p;
                            voiska.clear();

                            // отправляем 5 солдат
                            for (int j = 0; j < 5; j++) {
                                voiska.add(new Point(
                                        vibrana.x - j * 15,
                                        vibrana.y
                                ));
                            }

                            vibrana = null;
                        }
                    }
                    break;
                }
            }
        }


        else if (menu == 2) {
            if (y >= 200 && y <= 260) {
                if (x >= 230 && x <= 330) {
                    color = 0;
                }

                if (x >= 350 && x <= 450) {
                    color = 1;
                }

                if (x >= 470 && x <= 570) {
                    color = 2;
                }
            }

            if (x >= 300 && x <= 500 &&
                    y >= 340 && y <= 390) {
                menu = 0;
            }
        }

        repaint();
    }
    public void actionPerformed(ActionEvent e) {
        if (menu == 1 && kuda != null) {
            for (int i = voiska.size() - 1; i >= 0; i--) {
                Point p = voiska.get(i);

                double dx = kuda.x - p.x;
                double dy = kuda.y - p.y;

                double rast = Math.sqrt(dx * dx + dy * dy);

                if (rast < 5) {
                    voiska.remove(i);
                } else {
                    p.x = p.x + (int) Math.round(dx / rast * 3);
                    p.y = p.y + (int) Math.round(dy / rast * 3);
                }
            }

            if (voiska.size() == 0) {
                kuda = null;
            }
        }

        repaint();
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame okno = new JFrame();

        Main igra = new Main();

        okno.add(igra);
        okno.pack();

        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setLocationRelativeTo(null);
        okno.setResizable(false);
        okno.setVisible(true);
    }
}
