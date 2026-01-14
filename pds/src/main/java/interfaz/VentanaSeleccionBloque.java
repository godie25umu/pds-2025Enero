package interfaz;

import controlador.Controlador;
import aplicación.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Ventana para seleccionar un bloque de contenido de un curso antes de estudiar.
 * ACTUALIZADA: Incluye opción de Curso Completo con texto en negro.
 */
public class VentanaSeleccionBloque extends JFrame {
    private static final long serialVersionUID = 1L;
    private Controlador controlador;
    private Curso curso;
    private JPanel panelBloques;
    
    public VentanaSeleccionBloque(Controlador controlador, Curso curso) {
        this.controlador = controlador;
        this.curso = curso;
        inicializarComponentes();
        cargarBloques();
    }
    
    private void inicializarComponentes() {
        setTitle("Seleccionar Bloque - " + curso.getNombre());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior con información del curso y botón de curso completo
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 10));
        panelSuperior.setBackground(new Color(240, 248, 255));
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel("Curso: " + curso.getNombre());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(25, 25, 112));
        
        JLabel lblDominio = new JLabel(curso.getDominio() != null ? curso.getDominio() : "Curso");
        lblDominio.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDominio.setForeground(Color.GRAY);
        
        JLabel lblInstruccion = new JLabel("Selecciona un bloque o estudia todo el contenido:");
        lblInstruccion.setFont(new Font("Arial", Font.ITALIC, 12));
        
        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setOpaque(false);
        panelTextos.add(lblTitulo);
        panelTextos.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTextos.add(lblDominio);
        panelTextos.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTextos.add(lblInstruccion);
        
        JButton btnCursoCompleto = new JButton("Curso Completo");
        btnCursoCompleto.setFont(new Font("Arial", Font.BOLD, 14));
        btnCursoCompleto.setBackground(new Color(70, 130, 180));
        btnCursoCompleto.setForeground(Color.BLACK); 
        btnCursoCompleto.setPreferredSize(new Dimension(180, 45));
        btnCursoCompleto.setFocusPainted(false);
        btnCursoCompleto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCursoCompleto.addActionListener(e -> accionCursoCompleto());
        
        panelSuperior.add(panelTextos, BorderLayout.CENTER);
        panelSuperior.add(btnCursoCompleto, BorderLayout.EAST);
        
        panelBloques = new JPanel();
        panelBloques.setLayout(new BoxLayout(panelBloques, BoxLayout.Y_AXIS));
        panelBloques.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(panelBloques);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }

    private void accionCursoCompleto() {
        boolean tienePreguntas = curso.getBloques().stream()
                                      .anyMatch(b -> !b.getPreguntas().isEmpty());
        
        if (!tienePreguntas) {
            JOptionPane.showMessageDialog(this, 
                "Este curso no tiene ninguna pregunta disponible en sus bloques.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        controlador.iniciarEstudioCompleto(curso);
        this.dispose();
        new VentanaEstudio(controlador).setVisible(true);
    }
    
    private void cargarBloques() {
        panelBloques.removeAll();
        
        List<BloqueDeContenido> bloques = curso.getBloques();
        
        if (bloques.isEmpty()) {
            JLabel lblVacio = new JLabel("Este curso no tiene bloques de contenido disponibles.");
            lblVacio.setFont(new Font("Arial", Font.ITALIC, 14));
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblVacio.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
            panelBloques.add(lblVacio);
        } else {
            for (int i = 0; i < bloques.size(); i++) {
                BloqueDeContenido bloque = bloques.get(i);
                JPanel panelBloque = crearTarjetaBloque(bloque, i + 1);
                panelBloques.add(panelBloque);
                panelBloques.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        panelBloques.revalidate();
        panelBloques.repaint();
    }
    
    private JPanel crearTarjetaBloque(BloqueDeContenido bloque, int numero) {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        JLabel lblNumero = new JLabel("Bloque " + numero);
        lblNumero.setFont(new Font("Arial", Font.BOLD, 12));
        lblNumero.setForeground(new Color(70, 130, 180));
        
        JLabel lblNombre = new JLabel(bloque.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblPreguntas = new JLabel(
            bloque.getPreguntas().size() + " pregunta" + 
            (bloque.getPreguntas().size() != 1 ? "s" : "")
        );
        lblPreguntas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPreguntas.setForeground(Color.GRAY);
        
        panelInfo.add(lblNumero);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblPreguntas);
        
        JButton btnIniciar = new JButton("Iniciar Bloque");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciar.setBackground(new Color(34, 139, 34));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setPreferredSize(new Dimension(150, 40));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorderPainted(false);
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnIniciar.addActionListener(e -> {
            if (bloque.getPreguntas().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Este bloque no tiene preguntas.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            controlador.iniciarEstudio(bloque);
            this.dispose();
            new VentanaEstudio(controlador).setVisible(true);
        });
        
        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(btnIniciar, BorderLayout.EAST);
        
        return panel;
    }
}