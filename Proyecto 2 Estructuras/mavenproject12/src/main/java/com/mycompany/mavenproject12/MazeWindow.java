package com.mycompany.mavenproject12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Ventana principal para generar laberintos
 */
public class MazeWindow extends JFrame {

    private MazePanel mazePanel;
    private Maze maze;
    private JButton toggleSolutionButton;
    private boolean showSolution = true;
    private boolean navigable = false;
    private JButton navigateMazeButton; // Botón para activar la navegación

    private JScrollPane scrollPane;

    private JButton zoomInButton;
    private JButton zoomOutButton;

    public Maze getMaze() {
        return this.maze;
    }

    //constructor
    public MazeWindow(Maze maze) {

        this.maze = maze;
        mazePanel = new MazePanel(maze);

        // Configuración inicial de la ventana
        setTitle("Laberinto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear el botón de guardar y su ActionListener
        JButton saveButton = new JButton("Guardar Laberinto");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar laberinto como XML");
                int userSelection = fileChooser.showSaveDialog(MazeWindow.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    // Asegúrate de que el archivo tenga la extensión .xml
                    if (!filePath.endsWith(".xml")) {
                        filePath += ".xml";
                    }
                    maze.saveMazeToXml(filePath);
                }
            }
        });
        navigateMazeButton = new JButton("Recorrer laberinto");
        navigateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigable = !navigable; // Cambia el estado de navegación
                navigateMazeButton.setText(navigable ? "Disable Navigation" : "Enable Navigation");
                mazePanel.setNavigable(navigable); // Actualiza el panel con el nuevo estado
                if (navigable) {
                    mazePanel.requestFocusInWindow(); // Solicita el foco para capturar los eventos de teclado
                }
            }
        });
        JButton selectSolutionButton = new JButton("Select FINAL del laberinto");
        selectSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.enableTargetSelection(true); // Habilitar modo de selección en MazePanel
            }
        });

        JButton switchViewButton = new JButton("Cambiar vista");
        switchViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.setUsePaintComponent2(!mazePanel.isUsingPaintComponent2());
                switchViewButton.setText(mazePanel.isUsingPaintComponent2() ? "Use Default View" : "Use Alternative View");
            }
        });

        // Añadir el botón de navegación al panel de botones
        toggleSolutionButton = new JButton("Show Solution");
        toggleSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSolution = !showSolution; // Cambia el estado de la solución
                toggleSolutionButton.setText(showSolution ? "Hide Solution" : "Show Solution");
                mazePanel.setShowSolution(showSolution); // Actualiza el panel con el nuevo estado
            }
        });
        JButton selectStartButton = new JButton("Seleccione INICIO");
        selectStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.enableStartSelection(true); // Habilitar modo de selección de inicio en MazePanel
            }
        });

        // Añadir el botón de guardar a la ventana
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(toggleSolutionButton);
        buttonPanel.add(navigateMazeButton);
        buttonPanel.add(switchViewButton);
        buttonPanel.add(selectSolutionButton);
        buttonPanel.add(selectStartButton);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH); // Añadir el botón en la parte sur del layout

        zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.setScale(mazePanel.getScale() * 1.1);
            }
        });
        zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.setScale(mazePanel.getScale() / 1.1);
            }
        });

        JPanel zoomPanel = new JPanel();
        zoomPanel.add(zoomInButton);
        zoomPanel.add(zoomOutButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mazePanel, BorderLayout.CENTER);
        centerPanel.add(zoomPanel, BorderLayout.SOUTH);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

}
