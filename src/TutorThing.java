
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
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

    private static class TutorManagement extends JFrame {

        //Frame Stuff
        private final String TITLE = "Tutoring Management Program";
        private final int INITAL_WIDTH = 1200, INITAL_HEIGHT = 800;
        private final TutorManagement INSTANCE = this;

        // Panel Stuff
        private ArrayList<Container> containers = new ArrayList<Container>();
        private Container listContainer = new Container();
        private Container buttonContainer = new Container();
        private JPanel panel = new JPanel();

        private JScrollPane listPane;
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
        private JLabel startTime = new JLabel("Start Time");

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
        private JMenu menu = new JMenu();
        private JMenuBar menuBar = new JMenuBar();
        private JMenuItem newItem;
        private JMenuItem openItem;
        private JMenuItem saveItem;
        private JMenuItem saveAsItem;
        private JMenuItem exitItem;
        
        
        // JTable (testing)
        private JTable sessionTable = new JTable();
        private DefaultTableModel sessionTableModel = new DefaultTableModel(1, 7);

        // JList (testing)
        private JList LIST;// = new JList();
        String[] test = {"First Name", "Last Name", "ID", "Course", "Instructor", "Tutor", "Start Time"};

        private final static DefaultListModel<Session> T_LIST = new DefaultListModel();

        //Application Stuff
        private DefaultListModel<Session> sessionListModel = new DefaultListModel<>();

        //Saving stuff
        private final String FILE_EXTENSION = ".CSV";
        private int startingSize = 0;
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
            //this.buildList();
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

        private void buildJTable() {
            sessionTable = new JTable(sessionTableModel);
            sessionTableModel.removeRow(0);
            
            // Column names
            sessionTableModel.setColumnIdentifiers(test);
            
            //Scroll Pane
            this.listPane = new JScrollPane(sessionTable);
            //this.getContentPane().add(this.listPanel, BorderLayout.CENTER);
            this.add(this.listPane, BorderLayout.CENTER);
            this.listPane.setAutoscrolls(true);
            //this.listPanel.setViewportView(this.textArea);
            this.listPane.setBounds(0, 0, this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT);
            this.listPane.setPreferredSize(new Dimension(this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT));
            this.listPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.listPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
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

        private void buildListPanel() {
            //test1.add( new Session(System.currentTimeMillis(), "t1", "t1", "t1", "t1", "t1", "t1"));
            LIST = new JList(sessionListModel);
            //Scroll Pane
            this.listPane = new JScrollPane(LIST);
            //this.getContentPane().add(this.listPanel, BorderLayout.CENTER);
            this.add(this.listPane, BorderLayout.CENTER);
            this.listPane.setAutoscrolls(true);
            //this.listPanel.setViewportView(this.textArea);
            this.listPane.setBounds(0, 0, this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT);
            this.listPane.setPreferredSize(new Dimension(this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT));
            this.listPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.listPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        }

        private void buildPanels() {
            /*
             test1.add( new Session(System.currentTimeMillis(), "t1", "t1", "t1", "t1", "t1", "t1"));
             LIST = new JList(test1);
             //Scroll Pane
             this.listPane = new JScrollPane(LIST);
             //this.getContentPane().add(this.listPanel, BorderLayout.CENTER);
             this.add(this.listPane, BorderLayout.CENTER);
             this.listPane.setAutoscrolls(true);
             //this.listPanel.setViewportView(this.textArea);
             this.listPane.setBounds(0, 0, this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT);
             this.listPane.setPreferredSize(new Dimension(this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT));
             this.listPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
             this.listPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
             */
            //this.buildListPanel();
            this.makeMenuBar();
            this.buildJTable();
            //listPanel.add(LIST, BorderLayout.CENTER);
            //Button Panel
            this.buttonPanel.setPreferredSize(new Dimension(this.getWidth() - BUTTON_PANEL_OFFSET_WIDTH, this.getHeight()));
            this.buttonPanel.setBounds(0, 0, this.getWidth() - BUTTON_PANEL_OFFSET_WIDTH, this.getHeight());

            this.buttonPanel.add(this.fLabel, BorderLayout.WEST);
            this.fName.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.fName, BorderLayout.EAST);

            this.buttonPanel.add(this.lLabel, BorderLayout.WEST);
            this.lName.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.lName, BorderLayout.EAST);

            this.buttonPanel.add(this.idLabel, BorderLayout.WEST);
            this.iD.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.iD, BorderLayout.EAST);

            this.buttonPanel.add(this.courseLabel, BorderLayout.WEST);
            this.course.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.course, BorderLayout.EAST);

            this.buttonPanel.add(this.instructorLabel, BorderLayout.WEST);
            this.instructor.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.instructor, BorderLayout.EAST);

            this.buttonPanel.add(this.tutorLabel, BorderLayout.WEST);
            this.tutor.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.tutor, BorderLayout.EAST);

            this.ADD.addActionListener(new AddButtonListener());
            buttonPanel.add(ADD, BorderLayout.SOUTH);
        }
        
        private void makeMenuBar()
        {
            //Make menu bar
            this.menuBar = new JMenuBar();
            this.menu = new JMenu("File");
            this.setJMenuBar(menuBar);
            this.menuBar.add(this.menu);
            
            //Make MenuItems
            this.newItem = new JMenuItem("New");
            this.openItem = new JMenuItem("Open");
            this.saveItem = new JMenuItem("Export");
            this.saveAsItem = new JMenuItem("Export as");
            this.exitItem = new JMenuItem("Exit");
            
            
            //Make MenuItems Accelerators
            this.newItem.setAccelerator(KeyStroke.getKeyStroke('N',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(),true));
            this.openItem.setAccelerator(KeyStroke.getKeyStroke('O',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(),true));
            this.saveItem.setAccelerator(KeyStroke.getKeyStroke('S',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(),true));
            this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_DOWN_MASK,true));
            
            //Make MenuItems Mnemonic
            this.newItem.setMnemonic('N');
            this.openItem.setMnemonic('O');
            this.saveItem.setMnemonic('S');
            this.saveAsItem.setMnemonic('A');
            this.exitItem.setMnemonic('X');
            
            //Register Listeners to menuItems
            this.newItem.addActionListener(new NewDocumentListener());
            this.openItem.addActionListener(new OpenDocumentListener());
            this.saveItem.addActionListener(new ExportListener());
            this.saveAsItem.addActionListener(new ExportAsDocumentListener());
            this.exitItem.addActionListener(new ExitListener());
            
            //Add to Menu
            this.menu.add(this.newItem);
            this.menu.add(this.openItem);
            this.menu.add(this.saveItem);
            this.menu.add(this.saveAsItem);
            this.menu.add(this.exitItem);
            
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
                if (INSTANCE.fName.getText().isEmpty()
                        || INSTANCE.lName.getText().isEmpty()
                        || INSTANCE.iD.getText().isEmpty()
                        || INSTANCE.course.getText().isEmpty()
                        || INSTANCE.instructor.getText().isEmpty()
                        || INSTANCE.tutor.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "A Field is Missing Information", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else if(!INSTANCE.iD.getText().matches("@[0-9]{8}") && !INSTANCE.iD.getText().matches("[0-9]{8}")) JOptionPane.showMessageDialog(null, "ID is Invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                else {

                        
                   
                    Vector<String> fieldList = new Vector();
                    
                    fieldList.add(INSTANCE.fName.getText().trim());
                    fieldList.add(INSTANCE.lName.getText().trim());
                    if(INSTANCE.iD.getText().matches("[0-9]{8}"))
                        fieldList.add("@" + INSTANCE.iD.getText().trim());
                    else
                        fieldList.add(INSTANCE.iD.getText().trim());
                    fieldList.add(INSTANCE.course.getText().trim());
                    fieldList.add(INSTANCE.instructor.getText().trim());
                    fieldList.add(INSTANCE.tutor.getText().trim());
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    fieldList.add(dateFormat.format(new Date()));
                    /*
                    // * replaced the list - uncomment this part if you want to see what
                    // * the list version of this looked like.
                     INSTANCE.sessionListModel.addElement(new Session(System.currentTimeMillis(),
                     INSTANCE.fName.getText().trim(),
                     INSTANCE.lName.getText().trim(),
                     INSTANCE.iD.getText().trim(),
                     INSTANCE.course.getText().trim(),
                     INSTANCE.instructor.getText().trim(),
                     INSTANCE.tutor.getText().trim()));
                    
                    
                     // Update List
                     INSTANCE.LIST = new JList(sessionListModel);
                     //INSTANCE.listPane.add(INSTANCE.LIST);
                     LIST = new JList(sessionListModel);
                     //INSTANCE.buildListPanel();
                     */
                    /*
                    for (int i = 0; i < fieldList.size(); i++) {
                        sessionTableModel.setValueAt(fieldList.get(i), sessionTableModel.getRowCount() - 1, i);
                    }
                   */
                    
                    // adds a new row
                    sessionTableModel.addRow(fieldList);
                    
                    

                    // Reset TextFields
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
        
        
        /**
         * Broken..
         */
        private void detectChanges() {
            if (this.startingSize != this.sessionListModel.size()) {
                this.isModifed = true;
                this.startingSize = this.sessionListModel.size();
            } else {
                this.isModifed = false;
            }
        }

        private void saveData() {
            try {
                if(!this.file.getName().toUpperCase().endsWith(INSTANCE.FILE_EXTENSION))
                    this.file = new File(this.file + INSTANCE.FILE_EXTENSION);
                out = new PrintStream(this.file);
                /*
                 for (Session s : this.sessionListModel) {
                 out.println(s.toString());
                 }
                 */
                /*
                 I replaced the for loop above for the for loop below because the enhanced for loop 
                 only works on ojects that implement the iterable interface (DefaultListModel not being one of them)
                 so I made an equivalent for loop
                 */
                for (int i = 0; i < sessionListModel.getSize(); i++) {
                    out.println(sessionListModel.get(i).toString());
                }
                out.flush();
                out.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Opening File", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Listener to call save data when the user hits save
         */
        private class ExportListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (INSTANCE.file == null) {
                    INSTANCE.saveDialog();
                } else {
                    INSTANCE.saveData();
                }
            }

        }

        /**
         * If the user has not saved the new file before this method will open
         * up a file explorer to ask the user where to save the file
         */
        private void saveDialog() {
            JFileChooser chooser = new JFileChooser();
            FileFilter ff =  new FileFilter() {

        @Override
        public boolean accept(File f) {
            // TODO Auto-generated method stub
            return f.getName().endsWith(FILE_EXTENSION);
        }

        @Override
        public String getDescription() {
            return "CSV";
        }

    };
                    chooser.addChoosableFileFilter(ff);
                    chooser.setAcceptAllFileFilterUsed(true);
                    chooser.setFileFilter(ff);
            chooser.showSaveDialog(null);
            this.file =  chooser.getSelectedFile();
            //this.file =  new File(chooser.getSelectedFile().getName() + INSTANCE.FILE_EXTENSION);

            try {

                if (file != null) {
                    this.saveData();
                    this.isModifed = false;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Saving File", JOptionPane.ERROR_MESSAGE);

            }

        }

        /**
         * The ending action method will check if there is any unsaved changes
         * to the document and if there is unsaved changes then ask the user
         * what to do
         */
        private void endingAction() {
            this.detectChanges();
            if (this.isModifed) {
                //System.out.println(JOptionPane.showConfirmDialog(null, "Do you want to save unsaved changes?", "Hey User!", JOptionPane.YES_NO_CANCEL_OPTION));
                switch (JOptionPane.showConfirmDialog(null, "Do you want to save current data?", "Hey User!", JOptionPane.YES_NO_CANCEL_OPTION)) {
                    case 0: {
                        if (this.file != null) {
                            INSTANCE.saveData();
                            INSTANCE.dispose();
                            System.exit(0);
                            break;
                        }
                    }

                    case 1: {
                        INSTANCE.dispose();
                        System.exit(0);
                        break;
                    }
                }

                //cancel op 2
                //no op 1
                //yes op 0
            } else {
                INSTANCE.dispose();
                System.exit(0);
            }
        }

        /**
         * This listener will call the saveDialog method when the user hits the
         * save as button
         */
        private class ExportAsDocumentListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                INSTANCE.saveDialog();
            }

        }

        /**
         * This action listener will check call the closing of the program
         */
        private class ExitListener implements ActionListener {

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
    
    /**
     * TODO
     */
    private static class NewDocumentListener implements ActionListener {

        public NewDocumentListener() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    /**
     * TODO
     */
    private static class OpenDocumentListener implements ActionListener {

        public OpenDocumentListener() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
