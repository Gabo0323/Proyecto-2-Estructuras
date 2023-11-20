package com.mycompany.mavenproject12;

import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Stack;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Font;

/*
 * Clase MazePanel que dibuja la interfaz grafica de los laverintos
 */
public class MazePanel extends JPanel {

    private Maze maze;
    private boolean showSolution;
    private Cell currentCell;
    private boolean navigable = false;
    private boolean usePaintComponent2 = false;
    private boolean startSelectionMode = false;

    private double scale = 1.0;

    // Constructor de la clase MazePanel
    public MazePanel(Maze maze) {
        this.maze = maze;

        this.currentCell = maze.getCells()[0][0]; // celda de inicio

    }

    // Selecciona cuando mostrar la solucion
    public void setShowSolution(boolean show) {
        showSolution = show;
        repaint(); // Redibuja el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (usePaintComponent2) {
            paintComponent2(g);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            g2.translate(w / 2, h / 2);
            g2.scale(scale, scale);
            g2.translate(-w / 2, -h / 2);

            super.paintComponent(g);
            System.out.println("prueba del repaint");
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int cellWidth = panelWidth / maze.getCols();
            int cellHeight = panelHeight / maze.getRows();
            int cellSize = Math.min(cellWidth, cellHeight);
            // Dibuja las paredes del laberinto
            Cell[][] cells = maze.getCells();

            for (int r = 0; r < cells.length; r++) {
                for (int c = 0; c < cells[r].length; c++) {
                    Cell cell = cells[r][c];
                    int x = c * cellSize;
                    int y = r * cellSize;
                    if (cell.hasTopWall()) {
                        g.drawLine(x, y, x + cellSize, y);
                    }
                    if (cell.hasLeftWall()) {
                        g.drawLine(x, y, x, y + cellSize);
                    }
                    if (cell.hasBottomWall()) {
                        g.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                    }
                    if (cell.hasRightWall()) {
                        g.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                    }
                }
            }

            if (showSolution) {
                System.out.println("Estamos en el SHOW SOLUTION");
                // Si la variable showSolution es verdadera, dibuja la solución

                maze.resetVisited();

                Stack<Cell> solution = maze.solveMaze();

                Cell firstCell = solution.peek(); // La primera celda
                Cell lastCell = solution.lastElement(); // La última celda
                g.setColor(Color.BLACK); // Elige el color para la solución

                for (Cell cell : solution) {
                    System.out.println("Dibujando EN EL FOR  DE la solución");
                    int x = cell.getCol() * cellSize + cellSize / 2;
                    int y = cell.getRow() * cellSize + cellSize / 2;
                    int ovalSize = Math.max(1, cellSize / 4); // Ajusta el tamaño del círculo
                    g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);

                    if (cell.equals(firstCell)) {
                        g.setColor(Color.BLUE); // Color para la primera celda
                    } else if (cell.equals(lastCell)) {
                        g.setColor(Color.GREEN); // Color para la última celda
                    } else {
                        g.setColor(Color.BLACK); // Color para las demás celdas
                    }

                    g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);

                }

            } else {
                System.out.println("NOOO Dibujando la solución");
            }

            if (navigable && currentCell != null) {

                // Calcula las coordenadas del centro de la celda actual
                int x = currentCell.getCol() * cellSize + cellSize / 2;
                int y = currentCell.getRow() * cellSize + cellSize / 2;
                int ovalSize = Math.max(1, cellSize / 4); // Ajusta el tamaño del círculo si es necesario

                g.setColor(Color.RED);
                g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);
            }

            if (currentCell.equals(maze.getCells()[maze.getRows() - 1][maze.getCols() - 1])) {
                String message = "¡Felicidades, has completado el laberinto!";
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 20));

                FontMetrics metrics = g.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(message)) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

                g.drawString(message, x, y);
            }
        }
    }

    protected void paintComponent2(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        g2.translate(w / 2, h / 2);
        g2.scale(scale, scale);
        g2.translate(-w / 2, -h / 2);

        // Configuración del color y grosor de las paredes del laberinto
        g2.setColor(Color.ORANGE); // Color naranja para las paredes
        float thickness = 3.0f; // Grosor de las paredes
        g2.setStroke(new BasicStroke(thickness));

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / maze.getCols();
        int cellHeight = panelHeight / maze.getRows();
        int cellSize = Math.min(cellWidth, cellHeight);

        // Dibuja el laberinto
        Cell[][] cells = maze.getCells();
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                Cell cell = cells[r][c];
                int x = c * cellSize;
                int y = r * cellSize;
                if (cell.hasTopWall()) {
                    g2.drawLine(x, y, x + cellSize, y);
                }
                if (cell.hasLeftWall()) {
                    g2.drawLine(x, y, x, y + cellSize);
                }
                if (cell.hasBottomWall()) {
                    g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                }
                if (cell.hasRightWall()) {
                    g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                }
            }
        }
        if (showSolution) {
            System.out.println("Estamos en el SHOW SOLUTION");
            // Si la variable showSolution es verdadera, dibuja la solución

            maze.resetVisited();
            Stack<Cell> solution = maze.solveMaze();
            g.setColor(Color.BLACK); // Elige el color para la solución

            for (Cell cell : solution) {
                System.out.println("Dibujando EN EL FOR  DE la solución");
                int x = cell.getCol() * cellSize + cellSize / 2;
                int y = cell.getRow() * cellSize + cellSize / 2;
                int ovalSize = Math.max(1, cellSize / 4); // Ajusta el tamaño del círculo
                g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);

                System.out.println("x:" + x);
                System.out.println("y:" + y);

            }

        } else {
            System.out.println("NOOO Dibujando la solución");
        }

        if (navigable && currentCell != null) {

            // Calcula las coordenadas del centro de la celda actual
            int x = currentCell.getCol() * cellSize + cellSize / 2;
            int y = currentCell.getRow() * cellSize + cellSize / 2;
            int ovalSize = Math.max(1, cellSize / 4); // Ajusta el tamaño del círculo si es necesario

            g.setColor(Color.RED);
            g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);
        }

        if (currentCell.equals(maze.getCells()[maze.getRows() - 1][maze.getCols() - 1])) {
            String message = "¡Felicidades, has completado el laberinto!";
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 20));

            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(message)) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

            g.drawString(message, x, y);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        int w = maze.getCols() * 10;
        int h = maze.getRows() * 10;
        return new Dimension((int) (w * scale), (int) (h * scale));
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        revalidate();
        repaint();
    }

    public void setUsePaintComponent2(boolean use) {
        usePaintComponent2 = use;
        repaint();
    }

    public boolean isUsingPaintComponent2() {
        return usePaintComponent2;
    }

    public void setNavigable(boolean navigable) {
        this.navigable = navigable;
        setFocusable(navigable);
        if (navigable) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (!navigable) {
                        return; // Si no es navegable, no haga nada.
                    }
                    int newRow = currentCell.getRow();
                    int newCol = currentCell.getCol();

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            newRow--;
                            break;
                        case KeyEvent.VK_DOWN:
                            newRow++;
                            break;
                        case KeyEvent.VK_LEFT:
                            newCol--;
                            break;
                        case KeyEvent.VK_RIGHT:
                            newCol++;
                            break;

                    }

                    if (canMoveToCell(newRow, newCol)) {
                        currentCell = maze.getCells()[newRow][newCol];
                        repaint(); // Dibuja el laberinto con la nueva posición del punto rojo
                    }
                }
            });
            requestFocusInWindow(); // Solicita el foco para capturar los eventos de teclado
        }
        repaint();
    }

    private void moveCurrentCell(int rowDelta, int colDelta) {
        int newRow = currentCell.getRow() + rowDelta;
        int newCol = currentCell.getCol() + colDelta;

        // Obtiene la nueva celda potencial del laberinto
        Cell newCell = maze.getCells()[newRow][newCol];

        // Comprueba si se puede mover a la nueva celda (no hay paredes entre ellas)
        if (!currentCell.hasWallBetween(newCell)) {
            currentCell = newCell; // Actualiza la celda actual
            repaint(); // Repintar el laberinto con la nueva posición
        }
    }

    private boolean canMoveToCell(int newRow, int newCol) {
        // Comprueba si las nuevas coordenadas están dentro de los límites del laberinto
        if (newRow >= 0 && newRow < maze.getRows() && newCol >= 0 && newCol < maze.getCols()) {
            // Obtén la nueva celda basándote en las coordenadas proporcionadas
            Cell newCell = maze.getCells()[newRow][newCol];

            // Utiliza el método hasWallBetween() para verificar si hay paredes entre las celdas
            return !currentCell.hasWallBetween(newCell);
        }
        return false; // Las coordenadas están fuera de los límites del laberinto
    }
    private boolean targetSelectionMode = false;

    public void enableTargetSelection(boolean enable) {
        this.targetSelectionMode = enable;
        if (enable) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (targetSelectionMode) {
                        selectTargetCell(e.getX(), e.getY());
                        targetSelectionMode = false; // Deshabilitar el modo después de la selección
                    }
                }
            });
        }
    }

    public void enableStartSelection(boolean enable) {
        this.startSelectionMode = enable;
        if (enable) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (startSelectionMode) {
                        selectStartCell(e.getX(), e.getY());
                        startSelectionMode = false; // Deshabilitar el modo después de la selección
                    }
                }
            });
        }
    }

    private void selectTargetCell(int x, int y) {
        // Ajustar las coordenadas del ratón para tener en cuenta la transformación y escala
        int w = getWidth();
        int h = getHeight();
        int adjustedX = (int) ((x - w / 2) / scale + w / 2);
        int adjustedY = (int) ((y - h / 2) / scale + h / 2);

        // Calcular el tamaño de las celdas basado en el tamaño actual del panel
        int cellSize = Math.min(getWidth() / maze.getCols(), getHeight() / maze.getRows());

        // Convertir coordenadas de pantalla a coordenadas de celda
        int col = adjustedX / cellSize;
        int row = adjustedY / cellSize;

        // Comprobar si las coordenadas están dentro de los límites del laberinto
        if (row >= 0 && row < maze.getRows() && col >= 0 && col < maze.getCols()) {
            // Establecer la celda seleccionada como objetivo en Maze
            maze.setSolutionTarget(row, col);
            repaint(); // Repintar para mostrar la solución desde la nueva celda objetivo
        }
    }

    private void selectStartCell(int x, int y) {
        // Ajustar las coordenadas del ratón para tener en cuenta la transformación y escala
        int w = getWidth();
        int h = getHeight();
        int adjustedX = (int) ((x - w / 2) / scale + w / 2);
        int adjustedY = (int) ((y - h / 2) / scale + h / 2);

        // Calcular el tamaño de las celdas basado en el tamaño actual del panel
        int cellSize = Math.min(getWidth() / maze.getCols(), getHeight() / maze.getRows());

        // Convertir coordenadas de pantalla a coordenadas de celda
        int col = adjustedX / cellSize;
        int row = adjustedY / cellSize;

        // Comprobar si las coordenadas están dentro de los límites del laberinto
        if (row >= 0 && row < maze.getRows() && col >= 0 && col < maze.getCols()) {
            // Establecer la celda seleccionada como inicio en Maze
            maze.setStartCell(row, col);
            repaint(); // Repintar para mostrar cualquier cambio en la pantalla
        }
    }

}
