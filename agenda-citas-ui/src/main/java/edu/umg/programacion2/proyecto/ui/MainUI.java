package edu.umg.programacion2.proyecto.ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainUI {

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            try {
	                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            } catch (Exception ignored) {}

	            VentanaPrincipal ventana = new VentanaPrincipal();
	            ventana.setVisible(true);
	        });
	    }
	}


