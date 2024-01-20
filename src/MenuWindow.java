import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class MenuWindow extends JFrame {


    public MenuWindow() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon background = new ImageIcon("src/images/pacmanBackground.png");

        BackGroundPanel panel = new BackGroundPanel(background);

        panel.setLayout(new GridLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JButton newGame = new JButton("NEW GAME");
        newGame.setAlignmentX(CENTER_ALIGNMENT);

        newGame.addActionListener(e -> {
            JFrame sizeSelectionFrame = new JFrame();

            JPanel sizeSelectionPanel = new JPanel();
            sizeSelectionPanel.setLayout(new BoxLayout(sizeSelectionPanel, BoxLayout.Y_AXIS));

            JLabel labelWidth = new JLabel("Select board width: ");
            JLabel labelHeight = new JLabel("Select board height: ");
            JSpinner widthSizeSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));
            JSpinner heightSizeSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));

            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
            labelPanel.add(labelWidth);

            JPanel spinnerPanel = new JPanel();
            spinnerPanel.setLayout(new BoxLayout(spinnerPanel, BoxLayout.X_AXIS));
            spinnerPanel.add(widthSizeSpinner);

            JButton ok = new JButton("OK");
            ok.setAlignmentX(Component.CENTER_ALIGNMENT);
            sizeSelectionPanel.setPreferredSize(new Dimension(300, 250));
            labelPanel.setPreferredSize(new Dimension(300, 250));
            JButton startButton = new JButton("Start Game");
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setVisible(false);

            ok.addActionListener(e1 -> {
                labelPanel.removeAll();
                labelPanel.add(labelHeight);
                spinnerPanel.removeAll();
                spinnerPanel.add(heightSizeSpinner);
                ok.setVisible(false);
                startButton.setVisible(true);
                sizeSelectionPanel.setPreferredSize(new Dimension(300, 250));
                labelPanel.setPreferredSize(new Dimension(300, 250));
                sizeSelectionFrame.pack();
                sizeSelectionFrame.setLocationRelativeTo(null);
            });

            startButton.addActionListener(e2 -> {
                int width = (int) widthSizeSpinner.getValue();
                int height = (int) heightSizeSpinner.getValue();
                sizeSelectionFrame.dispose();
                this.setVisible(false);

                JFrame game = new JFrame();
                game.setLayout(new BorderLayout());
                JPanel scorePanel = new JPanel(new GridLayout(1, 3));
                JLabel scoreLabel = new JLabel("score: 0");
                scoreLabel.setFont(new Font("Dialog", Font.BOLD, 16));
                JLabel timerLabel = new JLabel("time: ");
                timerLabel.setFont(new Font("Dialog", Font.BOLD, 16));
                JPanel livesPanel = new JPanel();
                livesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                ImageIcon heart1 = new ImageIcon("src\\images\\heart.png");
                ImageIcon heart2 = new ImageIcon("src\\images\\heart.png");
                ImageIcon heart3 = new ImageIcon("src\\images\\heart.png");
                JLabel imageLabel1 = new JLabel(heart1);
                JLabel imageLabel2 = new JLabel(heart2);
                JLabel imageLabel3 = new JLabel(heart3);
                livesPanel.add(imageLabel1);
                livesPanel.add(imageLabel2);
                livesPanel.add(imageLabel3);


                GamePanel gamePanel = new GamePanel(imageLabel1, imageLabel2, imageLabel3, this, timerLabel, scoreLabel, game, width, height);


                scorePanel.setSize(width * 32, height * 32);
                scorePanel.add(livesPanel, BorderLayout.WEST);
                scorePanel.add(scoreLabel, BorderLayout.CENTER);
                scorePanel.add(timerLabel, BorderLayout.EAST);

                livesPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 25));
                scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 25));
                timerLabel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 20));

                game.getContentPane().add(gamePanel, BorderLayout.SOUTH);
                game.getContentPane().add(scorePanel, BorderLayout.NORTH);
                game.setResizable(true);
                game.pack();
                game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                game.setLocationRelativeTo(null);
                game.setVisible(true);


            });

            sizeSelectionPanel.add(labelPanel);
            sizeSelectionPanel.add(spinnerPanel);
            sizeSelectionPanel.add(Box.createVerticalGlue());
            sizeSelectionPanel.add(Box.createVerticalStrut(10));
            sizeSelectionPanel.add(ok);
            sizeSelectionPanel.add(Box.createVerticalStrut(10));
            sizeSelectionPanel.add(startButton);
            sizeSelectionPanel.add(Box.createVerticalGlue());

            sizeSelectionFrame.add(sizeSelectionPanel);
            sizeSelectionFrame.setResizable(false);
            sizeSelectionFrame.pack();
            sizeSelectionFrame.setLocationRelativeTo(null);
            sizeSelectionFrame.setVisible(true);
            sizeSelectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });


        JButton highScores = new JButton(" HIGH SCORES");
        highScores.setAlignmentX(CENTER_ALIGNMENT);
        highScores.addActionListener(e -> {
            setVisible(false);


            JFrame frame = new JFrame("HIGH SCORES");

            List<HighScore> scores = HSManager.loadHighScores();
            DefaultListModel<String> model = new DefaultListModel<>();

            for (HighScore score : scores) {
                String player = score.getNickname() + " - " + score.getScore();
                model.addElement(player);
            }

            JList<String> jList = new JList<>(model);


            JScrollPane jScrollPane = new JScrollPane(jList);

            frame.add(jScrollPane);
            frame.setLocationRelativeTo(null);
            frame.setSize(300, 500);
            frame.setResizable(true);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    setVisible(true);
                }
            });


        });
        JButton exit = new JButton("EXIT");
        exit.setAlignmentX(CENTER_ALIGNMENT);
        exit.addActionListener(e -> System.exit(0));

        panel.add(Box.createVerticalGlue());
        panel.add(Box.createHorizontalGlue());
        panel.add(newGame);
        panel.add(Box.createRigidArea(new Dimension(100, 30)));
        panel.add(highScores);
        panel.add(Box.createRigidArea(new Dimension(100, 30)));
        panel.add(exit);
        panel.add(Box.createHorizontalGlue());
        panel.add(Box.createVerticalGlue());

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.setSize(new Dimension(800, 600));
        this.setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);


    }


}
