package interfaz;

import controlador.Controlador;
import aplicación.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VentanaLogros extends JFrame {
    private static final long serialVersionUID = 1L;
    private Controlador controlador;
    private JPanel panelLogros;
    private JProgressBar barraProgreso;
    
    private final Color COLOR_ORO = new Color(255, 193, 7);
    private final Color COLOR_FONDO = new Color(248, 249, 250);
    private final Color COLOR_TEXTO_SEC = new Color(108, 117, 125);
    private final Color COLOR_BLOQUEADO = new Color(224, 224, 224);

    public VentanaLogros(Controlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        cargarLogros();
    }
    
    private void inicializarComponentes() {
        setTitle("Sala de Trofeos - Aprende Divertido 3000");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(25, 25));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel panelCabecera = new JPanel(new GridLayout(2, 1, 5, 5));
        panelCabecera.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Mis Logros");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(33, 37, 41));
        
        Usuario usuario = controlador.getUsuarioActual();
        int obtenidos = usuario.getLogros().size();
        int totales = controlador.obtenerTodosLosLogros().size();
        int porcentaje = (totales > 0) ? (obtenidos * 100 / totales) : 0;

        JPanel panelStats = new JPanel(new BorderLayout(10, 0));
        panelStats.setOpaque(false);
        
        JLabel lblInfo = new JLabel(obtenidos + " de " + totales + " desbloqueados (" + porcentaje + "%)");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo.setForeground(COLOR_TEXTO_SEC);
        
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(porcentaje);
        barraProgreso.setForeground(COLOR_ORO);
        barraProgreso.setBackground(COLOR_BLOQUEADO);
        barraProgreso.setBorderPainted(false);
        barraProgreso.setPreferredSize(new Dimension(200, 8));
        
        panelStats.add(lblInfo, BorderLayout.WEST);
        panelStats.add(barraProgreso, BorderLayout.SOUTH);
        
        panelCabecera.add(lblTitulo);
        panelCabecera.add(panelStats);
        
        panelLogros = new JPanel(new GridLayout(0, 4, 20, 20));
        panelLogros.setBackground(COLOR_FONDO);
        
        JScrollPane scrollPane = new JScrollPane(panelLogros);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private void cargarLogros() {
        panelLogros.removeAll();
        Usuario usuario = controlador.getUsuarioActual();
        List<Logro> logrosUsuario = usuario.getLogros();
        List<Logro> todosLosLogros = controlador.obtenerTodosLosLogros();
        
        for (Logro logro : todosLosLogros) {
            boolean desbloqueado = logrosUsuario.stream()
                .anyMatch(l -> l.getId().equals(logro.getId()));
            panelLogros.add(crearTarjetaLogro(logro, desbloqueado));
        }
    }
    
    private JPanel crearTarjetaLogro(Logro logro, boolean desbloqueado) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(170, 210));
        card.setBackground(Color.WHITE);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(desbloqueado ? COLOR_ORO : new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        JPanel badge = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(desbloqueado ? new Color(255, 193, 7, 40) : new Color(240, 240, 240));
                g2.fillOval(5, 5, 70, 70);
                
                g2.setStroke(new BasicStroke(2));
                g2.setColor(desbloqueado ? COLOR_ORO : new Color(200, 200, 200));
                g2.drawOval(5, 5, 70, 70);
                
            }
        };
        badge.setPreferredSize(new Dimension(80, 80));
        badge.setMaximumSize(new Dimension(80, 80));
        badge.setOpaque(false);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblNombre = new JLabel("<html><center>" + logro.getNombre() + "</center></html>");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(desbloqueado ? Color.BLACK : COLOR_TEXTO_SEC);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblPuntos = new JLabel("+" + logro.getPuntos() + " XP");
        lblPuntos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPuntos.setForeground(desbloqueado ? new Color(40, 167, 69) : COLOR_TEXTO_SEC);
        lblPuntos.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.setToolTipText("<html><div style='padding:5px; width:150px;'><b>" + logro.getNombre() + "</b><br>" + 
                           logro.getDescripcion() + "</div></html>");
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(252, 252, 252));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });
        
        card.add(badge);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblNombre);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblPuntos);
        
        return card;
    }
}