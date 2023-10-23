package project2;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI1024Panel extends JPanel {

	private int nRows, nCols;
	private JLabel[][] gameBoardUI = new JLabel[nRows][nCols];
	private NumberGameArrayList gameLogic = new NumberGameArrayList();
	private JPanel playPanel, gamePanel, statisticPanel;
	private int numGames = 0;
	private int numSlides = 0;
	private int highest = 0;

	private JMenuItem quitItem, resetItem, resizeItem, winningScore, resetStatistics;
	private JLabel numGamesWon, highScore, numGamesPlayed, currScore, currSlides, target;

	final Color[] colorTable = {
			new Color(0xECD008), new Color(0xCEA626), new Color(0xEC800E),
			new Color(0xD91D0F), new Color(0xDC2EE7), new Color(0x1ED748),
			new Color(0x7F37A2), new Color(0x3B1857), new Color(0x623B20),
			new Color(0x48EACC), new Color(0x11579F), new Color(0x122C72),
			new Color(0xC1C1C1), new Color(0x4B4B4B), new Color(0x000000)};

	private Color gridColor = new Color(0xAFA6A4);
	private Color emptyColor = new Color(0xFFFFFF);
	private Color startColor = new Color(0xFCE8C0);

	public GUI1024Panel(JMenuItem quitItem, JMenuItem resetItem,
						JMenuItem resizeItem, JMenuItem winningScore, JMenuItem resetStatistics) {

		this.quitItem = quitItem;
		this.resetItem = resetItem;
		this.resizeItem = resizeItem;
		this.winningScore = winningScore;
		this.resetStatistics = resetStatistics;

		quitItem.addActionListener(new Mylistener());
		resetItem.addActionListener(new Mylistener());
		resizeItem.addActionListener(new Mylistener());
		winningScore.addActionListener(new Mylistener());
		resetStatistics.addActionListener(new Mylistener());

		setFocusable(true);
		initializeGame();
	}

	private void initializeGame() {

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// Indicate which column
		c.gridx = 0;
		// Indicate which row
		c.gridy = 0;

		setBorder(BorderFactory.createLineBorder(Color.BLACK, 12));
		playPanel = new JPanel();
		add(playPanel, c);
		// Initialize the game panel
		gamePanel = new JPanel();
		gamePanel.setBackground(startColor);
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
		playPanel.add(gamePanel, c);
		//initialize stats panel
		statisticPanel = new JPanel();
		statisticPanel.setLayout(new GridLayout(6, 1));
		statisticPanel.setBackground(Color.LIGHT_GRAY);
		statisticPanel.setFont(new Font("SansSerif", Font.BOLD, 88));
		statisticPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		statisticPanel.setVisible(true);

		numGamesWon = new JLabel("Number of Games Won: " + numGames);
		highScore = new JLabel("High Score: " + highest);
		numGamesPlayed = new JLabel("Number of games played: " + numGamesPlayed);
		currScore = new JLabel("Score: " + currScore);
		currSlides = new JLabel("Number of slides: " + currSlides);
		target = new JLabel("Target to Win: " + gameLogic.getWinning_value());

		statisticPanel.add(target);
		statisticPanel.add(numGamesWon);
		statisticPanel.add(highScore);
		statisticPanel.add(numGamesPlayed);
		statisticPanel.add(currScore);
		statisticPanel.add(currSlides);
		playPanel.add(statisticPanel);
		// Allow keys to be pressed to control the game
		setFocusable(true);
		addKeyListener(new SlideListener());
		//Initialize the game GUI and logic
		setupBoard();
	}

	public void updateStatsPanel() {
		numGamesWon.setText("Number of Games Won: " + gameLogic.getNumGames());
		currScore.setText("Score: " + gameLogic.getCurrScore());
		highScore.setText("High Score: " + gameLogic.getHighest());
		numGamesPlayed.setText("Number of games played: " + gameLogic.getNumGamesPlayed());
		currSlides.setText("Number of slides: " + gameLogic.getCurrSlides());
		target.setText("Target to Win: " + gameLogic.getWinning_value());
	}

	private void updateBoard() {
		for (JLabel[] row : gameBoardUI)
			for (JLabel s : row) {
				s.setText("");
			}
		ArrayList<Cell> out = gameLogic.getNonEmptyTiles();
		if (out == null) {
			JOptionPane.showMessageDialog(null,
					"Incomplete implementation getNonEmptyTiles()");
			return;
		}
		for (Cell c : out) {
			JLabel z = gameBoardUI[c.getRow()][c.getColumn()];
			z.setText(String.valueOf(Math.abs(c.getValue())));
			//z.setHorizontalTextPosition(SwingConstants.CENTER);
			z.setHorizontalAlignment(SwingConstants.CENTER);
			int cVal = c.getValue();
			if (cVal == 2){
				z.setForeground(colorTable[14]);
			}
			if (cVal == 4){
				z.setForeground(colorTable[13]);
			}
			if (cVal == 8){
				z.setForeground(colorTable[11]);
			}
			if (cVal == 16){
				z.setForeground(colorTable[10]);
			}
			if (cVal == 32){
				z.setForeground(colorTable[9]);
			}
			if (cVal == 64){
				z.setForeground(colorTable[8]);
			}
			if (cVal == 128){
				z.setForeground(colorTable[3]);
			}
			if (cVal == 256){
				z.setForeground(colorTable[7]);
			}
			if (cVal == 512){
				z.setForeground(colorTable[5]);
			}
			if (cVal == 1024){
				z.setForeground(colorTable[4]);
			}
			if (cVal == 2048){
				z.setForeground(colorTable[0]);
			}
		}
	}

	public void setupBoard() {
		// Initialize the game logic
		try {
			String s1 = JOptionPane.showInputDialog("Enter width for game board: " +
					"								(between 3 and 15)");
			String s2 = JOptionPane.showInputDialog("Enter length for game board: " +
					"								(between 3 and 15)");
			int temp1 = Integer.parseInt(s1);
			int temp2 = Integer.parseInt(s2);
			if (temp1 < 4 || temp1 > 15 || temp2 < 4 || temp2 > 15) {
				throw new IllegalArgumentException();
			} else {
				nRows = temp1;
				nCols = temp2;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
							"Enter a valid width and height");
			nRows = 4;
			nCols = 4;
		}
		gameLogic = new NumberGameArrayList();
		gameLogic.resizeBoard(nRows, nCols, 1024);
		new BoxLayout(gamePanel, BoxLayout.Y_AXIS);
		setSize(new Dimension(300 * (nCols), 300 * (nRows)));
		gamePanel.setLayout(new GridLayout(nRows, nCols));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		gameBoardUI = new JLabel[nRows][nCols];

		for (int k = 0; k < nRows; k++) {
			for (int m = 0; m < nCols; m++) {
				gameBoardUI[k][m] = new JLabel();
				gameBoardUI[k][m].setFont(new Font("SansSerif", Font.BOLD, 48));
				//gameBoardUI[k][m].setVerticalTextPosition(SwingConstants.CENTER);
				//gameBoardUI[k][m].setHorizontalTextPosition(SwingConstants.CENTER);
				setBackground(gridColor);
				setFocusable(true);
				//gameBoardUI[k][m].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				gameBoardUI[k][m].setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
				gameBoardUI[k][m].setPreferredSize(new Dimension(150, 150));
				gameBoardUI[k][m].setMinimumSize(new Dimension(150, 150));
				c.gridx = m;
				c.gridy = k;
				c.gridwidth = 1;
				gamePanel.add(gameBoardUI[k][m]);
			}
		}
		gameLogic.reset();
		updateBoard();
	}

	public void resizeBoard(int height, int width) {
		//remove all elements of gamePanel to then recreate them
		gamePanel.removeAll();
		nRows = height;
		nCols = width;
		//set new size of board
		gameLogic.resizeBoard(nRows, nCols, gameLogic.getWinning_value());
		// Start with changing the panel size and creating a new gamePanel
		setSize(new Dimension(100 * (nCols), 100 * (nRows)));
		gamePanel.setLayout(new GridLayout(nRows, nCols));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		gameBoardUI = new JLabel[nRows][nCols];

		Font myTextFont = new Font(Font.SERIF, Font.BOLD, 40);
		for (int k = 0; k < nRows; k++) {
			for (int m = 0; m < nCols; m++) {
				gameBoardUI[k][m] = new JLabel();
				gameBoardUI[k][m].setFont(new Font("SansSerif", Font.BOLD, 48));
				setBackground(gridColor);
				setFocusable(true);
				gameBoardUI[k][m].setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
				gameBoardUI[k][m].setPreferredSize(new Dimension(150, 150));
				gameBoardUI[k][m].setMinimumSize(new Dimension(150, 150));
				c.gridx = m;
				c.gridy = k;
				c.gridwidth = 1;
				gamePanel.add(gameBoardUI[k][m]);
			}
		}
		//reset all values of game
		gameLogic.reset();
		//run the game like normal
		updateBoard();
	}

	private class SlideListener implements KeyListener, ActionListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {

			boolean moved = false;
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					moved = gameLogic.slide(SlideDirection.UP);
					break;
				case KeyEvent.VK_LEFT:
					moved = gameLogic.slide(SlideDirection.LEFT);
					break;
				case KeyEvent.VK_DOWN:
					moved = gameLogic.slide(SlideDirection.DOWN);
					break;
				case KeyEvent.VK_RIGHT:
					moved = gameLogic.slide(SlideDirection.RIGHT);
					break;
				case KeyEvent.VK_U:
					try {
						System.out.println("Attempt to undo");
						gameLogic.undo();
						moved = true;
					} catch (IllegalStateException exp) {
						JOptionPane.showMessageDialog(null,
								"Can't undo beyond the first move");
						moved = false;
					}
			}
			if (moved) {
				updateBoard();
				updateStatsPanel();
				if (gameLogic.getStatus().equals(GameStatus.USER_WON)) {
					JOptionPane.showMessageDialog(null, "You won!");
					gameLogic.incNumPlayed();
					gameLogic.incNumGames();
				} else if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
					gameLogic.incNumPlayed();
					int resp = JOptionPane.showConfirmDialog(null,
								"Do you want to play again?", "Game Over!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						updateBoard();
					} else {
						System.exit(0);
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}

	private class Mylistener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == quitItem)
				System.exit(1);
			if (event.getSource() == resetItem) {
				gameLogic.incNumPlayed();
				gameLogic.reset();
			}
			updateBoard();
			updateStatsPanel();
			if (event.getSource() == resizeItem)
				try {
					String s1 = JOptionPane.showInputDialog("Enter width for game board: (must be greater than 3)");
					String s2 = JOptionPane.showInputDialog("Enter length for game board: (must be greater than 3)");
					int temp1 = Integer.parseInt(s1);
					int temp2 = Integer.parseInt(s2);
					if (temp1 < 4 || temp1 > 15 || temp2 < 4 || temp2 > 15) {
						throw new IllegalArgumentException();
					}
					else {
						resizeBoard(temp2, temp1);
					}
				} catch (Exception e) {
					JOptionPane.showInternalMessageDialog(null,
								"Enter a valid width and height");
				}
			if (event.getSource() == winningScore) {
				String s = JOptionPane.showInputDialog("Enter new winning goal (must be divisible by 2)");
				int temp = Integer.parseInt(s);
				try {
					if (temp % 2 == 0) {
						gameLogic.setWinning_value(temp);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
								"Please enter a valid goal");
				}
			}
			if (event.getSource() == resetStatistics) {
				gameLogic.resetStatistics();
				updateStatsPanel();
			}
		}
	}
}