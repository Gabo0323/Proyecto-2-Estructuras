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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

public class Maze {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;
    private final List<Cell> frontiers;
     private Cell solutionTarget;
      private Cell startCell;
     

    
    public int getCols() {
        return cols;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getMazeInfo() {
        return rows;
    }
    public void setSolutionTarget(int row, int col) {
        this.solutionTarget = cells[row][col];
    }
    public void setStartCell(int row, int col) {
        this.startCell = cells[row][col];
        // Opcionalmente, puedes reiniciar el laberinto o actualizar cualquier estado relevante aquí
    }

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        this.frontiers = new ArrayList<>();

        // Initialize cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
    }
    private void resetMaze() {
        // Implementa la lógica para limpiar el laberinto anterior aquí
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c); // Reiniciar cada celda
            }
        }
    }

    public void generateMazePRIM() {
    // Initialize the grid
    resetMaze();

    // Randomly choose a starting cell
    Random random = new Random();
    int r = random.nextInt(rows);
    int c = random.nextInt(cols);
    Cell start = cells[r][c];

    // Initialize a list of visited cells
    List<Cell> visitedCells = new ArrayList<>();
    visitedCells.add(start);

    while (!visitedCells.isEmpty()) {
        // Pick a random visited cell
        Cell current = visitedCells.get(random.nextInt(visitedCells.size()));

        // Get unvisited neighbors
        List<Cell> unvisitedNeighbors = getUnvisitedNeighbors(current);

        if (!unvisitedNeighbors.isEmpty()) {
            // Randomly choose one unvisited neighbor
            Cell neighbor = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));

            // Connect the current cell to the chosen neighbor
            current.breakWall(neighbor);

            // Mark the neighbor as visited and add it to the list
            neighbor.setVisited(true);
            visitedCells.add(neighbor);
        } else {
            // If there are no unvisited neighbors, remove this cell from the list
            visitedCells.remove(current);
        }
    }
}
   
   
    private List<Cell> getUnvisitedNeighbors(Cell cell) {
    List<Cell> unvisitedNeighbors = new ArrayList<>();
    int row = cell.getRow();
    int col = cell.getCol();

    // Check the top neighbor
    if (row > 0 && !cells[row - 1][col].isVisited()) {
        unvisitedNeighbors.add(cells[row - 1][col]);
    }

    // Check the left neighbor
    if (col > 0 && !cells[row][col - 1].isVisited()) {
        unvisitedNeighbors.add(cells[row][col - 1]);
    }

    // Check the bottom neighbor
    if (row < rows - 1 && !cells[row + 1][col].isVisited()) {
        unvisitedNeighbors.add(cells[row + 1][col]);
    }

    // Check the right neighbor
    if (col < cols - 1 && !cells[row][col + 1].isVisited()) {
        unvisitedNeighbors.add(cells[row][col + 1]);
    }

    return unvisitedNeighbors;
}

    private void addFrontiers(Cell cell) {
        cell.setInMaze(true);
        for (Cell neighbour : getNeighbours(cell)) {
            if (!neighbour.isInMaze() && !frontiers.contains(neighbour)) {
                frontiers.add(neighbour);
            }
        }
    }

    private List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        int[] directions = {-1, 0, 1, 0, -1}; // N, E, S, W
        for (int i = 0; i < directions.length - 1; i++) {
            int dr = directions[i];
            int dc = directions[i + 1];
            int nr = cell.getRow() + dr;
            int nc = cell.getCol() + dc;

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                neighbours.add(cells[nr][nc]);
            }
        }
        return neighbours;
    }

    public Cell[][] getCells() {
        return cells;
    }
    
    
    
    
     public Stack<Cell> solveMaze() {
        Stack<Cell> path = new Stack<>();
    //Cell start = cells[0][0]; // Punto de inicio es la celda superior izquierda
    Cell start = startCell != null ? startCell : cells[0][0];
    //Cell end = cells[rows - 1][cols - 1]; // Punto final es la celda inferior derecha
    Cell end = solutionTarget != null ? solutionTarget : cells[rows - 1][cols - 1]; // Usar el objetivo si está establecido

    // Si el punto de inicio o final es una pared, no hay solución.
    if (start.hasAllWalls() || end.hasAllWalls()) {
        return path; // Retorna un camino vacío.
    }

    path.push(start);
    start.setVisited(true);

    while (!path.isEmpty()) {
        Cell current = path.peek();
        if (current == end) {
            break; // Llegamos al final.
        }

        Cell next = current.getUnvisitedNeighbour(cells);
        if (next != null) {
            path.push(next);
            next.setVisited(true);
        } else {
            path.pop();
        }
    }

    // Si el stack está vacío aquí, no se encontró una solución.
    if (path.isEmpty()) {
        // Puedes lanzar una excepción o manejar de alguna manera que no se encontró solución.
    }

    return path; // Retorna el camino encontrado.
    }
        
     public void saveMazeToXml(String filename) {
    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        // Raíz del documento XML
        Element rootElement = doc.createElement("maze9090");
        doc.appendChild(rootElement);

        // Añadir atributos de fila y columna
        rootElement.setAttribute("rows", String.valueOf(rows));
        rootElement.setAttribute("cols", String.valueOf(cols));

        // Añadir elementos de celda
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                Element cellElement = doc.createElement("cell");

                // Añadir atributos de celda
                cellElement.setAttribute("row", String.valueOf(cell.getRow()));
                cellElement.setAttribute("col", String.valueOf(cell.getCol()));
                cellElement.setAttribute("topWall", String.valueOf(cell.hasTopWall()));
                cellElement.setAttribute("leftWall", String.valueOf(cell.hasLeftWall()));
                cellElement.setAttribute("bottomWall", String.valueOf(cell.hasBottomWall()));
                cellElement.setAttribute("rightWall", String.valueOf(cell.hasRightWall()));

                rootElement.appendChild(cellElement);
            }
        }

        // Escribir contenido en archivo XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        //Transformer transformer = transformerFactory.newTransformer();
        
        
        Transformer transformer;
try {
    transformer = transformerFactory.newTransformer();
} catch (TransformerConfigurationException e) {
    e.printStackTrace(); // O alguna otra forma de manejar la excepción
    return; // O manejar de alguna otra manera si no puedes continuar
}
        
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException e) {
        e.printStackTrace();
    }
}
     
     
     public void loadMazeFromXml(String filename) {
    try {
        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("cell");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                
                // Obtener atributos de la celda
                int row = Integer.parseInt(eElement.getAttribute("row"));
                int col = Integer.parseInt(eElement.getAttribute("col"));
                boolean topWall = Boolean.parseBoolean(eElement.getAttribute("topWall"));
                boolean leftWall = Boolean.parseBoolean(eElement.getAttribute("leftWall"));
                boolean bottomWall = Boolean.parseBoolean(eElement.getAttribute("bottomWall"));
                boolean rightWall = Boolean.parseBoolean(eElement.getAttribute("rightWall"));
                
                // Reconstruir celda
                Cell cell = cells[row][col];
                cell.setTopWall(topWall);
                cell.setLeftWall(leftWall);
                cell.setBottomWall(bottomWall);
                cell.setRightWall(rightWall);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

     
     public void resetVisited() {
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            cells[r][c].setVisited(false);
        }
    }
}
  
}
