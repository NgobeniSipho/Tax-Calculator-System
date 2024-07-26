import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDetailsForm extends JFrame {

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField idField;
    private JTextField emailField;
    private JTextField contactField;
    private JButton submitButton;
    private JLabel resultLabel;

    public UserDetailsForm() {
        setTitle("Tax Calculator System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the components
        JLabel headingLabel = new JLabel("User Details Form", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameField = new JTextField(20);
        surnameField = new JTextField(20);
        idField = new JTextField(20);
        emailField = new JTextField(20);
        contactField = new JTextField(20);
        submitButton = new JButton("Submit");
        resultLabel = new JLabel("");
        resultLabel.setForeground(Color.RED);

        // Set up the layout
        setLayout(new BorderLayout());

        // Panel for form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("ID Number:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);

        // Submit button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitButton);

        // Result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultPanel.setLayout(new FlowLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.add(resultLabel);

        // Add panels to the frame
        add(headingLabel, BorderLayout.NORTH);  // Add heading label
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(resultPanel, BorderLayout.NORTH);

        // Set button action listener
        submitButton.addActionListener(new SubmitButtonListener());

        // Customize button appearance
        submitButton.setBackground(new Color(0x007BFF));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String id = idField.getText();
            String email = emailField.getText();
            String contact = contactField.getText();

            if (name.isEmpty() || surname.isEmpty() || id.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                resultLabel.setText("All fields are required.");
                return;
            }

            if (!isValidID(id)) {
                resultLabel.setText("Invalid South African ID.");
                return;
            }

            int age = calculateAgeFromID(id);
            if (age < 18) {
                resultLabel.setText("User must be at least 18 years old.");
                return;
            }

            new TaxCalculator().setVisible(true);
            dispose();
        }
    }

    private boolean isValidID(String id) {
        if (id.length() != 13 || !id.matches("\\d{13}")) {
            return false;
        }

        int month = Integer.parseInt(id.substring(2, 4));
        int day = Integer.parseInt(id.substring(4, 6));
        return month >= 1 && month <= 12 && day >= 1 && day <= 31;
    }

    private int calculateAgeFromID(String id) {
        int year = Integer.parseInt(id.substring(0, 2));
        int month = Integer.parseInt(id.substring(2, 4));
        int day = Integer.parseInt(id.substring(4, 6));

        // Assume 1900-1999 for simplicity. Adjust if necessary for newer IDs.
        year = (year > LocalDate.now().getYear() % 100) ? 1900 + year : 2000 + year;

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();
        return today.getYear() - birthDate.getYear() - (today.getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserDetailsForm().setVisible(true);
            }
        });
    }
}

// The existing TaxCalculator class implementation follows here

class TaxCalculator extends JFrame {

    private JTextField incomeField;
    private JButton calculateButton;
    private JLabel resultLabel;
    private JTextArea taxBracketsArea;

    public TaxCalculator() {
        setTitle("South African Tax Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the components
        incomeField = new JTextField(20);
        calculateButton = new JButton("Calculate Tax");
        resultLabel = new JLabel("Your tax will be displayed here.");
        taxBracketsArea = new JTextArea(8, 40);
        taxBracketsArea.setEditable(false);
        taxBracketsArea.setText(getTaxBracketsInfo());

        // Set up the layout
        setLayout(new BorderLayout());

        // Top panel for the input
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel incomeLabel = new JLabel("Enter your annual income:");
        incomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(incomeLabel);
        topPanel.add(incomeField);

        // Center panel for the button
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(calculateButton);

        // Bottom panel for the result and tax brackets
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.setLayout(new BorderLayout());
        
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(resultLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(taxBracketsArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add button action listener
        calculateButton.addActionListener(new CalculateButtonListener());
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String incomeText = incomeField.getText();
            try {
                double income = Double.parseDouble(incomeText);
                double tax = calculateTax(income);
                resultLabel.setText("Your tax is: R" + String.format("%.2f", tax));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Please enter a valid number.");
            }
        }
    }

    private double calculateTax(double income) {
        double tax = 0.0;
        
        // Check if income is below the tax threshold
        if (income <= 95750) {
            return tax; // No tax due
        }

        // Example tax brackets
        if (income <= 226000) {
            tax = income * 0.18;
        } else if (income <= 353100) {
            tax = 40680 + (income - 226000) * 0.26;
        } else if (income <= 488700) {
            tax = 73726 + (income - 353100) * 0.31;
        } else if (income <= 641400) {
            tax = 115762 + (income - 488700) * 0.36;
        } else if (income <= 817600) {
            tax = 170734 + (income - 641400) * 0.39;
        } else if (income <= 1731600) {
            tax = 239452 + (income - 817600) * 0.41;
        } else {
            tax = 614192 + (income - 1731600) * 0.45;
        }

        return tax;
    }

    private String getTaxBracketsInfo() {
        return "Tax Brackets for the Year:\n" +
                "1. Income up to R95,750: No tax\n" +
                "2. Income from R95,751 to R226,000: 18%\n" +
                "3. Income from R226,001 to R353,100: R40,680 + 26% of the amount above R226,000\n" +
                "4. Income from R353,101 to R488,700: R73,726 + 31% of the amount above R353,100\n" +
                "5. Income from R488,701 to R641,400: R115,762 + 36% of the amount above R488,700\n" +
                "6. Income from R641,401 to R817,600: R170,734 + 39% of the amount above R641,400\n" +
                "7. Income from R817,601 to R1,731,600: R239,452 + 41% of the amount above R817,600\n" +
                "8. Income above R1,731,600: R614,192 + 45% of the amount above R1,731,600\n";
    }
}
