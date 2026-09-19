package edu.umg.programacion2.proyecto.ui;

import edu.umg.programacion2.proyecto.dao.CitaDAO;
import edu.umg.programacion2.proyecto.modelo.Cita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class VentanaPrincipal extends JFrame {

	    private final CitaDAO citaDAO;
	    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	  
	    private JTextField txtId;
	    private JTextField txtCliente;
	    private JTextField txtFechaHora;
	    private JTextField txtServicio;
	    private JTextField txtDuracion;
	    private JComboBox<String> cmbEstado;

	    private JButton btnGuardar;
	    private JButton btnActualizar;
	    private JButton btnEliminar;
	    private JButton btnLimpiar;

	    private JTable tablaCitas;
	    private DefaultTableModel tableModel;

	    public VentanaPrincipal() {
	        this.citaDAO = new CitaDAO();
	        initUI();
	        cargarCitas();
	    }

	    private void initUI() {
	        setTitle("Agenda de Citas - Variante C");
	        setSize(850, 550);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout(10, 10));

	  
	        JPanel panelForm = new JPanel(new GridBagLayout());
	        panelForm.setBorder(BorderFactory.createTitledBorder("Detalle de la Cita"));
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5, 5, 5, 5);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        txtId = new JTextField();
	        txtId.setEditable(false);
	        txtCliente = new JTextField(15);
	        txtFechaHora = new JTextField(15);
	        txtServicio = new JTextField(15);
	        txtDuracion = new JTextField(15);
	        cmbEstado = new JComboBox<>(new String[]{"pendiente", "confirmada", "cancelada"});

	        int row = 0;
	        agregarCampo(panelForm, gbc, "ID:", txtId, row++);
	        agregarCampo(panelForm, gbc, "Cliente:", txtCliente, row++);
	        agregarCampo(panelForm, gbc, "Fecha/Hora (yyyy-MM-dd HH:mm):", txtFechaHora, row++);
	        agregarCampo(panelForm, gbc, "Servicio:", txtServicio, row++);
	        agregarCampo(panelForm, gbc, "Duración (min):", txtDuracion, row++);
	        agregarCampo(panelForm, gbc, "Estado:", cmbEstado, row++);

	        // --- BOTONES ---
	        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
	        btnGuardar = new JButton("Guardar");
	        btnActualizar = new JButton("Actualizar");
	        btnEliminar = new JButton("Eliminar");
	        btnLimpiar = new JButton("Limpiar");

	        panelBotones.add(btnGuardar);
	        panelBotones.add(btnActualizar);
	        panelBotones.add(btnEliminar);
	        panelBotones.add(btnLimpiar);

	        gbc.gridx = 0;
	        gbc.gridy = row;
	        gbc.gridwidth = 2;
	        panelForm.add(panelBotones, gbc);

	        add(panelForm, BorderLayout.WEST);

	        // --- TABLA DE CITAS (Centro) ---
	        String[] columnas = {"ID", "Cliente", "Fecha / Hora", "Servicio", "Duración", "Estado"};
	        tableModel = new DefaultTableModel(columnas, 0) {
	            @Override
	            public boolean isCellEditable(int r, int c) { return false; }
	        };

	        tablaCitas = new JTable(tableModel);
	        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        JScrollPane scrollTabla = new JScrollPane(tablaCitas);
	        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Citas"));
	        add(scrollTabla, BorderLayout.CENTER);

	        // --- EVENTOS ---
	        btnGuardar.addActionListener(e -> guardarCita());
	        btnActualizar.addActionListener(e -> actualizarCita());
	        btnEliminar.addActionListener(e -> eliminarCita());
	        btnLimpiar.addActionListener(e -> limpiarFormulario());

	        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
	            if (!e.getValueIsAdjusting() && tablaCitas.getSelectedRow() != -1) {
	                cargarFilaSeleccionada();
	            }
	        });
	    }

	    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String label, Component comp, int row) {
	        gbc.gridx = 0;
	        gbc.gridy = row;
	        gbc.gridwidth = 1;
	        panel.add(new JLabel(label), gbc);

	        gbc.gridx = 1;
	        panel.add(comp, gbc);
	    }

	    private void cargarCitas() {
	        tableModel.setRowCount(0);
	        try {
	            List<Cita> citas = citaDAO.listarTodos();
	            for (Cita c : citas) {
	                tableModel.addRow(new Object[]{
	                        c.getId(),
	                        c.getCliente(),
	                        c.getFechaHora().format(formatter),
	                        c.getServicio(),
	                        c.getDuracionMinutos() + " min",
	                        c.getEstado()
	                });
	            }
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(this, "Error al cargar citas: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    private void guardarCita() {
	        try {
	            String cliente = txtCliente.getText().trim();
	            String fechaStr = txtFechaHora.getText().trim();
	            String servicio = txtServicio.getText().trim();
	            int duracion = Integer.parseInt(txtDuracion.getText().trim());
	            String estado = (String) cmbEstado.getSelectedItem();

	            if (cliente.isEmpty() || servicio.isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
	                return;
	            }

	            LocalDateTime fechaHora = LocalDateTime.parse(fechaStr, formatter);
	            Cita nueva = new Cita(cliente, fechaHora, servicio, duracion, estado);

	            citaDAO.crear(nueva);
	            JOptionPane.showMessageDialog(this, "Cita registrada correctamente.");
	            limpiarFormulario();
	            cargarCitas();
	        } catch (DateTimeParseException ex) {
	            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa: yyyy-MM-dd HH:mm (Ej: 2026-10-15 14:30)", "Error", JOptionPane.ERROR_MESSAGE);
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(this, "La duración debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(this, "Error al guardar en BD: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    private void actualizarCita() {
	        if (txtId.getText().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Selecciona una cita de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        try {
	            int id = Integer.parseInt(txtId.getText());
	            String cliente = txtCliente.getText().trim();
	            LocalDateTime fechaHora = LocalDateTime.parse(txtFechaHora.getText().trim(), formatter);
	            String servicio = txtServicio.getText().trim();
	            int duracion = Integer.parseInt(txtDuracion.getText().trim().replace(" min", ""));
	            String estado = (String) cmbEstado.getSelectedItem();

	            Cita cita = new Cita(id, cliente, fechaHora, servicio, duracion, estado);
	            if (citaDAO.actualizar(cita)) {
	                JOptionPane.showMessageDialog(this, "Cita actualizada correctamente.");
	                limpiarFormulario();
	                cargarCitas();
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    private void eliminarCita() {
	        if (txtId.getText().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Selecciona una cita de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        int id = Integer.parseInt(txtId.getText());
	        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar la cita ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            try {
	                if (citaDAO.eliminar(id)) {
	                    JOptionPane.showMessageDialog(this, "Cita eliminada.");
	                    limpiarFormulario();
	                    cargarCitas();
	                }
	            } catch (SQLException ex) {
	                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }

	    private void cargarFilaSeleccionada() {
	        int fila = tablaCitas.getSelectedRow();
	        txtId.setText(tableModel.getValueAt(fila, 0).toString());
	        txtCliente.setText(tableModel.getValueAt(fila, 1).toString());
	        txtFechaHora.setText(tableModel.getValueAt(fila, 2).toString());
	        txtServicio.setText(tableModel.getValueAt(fila, 3).toString());
	        txtDuracion.setText(tableModel.getValueAt(fila, 4).toString().replace(" min", ""));
	        cmbEstado.setSelectedItem(tableModel.getValueAt(fila, 5).toString());
	    }

	    private void limpiarFormulario() {
	        txtId.setText("");
	        txtCliente.setText("");
	        txtFechaHora.setText("");
	        txtServicio.setText("");
	        txtDuracion.setText("");
	        cmbEstado.setSelectedIndex(0);
	        tablaCitas.clearSelection();
	    }
	}
