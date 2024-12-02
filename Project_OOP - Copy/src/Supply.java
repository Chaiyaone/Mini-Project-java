import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Supply extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private DefaultTableModel productTableModel;
    private JTable productTable;

    public Supply() {
        super("Supply");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel nameLabel = new JLabel("Name Product:");
        JLabel priceLabel = new JLabel("Price: ");
        JLabel quantityLabel = new JLabel("Quantity: ");
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonClickListener());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonClickListener());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonClickListener());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonClickListener());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        initProductTable();

        JScrollPane scrollPane = new JScrollPane(productTable);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        displayProductData();
    }

    private void initProductTable() {
        productTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Allow editing for all columns except the first one (product name)
            }
        };
        productTableModel.addColumn("Name Product");
        productTableModel.addColumn("Price");
        productTableModel.addColumn("Quantity");

        productTable = new JTable(productTableModel);
    }

    private void displayProductData() {
        productTableModel.setRowCount(0); // Clear existing rows
        try {
            File file = new File("Supply.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length >= 3) {
                    String productName = parts[0].trim();
                    String price = parts[1].trim();
                    String quantity = parts[2].trim();
                    productTableModel.addRow(new Object[]{productName, price, quantity});
                }
            }
            scanner.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Product file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class AddButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(Supply.this, "Please enter name, price, and quantity.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                // Check if the product already exists in the table
                boolean found = false;
                for (int i = 0; i < productTableModel.getRowCount(); i++) {
                    if (name.equalsIgnoreCase(productTableModel.getValueAt(i, 0).toString())) {
                        int existingQuantity = Integer.parseInt(productTableModel.getValueAt(i, 2).toString());
                        productTableModel.setValueAt(existingQuantity + quantity, i, 2);
                        found = true;
                        break;
                    }
                }

                // If the product does not exist, add a new row
                if (!found) {
                    Object[] newRow = {name, price, quantity};
                    productTableModel.addRow(newRow);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Supply.this, "Invalid price or quantity format.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(Supply.this, "Please select a product to delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(Supply.this, "Are you sure you want to delete this product?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                productTableModel.removeRow(selectedRow);
            }
        }
    }

    private class SaveButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveProductData();
        }
    }

    private void saveProductData() {
        try {
            FileWriter writer = new FileWriter("Supply.txt");
            for (int i = 0; i < productTableModel.getRowCount(); i++) {
                for (int j = 0; j < productTableModel.getColumnCount(); j++) {
                    writer.write(productTableModel.getValueAt(i, j).toString());
                    if (j < productTableModel.getColumnCount() - 1) {
                        writer.write(", ");
                    }
                }
                writer.write("\n");
            }
            writer.close();
            JOptionPane.showMessageDialog(Supply.this, "Supply data saved successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(Supply.this, "Error saving product data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class BackButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose(); // Close Supply frame
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Supply().setVisible(true);
            }
        });
    }
}