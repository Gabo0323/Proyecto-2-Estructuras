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
        loadMapButton = new JButton("Cargar laberinto");
        loadMapButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo de Laberinto");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Leer las dimensiones del laberinto desde el archivo XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(selectedFile);
                doc.getDocumentElement().normalize();
                
                int rows = Integer.parseInt(doc.getDocumentElement().getAttribute("rows"));
                int cols = Integer.parseInt(doc.getDocumentElement().getAttribute("cols"));

                // Crear la instancia de Maze con las dimensiones leídas
                Maze maze = new Maze(rows, cols);
                maze.loadMazeFromXml(selectedFile.getAbsolutePath());
                
                // Mostrar el laberinto en una nueva ventana
                MazeWindow mazeWindow = new MazeWindow(maze);
                mazeWindow.setVisible(true);
                openMazes.add(mazeWindow);
                updateMazeInfo();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar el laberinto: " + ex.getMessage());
            }
        }
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

    private void updateMazeInfo() {
    StringBuilder infoBuilder = new StringBuilder();
    int contador = 0;
    for (MazeWindow mazeWindow : openMazes) {
        Maze maze = mazeWindow.getMaze(); // Asumiendo que tienes un método para obtener el Maze desde MazeWindow
        int numRows = maze.getRows();
        int numCols = maze.getCols();
        
        infoBuilder.append("Laberinto ").append(contador + 1).append(": ")
                   .append(numRows).append(" filas x ")
                   .append(numCols).append(" columnas\n");
        contador++;
    }
    mazeInfoTextArea.setText(infoBuilder.toString());
    System.out.println("Total de laberintos abiertos: " + contador);
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
    
    
    
    
  
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
