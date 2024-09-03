import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class FormularioCliente extends JFrame {
    private JTextField nitField, nombreField, apellidoField, direccionField, telefonoField, sueldoField, bonificacionField;
    private JSpinner fechaNacimientoSpinner;
    private JTable table;
    private DefaultTableModel tableModel;

    // Configuración de la base de datos (para agregar después)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/formulario";
    private static final String USER = "root";  // Cambia esto por tu nombre de usuario de MySQL
    private static final String PASS = "andersonrey123";    
    // Cambia esto por tu contraseña de MySQL

    public FormularioCliente() {
        setTitle("Formulario de Cliente");
        setSize(1000, 600);  // Aumentado el tamaño para ajustar la tabla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GroupLayout layout = new GroupLayout(formPanel);    
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Crear los componentes del formulario
        JLabel nitLabel = new JLabel("NIT:");
        nitField = new JTextField(30);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(20);

        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoField = new JTextField(20);

        JLabel direccionLabel = new JLabel("Dirección:");
        direccionField = new JTextField(20);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoField = new JTextField(20);

        JLabel fechaNacimientoLabel = new JLabel("Fecha de Nacimiento:");
        fechaNacimientoSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaNacimientoSpinner, "dd/MM/yyyy");
        fechaNacimientoSpinner.setEditor(dateEditor);

        JLabel sueldoLabel = new JLabel("Sueldo:");
        sueldoField = new JTextField(20);

        JLabel bonificacionLabel = new JLabel("Bonificación:");
        bonificacionField = new JTextField(20);

        JButton saveButton = new JButton("Guardar");
        JButton deleteButton = new JButton("Eliminar");
        JButton selectButton = new JButton("Seleccionar");

        // Crear tabla para mostrar los registros
        tableModel = new DefaultTableModel(new Object[]{"NIT", "Nombre", "Apellido", "Dirección", "Teléfono", "Fecha de Nacimiento", "Sueldo", "Bonificación"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarRegistro();
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarRegistro();
            }
        });

        // Layout settings for the form
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nitLabel)
                .addComponent(nombreLabel)
                .addComponent(apellidoLabel)
                .addComponent(direccionLabel)
                .addComponent(telefonoLabel)
                .addComponent(fechaNacimientoLabel)
                .addComponent(sueldoLabel)
                .addComponent(bonificacionLabel)
                .addComponent(saveButton)
                .addComponent(deleteButton)
                .addComponent(selectButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nitField)
                .addComponent(nombreField)
                .addComponent(apellidoField)
                .addComponent(direccionField)
                .addComponent(telefonoField)
                .addComponent(fechaNacimientoSpinner)
                .addComponent(sueldoField)
                .addComponent(bonificacionField)
                .addComponent(tableScrollPane))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nitLabel)
                .addComponent(nitField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nombreLabel)
                .addComponent(nombreField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(apellidoLabel)
                .addComponent(apellidoField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(direccionLabel)
                .addComponent(direccionField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(telefonoLabel)
                .addComponent(telefonoField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(fechaNacimientoLabel)
                .addComponent(fechaNacimientoSpinner))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(sueldoLabel)
                .addComponent(sueldoField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(bonificacionLabel)
                .addComponent(bonificacionField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(saveButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(deleteButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(selectButton))
            .addComponent(tableScrollPane)
        );

        add(formPanel);
    }

    private void guardarDatos() {
        String nit = nitField.getText();
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        java.util.Date fechaNacimiento = (java.util.Date) fechaNacimientoSpinner.getValue();
        double sueldo = Double.parseDouble(sueldoField.getText());
        double bonificacion = Double.parseDouble(bonificacionField.getText());

        java.sql.Date sqlFechaNacimiento = new java.sql.Date(fechaNacimiento.getTime());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO clientes ( nombre, apellido, direccion, telefono, fecha_nacimiento, sueldo, bonificacion, nit) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nit);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, direccion);
            pstmt.setString(5, telefono);
            pstmt.setDate(6, sqlFechaNacimiento);
            pstmt.setDouble(7, sueldo);
            pstmt.setDouble(8, bonificacion);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Datos guardados exitosamente.");
            // Agregar el nuevo registro a la tabla
            tableModel.addRow(new Object[]{nit, nombre, apellido, direccion, telefono, sqlFechaNacimiento, sueldo, bonificacion});
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + ex.getMessage());
        }
    }

    private void eliminarRegistro() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Aquí deberías implementar la lógica para eliminar el registro de la base de datos
            // Para eliminar el registro de la tabla
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro para eliminar.");
        }
    }

    private void seleccionarRegistro() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Aquí podrías cargar los datos del registro seleccionado en los campos del formulario
            nitField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nombreField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            apellidoField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            direccionField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            telefonoField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            fechaNacimientoSpinner.setValue(tableModel.getValueAt(selectedRow, 5));
            sueldoField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            bonificacionField.setText(tableModel.getValueAt(selectedRow, 7).toString());
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro para editar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormularioCliente().setVisible(true);
            }
        });
    }
}
