/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject12;

/**
 *
 * @author esteb
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
    
    
     public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    
    public boolean hasAllWalls() {
    return topWall && leftWall && bottomWall && rightWall;
}
    public Cell getUnvisitedNeighbour(Cell[][] cells) {
        List<Cell> neighbours = getNeighbours(cells);
        for (Cell neighbour : neighbours) {
            if (!neighbour.isVisited() && !neighbour.hasWallBetween(this)) {
                return neighbour;
            }
        }
        return null;
    }

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
    
    
    private Cell parent;

public Cell getParent() {
    return parent;
}

public void setParent(Cell parent) {
    this.parent = parent;
}
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
