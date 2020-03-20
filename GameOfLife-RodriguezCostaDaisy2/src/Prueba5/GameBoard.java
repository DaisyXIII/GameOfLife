package Prueba5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
	Dimension d_gameBoardSize = new Dimension();
	ArrayList<Point> point = new ArrayList<Point>(0);
	Color bgColor = Color.BLACK;
	Cells cell = new Cells();
	Color randomColor;

	public GameBoard() {
		// Tenemos que añadir todas las escuchas
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setBackground(bgColor);

	}

	// Modificamos el Array del tablero
	public void updateArraySize() {
		ArrayList<Point> removeList = new ArrayList<Point>(0);
		for (Point current : point) {
			if ((current.x > d_gameBoardSize.width - 1) || (current.y > d_gameBoardSize.height - 1)) {
				removeList.add(current);
			}
		}
		point.removeAll(removeList);
		repaint();
	}

	//Color de las celulas random
	public Color cambiarColor() {
		//hacemos que la cantidad de cada color vaya del 75 al 255
		int rojo = (int) Math.floor(Math.random()*(255-75+1)+75);
		int verde = (int) Math.floor(Math.random()*(255-75+1)+75);
		int azul = (int) Math.floor(Math.random()*(255-75+1)+75);
		randomColor = new Color(rojo,verde,azul);
		
		return randomColor;

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		try {
			randomColor = cambiarColor();
			cell.paintCell(g, point, randomColor);

		} catch (ConcurrentModificationException cme) {
		}

		// Pintamos el contenido del tablero
		g.setColor(Color.LIGHT_GRAY);

		for (int i = 0; i <= d_gameBoardSize.width; i++) {
			g.drawLine(((i * MainFrame.BLOCK_SIZE) + MainFrame.BLOCK_SIZE), MainFrame.BLOCK_SIZE,
					(i * MainFrame.BLOCK_SIZE) + MainFrame.BLOCK_SIZE,
					MainFrame.BLOCK_SIZE + (MainFrame.BLOCK_SIZE * d_gameBoardSize.height));
		}
		for (int i = 0; i <= d_gameBoardSize.height; i++) {
			g.drawLine(MainFrame.BLOCK_SIZE, ((i * MainFrame.BLOCK_SIZE) + MainFrame.BLOCK_SIZE),
					MainFrame.BLOCK_SIZE * (d_gameBoardSize.width + 1),
					((i * MainFrame.BLOCK_SIZE) + MainFrame.BLOCK_SIZE));
		}

	}

	// Resetear todo el array
	public void resetBoard() {
		point.clear();
	}

	// Llenamos de forma eleatoria el tablero dependiendo del porcentaje
	public void randomlyFillBoard(int percent) {
		for (int i = 0; i < d_gameBoardSize.width; i++) {
			for (int j = 0; j < d_gameBoardSize.height; j++) {
				if (Math.random() * 100 < percent) {
					cell.addPoint(i, j, point);
				}
			}
		}
		repaint();
	}

	// Redimensionar el tablero dependiendo del tamaño de la pantalla o de la
	// ventana
	@Override
	public void componentResized(ComponentEvent e) {
		d_gameBoardSize = new Dimension(getWidth() / MainFrame.BLOCK_SIZE - 2, getHeight() / MainFrame.BLOCK_SIZE - 2);
		updateArraySize();
		repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (SwingUtilities.isLeftMouseButton(e)) {
			cell.addPoint(e, d_gameBoardSize, point);

		}
		if (SwingUtilities.isRightMouseButton(e)) {
			cell.removePointRightClick(e, point);
		}
		repaint();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Cada vez que arrastramos
		if (SwingUtilities.isLeftMouseButton(e)) {
			cell.addPoint(e, d_gameBoardSize, point);

		}
		if (SwingUtilities.isRightMouseButton(e)) {
			cell.removePointRightClick(e, point);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void run() {
		boolean[][] gameBoard = new boolean[d_gameBoardSize.width + 2][d_gameBoardSize.height + 2];
		for (Point current : point) {
			gameBoard[current.x + 1][current.y + 1] = true;
		}
		ArrayList<Point> survivingCells = new ArrayList<Point>(0);
		for (int i = 1; i < gameBoard.length - 1; i++) {
			for (int j = 1; j < gameBoard[0].length - 1; j++) {
				int surrounding = 0;
				if (gameBoard[i - 1][j - 1]) {
					surrounding++;
				}
				if (gameBoard[i - 1][j]) {
					surrounding++;
				}
				if (gameBoard[i - 1][j + 1]) {
					surrounding++;
				}
				if (gameBoard[i][j - 1]) {
					surrounding++;
				}
				if (gameBoard[i][j + 1]) {
					surrounding++;
				}
				if (gameBoard[i + 1][j - 1]) {
					surrounding++;
				}
				if (gameBoard[i + 1][j]) {
					surrounding++;
				}
				if (gameBoard[i + 1][j + 1]) {
					surrounding++;
				}
				if (gameBoard[i][j]) {
					// La célula está viva. La celula puede sobrevivir?(2-3)
					if ((surrounding == 2) || (surrounding == 3)) {
						survivingCells.add(new Point(i - 1, j - 1));
					}
				} else {
					// La célula está muerta, puede volver a nacer? (3)
					if (surrounding == 3) {
						survivingCells.add(new Point(i - 1, j - 1));
					}
				}
			}
		}
		resetBoard();
		point.addAll(survivingCells);
		repaint();
		try {
			Thread.sleep(1000 / MainFrame.i_movesPerSecond);
			run();
		} catch (InterruptedException ex) {
		}
	}
}
