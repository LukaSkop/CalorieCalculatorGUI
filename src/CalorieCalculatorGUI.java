import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalorieCalculatorGUI {

    // Constants for BMR formula coefficients and activity multipliers
    private static final double MALE_BMR_CONSTANT = 88.362;
    private static final double FEMALE_BMR_CONSTANT = 447.593;
    private static final double MALE_WEIGHT_COEFFICIENT = 13.397;
    private static final double FEMALE_WEIGHT_COEFFICIENT = 9.247;
    private static final double MALE_HEIGHT_COEFFICIENT = 4.799;
    private static final double FEMALE_HEIGHT_COEFFICIENT = 3.098;
    private static final double MALE_AGE_COEFFICIENT = 5.677;
    private static final double FEMALE_AGE_COEFFICIENT = 4.330;

    private static final double SEDENTARY_MULTIPLIER = 1.2;
    private static final double MODERATE_MULTIPLIER = 1.55;
    private static final double ACTIVE_MULTIPLIER = 1.725;

    public static void main(String[] args) {
        // Set a modern UI theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Create the main frame
        JFrame frame = new JFrame("Calorie Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.WHITE);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Input fields
        JLabel genderLabel = new JLabel("Gender (M/F):");
        JTextField genderField = new JTextField(10);

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(10);

        JLabel weightLabel = new JLabel("Weight (kg):");
        JTextField weightField = new JTextField(10);

        JLabel heightLabel = new JLabel("Height (cm):");
        JTextField heightField = new JTextField(10);

        JLabel activityLabel = new JLabel("Activity Level:");
        String[] activityOptions = {"Sedentary", "Moderate", "Active"};
        JComboBox<String> activityDropdown = new JComboBox<>(activityOptions);

        JButton calculateButton = new JButton("Calculate");

        // Results Panel (Formatted output)
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(240, 248, 255));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel resultTitle = new JLabel("Results:");
        resultTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel bmrResult = new JLabel("BMR: - kcal");
        bmrResult.setFont(new Font("Arial", Font.BOLD, 16));
        bmrResult.setForeground(new Color(0, 102, 204));

        JLabel calorieResult = new JLabel("Calories Needed: - kcal");
        calorieResult.setFont(new Font("Arial", Font.BOLD, 16));
        calorieResult.setForeground(new Color(0, 153, 51));

        resultPanel.add(resultTitle);
        resultPanel.add(bmrResult);
        resultPanel.add(calorieResult);
        resultPanel.setVisible(false); // Hidden until results are available

        // Add input fields to panel
        inputPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(genderField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(weightLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(weightField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(heightLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(heightField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(activityLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(activityDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        inputPanel.add(calculateButton, gbc);

        // Add components to frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(resultPanel, BorderLayout.SOUTH);

        // Button action listener
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Read inputs
                    String gender = genderField.getText().trim().toUpperCase();
                    int age = Integer.parseInt(ageField.getText().trim());
                    double weight = Double.parseDouble(weightField.getText().trim());
                    double height = Double.parseDouble(heightField.getText().trim());
                    String activityLevel = ((String) activityDropdown.getSelectedItem()).toLowerCase();

                    // Validate inputs
                    if (!gender.equals("M") && !gender.equals("F")) {
                        JOptionPane.showMessageDialog(frame, "Invalid gender. Please enter M or F.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Calculate BMR
                    double bmr = calculateBMR(gender, weight, height, age);

                    // Calculate daily calorie needs
                    double calorieNeeds = calculateCalorieNeeds(bmr, activityLevel);

                    // Display results
                    bmrResult.setText(String.format("BMR: %.0f kcal", bmr));
                    calorieResult.setText(String.format("Calories Needed: %.0f kcal", calorieNeeds));

                    resultPanel.setVisible(true);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    // Method to calculate BMR
    public static double calculateBMR(String gender, double weight, double height, int age) {
        return gender.equals("M") ?
                MALE_BMR_CONSTANT + (MALE_WEIGHT_COEFFICIENT * weight) + (MALE_HEIGHT_COEFFICIENT * height) - (MALE_AGE_COEFFICIENT * age) :
                FEMALE_BMR_CONSTANT + (FEMALE_WEIGHT_COEFFICIENT * weight) + (FEMALE_HEIGHT_COEFFICIENT * height) - (FEMALE_AGE_COEFFICIENT * age);
    }

    // Method to calculate daily calorie needs
    public static double calculateCalorieNeeds(double bmr, String activityLevel) {
        return switch (activityLevel) {
            case "sedentary" -> bmr * SEDENTARY_MULTIPLIER;
            case "moderate" -> bmr * MODERATE_MULTIPLIER;
            case "active" -> bmr * ACTIVE_MULTIPLIER;
            default -> throw new IllegalArgumentException("Invalid activity level");
        };
    }
}
