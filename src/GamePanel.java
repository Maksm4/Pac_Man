import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


public class GamePanel extends JPanel implements Runnable {

    final int tileSize = 32; // 32x32 pixels 1 tile
    private final Player player;
    public int currentHealth;
    private Ghost ghost1;
    private Ghost ghost2;
    private JLabel heart1;
    private JLabel heart2;
    private JLabel heart3;

    private final JFrame frame;
    private boolean running;

    private final int screenColumns;
    private final int screenRows;
    private ArrayList<Obstacle> walls;
    private static ConcurrentLinkedQueue<Coin> coins;
    private ArrayList<Ghost> ghosts;

    private ArrayList<ImageIcon> playerIcons;
    private int currentIconIndex;
    private ImageIcon currentPlayerIcon;
    private ImageIcon coinIcon;
    private ImageIcon ghostIcon1;
    private ImageIcon ghostIcon2;

    private JTable gameTable;
    private final GameTableModel tableModel;
    Thread gameThread;
    private int collectedCoins;

    KeyMovement keyMovement = new KeyMovement();


    private final int playerSpeed;
    private int startingTime;
    private int tookTime;
    private JLabel score;
    private JLabel timer;
    private JFrame mainFrame;

    public GamePanel(JLabel heart1, JLabel heart2, JLabel heart3, JFrame mainFrame, JLabel labelTimer, JLabel labelScore, JFrame frame, int width, int height) {
        screenColumns = width; //tiles
        screenRows = height; //tiles
        int playerX = screenColumns - 2;
        int playerY = screenRows - 2;
        playerSpeed = 1;
        tookTime = 0;
        running = true;
        collectedCoins = 0;
        player = new Player(playerX, playerY);
        this.mainFrame = mainFrame;
        this.score = labelScore;
        this.timer = labelTimer;
        this.startingTime = (int) System.currentTimeMillis();
        this.tookTime = 0;
        playerIcons = new ArrayList<>();
        walls = new ArrayList<>();
        coins = new ConcurrentLinkedQueue<>();
        ghosts = new ArrayList<>();
        currentHealth = player.getHealth();
        this.heart1 = heart1;
        this.heart2 = heart2;
        this.heart3 = heart3;


        for (int i = 1; i <= 18; i++) {
            String image = "src\\images\\Animation\\pacman" + i + ".png";
            try {
                File imageFile = new File(image);
                ImageIcon icon = new ImageIcon(ImageIO.read(imageFile));
                playerIcons.add(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        currentIconIndex = 0;
        currentPlayerIcon = playerIcons.get(currentIconIndex);


        try {
            File imageFile1 = new File("src\\images\\coin.png");
            coinIcon = new ImageIcon(ImageIO.read(imageFile1));

            File imageFile2 = new File("src\\images\\ghost.png");
            ghostIcon1 = new ImageIcon(ImageIO.read(imageFile2));

            File imageFile3 = new File("src\\images\\ghost2.png");
            ghostIcon2 = new ImageIcon(ImageIO.read(imageFile3));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.frame = frame;
        int screenWidth = screenColumns * tileSize; //board
        int screenHeight = screenRows * tileSize;  //board


        setLayout(new BorderLayout());
        setSize(new Dimension(screenWidth, screenHeight));


        tableModel = new GameTableModel(screenRows, screenColumns);

        gameTable = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                Object value = tableModel.getValueAt(row, column);
                if (value instanceof Color || value instanceof Coin || value instanceof ImageIcon || value == null || value instanceof Ghost) {
                    return new ColorCellRenderer();
                }

                return super.getCellRenderer(row, column);
            }
        };
        gameTable.setRowSelectionAllowed(false);
        gameTable.setColumnSelectionAllowed(false);
        gameTable.setCellSelectionEnabled(false);
        gameTable.setFocusable(false);
        gameTable.addMouseListener(new GameMouse(this));
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        sorter.setSortable(0, false);
        gameTable.setRowSorter(sorter);


        //no set column width available
        gameTable.setRowHeight(tileSize);
        for (int i = 0; i < screenColumns; i++) {
            gameTable.getColumnModel().getColumn(i).setPreferredWidth(tileSize);
        }

        this.add(gameTable, BorderLayout.CENTER);

        gameThread = new Thread(this);
        setBarrier();
        for (int i = 0; i < 10; i++) {
            middleWallsCreation();
        }
        coinCreation();


        ghost1 = new Ghost(coins, walls, tableModel, gameTable, ghostIcon1, screenColumns, screenRows);
        ghost2 = new Ghost(coins, walls, tableModel, gameTable, ghostIcon2, screenColumns, screenRows);

        gameThread.start();
        Thread threadGhost1 = new Thread(ghost1);
        Thread threadGhost2 = new Thread(ghost2);
        threadGhost1.start();
        threadGhost2.start();


        this.setFocusable(true);
        this.addKeyListener(keyMovement);
        this.setFocusable(true);


    }


    @Override
    public void run() {
        int i = 3;
        while (running) {
            if (coins.isEmpty() || player.getHealth() == 0) {
                running = false;
                JFrame gameOverFrame = new JFrame();
                JPanel gameOverPanel = new JPanel();
                JLabel gameOverLabel = new JLabel("GAME OVER");
                JPanel filler1 = new JPanel();
                JPanel filler2 = new JPanel();

                gameOverLabel.setFont(new Font("Serif", Font.BOLD, 18));

                JTextField gameOverText = new JTextField(5);
                gameOverText.setText("enter your nickname");
                gameOverText.setMaximumSize(new Dimension(300, 50));
                gameOverText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                gameOverLabel.setForeground(Color.YELLOW);
                filler2.setBackground(new Color(55, 26, 82));
                filler1.setBackground(new Color(55, 26, 82));
                gameOverPanel.setBackground(new Color(55, 26, 82));

                gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
                gameOverPanel.add(Box.createVerticalGlue());
                gameOverPanel.add(gameOverLabel);
                gameOverPanel.add(Box.createRigidArea(new Dimension(0, 50)));
                gameOverPanel.add(gameOverText);
                gameOverPanel.add(Box.createVerticalGlue());
                frame.setVisible(false);
                gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                gameOverText.addActionListener(e -> {
                    String nick = gameOverText.getText();
                    int points = collectedCoins * 100 / tookTime;
                    HighScore highScore = new HighScore(nick, points);
                    HSManager.saveHighScore(highScore);
                    gameOverFrame.setVisible(false);
                    mainFrame.setVisible(true);


                });

                gameOverFrame.setPreferredSize(new Dimension(600, 300));
                gameOverFrame.setLayout(new GridLayout());
                gameOverFrame.add(filler1);
                gameOverFrame.add(gameOverPanel);
                gameOverFrame.add(filler2);
                gameOverFrame.pack();
                gameOverFrame.setLocationRelativeTo(null);
                gameOverFrame.setVisible(true);
                gameOverFrame.setFocusable(true);
                gameOverFrame.setFocusableWindowState(true);

            }
            if (checkCollision(player.getY(), player.getX(), ghost1.getRow(), ghost1.getColumn()) || checkCollision(player.getY(), player.getX(), ghost2.getRow(), ghost2.getColumn())) {
                if (i == 3) {
                    heart3.setVisible(false);
                } else if (i == 2) {
                    heart2.setVisible(false);
                } else if (i == 1) {
                    heart1.setVisible(false);
                }

                i--;
                currentHealth = player.getHealth() - 1;
                player.setHealth(currentHealth);
            }

            updatePlayerIcon();
            updateTimerLabel();
            update();
            repaint();

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int row = 0; row < screenRows; row++) {
            for (int column = 0; column < screenColumns; column++) {
                Object value = tableModel.getValueAt(row, column);
                if (value instanceof Color) {
                    g2d.setColor((Color) value);
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                } else if (value instanceof ImageIcon) {
                    ImageIcon icon = (ImageIcon) value;
                    Image image = icon.getImage();
                    g2d.drawImage(image, column * tileSize, row * tileSize, null);
                } else if (value instanceof Coin) {

                    g2d.drawImage(coinIcon.getImage(), column * tileSize, row * tileSize, null);

                } else if (value instanceof Ghost) {
                    g2d.drawImage(ghostIcon1.getImage(), column * tileSize, row * tileSize, null);

                }
            }
        }
        g2d.dispose();
    }


    public void update() {
        if (keyMovement.isUp()) {
            if (player.getY() > 0 && !isWall(player.getY() - 1, player.getX())) {
                if (isCoin(player.getY() - 1, player.getX()) && !isCoinCollected(player.getY() - 1, player.getX())) {
                    collectedCoins += 10;

                    tableModel.setValueAt(null, player.getY() - 1, player.getX());
                    updateScoreLabel();
                    for (Coin coin : coins) {
                        if (coin.getRow() == player.getY() - 1 && coin.getColumn() == player.getX()) {
                            coin.setCollected(true);
                            coins.remove(coin);
                            break;
                        }
                    }
                }
                tableModel.setValueAt(null, player.getY(), player.getX());
                player.playerY -= playerSpeed;
                updatePlayerIcon();
                tableModel.setValueAt(currentPlayerIcon, player.getY(), player.getX());
            }
        } else if (keyMovement.isDown()) {
            if (player.getY() < screenRows - 1 && !isWall(player.getY() + 1, player.getX())) {
                if (isCoin(player.getY() + 1, player.getX()) && !isCoinCollected(player.getY() + 1, player.getX())) {
                    collectedCoins += 10;

                    tableModel.setValueAt(null, player.getY() + 1, player.getX());
                    updateScoreLabel();
                    for (Coin coin : coins) {
                        if (coin.getRow() == player.getY() + 1 && coin.getColumn() == player.getX()) {
                            coin.setCollected(true);
                            coins.remove(coin);
                            break;
                        }
                    }
                }
                tableModel.setValueAt(null, player.getY(), player.getX());
                player.playerY += playerSpeed;
                tableModel.setValueAt(currentPlayerIcon, player.getY(), player.getX());
            }
        } else if (keyMovement.isLeft()) {
            if (player.getX() > 0 && !isWall(player.getY(), player.getX() - 1)) {
                if (isCoin(player.getY(), player.getX() - 1) && !isCoinCollected(player.getY(), player.getX() - 1)) {
                    collectedCoins += 10;

                    tableModel.setValueAt(null, player.getY(), player.getX() - 1);
                    updateScoreLabel();
                    for (Coin coin : coins) {
                        if (coin.getRow() == player.getY() && coin.getColumn() == player.getX() - 1) {
                            coin.setCollected(true);
                            coins.remove(coin);
                            break;
                        }
                    }
                }
                tableModel.setValueAt(null, player.getY(), player.getX());
                player.playerX -= playerSpeed;
                tableModel.setValueAt(currentPlayerIcon, player.getY(), player.getX());
            }
        } else if (keyMovement.isRight()) {
            if (player.getX() < screenColumns - 1 && !isWall(player.getY(), player.getX() + 1)) {
                if (isCoin(player.getY(), player.getX() + 1) && !isCoinCollected(player.getY(), player.getX() + 1)) {
                    collectedCoins += 10;

                    tableModel.setValueAt(null, player.getY(), player.getX() + 1);
                    updateScoreLabel();
                    for (Coin coin : coins) {
                        if (coin.getRow() == player.getY() && coin.getColumn() == player.getX() + 1) {
                            coin.setCollected(true);
                            coins.remove(coin);
                            break;
                        }
                    }
                }
                tableModel.setValueAt(null, player.getY(), player.getX());
                player.playerX += playerSpeed;
                tableModel.setValueAt(currentPlayerIcon, player.getY(), player.getX());
            }
        } else if (keyMovement.isShortcut()) {
            frame.setVisible(false);
            mainFrame.setVisible(true);
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void setBarrier() {
        for (int row = 0; row < screenRows; row++) {
            addWall(row, 0);
            addWall(row, screenColumns - 1);
        }
        for (int column = 1; column < screenColumns; column++) {
            addWall(0, column - 1);
            addWall(screenRows - 1, column - 1);
        }
        int startRow = screenRows / 2 - 1;
        int startCol = screenColumns / 2 - 1;

        for (int row = startRow; row < startRow + 2; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                addWall(row, col);
            }
        }

    }

    private void addWall(int row, int column) {
        Obstacle wall = new Wall(row, column);
        walls.add(wall);
        tableModel.setValueAt(Color.blue, row, column);
    }

    private void addCoin(int row, int column) {
        Coin coin = new Coin(coinIcon, row, column);
        coins.add(coin);
        tableModel.setValueAt(coin, row, column);
    }


    private void middleWallsCreation() {
        Random rand = new Random();

        int[][] grid = new int[screenRows][screenColumns];

        for (int y = 2; y < screenRows - 1; y++) {
            for (int x = 2; x < screenColumns - 1; x++) {
                if (x == screenColumns - 2) {
                    grid[y][x + 1] = 1; // down
                } else if (y == screenRows - 2) {
                    grid[y + 1][x] = 1; //  right
                } else {
                    int choice = rand.nextInt(2);
                    if (choice == 0) {
                        grid[y][x + 1] = 1; //  down
                    } else {
                        grid[y + 1][x] = 1; // right
                    }
                }
            }
        }

        for (int y = 1; y < screenRows - 1; y++) {
            for (int x = 1; x < screenColumns - 1; x++) {
                if (grid[y][x] == 0 && !isWallAdjacentToBorder(y, x) && !isWallAdjacentToMiddleBlock(y, x) && !isWallDiagonallyAdjacent(y, x)) {
                    addWall(y, x);
                }
            }
        }


    }

    private void coinCreation() {
        for (int y = 1; y < screenRows - 1; y++) {
            for (int x = 1; x < screenColumns - 1; x++) {
                if (!isWall(y, x)) {
                    addCoin(y, x);
                }
            }
        }

    }

    public boolean checkCollision(int playerRow, int playerColumn, int ghostRow, int ghostColumn) {
        return playerRow == ghostRow && playerColumn == ghostColumn;
    }


    private boolean isWallDiagonallyAdjacent(int row, int column) {
        return isWall(row - 1, column - 1) || isWall(row - 1, column + 1) ||
                isWall(row + 1, column - 1) || isWall(row + 1, column + 1);
    }

    private boolean isWallAdjacentToBorder(int row, int column) {
        return row == 1 || row == screenRows - 2 || column == 1 || column == screenColumns - 2;
    }

    private boolean isWallAdjacentToMiddleBlock(int row, int column) {
        int startRow = screenRows / 2 - 1;
        int startCol = screenColumns / 2 - 1;
        return row >= startRow && row <= startRow + 1 && column >= startCol && column <= startCol + 2;
    }

    private boolean isWall(int row, int column) {
        for (Obstacle wall : walls) {
            if (wall.getRow() == row && wall.getColumn() == column) {
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean isCoin(int row, int column) {
        for (Coin coin : coins) {
            if (coin.getRow() == row && coin.getColumn() == column) {
                return true;
            }
        }
        return false;
    }

    public boolean isCoinCollected(int row, int column) {
        for (Coin coin : coins) {
            if (coin.getRow() == row && coin.getColumn() == column) {
                return coin.isCollected();

            }
        }
        return false;

    }

    public void updateScoreLabel() {
        score.setText("Score: " + collectedCoins);
    }

    public void updateTimerLabel() {
        timer.setText("Time: " + (int) (System.currentTimeMillis() - startingTime) / 1000);
        tookTime = (int) (System.currentTimeMillis() - startingTime) / 1000;
    }

    private void updatePlayerIcon() {
        currentIconIndex = (currentIconIndex + 1) % playerIcons.size();
        currentPlayerIcon = playerIcons.get(currentIconIndex);
    }


}

