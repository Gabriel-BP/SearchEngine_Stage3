package es.ulpgc;

import es.ulpgc.client.SearchEngineGUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SearchEngineGUI gui = new SearchEngineGUI();
            gui.setVisible(true);
        });
    }
}