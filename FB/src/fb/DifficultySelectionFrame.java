package fb;
import javax.swing.*; // jframes jpanel, jlabels
import java.awt.*; // color layouts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class DifficultySelectionFrame extends JFrame {
    public DifficultySelectionFrame() {
        setTitle("Flappy Bird - Difficulty Selection");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 223, 186));

        // Choose Game Level label
        JLabel chooseLevelLabel = new JLabel("Choose Game Difficulty Level", SwingConstants.CENTER);
        chooseLevelLabel.setFont(new Font("Arial", Font.BOLD, 24));
        chooseLevelLabel.setForeground(Color.BLACK);
        add(chooseLevelLabel, BorderLayout.NORTH);

        // Difficulty buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        buttonPanel.setBackground(new Color(255, 223, 186));

        JButton easyButton = createDifficultyButton("Easy");
        JButton normalButton = createDifficultyButton("Normal");
        JButton hardButton = createDifficultyButton("Hard");

        buttonPanel.add(easyButton);
        buttonPanel.add(normalButton);
        buttonPanel.add(hardButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Created by label and credits
        JPanel creditsPanel = new JPanel(new BorderLayout());
        creditsPanel.setBackground(new Color(255, 223, 186));

        JLabel createdByLabel = new JLabel("Created by", SwingConstants.CENTER);
        createdByLabel.setFont(new Font("Arial", Font.BOLD, 16));
        createdByLabel.setForeground(Color.BLACK);
        creditsPanel.add(createdByLabel, BorderLayout.NORTH);

        JTextArea creditsTextArea = new JTextArea(
                "\tAisf Hussain \t\t\t2021F-BSE-233\n" +
                        "\tSyed Aashir Ali\t\t2021F-BSE-235\n" +
                        "\tMuhammad Qasim\t\t2021F-BSE-249\n" +
                        "\tMuhammad Daniyal\t\t2021F-BSE-262\n" +
                        "\tMuhammad Tahir\t\t2021F-BSE-268"
        );
        creditsTextArea.setEditable(false);
        creditsTextArea.setFont(new Font("Arial", Font.BOLD, 14));
        creditsTextArea.setBackground(new Color(255, 223, 186));
        creditsPanel.add(creditsTextArea, BorderLayout.CENTER);

        add(creditsPanel, BorderLayout.SOUTH);

        setVisible(true);
        this.setResizable(false);
    }

    private JButton createDifficultyButton(String difficulty) {
        JButton button = new JButton(difficulty);
        button.setPreferredSize(new Dimension(20, 20)); // Set button size to 20px by 20px
        button.setFont(new Font("Arial", Font.BOLD, 12)); // Increase font size
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(100, 181, 246)); // Custom button background color
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pass the selected difficulty to the FlappyBirdGame constructor
                 new FlappyBirdGame(difficulty);
                // Close the difficulty selection frame
                DifficultySelectionFrame.this.dispose();
            }
        });
        return button;
    }

    public static void main(String[] args) {
       new DifficultySelectionFrame();
    }
}

