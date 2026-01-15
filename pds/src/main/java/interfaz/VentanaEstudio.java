package interfaz;

import controlador.Controlador;
import aplicación.*;
import javax.swing.*;
import java.awt.*;

public class VentanaEstudio extends JFrame {
    private static final long serialVersionUID = 1L;
    private Controlador controlador;
    private JLabel lblProgreso;
    private JLabel lblPregunta;
    private JPanel panelRespuesta;
    private JButton btnVerificar;
    private JButton btnSiguiente;
    private JLabel lblResultado;
    private JProgressBar barraProgreso;
    private JLabel lblRacha;
    
    private JTextField campoRespuesta;
    private JRadioButton[] opcionesRadio;
    private ButtonGroup grupoOpciones;
    
    private int xpInicialSesion;
    
    public VentanaEstudio(Controlador controlador) {
        this.controlador = controlador;
        
        if (controlador.getUsuarioActual() == null) {
            JOptionPane.showMessageDialog(this, 
                "Debe iniciar sesión primero", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }
        
        xpInicialSesion = controlador.getUsuarioActual().getEstadisticas().getExperienciaTotal();
        
        inicializarComponentes();
        cargarPregunta();
    }
    
    private void inicializarComponentes() {
        setTitle("Sesión de Estudio");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.WHITE);
        
        JPanel panelInfo = new JPanel(new BorderLayout(10, 0));
        panelInfo.setBackground(Color.WHITE);
        
        lblProgreso = new JLabel("Pregunta 1 de 10");
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 16));
        
        lblRacha = new JLabel("Racha: 0");
        lblRacha.setFont(new Font("Arial", Font.BOLD, 14));
        lblRacha.setForeground(new Color(255, 140, 0));
        
        panelInfo.add(lblProgreso, BorderLayout.WEST);
        panelInfo.add(lblRacha, BorderLayout.EAST);
        
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(10);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(0, 25));
        barraProgreso.setForeground(new Color(34, 139, 34));
        
        panelSuperior.add(panelInfo, BorderLayout.NORTH);
        panelSuperior.add(barraProgreso, BorderLayout.CENTER);
        
        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(Color.WHITE);
        
        JPanel panelPregunta = new JPanel(new BorderLayout());
        panelPregunta.setBackground(new Color(240, 248, 255));
        panelPregunta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        lblPregunta = new JLabel("<html><div style='text-align: center;'>Cargando pregunta...</div></html>");
        lblPregunta.setFont(new Font("Arial", Font.PLAIN, 18));
        lblPregunta.setHorizontalAlignment(SwingConstants.CENTER);
        panelPregunta.add(lblPregunta, BorderLayout.CENTER);
        
        panelRespuesta = new JPanel(new BorderLayout());
        panelRespuesta.setBackground(Color.WHITE);
        panelRespuesta.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        panelCentral.add(panelPregunta, BorderLayout.NORTH);
        panelCentral.add(panelRespuesta, BorderLayout.CENTER);
        
        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));
        lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(lblResultado, BorderLayout.SOUTH);
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelInferior.setBackground(Color.WHITE);
        
        btnVerificar = crearBoton("Verificar", new Color(70, 130, 180));
        btnVerificar.addActionListener(e -> verificarRespuesta());
        
        btnSiguiente = crearBoton("Siguiente", new Color(34, 139, 34));
        btnSiguiente.addActionListener(e -> siguientePregunta());
        btnSiguiente.setVisible(false);
        
        panelInferior.add(btnVerificar);
        panelInferior.add(btnSiguiente);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(130, 40));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    private void cargarPregunta() {
        Pregunta pregunta = controlador.getPreguntaActual();
        
        if (pregunta == null) {
            lblPregunta.setText("<html><div style='text-align: center;'>No hay preguntas disponibles</div></html>");
            btnVerificar.setEnabled(false);
            return;
        }
        
        int actual = controlador.getIndicePreguntaActual();
        int total = controlador.getTotalPreguntas();
        lblProgreso.setText("Pregunta " + actual + " de " + total);
        barraProgreso.setValue((actual * 100) / total);
        
        actualizarRacha();
        
        lblPregunta.setText("<html><div style='text-align: center;'>" + 
            pregunta.getPregunta() + "</div></html>");
        
        panelRespuesta.removeAll();
        lblResultado.setText(" ");
        btnVerificar.setVisible(true);
        btnSiguiente.setVisible(false);
        
        if (pregunta instanceof PreguntaTest) {
            configurarPreguntaTest((PreguntaTest) pregunta);
        } else if (pregunta instanceof PreguntaCompletar) {
            configurarPreguntaCompletar();
        } else if (pregunta instanceof PreguntaTarjeta) {
            configurarPreguntaTarjeta((PreguntaTarjeta) pregunta);
        }
        
        panelRespuesta.revalidate();
        panelRespuesta.repaint();
    }
    
    private void actualizarRacha() {
        int racha = controlador.getRachaActualSesion();
        lblRacha.setText("Racha: " + racha);
        
        if (racha >= 10) {
            lblRacha.setForeground(new Color(255, 0, 0)); 
        } else if (racha >= 5) {
            lblRacha.setForeground(new Color(255, 140, 0));
        } else {
            lblRacha.setForeground(Color.GRAY);
        }
    }
    
    private void configurarPreguntaTest(PreguntaTest pregunta) {
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBackground(Color.WHITE);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        grupoOpciones = new ButtonGroup();
        opcionesRadio = new JRadioButton[pregunta.getOpciones().size()];
        
        for (int i = 0; i < pregunta.getOpciones().size(); i++) {
            opcionesRadio[i] = new JRadioButton(pregunta.getOpciones().get(i));
            opcionesRadio[i].setFont(new Font("Arial", Font.PLAIN, 16));
            opcionesRadio[i].setBackground(Color.WHITE);
            opcionesRadio[i].setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            grupoOpciones.add(opcionesRadio[i]);
            panelOpciones.add(opcionesRadio[i]);
        }
        
        panelRespuesta.add(panelOpciones, BorderLayout.CENTER);
    }
    
    private void configurarPreguntaCompletar() {
        JPanel panelCampo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCampo.setBackground(Color.WHITE);
        panelCampo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        JLabel lblInstruccion = new JLabel("Escribe tu respuesta:");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 14));
        
        campoRespuesta = new JTextField(30);
        campoRespuesta.setFont(new Font("Arial", Font.PLAIN, 16));
        campoRespuesta.addActionListener(e -> btnVerificar.doClick());
        
        panelCampo.add(lblInstruccion);
        panelCampo.add(campoRespuesta);
        
        panelRespuesta.add(panelCampo, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(() -> campoRespuesta.requestFocus());
    }
    
    private void configurarPreguntaTarjeta(PreguntaTarjeta pregunta) {
        JPanel panelTarjeta = new JPanel(new BorderLayout(10, 10));
        panelTarjeta.setBackground(new Color(255, 250, 240));
        panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JTextArea areaInfo = new JTextArea(pregunta.getInformacion());
        areaInfo.setFont(new Font("Arial", Font.PLAIN, 16));
        areaInfo.setLineWrap(true);
        areaInfo.setWrapStyleWord(true);
        areaInfo.setEditable(false);
        areaInfo.setBackground(new Color(255, 250, 240));
        
        JScrollPane scroll = new JScrollPane(areaInfo);
        scroll.setBorder(null);
        
        panelTarjeta.add(scroll, BorderLayout.CENTER);
        panelRespuesta.add(panelTarjeta, BorderLayout.CENTER);
        
        btnVerificar.setText("Entendido");
    }
    
    private void verificarRespuesta() {
        Pregunta pregunta = controlador.getPreguntaActual();
        if (pregunta == null) return;
        
        String respuestaUsuario = obtenerRespuestaUsuario();
        boolean esCorrecta = controlador.verificarRespuesta(respuestaUsuario);
        
        actualizarRacha();
        
        if (esCorrecta) {
            lblResultado.setText("¡Correcto!");
            lblResultado.setForeground(new Color(34, 139, 34));
        } else {
            if (pregunta instanceof PreguntaTarjeta) {
                lblResultado.setText("Información vista");
                lblResultado.setForeground(new Color(34, 139, 34));
            } else {
                lblResultado.setText("Incorrecto. La respuesta era: " + pregunta.getRespuesta());
                lblResultado.setForeground(new Color(220, 20, 60));
            }
        }
        
        btnVerificar.setVisible(false);
        
        if (controlador.hayMasPreguntas()) {
            btnSiguiente.setVisible(true);
        } else {
            btnSiguiente.setText("Finalizar");
            btnSiguiente.setVisible(true);
        }
        
        deshabilitarInputs();
    }
    
    private String obtenerRespuestaUsuario() {
        Pregunta pregunta = controlador.getPreguntaActual();
        
        if (pregunta instanceof PreguntaTest) {
            for (JRadioButton radio : opcionesRadio) {
                if (radio.isSelected()) {
                    return radio.getText();
                }
            }
            return "";
        } else if (pregunta instanceof PreguntaCompletar) {
            return campoRespuesta.getText();
        } else if (pregunta instanceof PreguntaTarjeta) {
            return "visto";
        }
        
        return "";
    }
    
    private void deshabilitarInputs() {
        if (opcionesRadio != null) {
            for (JRadioButton radio : opcionesRadio) {
                radio.setEnabled(false);
            }
        }
        if (campoRespuesta != null) {
            campoRespuesta.setEnabled(false);
        }
    }
    
    private void siguientePregunta() {
        if (controlador.siguientePregunta()) {
            cargarPregunta();
        } else {
            controlador.finalizarSesionEstudio();
            mostrarResumen();
        }
    }
    
    private void mostrarResumen() {
        Estadísticas stats = controlador.getUsuarioActual().getEstadisticas();
        int xpGanada = stats.getExperienciaTotal() - xpInicialSesion;
        int mejorRachaSesion = controlador.getMejorRachaSesion();
        
        String mensaje = String.format(
            "¡Sesión completada!\n\n" +
            "Resumen:\n" +
            "Total de aciertos: %d\n" +
            "Total de fallos: %d\n" +
            "Precisión: %.1f%%\n" +
            "Mejor racha en esta sesión: %d\n\n" +
            "⭐ Progreso:\n" +
            "XP ganada: +%d\n" +
            "Nivel actual: %d\n" +
            "Rango: %s",
            stats.getNumAciertos(),
            stats.getNumFallos(),
            stats.getPorcentajeAciertos(),
            mejorRachaSesion,
            xpGanada,
            stats.getNivelActual(),
            stats.getRango()
        );
        
        JOptionPane.showMessageDialog(this, 
            mensaje, 
            "Resumen de la Sesión", 
            JOptionPane.INFORMATION_MESSAGE);
        
        this.dispose();
    }
}