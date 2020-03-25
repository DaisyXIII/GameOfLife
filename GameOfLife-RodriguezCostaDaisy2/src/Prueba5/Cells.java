package Prueba5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

public class Cells {

	// Añadimos las posiciones de la celula en el array
	public void addPoint(int x, int y, ArrayList<Point> point) {
		if (!point.contains(new Point(x, y))) {
			point.add(new Point(x, y));
		}
	}

	// Añadimos celulas en el array donde el ratón pulse
	public void addPoint(MouseEvent me, Dimension d_gameBoardSize, ArrayList<Point> point) {
		int x = me.getPoint().x / MainFrame.BLOCK_SIZE - 1;
		int y = me.getPoint().y / MainFrame.BLOCK_SIZE - 1;
		if ((x >= 0) && (x < d_gameBoardSize.width) && (y >= 0) && (y < d_gameBoardSize.height)) {
			
			addPoint(x, y, point);
		}
	}

	// Borrar posición de la celula en el array
	public void removePoint(int x, int y, ArrayList<Point> point) {
		point.remove(new Point(x, y));
	}

	// Borrar posición de la celula donde haga click derecho
	public void removePointRightClick(MouseEvent e, ArrayList<Point> point) {
		if (SwingUtilities.isRightMouseButton(e)) {
			int x = e.getPoint().x / MainFrame.BLOCK_SIZE - 1;
			int y = e.getPoint().y / MainFrame.BLOCK_SIZE - 1;
			removePoint(x, y,point);
		}
		
	}

	public void paintCell(Graphics g, ArrayList<Point> point, Color color) {
		for (Point newPoint : point) {
			// Pintar nueva celula
			g.setColor(color);
			g.fillRect(MainFrame.BLOCK_SIZE + (MainFrame.BLOCK_SIZE * newPoint.x),
					MainFrame.BLOCK_SIZE + (MainFrame.BLOCK_SIZE * newPoint.y), MainFrame.BLOCK_SIZE,
					MainFrame.BLOCK_SIZE);

		}

	}

}