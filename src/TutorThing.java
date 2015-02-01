
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.util.Vector;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;
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
        private Container listContainer = new Container();
        private Container buttonContainer = new Container();
        private JPanel panel = new JPanel();
  
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
        private final JButton ADD_BUTTON = new JButton("ADD");
        //private final JButton REMOVE_BUTTON = new JButton("REMOVE");
        private final JButton CLEAR_BUTTON = new JButton("CLEAR");
        private final ArrayList<JButton> REMOVE_BUTTON = new ArrayList();

        // JMenu (to be added) 
        private JMenu menu = new JMenu();
        private JMenuBar menuBar = new JMenuBar();
        private JMenuItem newItem;
        private JMenuItem openItem;
        private JMenuItem saveItem;
        private JMenuItem saveAsItem;
        private JMenuItem exitItem;

        // JList (testing)
        private JList LIST;// = new JList();
        String[] columnName = {"First Name", "Last Name", "ID", "Course", "Instructor", "Tutor", "Time Elapsed", "Start", "Remove"};

        private final static DefaultListModel<Session> T_LIST = new DefaultListModel();

        //Application Stuff
        private DefaultListModel<Session> sessionListModel = new DefaultListModel<>();

        //Saving stuff
        private final String FILE_EXTENSION = ".CSV";
        private int startingSize = 0;
        private File file;
        private PrintStream out;
        private boolean isModifed = false;

        private JTable SESSION_TABLE;
        private JScrollPane scrollPane;
        JTableButtonModel sessionTableModel = new JTableButtonModel();

        public TutorManagement() {
            TableCellRenderer defaultRenderer;

            sessionTableModel.generateData();
            SESSION_TABLE = new JTable(sessionTableModel);
            defaultRenderer = SESSION_TABLE.getDefaultRenderer(JButton.class);
            SESSION_TABLE.setDefaultRenderer(JButton.class,
                    new JTableButtonRenderer(defaultRenderer));
            SESSION_TABLE.setPreferredScrollableViewportSize(new Dimension(400, 200));
            SESSION_TABLE.addMouseListener(new SessionTableMouseListener(SESSION_TABLE));

            //Frame stuff
            this.setTitle(TITLE);
            this.setSize(INITAL_WIDTH, INITAL_HEIGHT);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.addWindowListener(new CloseWindowListener());

            // Layout
            this.setLayout(new BorderLayout());          

            buildPanels();
            this.makeMenuBar();
            this.add(buttonPanel, BorderLayout.EAST);

            //Used to center the Window to the center of the screen no matter what computer you are using
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setMaximizedBounds(null);
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

            //To adjust the size of the text area when the frame size is adjusted
            //this.addComponentListener(new JFrameComponentAdaptor());            

            //make it visable to the user
            this.setVisible(true);

        }

        private void buildJTable() {            
            //Scroll Pane            
            scrollPane = new JScrollPane(SESSION_TABLE);
            
            this.add(this.scrollPane, BorderLayout.CENTER);
            this.scrollPane.setAutoscrolls(true);            
            this.scrollPane.setBounds(0, 0, this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT);
            this.scrollPane.setPreferredSize(new Dimension(this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT));
            this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);            
        }
        
        private void buildPanels() {            
            this.buildJTable();
            
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

            this.ADD_BUTTON.addActionListener(new AddButtonListener());
            buttonPanel.add(ADD_BUTTON, BorderLayout.SOUTH);
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
            //this.newItem.addActionListener(new NewDocumentListener());
            //this.openItem.addActionListener(new OpenDocumentListener());
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


        /*
         private class ClockListener implements ActionListener {

         private int count;

         @Override
         public void actionPerformed(ActionEvent e) {
         count %= N;
         tf.setText(String.valueOf(count));
         count++;
         }
         }
         */
        /**
         * This class allows us to listen to button events - primarily when it's
         * pressed.
         */
        class SessionTableMouseListener implements MouseListener {

            private final JTable TABLE;

            private void forwardEventToButton(MouseEvent e) {
                TableColumnModel columnModel = TABLE.getColumnModel();
                int column = columnModel.getColumnIndexAtX(e.getX());
                int row = e.getY() / TABLE.getRowHeight();
                Object value;
                JButton button;
                MouseEvent buttonEvent;

                if (row >= TABLE.getRowCount() || row < 0 || column >= TABLE.getColumnCount() || column < 0) {
                    return;
                }

                value = TABLE.getValueAt(row, column);

                if (!(value instanceof JButton)) {
                    return;
                }

                button = (JButton) value;

                buttonEvent = (MouseEvent) SwingUtilities.convertMouseEvent(TABLE, e, button);
                button.dispatchEvent(buttonEvent);

                /* 
                 This is necessary so that when a button is pressed and released
                 it gets rendered properly.  Otherwise, the button may still appear
                 pressed down when it has been released.
                 */
                TABLE.repaint();
            }

            public SessionTableMouseListener(JTable table) {
                this.TABLE = table;
            }

            public void mouseClicked(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            public void mouseEntered(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            public void mouseExited(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            public void mousePressed(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            public void mouseReleased(MouseEvent e) {
                this.forwardEventToButton(e);
            }
        }

        /**
         * Purpose of this class is to draw a in a cell when called button.
         */
        private class JTableButtonRenderer implements TableCellRenderer {

            private TableCellRenderer defaultRenderer;

            public JTableButtonRenderer(TableCellRenderer renderer) {
                defaultRenderer = renderer;
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Component) {
                    return (Component) value;
                }
                return defaultRenderer.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
            }
        } // end JTableButtonRenderer

        static class JTableButtonModel extends AbstractTableModel {

            protected  Vector sessionRow = new Vector();
            protected  Vector columnNames = new Vector();

            protected void generateData() {                
                columnNames.add("First Name");
                columnNames.add("Last Name");
                columnNames.add("ID");
                columnNames.add("Course");
                columnNames.add("Instructor");
                columnNames.add("Tutor");
                columnNames.add("Elapsed Time");
                columnNames.add("Start");
                columnNames.add("Stop");                
            }

            public void addData(Object o) {
                sessionRow.add(o);
                this.fireTableRowsInserted(0, this.getRowCount());
                System.out.println("class " + sessionRow);
            }

            @Override
            public String getColumnName(int column) {
                return columnNames.get(column).toString();
            }

            @Override
            public int getRowCount() {                
                    return sessionRow.size() / columnNames.size();                               
            }

            @Override
            public int getColumnCount() {                
                return columnNames.size();
            }

            @Override
            public Object getValueAt(int row, int column) {

                return sessionRow.get((row * getColumnCount()) + column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
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
                } else {                    
                    // Timer newTimer = SESSION_TIMER.get(sessionTableModel.getRowCount() - 1);// = new Timer(0, null);
                    // newTimer = new Timer(0, null);
                    int seconds = 0;
                    
                    sessionTableModel.addData(INSTANCE.fName.getText().trim());
                    sessionTableModel.addData(INSTANCE.lName.getText().trim());
                    sessionTableModel.addData(INSTANCE.iD.getText().trim());
                    sessionTableModel.addData(INSTANCE.course.getText().trim());
                    sessionTableModel.addData(INSTANCE.instructor.getText().trim());
                    sessionTableModel.addData(INSTANCE.tutor.getText().trim());
                    sessionTableModel.addData(new Timer(0, null));
                    sessionTableModel.addData(new JButton("Re/Start"));
                    sessionTableModel.addData(new JButton("Stop"));
                    System.out.println("listener " + sessionTableModel.sessionRow);
                    
                    // Reset TextFields
                    INSTANCE.fName.setText("");
                    INSTANCE.lName.setText("");
                    INSTANCE.iD.setText("");
                    INSTANCE.course.setText("");
                    INSTANCE.instructor.setText("");
                    INSTANCE.tutor.setText("");                    
                }
            }

        }// end AddButtonListener

        private class RemoveButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        } // end RemoveButtonListener  

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
