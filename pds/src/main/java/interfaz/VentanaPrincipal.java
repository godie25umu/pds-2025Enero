package interfaz;

import controlador.Controlador;
import aplicación.Usuario;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private Controlador controlador;

    public VentanaPrincipal(Controlador controlador) {
        this.controlador = controlador;
        
        Usuario user = this.controlador.getUsuarioActual();
        String nickname = (user != null) ? user.getNickname() : "Invitado";

        setTitle("Aprende Divertido 3000 - Panel de " + nickname);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 255));

        JLabel lblTexto = new JLabel("¡Bienvenido, " + nickname + "!");
        lblTexto.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTexto.setHorizontalAlignment(SwingConstants.CENTER);
        lblTexto.setForeground(new Color(25, 25, 112));
        
        panel.add(lblTexto, BorderLayout.NORTH);

        JTextArea infoUser = new JTextArea();
        infoUser.setEditable(false);
        infoUser.setText("\n Tus estadísticas:\n" + 
                         " - Cursos inscritos: " + (user != null ? user.getCursos().size() : 0) + "\n" +
                         " - Logros obtenidos: " + (user != null ? user.getLogros().size() : 0));
        
        panel.add(new JScrollPane(infoUser), BorderLayout.CENTER);

        add(panel);
    }
}