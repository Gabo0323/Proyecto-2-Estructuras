/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject12;

/**
 *
 * @author esteb
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private JButton generateMazeButton;
    private JTextArea mazeInfoTextArea;
    private ArrayList<MazeWindow> openMazes;
    private Cell[][] cells;
    
  
  
  
    private JButton loadMapButton; // Botón para cargar laberintos
 
   
  

    public MainFrame() {
       
        super("Generador de Laberintos");
        openMazes = new ArrayList<>();
        int numRows = 10; // Cambia el tamaño según tus necesidades
        int numCols = 10; // Cambia el tamaño según tus necesidades
        cells = new Cell[numRows][numCols];

      
        loadMapButton = new JButton("Cargar Laberinto");
        loadMapButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo del laberinto");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos XML", "xml");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(MainFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadMazeFromFile(selectedFile.getAbsolutePath());
            }
        }
    });
        
        generateMazeButton = new JButton("Generar laberinto");
        generateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                int minSize = 5;
                int maxSize = 20; // Asegúrate de que maxSize sea mayor que minSize
                int width = rand.nextInt((maxSize - minSize) + 1) + minSize;
                int height = rand.nextInt((maxSize - minSize) + 1) + minSize;
                
                Maze maze = new Maze(width, height); // Aquí defines el tamaño del laberinto
                maze.generateMazePRIM(); // Generar el laberintof
                


        // Crear una nueva ventana de laberinto y pasarle la instancia del laberinto
        MazeWindow mazeWindow = new MazeWindow(maze);
        mazeWindow.setVisible(true); // Mostrar la ventan
        openMazes.add(mazeWindow);
        updateMazeInfo();
               
               //generateMaze();
            }
        });
        
       
        mazeInfoTextArea = new JTextArea(10, 30);
        mazeInfoTextArea.setEditable(false);

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(generateMazeButton);
        this.add(new JScrollPane(mazeInfoTextArea));
        this.add(loadMapButton);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    
   
    private void updateCellSize() {
        // Actualizar la lógica del tamaño de las celdas si es necesario
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        //cellSize = Math.min(panelWidth / maze.getCols(), panelHeight / maze.getRows());
    }
    
    private void generateMaze() {
        Maze maze = new Maze(6,6);
        MazeWindow mazeWindow = new MazeWindow(maze);
         
        openMazes.add(mazeWindow);
        updateMazeInfo();
    }
    
    
    private void loadMazeFromFile(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList cellList = doc.getElementsByTagName("cell");

            for (int i = 0; i < cellList.getLength(); i++) {
                Node node = cellList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int row = Integer.parseInt(element.getAttribute("row"));
                    int col = Integer.parseInt(element.getAttribute("col"));

                    // Asegúrate de que las coordenadas estén dentro de los límites de la matriz
                    if (row >= 0 && row < cells.length && col >= 0 && col < cells[0].length) {
                        boolean topWall = Boolean.parseBoolean(element.getAttribute("topWall"));
                        boolean leftWall = Boolean.parseBoolean(element.getAttribute("leftWall"));
                        boolean bottomWall = Boolean.parseBoolean(element.getAttribute("bottomWall"));
                        boolean rightWall = Boolean.parseBoolean(element.getAttribute("rightWall"));

                        // Crea la celda y configura sus propiedades
                        Cell cell = new Cell(row,col);
                        cell.setTopWall(topWall);
                        cell.setLeftWall(leftWall);
                        cell.setBottomWall(bottomWall);
                        cell.setRightWall(rightWall);

                        // Asigna la celda a la matriz
                        cells[row][col] = cell;
                    }
                }
            }

            // Realiza cualquier otra actualización o redibujado necesario aquí

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el laberinto desde el archivo XML: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMazeInfo() {
        StringBuilder infoBuilder = new StringBuilder();
         int contador=0;
        for (MazeWindow maze : openMazes) {
           contador+=1;
            // Aquí agregas la información del laberinto como prefieras.
            infoBuilder.append("www").append("\n");
        }
        mazeInfoTextArea.setText(infoBuilder.toString());
        System.out.println("contador: "+contador);
    }
    
    
    
    
  
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}