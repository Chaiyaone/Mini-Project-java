import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;



public class Shop extends JFrame implements ActionListener {
    JButton btnOption[], menu[], supplyEdit[], Confirm, Cancel, add_Supply;
    JTextArea Cart_Display, Menu_Display;
    JTextField Total, employeeField, customerField;
    String EmployeeStr[], CustomerStr[];
    JLabel Text_Total;
    Container c = getContentPane();
    private JComboBox<String> employeeComboBox;
    private JComboBox<String> customerComboBox;

    JPanel Option, Cart, Cart2;
    DecimalFormat twodigit = new DecimalFormat("##,##0.00");
    ArrayList<String> cartItems = new ArrayList<>();
    String output = "Product\tPrice\tQuantity\n=================================\n";
    private JTable productTable;
    // Sub JPanel
    Font font = new Font("Tahoma", Font.BOLD, 12);
    Font font2 = new Font("Tahoma", Font.BOLD, 20);
    Font font3 = new Font("Tahoma", Font.BOLD, 15);

    public Shop() {
        super("TSC Store");
        c.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Option();
        CartDisplay();
        loadEmployeeData(); 
        loadCustomerData();
        JButton summaryOfDayButton = new JButton("Summary of Day");
        summaryOfDayButton.addActionListener(e -> openSummaryOfDay());
        Option.add(summaryOfDayButton);
        
        setSize(700, 754);
        setResizable(false);
        setVisible(true);
        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void CartDisplay() {
        Cart = new JPanel();
        Cart.setPreferredSize(new Dimension(624, 544));
        Cart.setBorder(new LineBorder(Color.RED, 1));
        Cart.setBackground(Color.WHITE);

        Cart_Display = new JTextArea();
        Cart_Display.setPreferredSize(new Dimension(400, 400));
        Cart_Display.setBackground(Color.LIGHT_GRAY);
        Cart_Display.setFont(font3);
        Cart_Display.setText(output);
        Cart_Display.setForeground(Color.BLUE);

        Cart.add(Cart_Display);
        CartDisplay2();
        c.add(Cart);
    }

    public void CartDisplay2() {
        Cart2 = new JPanel();
        Cart2.setPreferredSize(new Dimension(460, 125));
        Cart2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        Text_Total = new JLabel("Total : ");
        Text_Total.setFont(font2);

        Total = new JTextField();
        Total.setPreferredSize(new Dimension(100, 50));
        Total.setEditable(false);
        Total.setFont(font3);
        Total.setText(twodigit.format(0));
        Total.setBackground(Color.GREEN);

        Confirm = new JButton("CONFIRM");
        Cancel = new JButton("CANCEL");
        add_Supply = new JButton("ADD");

        add_Supply.setPreferredSize(new Dimension(270, 50));
        Confirm.setPreferredSize(new Dimension(280, 50));
        Cancel.setPreferredSize(new Dimension(170, 50));

        add_Supply.setFont(font3);
        Confirm.setFont(font3);
        Cancel.setFont(font3);

        Confirm.setBackground(Color.GREEN);
        Cancel.setBackground(Color.RED);

        Confirm.setFocusPainted(false);
        Cancel.setFocusPainted(false);

        Confirm.addActionListener(this);
        Cancel.addActionListener(this);
        add_Supply.addActionListener(this);

        displayTotal();

        Cart2.add(Text_Total);
        Cart2.add(Total);
        Cart2.add(add_Supply);
        Cart2.add(Confirm);
        Cart2.add(Cancel);

        Cart.add(Cart2);

    }  
    private void openSummaryOfDay() {
        try {
            File file = new File("TotalRevenue.txt");
            Scanner scanner = new Scanner(file);
    
            StringBuilder summaryData = new StringBuilder();
            while (scanner.hasNextLine()) {
                summaryData.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
    
            JTextArea summaryTextArea = new JTextArea(summaryData.toString());
            summaryTextArea.setEditable(false);
    
            JScrollPane scrollPane = new JScrollPane(summaryTextArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
    
            JOptionPane.showMessageDialog(this, scrollPane, "Summary of Day", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading summary data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


     
    public void Option() {
        Option = new JPanel();
        Option.setPreferredSize(new Dimension(624, 100));
        Option.setBorder(new LineBorder(Color.RED, 1));
        Option.setBackground(Color.WHITE);
        
        JLabel employeeLabel = new JLabel("Employee ID:");
        employeeComboBox = new JComboBox<>();
        JLabel customerLabel = new JLabel("Customer ID:");
        customerComboBox = new JComboBox<>();
        
        String text[] = { "Supply", "Record", "Employee Info", "Customer Info","Summary", "Close Program", "Upload data" };
        btnOption = new JButton[text.length];
        for (int n = 0; n < text.length; n++) {
            btnOption[n] = new JButton(text[n]);
            btnOption[n].setFont(font);
            btnOption[n].setFocusPainted(false);
            btnOption[n].addActionListener(this);
            Option.add(btnOption[n]);
        }
        
        Option.add(employeeLabel);
        Option.add(employeeComboBox);
        Option.add(customerLabel);
        Option.add(customerComboBox);
        
        c.add(Option);
    }
    


    public void displayTotal() {
        double totalPrice = 0.0;
        for (String item : cartItems) {
            String[] parts = item.split("\t");
            double price = Double.parseDouble(parts[1]);
            int quantity = Integer.parseInt(parts[2]);
            totalPrice += price * quantity;
        }
        Total.setText(twodigit.format(totalPrice));
    }
    private void loadEmployeeData() {
        try {
            File file = new File("Employee.txt");
            Scanner scanner = new Scanner(file);
    
            ArrayList<String> employeeList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                employeeList.add(parts[0]); 
            }
            EmployeeStr = employeeList.toArray(new String[0]);
            scanner.close();
            
            updateEmployeeComboBox();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadCustomerData() {
        try {
            File file = new File("Customer.txt");
            Scanner scanner = new Scanner(file);
    
            ArrayList<String> customerList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                customerList.add(parts[0]); 
            }
            CustomerStr = customerList.toArray(new String[0]);
            scanner.close();
            
            updateCustomerComboBox();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void updateEmployeeComboBox() {
        employeeComboBox.removeAllItems();
        for (String id : EmployeeStr) {
            employeeComboBox.addItem(id);
        }
    }
    
    private void updateCustomerComboBox() {
        customerComboBox.removeAllItems();
        for (String id : CustomerStr) {
            customerComboBox.addItem(id);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOption[5])
            System.exit(1);
        else if(e.getSource() == btnOption[4]){
            Summary summary = new Summary();
            summary.setVisible(true);
        }
        else if(e.getSource() == btnOption[6]) 
            restartProgram();
        else if (e.getSource() == btnOption[3]) {
            Customer customer = new Customer();
            customer.setVisible(true);
        } else if (e.getSource() == btnOption[2]) {
            Employee employee = new Employee();
            employee.setVisible(true);
        } else if (e.getSource() == btnOption[1]) {
            Record record = new Record();
            record.setVisible(true);
        } else if (e.getSource() == btnOption[0]) {
            Supply supply = new Supply();
            supply.setVisible(true);
        } else if (e.getSource() == Confirm) {
            String employeeID = (String) employeeComboBox.getSelectedItem();
            String customerID = (String) customerComboBox.getSelectedItem();
    
            reduceStock();
            saveTransaction(employeeID, customerID);
            saveSummary();
    
            Cart_Display.setText(output);
            Total.setText(twodigit.format(0));
            cartItems.clear();
        
        } else if (e.getSource() == Cancel) {
            Cart_Display.setText(output);
            Total.setText(twodigit.format(0));
            cartItems.clear();
        } else if (e.getSource() == add_Supply) {
            displayProductTable();
        }
    }
   
    public boolean checkProductExists(String productName) {
        try {
            File file = new File("Supply.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                if (parts[0].trim().equalsIgnoreCase(productName)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void restartProgram() {
        dispose();
        new Shop();
    }

    private void reduceStock() {
        try {

            File file = new File("Supply.txt");
            Scanner scanner = new Scanner(file);

            ArrayList<String> updatedStock = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                String productName = parts[0].trim();
                double price = Double.parseDouble(parts[1].trim());
                int quantity = Integer.parseInt(parts[2].trim());

                for (String item : cartItems) {
                    String[] cartItem = item.split("\t");
                    String cartProductName = cartItem[0];
                    int cartQuantity = Integer.parseInt(cartItem[2]);

                    if (productName.equalsIgnoreCase(cartProductName)) {
                        quantity -= cartQuantity;
                        break;
                    }
                }

                String updatedLine = productName + ", " + price + ", " + quantity;
                updatedStock.add(updatedLine);
            }
            scanner.close();

            FileWriter writer = new FileWriter("Supply.txt");
            for (String stock : updatedStock) {
                writer.write(stock + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void saveSummary() {
        try {
            FileWriter writer = new FileWriter("Summary.txt", true);
    
            writer.write("Summary of Sales\n");
            writer.write("=========================================\n");
    
            int totalItems = 0;
            double totalPrice = 0.0;
    
            for (String item : cartItems) {
                String[] parts = item.split("\t");
                String productName = parts[0];
                int quantity = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[1]);
                totalItems += quantity;
                totalPrice += price * quantity;
    
                
                String productInfo = getProductInfo(productName);
                writer.write("Product: " + productInfo + "\n");
            }
    
            writer.write("Total Items Sold: " + totalItems + "\n");
            writer.write("Total Revenue: $" + twodigit.format(totalPrice) + "\n");
    
            writer.write("\n"+"=========================================\n\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private String getProductInfo(String productName) {
        String productInfo = "";
        try {
            File file = new File("Supply.txt");
            Scanner scanner = new Scanner(file);
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                if (parts[0].trim().equalsIgnoreCase(productName)) {
                    productInfo = line; 
                    break;
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productInfo;
    }
    
    private void saveTransaction(String employeeID, String customerID) {
        String employeeName = getEmployeeName(employeeID);
        String customerName = getCustomerName(customerID);
        try {
            FileWriter writer = new FileWriter("Record.txt", true);
    
            writer.write("Employee ID: " + employeeID + "\n");
            writer.write("Employee Name: " + employeeName + "\n");
            writer.write("Customer ID: " + customerID + "\n");
            writer.write("Customer Name: " + customerName + "\n");
            writer.write("=========================================\n");
            writer.write("|          name        |         price          |             Qr           |\n");
            writer.write("=========================================\n");
    
            for (String item : cartItems) {
                writer.write(" "+ item + "\n");
            }
    
            writer.write("\nTotal: " + Total.getText() + " Bath."+"\n");
            writer.write("=========================================\n\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public double getPriceByName(String productName) {
        try {
            File file = new File("Supply.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length >= 3 && parts[0].trim().equalsIgnoreCase(productName)) {
                    return Double.parseDouble(parts[1].trim());
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    private String getEmployeeName(String employeeID) {
        try {
            File employeeFile = new File("Employee.txt");
            Scanner employeeScanner = new Scanner(employeeFile);
            while (employeeScanner.hasNextLine()) {
                String line = employeeScanner.nextLine();
                String[] parts = line.split(", ");
                if (parts[0].trim().equals(employeeID)) {
                    employeeScanner.close();
                    return parts[1].trim(); 
                }
            }
            employeeScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "N/A"; 
    }
    private String getCustomerName(String customerID) {
        try {
            File customerFile = new File("Customer.txt");
            Scanner customerScanner = new Scanner(customerFile);
            while (customerScanner.hasNextLine()) {
                String line = customerScanner.nextLine();
                String[] parts = line.split(", ");
                if (parts[0].trim().equals(customerID)) {
                    customerScanner.close();
                    return parts[1].trim(); 
                }
            }
            customerScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "N/A"; 
    }
    private void displayProductTable() {
        JFrame frame = new JFrame("Product Selection");
        frame.setLayout(new BorderLayout());
    
        // สร้างตารางโมเดล
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Stock");
    
        
        try {
            File file = new File("Supply.txt");
            Scanner scanner = new Scanner(file);
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                model.addRow(new Object[]{parts[0], parts[1], parts[2]});
            }
            scanner.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    
        productTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(productTable);
    
        JButton selectButton = new JButton("Select Product");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productName = (String) productTable.getValueAt(selectedRow, 0);
                    double price = Double.parseDouble((String) productTable.getValueAt(selectedRow, 1));
    
                    String quantityStr = JOptionPane.showInputDialog(Shop.this, "Enter the quantity:");
                    if (quantityStr != null) {
                        int quantity = Integer.parseInt(quantityStr);
                        int currentStock = Integer.parseInt((String) productTable.getValueAt(selectedRow, 2));
    
                        if (quantity > currentStock) {
                            JOptionPane.showMessageDialog(Shop.this, "Not enough stock available.");
                        } else {
                            cartItems.add(productName + "\t" + price + "\t" + quantity);
                            StringBuilder cartOutput = new StringBuilder(
                                    "Product\tPrice\tQuantity\n=================================\n");
                            for (String item : cartItems) {
                                cartOutput.append(item).append("\n");
                            }
                            Cart_Display.setText(cartOutput.toString());
                            displayTotal();
                            frame.dispose(); 
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(Shop.this, "Please select a product.");
                }
            }
        });
    
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(selectButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Shop();
    }
}