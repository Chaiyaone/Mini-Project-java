import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Record extends JFrame {
    private JTextArea recordTextArea;

    public Record() {
        super("Record");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400); 
        setLayout(new BorderLayout());
    
        recordTextArea = new JTextArea();
        recordTextArea.setFont(new Font("Arial", Font.PLAIN, 16)); 
        JScrollPane scrollPane = new JScrollPane(recordTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        add(scrollPane, BorderLayout.CENTER);
    
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 16)); 
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearRecordData();
            }
        });
    
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 16)); 
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRecordData();
            }
        });
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    
        displayRecordData();
    }

    private void displayRecordData() {
        try {
            File file = new File("Record.txt");
            Scanner scanner = new Scanner(file);

            StringBuilder recordText = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                recordText.append(line).append("\n");
            }
            recordTextArea.setText(recordText.toString());

            scanner.close();
        } catch (IOException e) {
            recordTextArea.setText("Record file not found.");
        }
    }

    private void clearRecordData() {
        recordTextArea.setText("");
    }

    private void saveRecordData() {
        try {
            File file = new File("Record.txt");
            FileWriter writer = new FileWriter(file);
            writer.write(recordTextArea.getText());
            writer.close();
            JOptionPane.showMessageDialog(this, "Record saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Record().setVisible(true);
            }
        });
    }
}