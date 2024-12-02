import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Summary extends JFrame {
    private JTextArea recordTextArea;

    public Summary() {
        super("Summary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLayout(new BorderLayout());

        recordTextArea = new JTextArea();
        recordTextArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        recordTextArea.setEditable(false); 
        recordTextArea.setOpaque(false); 
        recordTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

        JScrollPane scrollPane = new JScrollPane(recordTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        displayRecordData();
        

        JButton saveButton = new JButton("Save Total Revenue");
        saveButton.addActionListener(e -> saveTotalRevenue());
        



        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
   

    private void displayRecordData() {
        try {
            File file = new File("Summary.txt");
            Scanner scanner = new Scanner(file);

            StringBuilder recordText = new StringBuilder();
            double totalRevenue = 0.0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                recordText.append(line).append("\n");

                if (line.startsWith("Total Revenue:")) {
                    if (line.matches("Total Revenue: \\$[0-9,]+(\\.\\d{1,2})?")) {
                        String[] parts = line.split("\\$");
                        String revenueStr = parts[1].trim().replace(",", "");
                        double revenue = Double.parseDouble(revenueStr);
                        totalRevenue += revenue;
                    } else {
                        recordText.append("Invalid Total Revenue Format\n");
                    }
                }
            }
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            recordText.insert(0, getCurrentDateTime() + "\n"); 
            recordText.append("\nTotal Revenue from all sales: $").append(formatter.format(totalRevenue));

            recordTextArea.setText(recordText.toString());

            scanner.close();
        } catch (IOException e) {
            recordTextArea.setText("Record file not found.");
        }
    }

    private void saveTotalRevenue() {
        try {
            File file = new File("Summary.txt");
            Scanner scanner = new Scanner(file);
    
            double totalRevenue = 0.0;
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
    
                if (line.startsWith("Total Revenue:")) {
                    if (line.matches("Total Revenue: \\$[0-9,]+(\\.\\d{1,2})?")) {
                        String[] parts = line.split("\\$");
                        String revenueStr = parts[1].trim().replace(",", "");
                        double revenue = Double.parseDouble(revenueStr);
                        totalRevenue += revenue;
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Total Revenue Format.", "Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                }
            }
            scanner.close();
    
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            String totalRevenueFormatted = formatter.format(totalRevenue);
    
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
    
            
            FileWriter writer = new FileWriter("TotalRevenue.txt", true);
            writer.write(getCurrentDateTime() + " Total Revenue from all sales: $" + totalRevenueFormatted + "\n");
            writer.close();
    
            
            FileWriter clearSummaryWriter = new FileWriter("Summary.txt", false);
            clearSummaryWriter.write("");
            clearSummaryWriter.close();
    
            JOptionPane.showMessageDialog(this, "Total revenue saved successfully!");
            
            
            recordTextArea.setText("");
    
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving total revenue.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Summary().setVisible(true);
            }
        });
    }
}