package oi.yeetaaron1;

import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CalculatorGUI {
    private JFrame frame;
    private JTextField textField;
    private JLabel historyLabel;
    private String operator;
    private double num1, num2, result;
    private boolean isNewInput = false;
    private JTextArea historyArea;
    private ArrayList<String> history = new ArrayList<>();

    public CalculatorGUI() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        historyLabel = new JLabel(" ");
        historyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        historyLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(historyLabel, BorderLayout.NORTH);

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.addActionListener(new ButtonClickListener());  // Allow typing directly in the text field
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    processInput();
                }
            }
        });
        topPanel.add(textField, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        // Left side for history display
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setBackground(Color.LIGHT_GRAY);
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setPreferredSize(new Dimension(150, 0));
        frame.add(historyScroll, BorderLayout.WEST);

        // Button panel (GridLayout)
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5)); // 4 rows, 4 columns, with spacing between buttons

        // Buttons for numbers and operators
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "+",
                "0", "EC", "C", "="
        };

        // Add buttons to the panel
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            // Handle number and operator inputs
            if (command.charAt(0) >= '0' && command.charAt(0) <= '9') {
                if (isNewInput) {
                    textField.setText(command);
                    isNewInput = false;
                } else {
                    textField.setText(textField.getText() + command);
                }
            } else if (command.equals("C")) { // Clear everything
                textField.setText("");
                historyLabel.setText(" ");
                num1 = num2 = result = 0;
                operator = "";
                history.clear();  // Clear history
                updateHistoryArea();  // Update history area to reflect changes
                isNewInput = false;
            } else if (command.equals("EC")) { // Clear current input (question)
                textField.setText("");
                isNewInput = false;
            } else if (command.equals("=")) { // Equal to calculate
                if (!textField.getText().isEmpty() && !operator.isEmpty()) {
                    try {
                        num2 = Double.parseDouble(textField.getText());
                        switch (operator) {
                            case "+":
                                result = num1 + num2;
                                break;
                            case "-":
                                result = num1 - num2;
                                break;
                            case "*":
                                result = num1 * num2;
                                break;
                            case "/":
                                if (num2 == 0) {
                                    textField.setText("Error");
                                    return;
                                }
                                result = num1 / num2;
                                break;
                        }
                        historyLabel.setText(num1 + " " + operator + " " + num2 + " =");
                        textField.setText(String.valueOf(result));
                        // Add current calculation to history
                        history.add(num1 + " " + operator + " " + num2 + " = " + result);
                        updateHistoryArea();  // Update the history display
                        isNewInput = true;
                    } catch (NumberFormatException ex) {
                        textField.setText("Error");
                    }
                } else {
                    textField.setText("Missing second value");
                }
            } else { // Operator input
                if (!textField.getText().isEmpty()) {
                    num1 = Double.parseDouble(textField.getText());
                    operator = command;
                    historyLabel.setText(num1 + " " + operator);
                    textField.setText("");
                    isNewInput = false;
                }
            }
        }
    }

    private void updateHistoryArea() {
        StringBuilder historyText = new StringBuilder();
        for (String equation : history) {
            historyText.append(equation).append("\n");
        }
        historyArea.setText(historyText.toString());
    }

    private void processInput() {
        String input = textField.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        try {
            // Handle basic arithmetic expressions (e.g., 1*2, 3+5)
            String[] operators = {"+", "-", "*", "/"};
            for (String op : operators) {
                if (input.contains(op)) {
                    String[] operands = input.split("\\" + op);
                    num1 = Double.parseDouble(operands[0].trim());
                    num2 = Double.parseDouble(operands[1].trim());
                    operator = op;

                    switch (operator) {
                        case "+":
                            result = num1 + num2;
                            break;
                        case "-":
                            result = num1 - num2;
                            break;
                        case "*":
                            result = num1 * num2;
                            break;
                        case "/":
                            if (num2 == 0) {
                                textField.setText("Error");
                                return;
                            }
                            result = num1 / num2;
                            break;
                    }
                    textField.setText(String.valueOf(result));
                    historyLabel.setText(num1 + " " + operator + " " + num2 + " =");
                    // Add to history
                    history.add(num1 + " " + operator + " " + num2 + " = " + result);
                    updateHistoryArea();
                    return;
                }
            }
            // Handle invalid input
            textField.setText("Invalid expression");
        } catch (Exception ex) {
            textField.setText("Error");
        }
    }

    public static void main(String[] args) {
        new CalculatorGUI();
    }
}
