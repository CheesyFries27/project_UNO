package thebestlistapp;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class toDoList extends JFrame {

	private JPanel contentPane;
	private JTextField taskInputField;
	private DefaultListModel<String> taskListModel;
	private JList<String> taskList;

	/**
	 * Launch the application (useful for testing this screen independently).
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					toDoList frame = new toDoList();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame for the To-Do List.
	 */
	public toDoList() {
		setTitle("My To-Do List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 500); // Slightly wider to fit task controls nicely
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(33, 33, 33));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Title label
		JTextField lblTitle = new JTextField();
		lblTitle.setText("YOUR TO-DO LIST");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTitle.setColumns(10);
		lblTitle.setBackground(new Color(33, 33, 33));
		lblTitle.setBounds(75, 20, 180, 20);
		lblTitle.setEditable(false);
		contentPane.add(lblTitle);
		
		// Scroll Pane and List for Tasks
		taskListModel = new DefaultListModel<>();
		taskList = new JList<>(taskListModel);
		taskList.setBackground(new Color(45, 45, 45));
		taskList.setForeground(Color.WHITE);
		taskList.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JScrollPane scrollPane = new JScrollPane(taskList);
		scrollPane.setBounds(30, 60, 275, 280);
		contentPane.add(scrollPane);
		
		// Text Field to enter a new task
		taskInputField = new JTextField();
		taskInputField.setBounds(30, 360, 275, 25);
		contentPane.add(taskInputField);
		taskInputField.setColumns(10);
		
		// Add Task Button
		JButton btnAddTask = new JButton("Add Task");
		btnAddTask.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnAddTask.setBounds(30, 400, 130, 25);
		contentPane.add(btnAddTask);
		
		// Delete Selected Task Button
		JButton btnDeleteTask = new JButton("Delete Selected");
		btnDeleteTask.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnDeleteTask.setBounds(175, 400, 130, 25);
		contentPane.add(btnDeleteTask);
		
		// --- Action Listeners ---
		
		// Add button functionality
		btnAddTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newTask = taskInputField.getText().trim();
				if (!newTask.isEmpty()) {
					taskListModel.addElement(newTask);
					taskInputField.setText(""); // Clear input field after adding
				}
			}
		});
		
		// Delete button functionality
		btnDeleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = taskList.getSelectedIndex();
				if (selectedIndex != -1) {
					taskListModel.remove(selectedIndex);
				}
			}
		});
		
		// Make the frame visible once initialized
		setVisible(true);
	}
}