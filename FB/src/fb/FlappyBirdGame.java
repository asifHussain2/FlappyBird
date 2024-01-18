package fb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class FlappyBirdGame extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 800;  //immutable object
    private static final int HEIGHT = 600;
    private static final int GROUND_HEIGHT = 50;
    private static final int BIRD_SIZE = 30;
    private static final int BLOCK_WIDTH = 50;

    private static final int BLOCK_GAP = 150;
    
    private String difficulty;
    private int birdY;
    private int birdVelocity=0;
    private int score;
    private Timer timer;
    private boolean gameStarted;
    private List<Block> blocks;
    private JLabel scoreLabel;

    private GamePanel gamePanel;

    public FlappyBirdGame(String difficulty) {
    	this.difficulty=difficulty;
        setTitle("Flappy Bird");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        birdY = HEIGHT / 2;
        score = 0;
        gameStarted = false;
        blocks = new ArrayList<>();

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(scoreLabel, BorderLayout.NORTH);

        gamePanel = new GamePanel(this);
        add(gamePanel);

        addKeyListener(this);

        timer = new Timer(20, this);
        initializeBlocks();
        setVisible(true);
GameLoop gameLoop = new GameLoop(this);
        new Thread(gameLoop).start();
    }

    private static class GameLoop implements Runnable {
        private FlappyBirdGame game;

        public GameLoop(FlappyBirdGame game) {
            this.game = game;
        }

        @Override
        public void run() {
            while (true) {
                game.move();
                game.getGamePanel().repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initializeBlocks() {
        blocks.clear();
        Random random = new Random();
        int currentX = WIDTH + BLOCK_WIDTH;

        while (currentX < WIDTH * 2) {
            int gapStart = random.nextInt(HEIGHT - GROUND_HEIGHT - BLOCK_GAP);
            blocks.add(new Block(currentX, 0, BLOCK_WIDTH, gapStart));
            blocks.add(new Block(currentX, gapStart + BLOCK_GAP, BLOCK_WIDTH, HEIGHT - GROUND_HEIGHT - (gapStart + BLOCK_GAP)));
            currentX += BLOCK_WIDTH + 200; // Adjust the distance between blocks
        }
    }

    private synchronized void move() {
        if (gameStarted) {
            birdVelocity += 2;
            birdY += birdVelocity;

            boolean birdCollided = false;

            // Check if bird hits the ground or top
            if (birdY < 0 || birdY > HEIGHT - GROUND_HEIGHT - BIRD_SIZE) {
                gameOver();
            }

            // Check for collisions with blocks and increment score when passing a block
            boolean passedThroughGap = false;

            for (Block block : blocks) {
                if (birdY < block.getY() + block.getHeight() &&
                        birdY + BIRD_SIZE > block.getY() &&
                        50 + BIRD_SIZE > block.getX() &&
                        50 < block.getX() + block.getWidth()) {
                    // Collision with a block
                    birdCollided = true;
                    break;
                } else if (!block.isPassed() && block.getX() + block.getWidth() < 50) {
                    // Bird passed the block, increment the score only once
                    block.setPassed(true);
                    passedThroughGap = true;
                }
            }

            // Increment the score outside the loop to avoid multiple increments
            if (passedThroughGap) {
                score++;
                updateScoreLabel();
            }

            // Check if bird has collided with any block in this frame
            if (birdCollided) {
                gameOver();
            }

            // Move blocks to the left
            for (Block block : blocks) {
                block.setX(block.getX() - 2);
            }

            // Remove off-screen blocks and add new ones
            blocks.removeIf(block -> block.getX() + block.getWidth() < 0);
            if (blocks.isEmpty()) {
                initializeBlocks();
            }
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    private void gameOver() {
        if (gameStarted) {
            timer.stop();

            int choice = JOptionPane.showOptionDialog(this,
                    "Game Over. Your Score: " + score,
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Quit", "Back to Difficulty"},
                    "Restart");

            switch (choice) {
                case 0:
                    restartGame();
                    break;
                case 1:
                    System.exit(0);
                    break;
                case 2:
                    dispose();  
                    new DifficultySelectionFrame();
                    break;
            }
        } else {
            startGame();
        }
    }

    private void restartGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        updateScoreLabel();
        initializeBlocks();
        gameStarted = true;
        timer.restart();
    }

    private void startGame() {
        gameStarted = true;
        timer.start();
    }

    private void jump() {
        switch (difficulty) {
            case "Easy":
                birdVelocity = -15;
                break;
            case "Normal":
                birdVelocity = -10;
                break;
            case "Hard":
                birdVelocity = -8;
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       new Thread(()-> move()).start();
       new Thread(()-> gamePanel.repaint()).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                startGame();
            } else {
                jump();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

   
    public static void main(String[] args) {
//        new FlappyBirdGame("Normal"));
    }

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public int getBirdY() {
		return birdY;
	}

	public void setBirdY(int birdY) {
		this.birdY = birdY;
	}

	public int getBirdVelocity() {
		return birdVelocity;
	}

	public void setBirdVelocity(int birdVelocity) {
		this.birdVelocity = birdVelocity;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public JLabel getScoreLabel() {
		return scoreLabel;
	}

	public void setScoreLabel(JLabel scoreLabel) {
		this.scoreLabel = scoreLabel;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getGroundHeight() {
		return GROUND_HEIGHT;
	}

	public int getBirdSize() {
		return BIRD_SIZE;
	}

	public int getBlockWidth() {
		return BLOCK_WIDTH;
	}


	public int getBlockGap() {
		return BLOCK_GAP;
	}
 
    
}

