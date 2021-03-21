package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class Client extends JFrame {
	private JTextField enterField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket cli;
	private String message = "";
	private String chatServer;




	public Client(String host) {
		super("Istemci Uygulaması");
		chatServer = host;
		Container container = getContentPane();
		enterField = new JTextField();
		enterField.setEnabled(false);
		enterField.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						sendData(e.getActionCommand());
					}
				});
		container.add( enterField, BorderLayout.NORTH );
		displayArea = new JTextArea();
		container.add(new JScrollPane(displayArea),BorderLayout.CENTER);
		setSize(300,400);
		setVisible(true);
	}
	
	public void runClient() {
		try {
			connectToServer();
			getStream();
			processConnection();
			closeConnection();
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Hata oldu");
		}
	}

	private void closeConnection() throws IOException {
		// TODO Auto-generated method stub
		displayArea.append( "\nClosing connection" );
		output.close();
		input.close();
		cli.close();

	}

	private void processConnection() {
		// TODO Auto-generated method stub
		String message="SERVER>>> Baglanti saglandi.";
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enterField.setEnabled( true );
		
		do {
			try {
				message=(String)input.readObject();
				displayArea.append( "\n" + message );
				displayArea.setCaretPosition(displayArea.getText().length());
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} while (!message.equals( "SERVER>>> TERMINATE" ) );
	}

	private void getStream() {
		// TODO Auto-generated method stub
		try {
			output = new ObjectOutputStream(cli.getOutputStream() );
			output.flush();
			input = new ObjectInputStream(cli.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println( "Output/Input Nesnesi olusturulamadi");
		}
		displayArea.append( "\nGot I/O streams\n" );
	}

	private void connectToServer() throws IOException {
		// TODO Auto-generated method stub
		displayArea.setText( "Attempting connection\n" );
		cli = new Socket(InetAddress.getByName( chatServer ), 5000 );
		displayArea.append( "\n Connected to: " + cli.getInetAddress().getHostName() );
	}

	private void sendData(String message) {
		// TODO Auto-generated method stub
		try {
			output.writeObject("CLIENT>>> "+message);
			output.flush();
			displayArea.append( "\nCLIENT>>> " + message );
		} catch (Exception e) {
			// TODO: handle exception
			displayArea.append( "\nError writing object" );

		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client application = new Client("127.0.0.1");
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.runClient();

		
	}

}
