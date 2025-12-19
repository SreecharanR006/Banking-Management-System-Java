import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class BankingAppGUI extends JFrame {

    // -------- SAFE FONTS (WORK EVERYWHERE) --------
    private static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final Font MONO_FONT = new Font("Monospaced", Font.PLAIN, 13);

    private JTextField accNoField, nameField, amountField;
    private JTextArea outputArea;
    private double balance = 0.0;

    private static final String FILE_NAME = "bank_data.txt";

    public BankingAppGUI() {
        setTitle("Banking Management System");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        add(createHeader(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createOutputPanel(), BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadData();
    }

    // -------- HEADER --------
    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(33, 150, 243));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Banking Management System");
        title.setFont(TITLE_FONT);
        title.setForeground(Color.WHITE);

        panel.add(title);
        return panel;
    }

    // -------- FORM PANEL --------
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Account Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        accNoField = new JTextField(15);
        nameField = new JTextField(15);
        amountField = new JTextField(15);

        accNoField.setFont(MAIN_FONT);
        nameField.setFont(MAIN_FONT);
        amountField.setFont(MAIN_FONT);

        addField(panel, gbc, "Account Number:", accNoField, 0);
        addField(panel, gbc, "Account Holder Name:", nameField, 1);
        addField(panel, gbc, "Amount (₹):", amountField, 2);

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc,
                          String labelText, JTextField field, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(MAIN_FONT);

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // -------- OUTPUT PANEL --------
    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Transaction Output"));

        outputArea = new JTextArea(12, 24);
        outputArea.setFont(MONO_FONT);
        outputArea.setEditable(false);

        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return panel;
    }

    // -------- BUTTON PANEL --------
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton createBtn = createButton("Create Account");
        JButton depositBtn = createButton("Deposit");
        JButton withdrawBtn = createButton("Withdraw");
        JButton viewBtn = createButton("View Details");

        createBtn.addActionListener(e -> createAccount());
        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
        viewBtn.addActionListener(e -> viewDetails());

        panel.add(createBtn);
        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(viewBtn);

        return panel;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(MAIN_FONT);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        return btn;
    }

    // -------- LOGIC --------
    private void createAccount() {
        balance = 0;
        outputArea.setText("✔ Account created successfully\n");
        saveData();
    }

    private void deposit() {
        try {
            double amt = Double.parseDouble(amountField.getText());
            balance += amt;
            outputArea.append("Deposited: ₹" + amt + "\n");
            saveData();
        } catch (Exception e) {
            showError("Enter a valid amount");
        }
    }

    private void withdraw() {
        try {
            double amt = Double.parseDouble(amountField.getText());
            if (amt > balance) {
                showError("Insufficient balance");
                return;
            }
            balance -= amt;
            outputArea.append("Withdrawn: ₹" + amt + "\n");
            saveData();
        } catch (Exception e) {
            showError("Enter a valid amount");
        }
    }

    private void viewDetails() {
        outputArea.setText(
                "Account Number : " + accNoField.getText() + "\n" +
                "Account Holder : " + nameField.getText() + "\n" +
                "Current Balance: ₹" + balance + "\n"
        );
    }

    // -------- FILE HANDLING --------
    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println(accNoField.getText());
            pw.println(nameField.getText());
            pw.println(balance);
        } catch (IOException e) {
            showError("Error saving data");
        }
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            accNoField.setText(br.readLine());
            nameField.setText(br.readLine());
            balance = Double.parseDouble(br.readLine());
        } catch (Exception ignored) {}
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // -------- MAIN --------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingAppGUI().setVisible(true));
    }
}
