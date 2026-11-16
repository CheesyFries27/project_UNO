package projectunov1;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import javax.swing.*;

public final class TaskCreationDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JTextField title = new JTextField(30);
    private final JTextArea description = new JTextArea(4, 30);

    private final JComboBox<Assignee> assignee = new JComboBox<>();

    private final JComboBox<String> priority = new JComboBox<>(
        new String[] {"None", "Low", "Medium", "High"}
    );

    private final JCheckBox hasDueDate =
        new JCheckBox("Set a due date");

    private final JTextField dueDate =
        new JTextField(LocalDate.now().toString(), 12);

    private Tasks result;

    private static final class Assignee {
        final int id;
        final String name;

        Assignee(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " (#" + id + ")";
        }
    }

    public static Tasks showDialog(
            JFrame owner,
            int currentUserId,
            String initialTitle) {

        Map<Integer, String> users;

        try {
            users = DatabaseConnection.getUsers();
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(
                owner,
                "Could not load users. Please try again.",
                "Create task",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        if (!users.containsKey(currentUserId)) {
            JOptionPane.showMessageDialog(
                owner,
                "Please log in as a registered user to create tasks."
            );
            return null;
        }

        TaskCreationDialog dialog = new TaskCreationDialog(
            owner, currentUserId, initialTitle, users
        );

        dialog.setVisible(true);
        return dialog.result;
    }

    private TaskCreationDialog(
            JFrame owner,
            int currentUserId,
            String initialTitle,
            Map<Integer, String> users) {

        super(owner, "Create task", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        title.setText(initialTitle);

        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        for (Map.Entry<Integer, String> user : users.entrySet()) {
            Assignee item = new Assignee(
                user.getKey(), user.getValue()
            );

            assignee.addItem(item);

            if (item.id == currentUserId) {
                assignee.setSelectedItem(item);
            }
        }

        dueDate.setEnabled(false);

        hasDueDate.addActionListener(e ->
            dueDate.setEnabled(hasDueDate.isSelected())
        );

        JPanel form = new JPanel(new GridBagLayout());

        form.setBorder(
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        addRow(form, 0, "Title *", title);
        addRow(form, 1, "Description", new JScrollPane(description));
        addRow(form, 2, "Assign to *", assignee);
        addRow(form, 3, "Priority", priority);
        addRow(form, 4, "Due date", hasDueDate);
        addRow(form, 5, "YYYY-MM-DD", dueDate);

        JButton create = new JButton("Create task");
        JButton cancel = new JButton("Cancel");

        JPanel buttons = new JPanel();
        buttons.add(cancel);
        buttons.add(create);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(create);

        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        create.addActionListener(e -> {
            if (title.getText().trim().isEmpty()) {
                showError("Enter a task title.");
                title.requestFocusInWindow();
                return;
            }

            LocalDate date = null;

            if (hasDueDate.isSelected()) {
                try {
                    date = LocalDate.parse(dueDate.getText().trim());
                } catch (DateTimeParseException ex) {
                    showError(
                        "Enter a valid due date in YYYY-MM-DD format, " +
                        "such as 2026-12-25."
                    );
                    dueDate.requestFocusInWindow();
                    return;
                }
            }

            Assignee selected =
                (Assignee) assignee.getSelectedItem();

            if (selected == null) {
                showError("Choose a user to assign this task to.");
                return;
            }

            Tasks task = new Tasks(
                title.getText(),
                description.getText().trim(),
                priority.getSelectedIndex(),
                date,
                currentUserId,
                selected.id
            );

            int id = DatabaseConnection.addTask(
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                currentUserId,
                selected.id,
                date,
                false
            );

            if (id == -1) {
                showError(
                    "Could not save the task. Your entries have " +
                    "been kept so you can try again."
                );
                return;
            }

            task.setId(id);
            result = task;
            dispose();
        });

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Create task",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static void addRow(
            JPanel panel,
            int row,
            String text,
            JComponent input) {

        GridBagConstraints c = new GridBagConstraints();

        c.gridy = row;
        c.gridx = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(5, 5, 5, 12);

        JLabel label = new JLabel(text);
        label.setLabelFor(input);
        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(input, c);
    }
}