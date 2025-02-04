import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

class TicTacToeGUI extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private String player1, player2;
    private boolean playerTurn = true; // true for Player 1 (X), false for Player 2 (O)
    private int scorePlayer1 = 0, scorePlayer2 = 0;
    private JLabel scoreLabel;
    private Clip winClip, drawClip, placeClip;

    public TicTacToeGUI(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;

        setTitle("Tic-Tac-Toe Game");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize game board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusable(false);
                buttons[i][j].setBackground(Color.CYAN);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                buttons[i][j].addActionListener(this);
                boardPanel.add(buttons[i][j]);
            }
        }

        // Score panel
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        scoreLabel = new JLabel("Player 1: " + scorePlayer1 + " | Player 2: " + scorePlayer2);
        scorePanel.add(scoreLabel);

        // Restart button
        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
                scorePlayer1 = 0;
                scorePlayer2 = 0;
                updateScore();
            }
        });
        scorePanel.add(restartButton);

        // Add panels to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.SOUTH);

        // Load sound effects
        loadSounds();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        // If button already has a symbol, ignore
        if (!clickedButton.getText().equals("")) {
            return;
        }

        // Set the symbol for the current player
        String symbol = playerTurn ? "X" : "O";
        clickedButton.setText(symbol);

        // Play a sound when placing a symbol
        playPlaceSound();

        // Simple animation (color change when clicked)
        clickedButton.setBackground(playerTurn ? Color.RED : Color.GREEN);

        // Check if the current player has won
        if (checkWinner(symbol)) {
            playWinSound();
            JOptionPane.showMessageDialog(this, (playerTurn ? player1 : player2) + " wins!");
            if (playerTurn) {
                scorePlayer1++;
            } else {
                scorePlayer2++;
            }
            updateScore();
            resetBoard();
            return;
        }

        // Check if the game is a draw
        if (isBoardFull()) {
            playDrawSound();
            JOptionPane.showMessageDialog(this, "It's a draw!");
            resetBoard();
            return;
        }

        // Switch turn
        playerTurn = !playerTurn;
    }

    private boolean checkWinner(String symbol) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) && buttons[i][1].getText().equals(symbol)
                    && buttons[i][2].getText().equals(symbol))
                return true;
            if (buttons[0][i].getText().equals(symbol) && buttons[1][i].getText().equals(symbol)
                    && buttons[2][i].getText().equals(symbol))
                return true;
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(symbol) && buttons[1][1].getText().equals(symbol)
                && buttons[2][2].getText().equals(symbol))
            return true;
        if (buttons[0][2].getText().equals(symbol) && buttons[1][1].getText().equals(symbol)
                && buttons[2][0].getText().equals(symbol))
            return true;

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.CYAN); // Reset color
            }
        }
        playerTurn = true;
    }

    private void updateScore() {
        scoreLabel.setText("Player 1: " + scorePlayer1 + " | Player 2: " + scorePlayer2);
    }

    // Sound methods
    private void loadSounds() {
        try {
            // Load sound clips (make sure these files are in the correct path)
            File winSound = new File("win.wav");
            winClip = AudioSystem.getClip();
            winClip.open(AudioSystem.getAudioInputStream(winSound));

            File drawSound = new File("draw.wav");
            drawClip = AudioSystem.getClip();
            drawClip.open(AudioSystem.getAudioInputStream(drawSound));

            File placeSound = new File("place.wav");
            placeClip = AudioSystem.getClip();
            placeClip.open(AudioSystem.getAudioInputStream(placeSound));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playWinSound() {
        if (winClip != null) {
            winClip.setFramePosition(0);
            winClip.start();
        }
    }

    private void playDrawSound() {
        if (drawClip != null) {
            drawClip.setFramePosition(0);
            drawClip.start();
        }
    }

    private void playPlaceSound() {
        if (placeClip != null) {
            placeClip.setFramePosition(0);
            placeClip.start();
        }
    }

    public static void main(String[] args) {
        String p1 = JOptionPane.showInputDialog("Enter Player 1's name:");
        String p2 = JOptionPane.showInputDialog("Enter Player 2's name:");

        if (p1 == null || p2 == null || p1.trim().isEmpty() || p2.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid input! Exiting game.");
            System.exit(0);
        }

        new TicTacToeGUI(p1, p2);
    }
}