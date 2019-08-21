package client;


import javax.swing.*; 

public class ClientUI extends JFrame {

	public ClientUI() {
		buildInterface();
	}
	
	public final void buildInterface() {
	// Create the blank canvas 
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null); // enables absolute positioning 
		
		JButton connectButton = new JButton("Connect");
		JButton disconnectButton = new JButton("Disconnect");
		JTextField hostField = new JTextField();
		JTextField portField = new JTextField();
		JLabel hostLabel = new JLabel("Host");
		JLabel portLabel = new JLabel("Port");
		
		JTextArea resultDisplay = new JTextArea();
		resultDisplay.setEditable(false);

		// set bounds
		hostField.setBounds(10,30,200,40);
		portField.setBounds(210, 30, 50, 40);
		hostLabel.setBounds(100, 15, 30, 10);
		portLabel.setBounds(225, 15, 30, 10);
		connectButton.setBounds(270, 30, 100, 40);
		disconnectButton.setBounds(370, 30, 100, 40);
		resultDisplay.setBounds(15, 80, 500, 200);
		
		// add components
		panel.add(hostField);
		panel.add(portField);
		panel.add(connectButton);
		panel.add(disconnectButton);
		panel.add(hostLabel);
		panel.add(portLabel);
		panel.add(resultDisplay);
		
		
		setTitle("DictionaryServer");
		setSize(600, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE); //close when we click the close button

				
	}
	
	
	public static void main(String[] args) {
			ClientUI ui = new ClientUI();
			ui.setVisible(true);
	}
}
