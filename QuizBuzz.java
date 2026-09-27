/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


package com.mycompany.quizbuzz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class QuizBuzz {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizAppGUI::showMainWindow);
    }
}

interface Quiz {
    void startQuiz();
}

class QuizAppGUI {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    static String[] users = {"Nusrat", "Bintee","Samiha","Shormi","Rabina","Tasbiha","Arpita"};
    static final String COMMON_PASSWORD = "CSE60A";

    public static void showMainWindow() {
        JFrame frame = createFrame("QuizBuzz");
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 255, 240));

        JLabel title = new JLabel("Welcome to QuizBuzz");
        title.setFont(new Font("Times new Roman", Font.BOLD, 36));
        title.setForeground(Color.BLACK);

        JButton loginBtn = createButton("Login", new Color(152, 251, 152));
        loginBtn.setFont(new Font("Times new Roman", Font.BOLD, 28));
        loginBtn.setForeground(Color.BLACK);

        loginBtn.addActionListener(e -> {
            frame.dispose();
            showLoginForm();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(title, gbc);
        gbc.gridy = 1;
        panel.add(loginBtn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void showLoginForm() {
        JFrame f = createFrame("Login");
        f.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField user = new JTextField(15);
        JPasswordField pass = new JPasswordField(15);
        JButton submit = createButton("Submit", new Color(60, 179, 113));

        g.gridx = 0; g.gridy = 0; f.add(new JLabel("Username:"), g);
        g.gridy = 1; f.add(user, g);
        g.gridy = 2; f.add(new JLabel("Password:"), g);
        g.gridy = 3; f.add(pass, g);
        g.gridy = 4; f.add(submit, g);

        submit.addActionListener(e -> {
            String u = user.getText();
            String p = new String(pass.getPassword());

            for (String usr : users) {
        if (usr.equals(u) && COMMON_PASSWORD.equals(p)) {
            f.dispose();
            showInstruction(u);
             return;
            }
            }
            JOptionPane.showMessageDialog(f, "Invalid Login");
        });

        f.setVisible(true);
    }

    static void showInstruction(String user) {
        JFrame f = createFrame("Instructions");
        JTextArea area = new JTextArea(
                "Hello, " + user + "!\n\n" +
                "Instructions:\n" +
                "• 10 questions per quiz\n" +
                "• Time: 1 minute\n" +
                "• No negative marking\n" +
                "• Auto submit when time ends\n\n" +
                "Click Start to begin"
        );
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(area);

        f.setLayout(new BorderLayout());

        area.setEditable(false);
        area.setFont(new Font("Times new Roman", Font.BOLD, 24));
        area.setBackground(new Color(255, 250, 240));
        area.setMargin(new Insets(20, 20, 20, 20));

        JButton start = createButton("Start Quiz", new Color(152, 251, 152));
        start.setForeground(Color.BLACK);
        start.addActionListener(e -> {
            f.dispose();
            showLanguageSelection(user);
        });

        f.add(centerPanel, BorderLayout.CENTER);
        f.add(start, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    static void showLanguageSelection(String user) {
        JFrame f = createFrame("Select Language");
        f.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(15, 15, 15, 15);

        JButton javaBtn = createButton("Java", new Color(205, 92, 92));
        JButton cBtn = createButton("C", new Color(34, 139, 34));
        JButton cppBtn = createButton("C++", new Color(102, 51, 153));

        javaBtn.addActionListener(e -> { f.dispose(); new JavaQuiz(user).startQuiz(); });
        cBtn.addActionListener(e -> { f.dispose(); new CQuiz(user).startQuiz(); });
        cppBtn.addActionListener(e -> { f.dispose(); new CPPQuiz(user).startQuiz(); });

        g.gridx = 0; g.gridy = 0; f.add(javaBtn, g);
        g.gridy = 1; f.add(cBtn, g);
        g.gridy = 2; f.add(cppBtn, g);

        f.setVisible(true);
    }

    // ---------- Helper ----------
    static JFrame createFrame(String title) {
        JFrame f = new JFrame(title);
        f.setSize(WIDTH, HEIGHT);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return f;
    }

    static JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(200, 50));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Times new Roman", Font.BOLD, 18));
        return b;
    }
}

// ================= BASE QUIZ =================
abstract class BaseQuiz implements Quiz {

    String username;
    int index = 0, score = 0, time = 60;
    Timer timer;

    JFrame frame;
    JLabel questionLabel, timerLabel;
    JRadioButton a, b, c, d;
    ButtonGroup group;

    String[] questions;
    String[][] options;
    char[] answers;

    BaseQuiz(String u) {
        username = u;
    }

    public void startQuiz() {
        frame = new JFrame(username + " Quiz");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        timerLabel = new JLabel("Time: 60");
        timerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        timerLabel.setForeground(Color.RED);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Serif", Font.BOLD, 18));

        a = new JRadioButton();
        b = new JRadioButton();
        c = new JRadioButton();
        d = new JRadioButton();

        group = new ButtonGroup();
        group.add(a); group.add(b); group.add(c); group.add(d);

        JButton next = new JButton("Next");
        next.setFont(new Font("Serif", Font.BOLD, 16));
        next.setBackground(new Color(255, 140, 0));
        next.setForeground(Color.WHITE);

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; frame.add(timerLabel, g);
        g.gridy = 1; frame.add(questionLabel, g);
        g.gridwidth = 1;
        g.gridy = 2; frame.add(a, g);
        g.gridy = 3; frame.add(b, g);
        g.gridy = 4; frame.add(c, g);
        g.gridy = 5; frame.add(d, g);
        g.gridy = 6; g.gridwidth = 2; frame.add(next, g);

        loadQuestion();

        next.addActionListener(e -> nextQuestion());

        timer = new Timer(1000, e -> {
            time--;
            timerLabel.setText("Time: " + time);
            if (time <= 0) {
                timer.stop();
                finish(true);
            }
        });
        timer.start();

        frame.setVisible(true);
    }

    void loadQuestion() {
        questionLabel.setText((index + 1) + ". " + questions[index]);
        a.setText("A. " + options[index][0]);
        b.setText("B. " + options[index][1]);
        c.setText("C. " + options[index][2]);
        d.setText("D. " + options[index][3]);
        group.clearSelection();
    }

    void nextQuestion() {
        char selected = 'X';
        if (a.isSelected()) selected = 'A';
        else if (b.isSelected()) selected = 'B';
        else if (c.isSelected()) selected = 'C';
        else if (d.isSelected()) selected = 'D';

        if (selected == answers[index]) score++;
        index++;

        if (index == questions.length) finish(false);
        else loadQuestion();
    }

    void finish(boolean timeUp) {
        frame.dispose();
        String msg = (timeUp ? "Time's Up!\n" : "Quiz Finished!\n") +
                username + " scored: " + score + "/" + questions.length + "\n\nCorrect Answers:\n";

        for (int i = 0; i < questions.length; i++) {
            msg += (i + 1) + ". " + answers[i] + "\n";
        }

        JOptionPane.showMessageDialog(null, msg);
    }
}

class JavaQuiz extends BaseQuiz {
    JavaQuiz(String u) {
        super(u);
        questions = new String[]{
                "Which keyword is used to define a class in Java?",
                "Which is not a Java primitive type?",
                "Which method is entry point of Java program?",
                "Which symbol is used for comments?",
                "Which company developed Java?",
                "Which keyword is used to inherit a class?",
                "Which keyword makes a variable constant?",
                "Which is not an access modifier?",
                "What is used to handle exceptions?",
                "Which keyword is used to create an object?"
        };
        options = new String[][]{
                {"class","def","structure","define"},
                {"int","String","float","char"},
                {"start()","main()","run()","init()"},
                {"//","#","<!-- -->","--"},
                {"Google","Microsoft","Sun Microsystems","Apple"},
                {"inherit","extends","implements","super"},
                {"constant","final","static","const"},
                {"public","private","static","protected"},
                {"if","try-catch","switch","loop"},
                {"create","make","new","build"}
        };
        answers = new char[]{'A','B','B','A','C','B','B','C','B','C'};
    }
}

class CQuiz extends BaseQuiz {
    CQuiz(String u) {
        super(u);
        questions = new String[]{
                "Which symbol is used to include header files?",
                "Data type for single character?",
                "Function to print output?",
                "Keyword to declare constant?",
                "Array index starts with?",
                "Operator to get address of variable?",
                "Loop that runs at least once?",
                "Which is not valid data type?",
                "Function to allocate memory dynamically?",
                "Symbol to mark end of statement?"
        };
        options = new String[][]{
                {"#include","import","using","include"},
                {"int","char","string","float"},
                {"print()","cout","printf()","echo()"},
                {"final","constant","const","static"},
                {"0","1","-1","depends"},
                {"*","&","@","%"},
                {"for","while","do-while","goto"},
                {"float","double","char","string"},
                {"malloc()","scanf()","free()","sizeof"},
                {":",";",".","!"}
        };
        answers = new char[]{'A','B','B','C','A','B','C','D','A','B'};
    }
}

class CPPQuiz extends BaseQuiz {
    CPPQuiz(String u) {
        super(u);
        questions = new String[]{
                "Who invented C++?",
                "Single line comment symbol?",
                "Which header file is required for cout and cin?",
                "Operator for scope resolution?",
                "Which keyword is used to define a constant variable in C++?",
                "Which symbol is used to end a statement in C++?",
                "What is the correct file extension for a C++ program?",
                "Which operator is used to get the address of a variable?",
                "Which function is used to read input from the user in C++?",
                "Statement to create new memory block?"
        };
        options = new String[][]{
                {"James Gosling","Bjarne Stroustrup","Dennis Ritchie","Linus Torvalds"},
                {"#","//","<!-- -->",";"},
                {"<stdio.h>","<conio.h>","<iostream>","<string"},
                {"->","::","@@","//"},
                {"constant","final","define","const"},
                {":",".",";",","},
                {".c",".java",".cpp",".cs"},
                {"*","&","%","#"},
                {"scanf()","input()","cin","get()"},
                {"malloc()","new","calloc()","pointer"}
        };
        answers = new char[]{'B','B','C','B','D','C','C','B','C','B'};
    }
}