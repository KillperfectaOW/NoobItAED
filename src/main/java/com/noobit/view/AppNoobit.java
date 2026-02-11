package com.noobit.view;

import com.noobit.dao.UsuarioDAO;
import com.noobit.model.Usuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AppNoobit {

    //  CONFIGURACIÓN DE ESTILO
    private static final Color NEON_PURPLE = new Color(138, 43, 226);
    private static final Color DARK_OVERLAY = new Color(10, 5, 20, 230);
    private static final Color INPUT_BG = new Color(20, 15, 30);
    private static final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

    //Creamos el DAO
    private static UsuarioDAO dao = new UsuarioDAO();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppNoobit::mostrarLogin);
    }

    public static void mostrarLogin() {
        JFrame frame = new JFrame("NOOBIT CLIENT - LOGIN");

        //  BLOQUEAR MOVIMIENTO Y TAMAÑO
        frame.setUndecorated(true); // Quita la barra de título
        frame.setResizable(false);  // Bloquea el tamaño


        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Se centra al abrirse y ahí se queda

        // PANEL DE FONDO
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage img = ImageIO.read(getClass().getResource("/images/background.jpg"));
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(20, 0, 35));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        // Añadimos un borde al fondo para que se note dónde acaba la ventana
        backgroundPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        frame.setContentPane(backgroundPanel);

        // CAJA DE LOGIN
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setPreferredSize(new Dimension(400, 600)); // Un poco más alta para el botón Salir
        loginBox.setBackground(DARK_OVERLAY);
        loginBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NEON_PURPLE, 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // COMPONENTES
        JLabel lblLogo = cargarLogo();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUserTitle = new JLabel("USUARIO");
        lblUserTitle.setForeground(Color.CYAN);
        lblUserTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtUser = createGamerInput();

        JLabel lblPassTitle = new JLabel("CONTRASEÑA");
        lblPassTitle.setForeground(Color.CYAN);
        lblPassTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField txtPass = createGamerPassword();

        JButton btnLogin = createNeonButton("INICIAR SESIÓN");

        JLabel lblRegister = new JLabel("¿No tienes cuenta? CREAR CUENTA");
        lblRegister.setForeground(Color.LIGHT_GRAY);
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //  BOTÓN DE SALIR (NECESARIO PORQUE QUITAMOS LA X)
        JButton btnSalir = new JButton("SALIR DEL SISTEMA");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setBackground(new Color(150, 0, 0));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        // LÓGICA
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            Usuario u = dao.login(user, pass);
            if (u != null) {
                frame.dispose();
                if (u.isEsAdmin()) new VistaAdmin();
                else new VistaJugador(u);
            } else {
                JOptionPane.showMessageDialog(frame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarDialogoRegistro(frame);
            }
        });

        // AÑADIR A LA CAJA
        loginBox.add(Box.createVerticalGlue());
        loginBox.add(lblLogo);
        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));

        loginBox.add(lblUserTitle);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(txtUser);

        loginBox.add(Box.createRigidArea(new Dimension(0, 15)));

        loginBox.add(lblPassTitle);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(txtPass);

        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));
        loginBox.add(btnLogin);
        loginBox.add(Box.createRigidArea(new Dimension(0, 20)));
        loginBox.add(lblRegister);

        loginBox.add(Box.createRigidArea(new Dimension(0, 40))); // Separación
        loginBox.add(btnSalir); // Botón salir abajo

        loginBox.add(Box.createVerticalGlue());

        backgroundPanel.add(loginBox);
        frame.setVisible(true);
    }

    // MÉTODOS AUXILIARES
    private static void mostrarDialogoRegistro(JFrame parent) {
        JTextField fieldUser = new JTextField();
        JPasswordField fieldPass = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nuevo Nickname:")); panel.add(fieldUser);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Nueva Contraseña:")); panel.add(fieldPass);

        int res = JOptionPane.showConfirmDialog(parent, panel, "NUEVO PILOTO", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String u = fieldUser.getText();
            String p = new String(fieldPass.getPassword());
            if (!u.isEmpty() && !p.isEmpty()) {
                Usuario user = new Usuario();
                user.setUsername(u); user.setPassword(p); user.setEmail(u+"@noobit.com");
                try {
                    dao.guardar(user);
                    JOptionPane.showMessageDialog(parent, "¡Registrado! Loguéate ahora.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Ese usuario ya existe.");
                }
            }
        }
    }

    //Logo cremita
    private static JLabel cargarLogo() {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(AppNoobit.class.getResource("/images/logo.png"));
            Image img = icon.getImage();
            int width = 180;
            int height = (width * icon.getIconHeight()) / icon.getIconWidth();
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            label.setText("NOOBIT");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.BOLD, 40));
        }
        return label;
    }

    private static JTextField createGamerInput() {
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        f.setBackground(INPUT_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.CYAN);
        f.setHorizontalAlignment(JTextField.CENTER);
        f.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 100)));
        f.setFont(FONT_TEXT);
        return f;
    }

    private static JPasswordField createGamerPassword() {
        JPasswordField f = new JPasswordField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        f.setBackground(INPUT_BG);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.CYAN);
        f.setHorizontalAlignment(JTextField.CENTER);
        f.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 100)));
        f.setFont(FONT_TEXT);
        return f;
    }

    private static JButton createNeonButton(String text) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setBackground(NEON_PURPLE);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(NEON_PURPLE.brighter()); }
            public void mouseExited(MouseEvent e) { b.setBackground(NEON_PURPLE); }
        });
        return b;
    }
}