package interfaz;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana de inicio de sesión y registro de usuarios.
 */
public class VentanaLogin extends JFrame {
	private static final long serialVersionUID = 1L;
	private Controlador controlador;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JLabel lblMensaje;
    
    public VentanaLogin(Controlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Aprende Divertido 3000 - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con fondo
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 248, 255));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel del título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(240, 248, 255));
        JLabel lblTitulo = new JLabel("Aprende Divertido 3000");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(25, 25, 112));
        panelTitulo.add(lblTitulo);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        panelFormulario.add(lblUsuario, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        campoUsuario = new JTextField(20);
        campoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(campoUsuario, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Arial", Font.BOLD, 14));
        panelFormulario.add(lblContraseña, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        campoContraseña = new JPasswordField(20);
        campoContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(campoContraseña, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 12));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelFormulario.add(lblMensaje, gbc);
        
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 10, 8, 5);
        
        gbc.gridx = 0;
        btnIniciarSesion = crearBoton("Iniciar Sesión", new Color(34, 139, 34));
        btnIniciarSesion.addActionListener(this::iniciarSesion);
        panelFormulario.add(btnIniciarSesion, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 8, 10);
        btnRegistrarse = crearBoton("Registrarse", new Color(70, 130, 180));
        btnRegistrarse.addActionListener(this::registrarse);
        panelFormulario.add(btnRegistrarse, gbc);
        
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        JPanel panelPie = new JPanel();
        panelPie.setBackground(new Color(240, 248, 255));
        JLabel lblPie = new JLabel("Versión 1.0 - Sistema de Aprendizaje");
        lblPie.setFont(new Font("Arial", Font.PLAIN, 10));
        lblPie.setForeground(Color.GRAY);
        panelPie.add(lblPie);
        panelPrincipal.add(panelPie, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        campoUsuario.addActionListener(e -> btnIniciarSesion.doClick());
        campoContraseña.addActionListener(e -> btnIniciarSesion.doClick());
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 35));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    private void iniciarSesion(ActionEvent e) {
        String usuario = campoUsuario.getText().trim();
        String contraseña = new String(campoContraseña.getPassword());
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos", Color.RED);
            return;
        }
        
        if (controlador.iniciarSesion(usuario, contraseña)) {
            mostrarMensaje("¡Bienvenido!", new Color(34, 139, 34));
            
            Timer timer = new Timer(500, ev -> {
                this.dispose();
                new VentanaPrincipal(controlador).setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            mostrarMensaje("Usuario o contraseña incorrectos", Color.RED);
            campoContraseña.setText("");
        }
    }
    
    private void registrarse(ActionEvent e) {
        String usuario = campoUsuario.getText().trim();
        String contraseña = new String(campoContraseña.getPassword());
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos", Color.RED);
            return;
        }
        
        if (contraseña.length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres", Color.RED);
            return;
        }
        
        if (controlador.registrarUsuario(usuario, contraseña)) {
            mostrarMensaje("¡Usuario registrado! Ahora puede iniciar sesión", new Color(34, 139, 34));
            campoContraseña.setText("");
        } else {
            mostrarMensaje("El usuario ya existe", Color.RED);
        }
    }
    
    private void mostrarMensaje(String mensaje, Color color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(color);
    }
}