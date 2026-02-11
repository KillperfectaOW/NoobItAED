package com.noobit.view;

import com.noobit.dao.UsuarioDAO;
import com.noobit.model.Recompensa;
import com.noobit.model.Usuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
public class VistaJugador extends JFrame {

    // COLORES
    private static final Color NEON_CYAN = new Color(0, 255, 255);
    private static final Color NEON_PINK = new Color(255, 20, 147);
    private static final Color GLASS_BG = new Color(10, 10, 25, 220); // Fondo oscuro sólido
    private static final Font FONT_BIG = new Font("Segoe UI", Font.BOLD, 36);
    private static final Font FONT_MED = new Font("Segoe UI", Font.BOLD, 18);

    private Usuario usuario;
    private UsuarioDAO dao = new UsuarioDAO();

    // Labels para actualizar
    private JLabel lblRanked, lblMarket, lblNivel;

    public VistaJugador(Usuario u) {

        this.usuario = u;
        // 1. Quitar la barra de título
        setUndecorated(true);
        // 2. Bloquear tamaño
        setResizable(false);

        setTitle("NOOBIT - " + usuario.getUsername());
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // FONDO
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage img = ImageIO.read(getClass().getResource("/images/background.jpg"));
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        background.setLayout(new BorderLayout(20, 20));
        background.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setContentPane(background);

        //  HEADER (Usuario y Nivel)
        JPanel header = new JPanel(new GridLayout(1, 2));
        header.setOpaque(false);

        JLabel lblUser = new JLabel("JUGADOR: " + usuario.getUsername().toUpperCase());
        lblUser.setFont(FONT_MED);
        lblUser.setForeground(Color.WHITE);

        lblNivel = new JLabel("RANGO: " + calcularRango());
        lblNivel.setFont(FONT_MED);
        lblNivel.setForeground(NEON_CYAN);
        lblNivel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(lblUser);
        header.add(lblNivel);
        background.add(header, BorderLayout.NORTH);

        //  ESTADÍSTICAS (CENTRO)
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        statsPanel.setOpaque(false);

        // Tarjeta Ranked
        lblRanked = crearTarjeta(statsPanel, "RANKED POINTS", usuario.getPuntosRanked(), NEON_CYAN);
        // Tarjeta Market
        lblMarket = crearTarjeta(statsPanel, "MARKET COINS", usuario.getPuntosMarket(), NEON_PINK);

        background.add(statsPanel, BorderLayout.CENTER);

        // BOTONES DE ACCIÓN (ABAJO)
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setPreferredSize(new Dimension(0, 80));

        JButton btnJugar = crearBoton("JUGAR PARTIDA", new Color(0, 150, 0)); // Verde
        JButton btnTienda = crearBoton("ABRIR TIENDA", new Color(200, 100, 0)); // Naranja
        JButton btnCerrarSesion = crearBoton("CERRAR SESIÓN", new Color(150, 0, 0));//Rojo
        JButton btnInventario = crearBoton("MIS OBJETOS", new Color(70, 0, 130));// Morado


        //Añadimos los botones a la vista con su logica
        btnPanel.add(btnJugar);
        btnPanel.add(btnTienda);
        btnPanel.add(btnInventario);
        btnPanel.add(btnCerrarSesion);

        background.add(btnPanel, BorderLayout.SOUTH);

        //Logica de inventario
        btnInventario.addActionListener(e -> abrirInventario());


        //Logica Cerrar Sesion
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            AppNoobit.mostrarLogin();
        });


        // Logica de Eventos

        btnJugar.addActionListener(e -> {
            boolean gana = Math.random() > 0.5;
            if (gana) {
                int pts = 20 + (int)(Math.random() * 11);
                usuario.setPuntosRanked(usuario.getPuntosRanked() + pts);
                usuario.setPuntosMarket(usuario.getPuntosMarket() + 10);

                // Muestra el mensaje
                JOptionPane.showMessageDialog(this, "¡VICTORIA! \n+" + pts + " Ranked \n+10 Monedas");
            } else {
                int actuales = usuario.getPuntosRanked();
                usuario.setPuntosRanked(Math.max(0, actuales - 20));

                // Muestra el mensaje
                JOptionPane.showMessageDialog(this, "DERROTA... \n-20 Ranked Points", "Resultado", JOptionPane.ERROR_MESSAGE);
            }

            guardarYActualizar();


            this.revalidate(); // Recalcula la posición de las cosas
            this.repaint();    // BORRA los fantasmas y vuelve a pintar el fondo
        });

        // Boton Tienda
        btnTienda.addActionListener(e -> abrirTienda());

        setVisible(true);
    }

    //metodo para abrir el inventario

    private void abrirInventario() {
        List<Recompensa> items = dao.obtenerInventarioUsuario(usuario.getId());

        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No has comprado ningún objeto todavía.",
                    "Inventario vacío",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("TUS OBJETOS:\n\n");

        for (Recompensa r : items) {
            sb.append("• ").append(r.getNombre()).append("\n");
        }

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Inventario",
                JOptionPane.INFORMATION_MESSAGE);

        this.revalidate();
        this.repaint();
    }


    //  MÉTODOS DE LA TIENDA
    private void abrirTienda() {
        List<Recompensa> items = dao.obtenerRecompensas();

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La tienda está vacía. Pide al Admin que añada cosas.");
            this.repaint(); // <--- Limpiar por si acaso
            return;
        }

        // Crear un array de Strings para mostrar en el selector
        String[] opciones = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            Recompensa r = items.get(i);
            opciones[i] = r.getNombre() + " - [Precio: " + r.getPrecio() + "]";
        }

        // Mostrar selector
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Tu saldo: " + usuario.getPuntosMarket() + "\nElige un objeto:",
                "MARKETPLACE",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            // Buscar cual selecciono
            for (Recompensa r : items) {
                if (seleccion.startsWith(r.getNombre())) {
                    comprarObjeto(r);
                    break;
                }
            }
        }

        // Forzar limpieza al cerrar el selector de items
        this.revalidate();
        this.repaint();
    }

    private void comprarObjeto(Recompensa r) {
        if (usuario.getPuntosMarket() >= r.getPrecio()) {
            // Restar dinero
            usuario.setPuntosMarket(usuario.getPuntosMarket() - r.getPrecio());
            guardarYActualizar();
            JOptionPane.showMessageDialog(this, "¡COMPRA EXITOSA!\nHas adquirido: " + r.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "¡SALDO INSUFICIENTE!\nTe faltan " + (r.getPrecio() - usuario.getPuntosMarket()) + " monedas.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        //  Forzar limpieza al cerrar el mensaje de compra/error
        this.revalidate();
        this.repaint();
    }

    private void guardarYActualizar() {
        dao.guardar(usuario);
        lblRanked.setText(String.valueOf(usuario.getPuntosRanked()));
        lblMarket.setText(String.valueOf(usuario.getPuntosMarket()));
        lblNivel.setText("RANGO: " + calcularRango());
    }

    //Calculamos el Rango
    private String calcularRango() {
        int p = usuario.getPuntosRanked();
        if (p < 100) return "LVL 1";
        if (p < 200) return "LVL 2";
        if (p < 300) return "LVL 3";
        if (p < 400) return "LVL 4";
        if (p < 500) return "LVL 5";
        if (p < 600) return "LVL 6";
        if (p < 700) return "LVL 7";
        if (p < 800) return "LVL 8";
        if (p < 900) return "LVL 9";
        if (p < 1000) return "LVL 10";
        return "Pro Player";
    }

    //  Diseño
    private JLabel crearTarjeta(JPanel parent, String titulo, int valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GLASS_BG);
        panel.setBorder(new LineBorder(color, 2));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(color);
        lblTitulo.setFont(FONT_MED);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        JLabel lblValor = new JLabel(String.valueOf(valor), SwingConstants.CENTER);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(FONT_BIG);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        parent.add(panel);

        return lblValor; // Devolvemos el label del número para poder actualizarlo
    }

    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_MED);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Color.WHITE, 1));
        return btn;
    }
}