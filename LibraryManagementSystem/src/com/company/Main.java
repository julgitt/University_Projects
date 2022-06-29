package com.company;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() { //it puts runnable event on a queue
            @Override
            public void run() {
                Manager manager = new Manager();
                File file = new File("save.ser");
                if (file.isFile() && file.canRead()) {
                    try {
                        FileInputStream input = new FileInputStream("save.ser");
                        ObjectInputStream objInput = new ObjectInputStream(input);

                        manager = (Manager) objInput.readObject();

                        objInput.close();
                        input.close();
                    } catch (IOException | ClassNotFoundException ioe) {
                        ioe.printStackTrace();
                    }
                }
                else {
                    try {
                        if (file.createNewFile()) {
                            System.out.println("File has been created");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }
                Window window = new Window(manager);
                JPanel mainPanel = window.mainPanel;
                JFrame frame = new JFrame("Library Management System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(mainPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

    }
}
