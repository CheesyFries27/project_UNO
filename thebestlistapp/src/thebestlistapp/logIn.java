package thebestlistapp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

public class logIn {

	private JFrame frame;
	private JTextField txtLogInHere;
	private JTextField txtUsername;
	private JTextField user;
	private JTextField txtPassword;
	private JTextField pwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logIn window = new logIn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public logIn() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(33, 33, 33));
		frame.setBounds(100, 100, 300, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtLogInHere = new JTextField();
		txtLogInHere.setBackground(new Color(33, 33, 33));
		txtLogInHere.setForeground(new Color(255, 255, 255));
		txtLogInHere.setFont(new Font("Tahoma", Font.BOLD, 10));
		txtLogInHere.setHorizontalAlignment(SwingConstants.CENTER);
		txtLogInHere.setText("LOG IN HERE");
		txtLogInHere.setBounds(99, 30, 96, 18);
		frame.getContentPane().add(txtLogInHere);
		txtLogInHere.setColumns(10);
		
		txtUsername = new JTextField();
		txtUsername.setText("Username:");
		txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
		txtUsername.setForeground(Color.WHITE);
		txtUsername.setFont(new Font("Tahoma", Font.BOLD, 10));
		txtUsername.setColumns(10);
		txtUsername.setBackground(new Color(33, 33, 33));
		txtUsername.setBounds(99, 97, 96, 18);
		frame.getContentPane().add(txtUsername);
		
		user = new JTextField();
		user.setBounds(99, 125, 96, 18);
		frame.getContentPane().add(user);
		user.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setText("Password:");
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setForeground(Color.WHITE);
		txtPassword.setFont(new Font("Tahoma", Font.BOLD, 10));
		txtPassword.setColumns(10);
		txtPassword.setBackground(new Color(33, 33, 33));
		txtPassword.setBounds(99, 153, 96, 18);
		frame.getContentPane().add(txtPassword);
		
		pwd = new JTextField();
		pwd.setBounds(99, 181, 96, 18);
		frame.getContentPane().add(pwd);
		pwd.setColumns(10);
		
		JButton verify = new JButton("Verify");
		verify.setFont(new Font("Tahoma", Font.BOLD, 10));
		verify.setBounds(99, 236, 96, 20);
		frame.getContentPane().add(verify);
		verify.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (user.getText().equals("admin") && pwd.getText().equals("admin")) {
		            System.out.println("Access Granted");
		            
		            // Close the login window
		            frame.dispose();
		            
		            // Open the To-Do List window
		            EventQueue.invokeLater(new Runnable() {
		                public void run() {
		                    try {
		                        toDoList window = new toDoList();
		                        
		                    } catch (Exception ex) {
		                        ex.printStackTrace();
		                    }
		                }
		            });
		            
		        } else {
		            System.out.println("Access Denied");
		        }
		    }
		});
	}
}