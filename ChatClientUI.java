import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientUI extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    
    public ChatClientUI() {
        initUI();
        
        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 12345;
        
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            new Thread(new MessageReceiver()).start();
            
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    sendMessage(message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initUI() {
        chatArea = new JTextArea(20, 40);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        messageField = new JTextField(30);
        sendButton = new JButton("Send");
        
        JPanel inputPanel = new JPanel();
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void sendMessage(String message) {
        out.println(message);
        displaySentMessage(message);
        messageField.setText("");
    }
    
    private void displayReceivedMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append("Received: " + message + "\n"));
    }
    
    private void displaySentMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append("Sent: " + message + "\n"));
    }
    
    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    displayReceivedMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClientUI());
    }
}
