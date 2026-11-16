package projectunov1;
import java.awt.Color;

import javax.swing.JOptionPane;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class toDoList extends JFrame {

	private static final long serialVersionUID = 1L;
	
    private JPanel contentPane;
    private JTextField taskInputField;
    private DefaultListModel<Tasks> taskListModel;
    private JList<Tasks> taskList;
    private int currentUserId;

    /**
     * Default constructor for standalone preview/testing.
     */
    public toDoList() {
        this(0);
    }

    /**
     * Main constructor accepting the logged-in or newly created User ID.
     */
    public toDoList(int userId) {
        this.currentUserId = userId;
        initialize();
    }

    private void initialize() {
        setTitle("My To-Do List (UID: " + currentUserId + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 500);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(33, 33, 33));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title header
        JTextField lblTitle = new JTextField();
        lblTitle.setText("YOUR TO-DO LIST (USER #" + currentUserId + ")");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTitle.setColumns(10);
        lblTitle.setBackground(new Color(33, 33, 33));
        lblTitle.setBounds(50, 20, 240, 20);
        lblTitle.setEditable(false);
        contentPane.add(lblTitle);

        // Scroll pane and task list
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setCellRenderer((list, task, index, isSelected, cellHasFocus) -> {
            javax.swing.DefaultListCellRenderer renderer =
                new javax.swing.DefaultListCellRenderer();
            String[] priorities = {"None", "Low", "Medium", "High"};
            int priorityIndex = task.getPriority();
            String text = task.toString()
                    + " | Assigned to #" + task.getAssignedTo()
                    + " | " + priorities[priorityIndex]
                    + (task.getDueDate() == null
                            ? ""
                            : " | Due " + task.getDueDate()
                    );

            return renderer.getListCellRendererComponent(
                list,
                text,
                index,
                isSelected,
                cellHasFocus
            );
        });
        taskList.setBackground(new Color(45, 45, 45));
        taskList.setForeground(Color.WHITE);
        taskList.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBounds(30, 60, 275, 280);
        contentPane.add(scrollPane);

        // Text field to enter new task
        taskInputField = new JTextField();
        taskInputField.setBounds(30, 360, 275, 25);
        contentPane.add(taskInputField);
        taskInputField.setColumns(10);

        // Add task button
        JButton btnAddTask = new JButton("Add Task");
        btnAddTask.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnAddTask.setBounds(30, 400, 130, 25);
        contentPane.add(btnAddTask);

        // Delete selected task button
        JButton btnDeleteTask = new JButton("Delete Selected");
        btnDeleteTask.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnDeleteTask.setBounds(175, 400, 130, 25);
        contentPane.add(btnDeleteTask);

        // Button listeners
        btnAddTask.addActionListener(e -> {
            Tasks created = TaskCreationDialog.showDialog(
                this,
                currentUserId,
                taskInputField.getText()
            );

            if (created != null) {
                taskListModel.add(0, created);
                taskInputField.setText("");
            }
        });

        taskInputField.addActionListener(e ->
            btnAddTask.doClick()
        );

        btnDeleteTask.addActionListener(e -> {
            Tasks selectedTask = taskList.getSelectedValue();

            if (selectedTask == null) {
                return;
            }

            boolean deleted = DatabaseConnection.deleteTask(
                selectedTask.getId(),
                currentUserId
            );

            if (deleted) {
                taskListModel.removeElement(selectedTask);
            } else {
                JOptionPane.showMessageDialog(
                    contentPane,
                    "Could not delete task. " +
                    "Only its creator can delete it."
                );
            }
        });

        try {
            for (Tasks task :
                    DatabaseConnection.getTasks(currentUserId)) {
                taskListModel.addElement(task);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load saved tasks: " + ex.getMessage(),
                "Load error",
                JOptionPane.ERROR_MESSAGE
            );
        }

        setVisible(true);
    }

    /**
     * Standalone runner for testing the frame.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    toDoList frame = new toDoList(1);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
   }

