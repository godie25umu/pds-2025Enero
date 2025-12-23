package interfaz;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaPrincipal() {
        // Configuración básica de la ventana
        setTitle("Aplicación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crear un panel para contener el texto
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Para centrar el contenido fácilmente
        
        // Crear la etiqueta solicitada
        JLabel lblTexto = new JLabel("Ventana principal");
        lblTexto.setFont(new Font("Tahoma", Font.BOLD, 20));
        
        panel.add(lblTexto);
        add(panel);
    }

    public static void main(String[] args) {
        // Ejecución de la interfaz
        EventQueue.invokeLater(() -> {
            try {
                VentanaPrincipal frame = new VentanaPrincipal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}