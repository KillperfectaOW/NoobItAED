package com.noobit.view;

import com.noobit.dao.UsuarioDAO;
import com.noobit.model.Recompensa;
import com.noobit.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
public class VistaAdmin extends JFrame {

    private UsuarioDAO dao = new UsuarioDAO();
    private DefaultTableModel modeloTabla;
    private JTable tablaUsuarios;

    public VistaAdmin() {

        // 1. Quitar la barra de título (Cerrar, Minimizar, Maximizar)
        setUndecorated(true);
        // 2. Bloquear que el usuario estire la ventana con el ratón
        setResizable(false);

        setTitle("PANEL DE ADMINISTRADOR - NOOBIT");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // PESTAÑAS
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("👥 Gestión Usuarios", crearPanelUsuarios());
        tabs.addTab("🛒 Añadir al Market", crearPanelMarket());
        tabs.addTab(" Cerrar Sesion", crearPanelCerrarSesion());

        add(tabs);
        setVisible(true);
    }

    //Boton para cerrar sesion
    private JPanel crearPanelCerrarSesion() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton btnCerrarSesion = new JButton("VOLVER AL LOGIN");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCerrarSesion.setBackground(Color.RED);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);

        btnCerrarSesion.addActionListener(e -> {
            dispose(); // cerrar ventana admin
            AppNoobit.mostrarLogin(); // volver al login
        });

        JPanel panelCentro = new JPanel();
        panelCentro.add(btnCerrarSesion);

        panel.add(panelCentro, BorderLayout.CENTER);

        return panel;
    }


    // GESTIÓN DE USUARIOS
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabla de datos
        String[] columnas = {"ID", "Usuario", "Ranked Pts", "Market Pts", "Es Admin"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaUsuarios = new JTable(modeloTabla);
        cargarUsuarios();

        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        // Botonera
        JPanel panelBotones = new JPanel();
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnModificar = new JButton("Modificar Puntos");
        JButton btnBorrar = new JButton("BORRAR USUARIO"); // <--- NUEVO BOTÓN
        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN"); // <-- NUEVO BOTÓN

        //Colores de Elimiar
        btnBorrar.setBackground(Color.RED);
        btnBorrar.setForeground(Color.WHITE);

        //Colores de cerrar sesion
        btnCerrarSesion.setBackground(new Color(150, 0, 0));
        btnCerrarSesion.setForeground(Color.WHITE);

        btnRefrescar.addActionListener(e -> cargarUsuarios());

        // Lógica Modificar
        btnModificar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
                return;
            }
            int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
            Usuario u = dao.buscarPorId(idUsuario);

            if (u != null) {
                String input = JOptionPane.showInputDialog(this,
                        "Usuario: " + u.getUsername() + "\nNuevos puntos Ranked:",
                        u.getPuntosRanked());
                if (input != null) {
                    try {
                        u.setPuntosRanked(Integer.parseInt(input));
                        dao.guardar(u);
                        cargarUsuarios();
                    } catch (Exception ex) { /* Ignorar */ }
                }
            }
        });

        // Lógica Borrar
        btnBorrar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona a quién quieres banear.");
                return;
            }

            int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
            String nombre = (String) modeloTabla.getValueAt(fila, 1);

            // Confirmación de seguridad
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quieres eliminar a " + nombre + "?\nEsta acción no se puede deshacer.",
                    "CONFIRMAR BORRADO", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminarUsuario(idUsuario); // Llamada al DAO
                cargarUsuarios(); // Refrescar tabla
                JOptionPane.showMessageDialog(this, "Usuario eliminado del sistema.");
            }
        });

        // LÓGICA CERRAR SESIÓN
        btnCerrarSesion.addActionListener(e -> {
            dispose();                 // Cierra ventana admin
            AppNoobit.mostrarLogin();  // Vuelve al login
        });

        //Añadimos los botones y los llamamos
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnCerrarSesion);

        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    //  AÑADIR AL MARKET
    private JPanel crearPanelMarket() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JTextField txtNombre = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtPrecio = new JTextField();
        JButton btnGuardar = new JButton("CREAR ARTÍCULO");

        panel.add(new JLabel("Nombre del Artículo:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripción:")); panel.add(txtDesc);
        panel.add(new JLabel("Precio (Market Pts):")); panel.add(txtPrecio);
        panel.add(new JLabel("")); panel.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String desc = txtDesc.getText();
                int precio = Integer.parseInt(txtPrecio.getText());

                Recompensa r = new Recompensa(nombre, desc, precio);
                dao.guardar(r);

                JOptionPane.showMessageDialog(this, "¡Artículo '" + nombre + "' añadido!");
                txtNombre.setText(""); txtDesc.setText(""); txtPrecio.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: El precio debe ser un número.");
            }
        });

        return panel;
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = dao.listarUsuarios();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                    u.getId(), u.getUsername(), u.getPuntosRanked(), u.getPuntosMarket(), u.isEsAdmin() ? "SÍ" : "NO"
            });
        }
    }
}