package com.mycompany.mavenproject12;

/**
 * Representa una celda en un laberinto. Cada celda tiene coordenadas (fila,
 * columna) y puede tener paredes en las cuatro direcciones. También se utiliza
 * para rastrear si la celda ha sido visitada durante la generación o resolución
 * del laberinto. Además, se puede utilizar para seguir el camino en la
 * resolución del laberinto.
 */
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author esteb
 */
public class Cell {

    private final int row;
    private final int col;
    private boolean isInMaze;
    private boolean topWall;
    private boolean leftWall;
    private boolean bottomWall;
    private boolean rightWall;

    private boolean visited;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        isInMaze = false;
        topWall = true;
        leftWall = true;
        bottomWall = true;
        rightWall = true;

        visited = false;
    }
// Rompe la pared entre la celda actual y otra celda

    public void breakWall(Cell other) {
        if (row < other.row) {
            bottomWall = false;
            other.topWall = false;
        } else if (row > other.row) {
            topWall = false;
            other.bottomWall = false;
        } else if (col < other.col) {
            rightWall = false;
            other.leftWall = false;
        } else if (col > other.col) {
            leftWall = false;
            other.rightWall = false;
        }
    }

    // Setter para las paredes
    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    // Getter y Setter para la marca de visita
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    // Verifica si la celda tiene todas las paredes
    public boolean hasAllWalls() {
        return topWall && leftWall && bottomWall && rightWall;
    }

    // Obtiene un vecino no visitado de la celda
    public Cell getUnvisitedNeighbour(Cell[][] cells) {
        List<Cell> neighbours = getNeighbours(cells);
        for (Cell neighbour : neighbours) {
            if (!neighbour.isVisited() && !neighbour.hasWallBetween(this)) {
                return neighbour;
            }
        }
        return null;
    }

    // Verifica si hay una pared entre la celda actual y otra celda
    public boolean hasWallBetween(Cell other) {
        if (row == other.row) {
            if (col == other.col - 1) {
                // La otra celda está a la derecha
                return rightWall;
            } else if (col == other.col + 1) {
                // La otra celda está a la izquierda
                return leftWall;
            }
        } else if (col == other.col) {
            if (row == other.row - 1) {
                // La otra celda está debajo
                return bottomWall;
            } else if (row == other.row + 1) {
                // La otra celda está arriba
                return topWall;
            }
        }
        return true; // Si las celdas no son vecinas, asumimos que hay una pared
    }

    // Obtiene la lista de vecinos de la celda
    public List<Cell> getNeighbours(Cell[][] cells) {
        List<Cell> neighbours = new ArrayList<>();

        // Arriba
        if (row > 0) {
            neighbours.add(cells[row - 1][col]);
        }
        // Derecha
        if (col < cells[0].length - 1) {
            neighbours.add(cells[row][col + 1]);
        }
        // Abajo
        if (row < cells.length - 1) {
            neighbours.add(cells[row + 1][col]);
        }
        // Izquierda
        if (col > 0) {
            neighbours.add(cells[row][col - 1]);
        }

        return neighbours;
    }

    // Getters para las propiedades de la celda
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isInMaze() {
        return isInMaze;
    }

    public void setInMaze(boolean inMaze) {
        isInMaze = inMaze;
    }

    // Getters for walls...
    public boolean hasTopWall() {
        return topWall;
    }

    public boolean hasLeftWall() {
        return leftWall;
    }

    public boolean hasBottomWall() {
        return bottomWall;
    }

    public boolean hasRightWall() {
        return rightWall;
    }

    /////////////////////////////////////
    // Propiedades adicionales para la resolución del laberinto
    private Cell parent;

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }
// Obtiene vecinos conectados en el laberinto

    public List<Cell> getConnectedNeighbours(Cell[][] cells) {
        List<Cell> connectedNeighbours = new ArrayList<>();
        List<Cell> neighbours = getNeighbours(cells);

        for (Cell neighbour : neighbours) {
            if (neighbour.isInMaze() && !this.hasWallBetween(neighbour)) {
                connectedNeighbours.add(neighbour);
            }
        }
        return connectedNeighbours;
    }

}
