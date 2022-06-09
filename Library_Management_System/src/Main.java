import javax.swing.*;



public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WindowForm window = new WindowForm();
                JPanel mainPanel = window.mainPanel;
                JFrame frame = new JFrame("Library Management System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(mainPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        //Manager manager = new Manager();


        /*File file = new File(args[0]);
        if (file.isFile() && file.canRead()) {
            try {
                FileInputStream input = new FileInputStream(args[0]);
                ObjectInputStream objInput = new ObjectInputStream(input);

                frame = (Window) objInput.readObject();

                objInput.close();
                input.close();
            } catch (IOException | ClassNotFoundException ioe) {
                ioe.printStackTrace();
            }
        } else if (file.createNewFile())
            System.out.println("File has been created");


    }*/
    }
}