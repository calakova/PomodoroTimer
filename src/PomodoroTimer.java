import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PomodoroTimer extends JFrame {
    private JPanel panelMain;
    private JToolBar toolBar;
    private JButton buttonAddTodo;
    private JButton buttonFinishTodo;
    private JLabel labelCurrentTimer;
    private JButton buttonStartTimer;
    private JLabel labelTimer;
    private JPanel panelTimer;
    private JList<Todo> listTodo;
    private JScrollPane scrollPane;

    private final DefaultListModel<Todo> defaultTodoListModel = new DefaultListModel<>();

    private final Timer timer = new Timer(1000, new TimerListener());
    private int pomodoroCounter = 0;
    // Initial Pomodoro duration
    private int remainingSeconds = 5;

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            remainingSeconds--;

            labelTimer.setText(secondsToMinuteSecondsString(remainingSeconds));

            if (remainingSeconds <= 0) {
                timer.stop();
                buttonStartTimer.setText("Start Timer");

                switch (labelCurrentTimer.getText()) {
                    case "Pomodoro":
                        pomodoroCounter++;

                        try {
                            defaultTodoListModel.get(0).incrementFinishedPomodoros();
                            listTodo.updateUI();
                        } catch (IndexOutOfBoundsException exception) {
                            JOptionPane.showMessageDialog(panelMain, "No To-Dos in list");
                        }

                        if (pomodoroCounter % 4 == 0) {
                            // Long Break duration
                            switchCounter(9, "Long Break");
                        } else {
                            // Short Break duration
                            switchCounter(3, "Short Break");
                        }
                        break;
                    case "Short Break", "Long Break":
                        // Pomodoro duration
                        switchCounter(5, "Pomodoro");
                        break;
                }
            }
        }
    }

    public PomodoroTimer() {
        buttonStartTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    buttonStartTimer.setText("Start Timer");
                    return;
                }

                timer.start();
                buttonStartTimer.setText("Stop Timer");
            }
        });

        buttonAddTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String todoName = JOptionPane.showInputDialog("Enter the name of the To-Do");
                try {
                    int estimatedPomodoros = Integer
                            .parseInt(JOptionPane.showInputDialog("Enter estimated amount of pomodoros"));
                    defaultTodoListModel.addElement(new Todo(todoName, estimatedPomodoros));
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(panelMain, "Invalid number!");
                }
            }
        });

        buttonFinishTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    defaultTodoListModel.remove(0);
                } catch (IndexOutOfBoundsException exception) {
                    JOptionPane.showMessageDialog(panelMain, "No To-Dos in list");
                }
            }
        });

        listTodo.setModel(defaultTodoListModel);
    }

    public void init() {
        PomodoroTimer pomodoroTimer = new PomodoroTimer();
        pomodoroTimer.setContentPane(panelMain);
        pomodoroTimer.setTitle("Pomodoro Timer");
        pomodoroTimer.setBounds(600, 200, 500, 500);
        pomodoroTimer.setVisible(true);
        pomodoroTimer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void switchCounter(int seconds, String counterType) {
        remainingSeconds = seconds;
        labelCurrentTimer.setText(counterType);
        labelTimer.setText(secondsToMinuteSecondsString(seconds));
    }

    private String secondsToMinuteSecondsString(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
