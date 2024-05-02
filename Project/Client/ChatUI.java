package Project.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ChatUI extends JFrame {
    private JTextField usernameField;
    private JTextField hostField;
    private JTextField portField;
    private JButton registerButton;

    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextArea chatHistoryTextArea;
    private JTextField chatInputField;
    private JButton sendButton;

    public ChatUI() {
        setTitle("Chat Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(173, 216, 230)); // Lighter Blue

        // Registration Panel
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel hostLabel = new JLabel("Host:");
        hostLabel.setForeground(Color.BLACK);
        hostLabel.setFont(new Font("Arial", Font.BOLD, 14));
        hostField = new JTextField(20);
        hostField.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel portLabel = new JLabel("Port:");
        portLabel.setForeground(Color.BLACK);
        portLabel.setFont(new Font("Arial", Font.BOLD, 14));
        portField = new JTextField(20);
        portField.setFont(new Font("Arial", Font.BOLD, 14));

        registerPanel.add(usernameLabel, gbc);
        gbc.gridx++;
        registerPanel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(hostLabel, gbc);
        gbc.gridx++;
        registerPanel.add(hostField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(portLabel, gbc);
        gbc.gridx++;
        registerPanel.add(portField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        registerPanel.add(registerButton, gbc);

        add(registerPanel, BorderLayout.NORTH);

        // Chat Room Panel
        JPanel chatRoomPanel = new JPanel(new BorderLayout());
        chatRoomPanel.setBackground(new Color(173, 216, 230)); // Lighter Blue

        JPanel userPanel = new JPanel(new GridBagLayout());
        userPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints userPanelGBC = new GridBagConstraints();
        userPanelGBC.gridx = 0;
        userPanelGBC.gridy = 0;
        userPanelGBC.fill = GridBagConstraints.HORIZONTAL;
        userPanelGBC.insets = new Insets(5, 5, 5, 5);

        JLabel userListLabel = new JLabel("Users in the room");
        userListLabel.setForeground(Color.BLACK);
        userListLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane userListScrollPane = new JScrollPane(userList);

        userPanel.add(userListLabel, userPanelGBC);
        userPanelGBC.gridy++;
        userPanel.add(userListScrollPane, userPanelGBC);

        chatHistoryTextArea = new JTextArea(10, 40);
        chatHistoryTextArea.setEditable(false);
        chatHistoryTextArea.setFont(new Font("Arial", Font.BOLD, 14));
        chatHistoryTextArea.setBackground(Color.WHITE);
        JScrollPane chatHistoryScrollPane = new JScrollPane(chatHistoryTextArea);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints inputPanelGBC = new GridBagConstraints();
        inputPanelGBC.gridx = 0;
        inputPanelGBC.gridy = 0;
        inputPanelGBC.fill = GridBagConstraints.HORIZONTAL;
        inputPanelGBC.insets = new Insets(5, 5, 5, 5);

        chatInputField = new JTextField(30);
        chatInputField.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));

        inputPanel.add(chatInputField, inputPanelGBC);
        inputPanelGBC.gridx++;
        inputPanel.add(sendButton, inputPanelGBC);

        chatRoomPanel.add(userPanel, BorderLayout.WEST);
        chatRoomPanel.add(chatHistoryScrollPane, BorderLayout.CENTER);
        chatRoomPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatRoomPanel, BorderLayout.CENTER);

        setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (!username.isEmpty()) {
                    registerButton.setEnabled(false);
                    usernameField.setEditable(false);
                    chatInputField.setEnabled(true);
                    sendButton.setEnabled(true);
                    chatInputField.requestFocus();
                    userListModel.addElement(username);
                    chatHistoryTextArea.append(username + " has joined the room.\n");
                } else {
                    JOptionPane.showMessageDialog(ChatUI.this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatInputField.getText();
                if (!message.isEmpty()) {
                    if (message.startsWith("/flip")) {
                        flipCoin();
                    } else if (message.startsWith("/roll")) {
                        rollDice(message);
                    } else {
                        chatHistoryTextArea.append(usernameField.getText() + ": " + message + "\n");
                    }
                    chatInputField.setText("");
                }
            }
        });
    }

    private void flipCoin() {
        String result;
        Random random = new Random();
        int toss = random.nextInt(2); // 0 for Heads, 1 for Tails
        if (toss == 0) {
            result = "Heads";
        } else {
            result = "Tails";
        }
        chatHistoryTextArea.append(usernameField.getText() + " flipped a coin. Result: " + result + "\n");
    }

    private void rollDice(String message) {
        String[] tokens = message.split(" ");
        if (tokens.length == 2) {
            try {
                if (tokens[1].matches("\\d+")) {
                    int rolls = Integer.parseInt(tokens[1]);
                    if (rolls <= 0) {
                        chatHistoryTextArea.append("Invalid roll command!\n");
                        return;
                    }
                    int result = new Random().nextInt(6) + 1; // default 6-sided dice
                    StringBuilder rollResults = new StringBuilder();
                    int total = 0;
                    for (int i = 0; i < rolls; i++) {
                        result = new Random().nextInt(6) + 1;
                        rollResults.append(result).append(" ");
                        total += result;
                    }
                    chatHistoryTextArea.append(usernameField.getText() + " rolled " + rolls + " dice. Results: " + rollResults + "Total: " + total + "\n");
                } else if (tokens[1].matches("\\d+d\\d+")) {
                    String[] dice = tokens[1].split("d");
                    int rolls = Integer.parseInt(dice[0]);
                    int sides = Integer.parseInt(dice[1]);
                    if (rolls <= 0 || sides <= 0) {
                        chatHistoryTextArea.append("Invalid roll command!\n");
                        return;
                    }
                    StringBuilder rollResults = new StringBuilder();
                    int total = 0;
                    for (int i = 0; i < rolls; i++) {
                        int roll = new Random().nextInt(sides) + 1;
                        rollResults.append(roll).append(" ");
                        total += roll;
                    }
                    chatHistoryTextArea.append(usernameField.getText() + " rolled " + rolls + "d" + sides + ". Results: " + rollResults + "Total: " + total + "\n");
                } else {
                    chatHistoryTextArea.append("Invalid roll command!\n");
                }
            } catch (NumberFormatException e) {
                chatHistoryTextArea.append("Invalid roll command!\n");
            }
        } else {
            chatHistoryTextArea.append("Invalid roll command!\n");
        }
    }
    

    public static void main(String[] args) {
        new ChatUI();
    }
}
