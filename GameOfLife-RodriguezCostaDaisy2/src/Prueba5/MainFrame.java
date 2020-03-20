package Prueba5;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class MainFrame extends JFrame implements ActionListener {
	private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
	private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);
	static int BLOCK_SIZE = 50;
	private JMenuBar MenuBar;
	private JMenu MenuFile, MenuGame, MenuAbout;
	private JMenuItem MenuItemFileSpeed, MenuItemBlockSize, MenuItemFileExit;
	private JMenuItem MenuItemGameAutofill, MenuItemGamePlay, MenuItemGameStop, MenuItemGameReset;
	private JMenuItem MenuItemAboutAbout, MenuItemAboutControls;
	static int i_movesPerSecond = 3;
	private GameBoard gb_gameBoard;
	private Thread game;

	public MainFrame() {

		// Configuramos la ventana

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Game of Life");
		ImageIcon ImageIcon = new ImageIcon("img/icon.png");
		Image image = ImageIcon.getImage();
		setIconImage(image);
		setSize(DEFAULT_WINDOW_SIZE);
		setMinimumSize(MINIMUM_WINDOW_SIZE);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
		setVisible(true);

		// Montamos el menu
		MenuBar = new JMenuBar();
		setJMenuBar(MenuBar);

		MenuFile = new JMenu("File");
		MenuBar.add(MenuFile);

		MenuGame = new JMenu("Game");
		MenuBar.add(MenuGame);

		MenuAbout = new JMenu("About");
		MenuBar.add(MenuAbout);

		// Submenus File
		MenuItemFileSpeed = new JMenuItem("Speed");
		MenuItemFileSpeed.addActionListener(this);

		MenuItemBlockSize = new JMenuItem("Block Size");
		MenuItemBlockSize.addActionListener(this);

		MenuItemFileExit = new JMenuItem("Exit");
		MenuItemFileExit.addActionListener(this);

		// Ordenamos los submenus de file
		MenuFile.add(MenuItemFileSpeed);
		MenuFile.add(new JSeparator());
		MenuFile.add(MenuItemBlockSize);
		MenuFile.add(new JSeparator());
		MenuFile.add(MenuItemFileExit);

		// Submenus Game
		MenuItemGameAutofill = new JMenuItem("Autofill");
		MenuItemGameAutofill.addActionListener(this);

		MenuItemGamePlay = new JMenuItem("Play");
		MenuItemGamePlay.addActionListener(this);

		MenuItemGameStop = new JMenuItem("Stop");// Al principio si el juego no está activo este botón no se podrá
													// seleccionar
		MenuItemGameStop.setEnabled(false);
		MenuItemGameStop.addActionListener(this);

		MenuItemGameReset = new JMenuItem("Reset");
		MenuItemGameReset.addActionListener(this);

		// Ordenamos los submenus de Game
		MenuGame.add(MenuItemGameAutofill);
		MenuGame.add(new JSeparator());
		MenuGame.add(MenuItemGamePlay);
		MenuGame.add(new JSeparator());
		MenuGame.add(MenuItemGameStop);
		MenuGame.add(new JSeparator());
		MenuGame.add(MenuItemGameReset);

		// Submenus About

		MenuItemAboutAbout = new JMenuItem("About");
		MenuItemAboutAbout.addActionListener(this);

		MenuItemAboutControls = new JMenuItem("Controls");
		MenuItemAboutControls.addActionListener(this);

		// Ordenamos los submenus de About
		MenuAbout.add(MenuItemAboutAbout);
		MenuAbout.add(new JSeparator());
		MenuAbout.add(MenuItemAboutControls);

		// Llamamos el tablero
		gb_gameBoard = new GameBoard();
		add(gb_gameBoard);

	}

	// Controlar juego
	public void setGameBeingPlayed(boolean isBeingPlayed) {
		if (isBeingPlayed) {
			MenuItemGamePlay.setEnabled(false);
			MenuItemGameStop.setEnabled(true);
			game = new Thread(gb_gameBoard);
			game.start();
		} else {
			MenuItemGamePlay.setEnabled(true);
			MenuItemGameStop.setEnabled(false);
			game.interrupt();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(MenuItemFileExit)) {// Salir del juego
			System.exit(0);
		} else if (ae.getSource().equals(MenuItemFileSpeed)) {// Si abrimos la velocidad

			// En la opción options vamos a sacar una ventana emergente
			final JFrame f_speeds = new JFrame();
			f_speeds.setTitle("Speed");
			f_speeds.setSize(300, 60);
			f_speeds.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_speeds.getWidth()) / 2,
					(Toolkit.getDefaultToolkit().getScreenSize().height - f_speeds.getHeight()) / 2);
			f_speeds.setResizable(false);
			JPanel p_speeds = new JPanel();
			p_speeds.setOpaque(false);
			f_speeds.add(p_speeds);

			// Vamos a crear un menú deplegable en el cual podremos cambiar el número de
			// movimientos por segundos
			p_speeds.add(new JLabel("Number of moves per second:"));
			Integer[] secondSpeeds = { 1, 2, 3, 4, 5, 10, 15, 20 };
			final JComboBox cb_seconds = new JComboBox(secondSpeeds);
			p_speeds.add(cb_seconds);
			cb_seconds.setSelectedItem(i_movesPerSecond);
			cb_seconds.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					i_movesPerSecond = (Integer) cb_seconds.getSelectedItem();
					f_speeds.dispose();
				}
			});
			f_speeds.setVisible(true);

		} else if (ae.getSource().equals(MenuItemBlockSize)) {// Si abrimos el tamaño

			// Vamos a sacar una ventana emergente
			final JFrame f_blockSize = new JFrame();
			f_blockSize.setTitle("Block Size");
			f_blockSize.setSize(500, 60);
			f_blockSize.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_blockSize.getWidth()) / 2,
					(Toolkit.getDefaultToolkit().getScreenSize().height - f_blockSize.getHeight()) / 2);
			f_blockSize.setResizable(false);
			JPanel p_blockSize = new JPanel();
			p_blockSize.setOpaque(false);
			f_blockSize.add(p_blockSize);

			// Vamos a crear un menú deplegable en el cual podremos cambiar el tamaño de los
			// bloques
			p_blockSize.add(new JLabel("What size do you want for your board?:"));
			Integer[] secondblockSize = { 50, 30, 20, 10 };
			final JComboBox cb_seconds = new JComboBox(secondblockSize);
			p_blockSize.add(cb_seconds);
			cb_seconds.setSelectedItem(BLOCK_SIZE);
			cb_seconds.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					BLOCK_SIZE = (Integer) cb_seconds.getSelectedItem();
					f_blockSize.dispose();
					if (BLOCK_SIZE == 10) {
						gb_gameBoard.d_gameBoardSize = new Dimension(getWidth() / MainFrame.BLOCK_SIZE - 5,getHeight() / MainFrame.BLOCK_SIZE - 10);
					} else if (BLOCK_SIZE == 20) {
						gb_gameBoard.d_gameBoardSize = new Dimension(getWidth() / MainFrame.BLOCK_SIZE - 3,getHeight() / MainFrame.BLOCK_SIZE - 5);
					} else {
						gb_gameBoard.d_gameBoardSize = new Dimension(getWidth() / MainFrame.BLOCK_SIZE - 2,getHeight() / MainFrame.BLOCK_SIZE - 4);
					}
					gb_gameBoard.updateArraySize();
					gb_gameBoard.repaint();
				}
			});
			f_blockSize.setVisible(true);

		} else if (ae.getSource().equals(MenuItemGameAutofill)) {// Autorellenar
			// Al pulsar vamos a abrir otra ventana
			final JFrame f_autoFill = new JFrame();
			f_autoFill.setTitle("Autofill");
			f_autoFill.setSize(360, 60);
			f_autoFill.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_autoFill.getWidth()) / 2,
					(Toolkit.getDefaultToolkit().getScreenSize().height - f_autoFill.getHeight()) / 2);
			f_autoFill.setResizable(false);
			JPanel p_autoFill = new JPanel();
			p_autoFill.setOpaque(false);
			f_autoFill.add(p_autoFill);

			// Le pediremos al usuario que nos diga el porcentaje que quiere cubrir del
			// tablero
			p_autoFill.add(new JLabel("What percentage should be filled? "));
			Object[] percentageOptions = { "Select", 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 95 };
			final JComboBox cb_percent = new JComboBox(percentageOptions);
			p_autoFill.add(cb_percent);
			cb_percent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (cb_percent.getSelectedIndex() > 0) {
						gb_gameBoard.resetBoard();
						gb_gameBoard.randomlyFillBoard((Integer) cb_percent.getSelectedItem());
						f_autoFill.dispose();
					}
				}
			});
			f_autoFill.setVisible(true);
		} else if (ae.getSource().equals(MenuItemGameReset)) {// resetear el tablero
			gb_gameBoard.resetBoard();
			gb_gameBoard.repaint();
		} else if (ae.getSource().equals(MenuItemGamePlay)) {// empezar partida
			setGameBeingPlayed(true);
		} else if (ae.getSource().equals(MenuItemGameStop)) {// parar partida
			setGameBeingPlayed(false);
		} else if (ae.getSource().equals(MenuItemAboutAbout)) {// Informacion del juego
			JOptionPane.showMessageDialog(null,
					"Se trata de un juego de cero jugadores, lo que quiere decir que su evolución está determinada por el estado inicial "
							+ "\n y no necesita ninguna entrada de datos posterior. El tablero de juego es una malla plana formada por cuadrados (las células) que se extiende"
							+ "\n por el infinito en todas las direcciones. Por tanto, cada célula tiene 8 células vecinas, que son las que están próximas a ella, incluidas "
							+ "\n las diagonales. Las células tienen dos estados: están vivas o muertas (o encendidas y apagadas). El estado de las células evoluciona a lo "
							+ "\n largo de unidades de tiempo discretas (se podría decir que por turnos). El estado de todas las células se tiene en cuenta para calcular"
							+ "\n el estado de las mismas al turno siguiente. Todas las células se actualizan simultáneamente en cada turno, siguiendo estas reglas:"
							+ "\n \n -Una célula muerta con exactamente 3 células vecinas vivas \"nace\" (es decir, al turno siguiente estará viva)."
							+ "\n \n -Una célula viva con 2 o 3 células vecinas vivas sigue viva, en otro caso muere por soledad o superpoblación.");

		} else if (ae.getSource().equals(MenuItemAboutControls)) {// Información de las funcionalidades

			JOptionPane.showMessageDialog(null, " FUNCIONALIDAD DEL JUEGO "
					+ "\n \n En la pestaña File podemos acceder a la velocidad del avance de las células y del tamaño de los bloques del tablero."
					+ "\n \n En la pestaña Game tendremos varias opciones: "
					+ "\n -Autofill: nos dará a elegir ciertos porcentajes para que se rellene solo el tablero con células aleatorias. "
					+ "\n -Play:empezar el juego."
					+ "\n -Stop: parar, solo está disponible cuando esté en marcha el juego."
					+ "\n -Reset: borramos completamente el tablero."
					+ "\n \n La pestaña About tiene información sobre el juego y esta explicación. "
					+ "\n \n Como se juega:"
					+ "\n -Con click izquierdo podremos colocar las células, igualmente si arrastramos mientras hacemos click también colocaremos."
					+ "\n -Con click derecho podremos borrar una célula, solo nos tenemos que poner encima de la elegida.");
		}
	}

}
