package interfaz;

import controlador.Controlador;
import java.util.List;
import aplicación.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class VentanaPrincipal extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private Controlador controlador;
    private JPanel panelContenido;
    private JLabel lblBienvenida;
    
    public VentanaPrincipal(Controlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        cargarDashboard();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.cerrarSesion();
            }
        });
    }
    
    private void inicializarComponentes() {
        Usuario usuario = controlador.getUsuarioActual();
        Estadísticas stats = usuario.getEstadisticas();
        
        setTitle("Aprende Divertido 3000 - " + usuario.getNickname() + " [" + stats.getRango() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        JPanel panelLateral = crearPanelLateral();
        add(panelLateral, BorderLayout.WEST);
        
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelContenido, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        Usuario usuario = controlador.getUsuarioActual();
        Estadísticas stats = usuario.getEstadisticas();
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        lblBienvenida = new JLabel("Usuario: " + usuario.getNickname());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(Color.WHITE);
        
        JLabel lblNivel = new JLabel("Nivel " + stats.getNivelActual() + stats.getRango() + " • " + stats.getExperienciaTotal() + " XP");
        lblNivel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNivel.setForeground(new Color(200, 200, 255));
        
        panelInfo.add(lblBienvenida);
        panelInfo.add(lblNivel);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(new Color(220, 20, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        panel.add(panelInfo, BorderLayout.WEST);
        panel.add(btnCerrarSesion, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 248, 255));
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));
        
        JLabel lblMenu = new JLabel("MENÚ");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 16));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMenu.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblMenu);
        
        panel.add(crearBotonMenu("Inicio", e -> cargarDashboard()));
        panel.add(crearBotonMenu("Mis Cursos", e -> cargarCursos()));
        panel.add(crearBotonMenu("Logros", e -> cargarLogros()));
        panel.add(crearBotonMenu("Estadísticas", e -> cargarEstadisticas()));
        panel.add(crearBotonMenu("Configuración", e -> cargarConfiguracion()));
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JButton crearBotonMenu(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 40));
        boton.setBackground(new Color(240, 248, 255));
        boton.setForeground(new Color(25, 25, 112));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 130, 180));
                boton.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(240, 248, 255));
                boton.setForeground(new Color(25, 25, 112));
            }
        });
        
        boton.addActionListener(accion);
        return boton;
    }
        
    private void cargarDashboard() {
        panelContenido.removeAll();
        
        JPanel dashboard = new JPanel(new BorderLayout(20, 20));
        dashboard.setBackground(Color.WHITE);
        
        Estadísticas stats = controlador.getUsuarioActual().getEstadisticas();
        JLabel titulo = new JLabel("Dashboard - " + stats.getRango());
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel panelProgreso = new JPanel(new BorderLayout(10, 5));
        panelProgreso.setBackground(Color.WHITE);
        
        int xpActual = stats.getExperienciaTotal();
        int xpInicio = stats.getXpNivelActual();
        int xpFin = stats.getXpParaSiguienteNivel();
        int progreso = stats.getProgresoNivel();
        
        JLabel lblNivelInfo = new JLabel(String.format("Nivel %d → Nivel %d (%d / %d XP)", 
            stats.getNivelActual(), 
            stats.getNivelActual() + 1,
            xpActual - xpInicio,
            xpFin - xpInicio
        ));
        lblNivelInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNivelInfo.setForeground(Color.GRAY);
        
        JProgressBar barraNivel = new JProgressBar(0, 100);
        barraNivel.setValue(progreso);
        barraNivel.setStringPainted(true);
        barraNivel.setString(progreso + "%");
        barraNivel.setPreferredSize(new Dimension(0, 25));
        barraNivel.setForeground(new Color(70, 130, 180));
        
        panelProgreso.add(lblNivelInfo, BorderLayout.NORTH);
        panelProgreso.add(barraNivel, BorderLayout.CENTER);
        
        JPanel panelTitulo = new JPanel(new BorderLayout(10, 10));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.add(titulo, BorderLayout.NORTH);
        panelTitulo.add(panelProgreso, BorderLayout.CENTER);
        
        dashboard.add(panelTitulo, BorderLayout.NORTH);
        
        JPanel panelResumen = new JPanel(new GridLayout(2, 2, 15, 15));
        panelResumen.setBackground(Color.WHITE);
        
        panelResumen.add(crearTarjetaEstadistica("Aciertos", 
            String.valueOf(stats.getNumAciertos()), new Color(34, 139, 34)));
        panelResumen.add(crearTarjetaEstadistica("Fallos", 
            String.valueOf(stats.getNumFallos()), new Color(220, 20, 60)));
        panelResumen.add(crearTarjetaEstadistica("Nivel", 
            String.valueOf(stats.getNivelActual()), new Color(255, 140, 0)));
        panelResumen.add(crearTarjetaEstadistica("Logros", 
            String.valueOf(controlador.getUsuarioActual().getLogros().size()), 
            new Color(70, 130, 180)));
        
        dashboard.add(panelResumen, BorderLayout.CENTER);
        
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAccion.setBackground(Color.WHITE);
        JButton btnEmpezar = new JButton("Empezar a Estudiar");
        btnEmpezar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEmpezar.setBackground(new Color(34, 139, 34));
        btnEmpezar.setForeground(Color.WHITE);
        btnEmpezar.setPreferredSize(new Dimension(250, 50));
        btnEmpezar.setFocusPainted(false);
        btnEmpezar.setBorderPainted(false);
        btnEmpezar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEmpezar.addActionListener(e -> cargarCursos());
        panelAccion.add(btnEmpezar);
        dashboard.add(panelAccion, BorderLayout.SOUTH);
        
        panelContenido.add(dashboard);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(color);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 32));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarCursos() {
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout(10, 10));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.setBackground(Color.WHITE);
        
        JButton btnImportar = new JButton("+ Importar Nuevo Curso (JSON)");
        btnImportar.addActionListener(e -> ejecutarImportacion());
        panelAcciones.add(btnImportar);
        
        panelContenido.add(panelAcciones, BorderLayout.NORTH);

        JPanel panelGrid = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGrid.setBackground(Color.WHITE);

        List<Curso> cursos = controlador.obtenerCursosDesdeCarpeta(); 

        for (Curso curso : cursos) {
            JButton btnCurso = new JButton("<html><center><b>" + curso.getNombre() + "</b><br>" 
                                            + "<font color='gray'>" + curso.getDominio() + "</font></center></html>");
            btnCurso.setPreferredSize(new Dimension(150, 80));
            btnCurso.addActionListener(e -> {
                controlador.seleccionarCurso(curso);
                
                if (!curso.getBloques().isEmpty()) {
                    new VentanaSeleccionBloque(controlador, curso).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Este curso no tiene bloques de contenido.", 
                        "Aviso", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            panelGrid.add(btnCurso);
        }

        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.setBorder(null);
        panelContenido.add(scroll, BorderLayout.CENTER);

        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private void ejecutarImportacion() {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Cursos en formato JSON", "json"));
        
        int estado = selector.showOpenDialog(this);
        
        if (estado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = selector.getSelectedFile();
            try {
                controlador.importarCurso(archivoSeleccionado);
                JOptionPane.showMessageDialog(this, "¡Curso añadido a tu biblioteca!");
                cargarCursos(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al importar el archivo: " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarLogros() {
        new VentanaLogros(controlador).setVisible(true);
    }
    
    private void cargarEstadisticas() {
        panelContenido.removeAll();
        
        JPanel panelStats = new JPanel(new BorderLayout(20, 20));
        panelStats.setBackground(Color.WHITE);
        
        JLabel titulo = new JLabel("Estadísticas Detalladas");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        panelStats.add(titulo, BorderLayout.NORTH);
        
        Estadísticas stats = controlador.getUsuarioActual().getEstadisticas();
        
        JPanel panelInfo = new JPanel(new GridLayout(0, 1, 10, 10));
        panelInfo.setBackground(Color.WHITE);
        
        panelInfo.add(crearFilaEstadistica("Nivel actual:", 
            String.valueOf(stats.getNivelActual())));
        panelInfo.add(crearFilaEstadistica("Rango:", 
            stats.getRango()));
        panelInfo.add(crearFilaEstadistica("Experiencia total:", 
            stats.getExperienciaTotal() + " XP"));
        panelInfo.add(crearFilaEstadistica("XP para siguiente nivel:", 
            (stats.getXpParaSiguienteNivel() - stats.getExperienciaTotal()) + " XP"));
        panelInfo.add(crearFilaEstadistica("Total de preguntas:", 
            String.valueOf(stats.getTotalPreguntas())));
        panelInfo.add(crearFilaEstadistica("Aciertos:", 
            String.valueOf(stats.getNumAciertos())));
        panelInfo.add(crearFilaEstadistica("Fallos:", 
            String.valueOf(stats.getNumFallos())));
        panelInfo.add(crearFilaEstadistica("Precisión:", 
            String.format("%.1f%%", stats.getPorcentajeAciertos())));
        panelInfo.add(crearFilaEstadistica("Logros desbloqueados:", 
            String.valueOf(controlador.getUsuarioActual().getLogros().size())));
        
        panelStats.add(panelInfo, BorderLayout.CENTER);
        
        panelContenido.add(panelStats);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private JPanel crearFilaEstadistica(String etiqueta, String valor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 16));
        lblValor.setForeground(new Color(34, 139, 34));
        
        panel.add(lblEtiqueta, BorderLayout.WEST);
        panel.add(lblValor, BorderLayout.EAST);
        
        return panel;
    }

    private void cargarConfiguracion() {
        panelContenido.removeAll();
        
        JPanel panelConfig = new JPanel(new BorderLayout(20, 20));
        panelConfig.setBackground(Color.WHITE);
        
        JLabel titulo = new JLabel("Configuración");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        panelConfig.add(titulo, BorderLayout.NORTH);
        
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBackground(Color.WHITE);
        
        Estrategia estActual = controlador.getUsuarioActual().getEstrategia();
        String nombreEstActual = "Secuencial";
        
        if (estActual instanceof EstrategiaAleatoria) {
            nombreEstActual = "Aleatoria";
        } else if (estActual instanceof EstrategiaRepeticiónEspaciada) {
            nombreEstActual = "Espaciada";
        }

        JPanel panelEstrategia = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstrategia.setBackground(Color.WHITE);
        panelEstrategia.add(new JLabel("Estrategia de estudio:"));
        
        String[] estrategias = {"Secuencial", "Aleatoria", "Espaciada"};
        JComboBox<String> comboEstrategias = new JComboBox<>(estrategias);
        comboEstrategias.setSelectedItem(nombreEstActual);
        
        JLabel lblModoActivo = new JLabel(" (Modo activo: " + nombreEstActual + ")");
        lblModoActivo.setFont(new Font("Arial", Font.ITALIC, 14));
        lblModoActivo.setForeground(new Color(70, 130, 180));

        comboEstrategias.addActionListener(e -> {
            String seleccion = (String) comboEstrategias.getSelectedItem();
            controlador.cambiarEstrategia(seleccion);
            lblModoActivo.setText(" (Modo activo: " + seleccion + ")");
            
            JOptionPane.showMessageDialog(this, 
                "Estrategia cambiada a: " + seleccion, 
                "Configuración", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        panelEstrategia.add(comboEstrategias);
        panelEstrategia.add(lblModoActivo);
        
        panelOpciones.add(panelEstrategia);
        panelConfig.add(panelOpciones, BorderLayout.CENTER);
        
        panelContenido.add(panelConfig);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            controlador.cerrarSesion();
            this.dispose();
            new VentanaLogin(controlador).setVisible(true);
        }
    }
}