
//import java.awt.Container;
//import java.awt.Dimension;
//import java.awt.Toolkit;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;
//import javax.swing.JFrame;
//import static javax.swing.JFrame.EXIT_ON_CLOSE;
//import javax.swing.JPanel;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
// import java.*; // you know you want to p.s. i think i got rid of a few imports by accident

/**
 *
 * @author Tyler Atiburcio, Lawrence Ruffin
 * @version 0
 */
public class TutorThing {

    // Scanner here for testing (will be removed)
    static Scanner s = new Scanner(System.in);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TutorManagement();
    }

    private static class TutorManagement  extends JFrame{
        
        
        //Frame Stuff
        private final String TITLE = "Tutoring Management Program";
        private final int INITAL_WIDTH = 1200, INITAL_HEIGHT = 800;
        private final TutorManagement INSTANCE = this;
        
        // Panel Stuff
        private ArrayList<Container> containers = new ArrayList<Container>();
        private Container listContainer = new Container();
        private Container buttonContainer = new Container();
        private JPanel panel = new JPanel();
        
        private JScrollPane listPanel;
        private final int SCROLL_PANE_OFFSET_WIDTH = 0, SCROLL_PANE_OFFSET_HEIGHT = 0;
        
        private JPanel buttonPanel = new JPanel();
        private final int BUTTON_PANEL_OFFSET_WIDTH = 800;
        
        //JLabels
        private JLabel fLabel = new JLabel("First Name");
        private JLabel lLabel = new JLabel("Last Name");
        private JLabel idLabel = new JLabel("ID");
        private JLabel courseLabel = new JLabel("Course");
        private JLabel instructorLabel = new JLabel("Instructor");
        private JLabel tutorLabel = new JLabel("Tutor");
        
        //JTextFields
        private final int COL_WIDTH = 30;
        private JTextField fName = new JTextField();
        private JTextField lName = new JTextField();
        private JTextField iD = new JTextField();
        private JTextField course = new JTextField();
        private JTextField instructor = new JTextField();
        private JTextField tutor = new JTextField();
        
        // Buttons
        private final JButton ADD = new JButton("ADD");
        private final JButton REMOVE = new JButton("REMOVE");

        // JMenu (to be added)
        // JList (testing)
        private static JList LIST;// = new JList();
        String[] test = {"t1", "t2", "t3"};
        private final static DefaultListModel<Session> T_LIST = new DefaultListModel();
        
        //Application Stuff
        private ArrayList<Session> list = new ArrayList<Session>();
        
        //Saving stuff
        private final String FILE_EXTENSION = ".CSV";
        private int startingSize= 0;
        private File file;
        private PrintStream out;
        private boolean isModifed = false;

        public TutorManagement() {
            
            //Frame stuff
            this.setTitle(TITLE);
            this.setSize(INITAL_WIDTH, INITAL_HEIGHT);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.addWindowListener(new CloseWindowListener());

            // Layout
            this.setLayout(new BorderLayout());
            
            // builds list
            this.buildList();
            
            // add containers
            this.containers.stream().forEach((c) -> {
                this.add(c);
            });
            
            buildPanels();
            //this.add(listPanel, BorderLayout.CENTER);
            this.add(buttonPanel, BorderLayout.EAST);

            //Used to center the Window to the center of the screen no matter what computer you are using
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setMaximizedBounds(null);
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

            //To adjust the size of the text area when the frame size is adjusted
            //this.addComponentListener(new JFrameComponentAdaptor());            

            /*
             //Container stuff
             for (Container c : this.containers) {
             this.panel.add(c);
             }
             this.add(this.panel);
             */
            //make it visable to the user
            this.setVisible(true);
            
        }

        private void buildList() {
            //LIST.a
            //LIST = new JList(test);
            //LIST.add("hello");
//            T_LIST.addElement("TEST");
            //T_LIST.
            LIST = new JList(T_LIST);
            LIST.setVisibleRowCount(10);
        }

        private void buildButtonPanel() {
            buttonPanel.add(ADD, BorderLayout.EAST);
        }

        private void buildPanels() {
            
            //Scroll Pane
            this.listPanel = new JScrollPane(LIST);
            //this.getContentPane().add(this.listPanel, BorderLayout.CENTER);
            this.add(this.listPanel, BorderLayout.CENTER);
            this.listPanel.setAutoscrolls(true);
            //this.listPanel.setViewportView(this.textArea);
            this.listPanel.setBounds(0, 0, this.getWidth()-SCROLL_PANE_OFFSET_WIDTH, this.getHeight()-SCROLL_PANE_OFFSET_HEIGHT);
            this.listPanel.setPreferredSize(new Dimension(this.getWidth()-SCROLL_PANE_OFFSET_WIDTH, this.getHeight()-SCROLL_PANE_OFFSET_HEIGHT));
            this.listPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.listPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            
            //listPanel.add(LIST, BorderLayout.CENTER);
            
            //Button Panel
            this.buttonPanel.setPreferredSize(new Dimension(this.getWidth()-BUTTON_PANEL_OFFSET_WIDTH, this.getHeight()));
            this.buttonPanel.setBounds(0, 0, this.getWidth()-BUTTON_PANEL_OFFSET_WIDTH, this.getHeight());
            
            this.buttonPanel.add(this.fLabel,BorderLayout.WEST);
            this.fName.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.fName, BorderLayout.EAST);
            
            this.buttonPanel.add(this.lLabel,BorderLayout.WEST);
            this.lName.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.lName, BorderLayout.EAST);
            
            this.buttonPanel.add(this.idLabel,BorderLayout.WEST);
            this.iD.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.iD, BorderLayout.EAST);
            
            this.buttonPanel.add(this.courseLabel,BorderLayout.WEST);
            this.course.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.course, BorderLayout.EAST);
            
            this.buttonPanel.add(this.instructorLabel,BorderLayout.WEST);
            this.instructor.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.instructor, BorderLayout.EAST);
            
            this.buttonPanel.add(this.tutorLabel,BorderLayout.WEST);
            this.tutor.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.tutor, BorderLayout.EAST);
            
            
            this.ADD.addActionListener(new AddButtonListener());
            buttonPanel.add(ADD, BorderLayout.SOUTH);
        }



        private void makeContainers() {
           //this.addActionListeners();

            //Container listContainer = new Container();
            listContainer.add(LIST, BorderLayout.CENTER);
            buttonContainer.add(ADD, BorderLayout.EAST);
            //containers.add(listContainer);
            //containers.add(buttonContainer);
        }
        
        private class AddButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(INSTANCE.fName.getText().isEmpty() ||
                        INSTANCE.lName.getText().isEmpty() ||
                        INSTANCE.iD.getText().isEmpty() ||
                        INSTANCE.course.getText().isEmpty() ||
                        INSTANCE.instructor.getText().isEmpty() ||
                        INSTANCE.tutor.getText().isEmpty() 
                  )
                {
                    JOptionPane.showMessageDialog(null, "A Field is Missing Information", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                
                    INSTANCE.list.add(new Session(System.currentTimeMillis(),
                        INSTANCE.fName.getText().trim(),
                        INSTANCE.lName.getText().trim(),
                        INSTANCE.iD.getText().trim(),
                        INSTANCE.course.getText().trim(),
                        INSTANCE.instructor.getText().trim(),
                        INSTANCE.tutor.getText().trim()));
                    
                    INSTANCE.fName.setText("");
                    INSTANCE.lName.setText("");
                    INSTANCE.iD.setText("");
                    INSTANCE.course.setText("");
                    INSTANCE.instructor.setText("");
                    INSTANCE.tutor.setText("");
                    /*
                    
                System.out.println("ADD BUTTON - BEGINNING ACTION");
                System.out.println("To save time and trouble, just enter all the info at once delimited by a space..."
                        + "format = (fname lname ID course instructor tutor)");
                //String line = s.nextLine();
                String firstName = s.next();
                String lastName = s.next();
                String ID = s.next();
                String course = s.next();
                String instructor = s.next();
                String tutor = s.next();

                //for (int i = 0; i < 10; i++) {
                System.out.println("adding to list...");
                T_LIST.addElement(new Session(System.currentTimeMillis(), firstName, lastName, ID, course, instructor, tutor));
                System.out.println("successfully added...");
                //}
                LIST = new JList(T_LIST);
                System.out.println("ADD BUTTON - ACTION FINISHED");
                    */
                }
            } 

                      
        }// end AddButtonListener
        
        private class RemoveButtonListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            } // end RemoveButtonListener  
        
        
        private void detectChanges()
        {
            if(this.startingSize != this.list.size())
            {
                this.isModifed = true;
                this.startingSize = this.list.size();
            }
            else
                this.isModifed = false;
        }
        
        private void saveData()
        {
            try{
                out = new PrintStream(this.file);
                for(Session s : this.list) out.println(s.toString());
                out.flush();
                out.close();
            }catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Opening File", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        /**
         * Listener to call save data when the user hits save
         */
        private class ExportListener implements ActionListener
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(INSTANCE.file == null) INSTANCE.saveDialog();
                else
                {
                    INSTANCE.saveData();
                }
            }
            
        }
        /**
         * If the user has not saved the new file before this method will open up a file explorer to ask the user
         * where to save the file
         */
        private void saveDialog()
        {
            JFileChooser chooser = new JFileChooser();
            chooser.showSaveDialog(null);
            this.file = chooser.getSelectedFile();
            
            try{
                
                if(file != null){
                    this.saveData();
                    this.isModifed = false;
                }
                
            }catch(Exception e)
            {                    
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Saving File", JOptionPane.ERROR_MESSAGE);
 
            }
            
        }
        
        /**
         * The ending action method will check if there is any unsaved changes to the document
         * and if there is unsaved changes then ask the user what to do
         */
        private void endingAction()
        {
            this.detectChanges();
            if(this.isModifed)
            {
                //System.out.println(JOptionPane.showConfirmDialog(null, "Do you want to save unsaved changes?", "Hey User!", JOptionPane.YES_NO_CANCEL_OPTION));
                switch(JOptionPane.showConfirmDialog(null, "Do you want to save current data?", "Hey User!", JOptionPane.YES_NO_CANCEL_OPTION))
                {
                    case 0:
                    {
                        if(this.file != null)
                        {
                            INSTANCE.saveData();
                            INSTANCE.dispose();
                            System.exit(0);
                            break;
                        }
                    }
                    
                    case 1:
                    {
                        INSTANCE.dispose();
                        System.exit(0);
                        break;
                    }
                }
                        
                
                //cancel op 2
                //no op 1
                //yes op 0
            }
            else
            {
                INSTANCE.dispose();
                System.exit(0);
            }
        }
        
        /**
         * This listener will call the saveDialog method when the user hits the save as button
         */
        private class ExportAsDocumentListener implements ActionListener
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                INSTANCE.saveDialog();
            }
            
        }
        
        /**
         * This action listener will check call the closing of the program
         */
        private class ExitListener implements ActionListener
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                INSTANCE.endingAction();
            }
            
        }
        
        private class CloseWindowListener implements WindowListener {

        public CloseWindowListener() {
        }

        @Override
        public void windowOpened(WindowEvent we) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowClosing(WindowEvent we) {
            INSTANCE.endingAction();
        }

        @Override
        public void windowClosed(WindowEvent we) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowIconified(WindowEvent we) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowDeiconified(WindowEvent we) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowActivated(WindowEvent we) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowDeactivated(WindowEvent we) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
    }

   

class Session {

    long start, end;
    String fName, lName, iD, course, instructor, tutor;

    public Session(Long start, String fName, String lName, String iD, String course, String instructor, String tutor) {
        this.start = start;
        this.fName = fName;
        this.lName = lName;
        this.iD = iD;
        this.course = course;
        this.instructor = instructor;
        this.tutor = tutor;
    }

    public long calcTime() {
        return (System.nanoTime() - start) / ((long) Math.pow(10, 9));
    }

    @Override
    public String toString() {
        return this.lName + "," + this.fName + "," + this.iD + "," + this.course + "," + this.instructor + "," + this.tutor + "," + this.start + "," + this.end;
    }
    
    

}
