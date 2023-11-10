/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject12;

/**
 *
 * @author esteb
 */
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Stack;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazePanel extends JPanel {
    private Maze maze;
  // private boolean showSolution = true;
   private boolean showSolution;
   private Cell currentCell;
   private boolean navigable = false;
   
   
   
    public MazePanel(Maze maze) {
        this.maze = maze;
        
        this.currentCell = maze.getCells()[0][0]; // o cualquier celda de inicio que desees

    }


    
    public void setShowSolution(boolean show) {
        showSolution = show;
        repaint(); // Esto asegura que el panel se redibuje con la solución visible o no
    }

    @Override
    protected void paintComponent(Graphics g) {
       
        
        super.paintComponent(g);
        System.out.println("prueba del repaint");
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / maze.getCols();
        int cellHeight = panelHeight / maze.getRows();
        int cellSize = Math.min(cellWidth, cellHeight);
        // Draw the maze
        Cell[][] cells = maze.getCells();
        // cellSize = 20; // Adjust as needed
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
            //Stack<Cell> solution = maze.solveMaze();
            maze.resetVisited();
            Stack<Cell> solution = maze.solveMaze();
            g.setColor(Color.BLACK); // Elige el color para la solución
        
        for (Cell cell : solution) {
            System.out.println("Dibujando EN EL FOR  DE la solución");
            int x = cell.getCol() * cellSize + cellSize / 2;
            int y = cell.getRow() * cellSize + cellSize / 2;
            int ovalSize = Math.max(1, cellSize / 4); // Ajusta el tamaño del círculo
            g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);

            System.out.println("x:"+x);
            System.out.println("y:" +y);

            
       }
         //Cell start = solution.get(0); // Asumiendo que la solución comienza con la celda inicial.
         //dibujarPuntoEspecial(g, start, Color.GREEN);       
        

    }
    
        else{
            System.out.println("NOOO Dibujando la solución");
        }
    /*
    if (!currentSolutionSteps.isEmpty()) {
        g.setColor(Color.RED);
        for (Cell cell : currentSolutionSteps) {
            // Dibuja cada paso de la solución
            int x = cell.getCol() * cellSize + cellSize / 2;
            int y = cell.getRow() * cellSize + cellSize / 2;
            int ovalSize = Math.max(1, cellSize / 4);
            g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);
        }
    }*/
     if (navigable && currentCell != null) {
       

        // Calcula las coordenadas del centro de la celda actual
        int x = currentCell.getCol() * cellSize + cellSize / 2;
        int y = currentCell.getRow() * cellSize + cellSize / 2;
        int ovalSize = Math.max(1, cellSize / 4); // Ajusta el tamaño del círculo si es necesario

        g.setColor(Color.RED);
        g.fillOval(x - ovalSize / 2, y - ovalSize / 2, ovalSize, ovalSize);
    }
    }

    @Override
    public Dimension getPreferredSize() {
        // Aquí puedes definir el tamaño preferido basándote en el tamaño del laberinto
        return new Dimension(maze.getCols() * 10, maze.getRows() * 10); // Sustituye 10 por el tamaño deseado de cada celda
    }
    
    
    
    
     public void setNavigable(boolean navigable) {
        this.navigable = navigable;
        setFocusable(navigable);
        if (navigable) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (!navigable) return; // Si no es navegable, no haga nada.

                    int newRow = currentCell.getRow();
                    int newCol = currentCell.getCol();

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:    newRow--; break;
                        case KeyEvent.VK_DOWN:  newRow++; break;
                        case KeyEvent.VK_LEFT:  newCol--; break;
                        case KeyEvent.VK_RIGHT: newCol++; break;
                        // puedes agregar más casos si necesitas otras teclas
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

    
    
}