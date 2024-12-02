import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Customer extends JFrame {
    private JTextField ID_customer;
    private JTextField name_customer;
    private JTextField surname_customer;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public Customer() {
        super("Customer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JLabel IDLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel surnameLabel = new JLabel("Surname:");
        ID_customer = new JTextField();
        name_customer = new JTextField();
        surname_customer = new JTextField();
        inputPanel.add(IDLabel);
        inputPanel.add(ID_customer);
        inputPanel.add(nameLabel);
        inputPanel.add(name_customer);
        inputPanel.add(surnameLabel);
        inputPanel.add(surname_customer);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonClickListener());
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonClickListener());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonClickListener());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonClickListener());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonClickListener());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        tableModel = new DefaultTableModel();
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Surname");

        displayCustomerData();
    }

    private void displayCustomerData() {
        try {
            File file = new File("Customer.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(", ");
                tableModel.addRow(data);
            }

            scanner.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Customer file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class AddButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String ID = ID_customer.getText().trim();
            String name = name_customer.getText().trim();
            String surname = surname_customer.getText().trim();

            if (ID.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(Customer.this, "Please enter ID, name, and surname.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(ID)) {
                    JOptionPane.showMessageDialog(Customer.this, "Customer with the same ID already exists.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String[] rowData = {ID, name, surname};
            tableModel.addRow(rowData);

            saveCustomerData();
        }
    }

    private class EditButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(Customer.this, "Please select a row to edit.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String ID = ID_customer.getText().trim();
            String name = name_customer.getText().trim();
            String surname = surname_customer.getText().trim();

            if (ID.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(Customer.this, "Please enter ID, name, and surname.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.setValueAt(ID, selectedRow, 0);
            tableModel.setValueAt(name, selectedRow, 1);
            tableModel.setValueAt(surname, selectedRow, 2);

            saveCustomerData();
        }
    }

    private class DeleteButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(Customer.this, "Please select a row to delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.removeRow(selectedRow);

            saveCustomerData();
        }
    }

    private void saveCustomerData() {
        try {
            FileWriter writer = new FileWriter("Customer.txt");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.write(", ");
                    }
                }
                writer.write("\n");
            }
            writer.close();
            JOptionPane.showMessageDialog(this, "Customer data saved successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving customer data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SaveButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveCustomerData();
        }
    }

    private class BackButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Customer().setVisible(true);
            }
        });
    }
}
