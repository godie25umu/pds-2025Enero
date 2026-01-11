package ejecutable;

import controlador.Controlador;
import interfaz.VentanaLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal que inicia la aplicación Aprende Divertido 3000.
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel del sistema");
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                Controlador controlador = new Controlador();
                
                VentanaLogin ventanaLogin = new VentanaLogin(controlador);
                ventanaLogin.setVisible(true);
                
                System.out.println("===========================================");
                System.out.println("  Aprende Divertido 3000 - Iniciado");
                System.out.println("===========================================");
                
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación:");
                e.printStackTrace();
                
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la aplicación:\n" + e.getMessage(), 
                    "Error Fatal", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                
                System.exit(1);
            }
        });
    }
}