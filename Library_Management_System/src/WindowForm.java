import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowForm {
    public JPanel mainPanel;
    public JPanel ButtonsCard;
    private JPanel parentPanel;
    private JButton booksButton;
    private JButton authorsButton;
    private JButton usersButton;
    private JPanel cardBooks;
    private JPanel cardAuthors;
    private JPanel cardUsers;

    public WindowForm() {
        booksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(cardBooks);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        authorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(cardAuthors);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(cardUsers);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
    }

    private void createUIComponents() {


    }
    private void $$$setupUI$$$() {
        createUIComponents();
    }
}
