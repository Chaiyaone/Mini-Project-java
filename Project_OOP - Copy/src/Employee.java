import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Employee extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField ID_employee;
    private JTextField name_employee;
    private JTextField surname_employee;
    private int selectedRow = -1; // Store the selected row index

    public Employee() {
        super("Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Surname", "Edit", "Delete"}, 0);
        employeeTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JLabel IDLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel surnameLabel = new JLabel("Surname:");
        ID_employee = new JTextField();
        name_employee = new JTextField();
        surname_employee = new JTextField();
        inputPanel.add(IDLabel);
        inputPanel.add(ID_employee);
        inputPanel.add(nameLabel);
        inputPanel.add(name_employee);
        inputPanel.add(surnameLabel);
        inputPanel.add(surname_employee);
        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonClickListener());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonClickListener());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonClickListener());
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add Edit button to each row of the table
        TableColumn editColumn = employeeTable.getColumnModel().getColumn(3);
        editColumn.setCellRenderer(new ButtonRenderer());
        editColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        displayEmployeeData();
    }

    private void displayEmployeeData() {
        try {
            File file = new File("Employee.txt");
            Scanner scanner = new Scanner(file);

            tableModel.setRowCount(0); // Clear existing rows

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(", ");
                String[] rowData = new String[data.length + 2];
                System.arraycopy(data, 0, rowData, 0, data.length);
                rowData[data.length] = "Edit";
                rowData[data.length + 1] = "Delete";
                tableModel.addRow(rowData);
            }
            TableColumn deleteColumn = employeeTable.getColumnModel().getColumn(4);
            deleteColumn.setCellRenderer(new DeleteButtonRenderer());
            deleteColumn.setCellEditor(new DeleteButtonEditor(new JCheckBox()));
            scanner.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Employee.this, "Employee file not found.");
        }
    }

    private class AddButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String ID = ID_employee.getText().trim();
            String name = name_employee.getText().trim();
            String surname = surname_employee.getText().trim();

            if (ID.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(Employee.this, "Please enter ID, name, and surname.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Add new row to the table
                tableModel.addRow(new String[]{ID, name, surname, "Edit"});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Employee.this, "Invalid ID format.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveEmployeeData() {
        try {
            FileWriter writer = new FileWriter("Employee.txt");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount() - 1; j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 2) {
                        writer.write(", ");
                    }
                }
                writer.write("\n");
            }
            writer.close();
            JOptionPane.showMessageDialog(Employee.this, "Employee data saved successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(Employee.this, "Error saving employee data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SaveButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveEmployeeData();
            ((ButtonEditor)employeeTable.getCellEditor()).fireEditingStopped();
        }
    }

    private class BackButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose(); 
        }
    }

    
    class ButtonRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton("Edit");
            return button;
        }
    }

   
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row; 
            
            ID_employee.setText(tableModel.getValueAt(row, 0).toString());
            name_employee.setText(tableModel.getValueAt(row, 1).toString());
            surname_employee.setText(tableModel.getValueAt(row, 2).toString());
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
            tableModel.setValueAt(ID_employee.getText(), selectedRow, 0);
            tableModel.setValueAt(name_employee.getText(), selectedRow, 1);
            tableModel.setValueAt(surname_employee.getText(), selectedRow, 2);
        }
    }
    
    class DeleteButtonRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton("Delete");
            return button;
        }
    }
    
    
    class DeleteButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
    
        public DeleteButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }
    
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row; 
            ID_employee.setText(tableModel.getValueAt(row, 0).toString());
            name_employee.setText(tableModel.getValueAt(row, 1).toString());
            surname_employee.setText(tableModel.getValueAt(row, 2).toString());
            return button;
        }
    
        public Object getCellEditorValue() {
            if (isPushed) {
                
            }
            isPushed = false;
            return label;
        }
    
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    
        protected void fireEditingStopped() {
            super.fireEditingStopped();
            
            tableModel.removeRow(selectedRow);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Employee().setVisible(true);
            }
        });
    }
}