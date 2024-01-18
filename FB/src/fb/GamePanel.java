package fb;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
    private FlappyBirdGame game;
    private Image birdImage;
    private Image backgroundImage;
    private Image blockImage;
    private Image groundImage;

    public GamePanel(FlappyBirdGame game) {
        this.game = game;
        loadImages();
    }

    private void loadImages() {
    try {
        birdImage = loadImage("/flappybird.png");
        backgroundImage = loadImage("/background.png");
        blockImage = loadImage("/block.png");
        groundImage = loadImage("/ground.png");
    } catch (Exception e) {
        
        e.printStackTrace(); 
    }
}

    private Image loadImage(String path) throws NullPointerException{
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
        if (imageIcon.getImage() == null) {
            throw new NullPointerException("Null Image");
        }
        else{
        return imageIcon.getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw ground
        g.drawImage(groundImage, 0, getHeight() - game.getGroundHeight(), getWidth(), game.getGroundHeight(), this);

        // Draw bird
        g.drawImage(birdImage, 50, game.getBirdY(), game.getBirdSize(), game.getBirdSize(), this);

        // Draw blocks
        for (Block block : game.getBlocks()) {
            g.drawImage(blockImage, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}


