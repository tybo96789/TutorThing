
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
import javax.swing.table.*;
import javax.swing.filechooser.FileFilter;
import java.text.SimpleDateFormat;
// import java.*; // you know you want to p.s. i think i got rid of a few imports by accident

/**
 *
 * @author Tyler Atiburcio, Lawrence Ruffin
 * @version 1-ALPHA
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
        
        private JTabbedPane topRightSidePane = new JTabbedPane();
        private JTabbedPane topLeftSidePane = new JTabbedPane();
        
        private final int SCROLL_PANE_OFFSET_WIDTH = 0, SCROLL_PANE_OFFSET_HEIGHT = 500;

        private JPanel buttonPanel = new JPanel();
        private final int BUTTON_PANEL_OFFSET_WIDTH = 800, BUTTON_PANEL_OFFSET_HEIGHT = 200;
        private JPanel auxPanel = new JPanel();
        
        private JScrollPane sessionPane;
        private JScrollPane appointmentPane;
        
        private JPanel sessionPlaceHolderPanel = new JPanel();
        private JPanel sessionPanel =  new JPanel();
        private JPanel addSessionPlaceHolder = new JPanel();
        private JPanel appointmentPanel = new JPanel();
        

        //JLabels
        private JLabel fLabel = new JLabel("First Name");
        private JLabel lLabel = new JLabel("Last Name");
        private JLabel idLabel = new JLabel("ID");
        private JLabel courseLabel = new JLabel("Course");
        private JLabel instructorLabel = new JLabel("Instructor");
        private JLabel tutorLabel = new JLabel("Tutor");
        private JLabel startTime = new JLabel("Start Time");
        private JLabel commentsLable = new JLabel("Comments");
        private JLabel appointmentLable = new JLabel("Appointment?");
        private JLabel sessionLenLabel = new JLabel("Session Length (min)");

        //JTextFields
        private final int COL_WIDTH = 30;
        private JTextField fName = new JTextField();
        private JTextField lName = new JTextField();
        private JTextField iD = new JTextField();
        private JTextField course = new JTextField();
        private JTextField instructor = new JTextField();
        private JTextField tutor = new JTextField();
        private JTextField comments = new JTextField();
        private JTextField sessionLength = new JTextField();
        
        //JCheckBox
        private JCheckBox appointment = new JCheckBox();

        // Timers
        private final ArrayList<Timer> TIMERS = new ArrayList();
        private final SimpleDateFormat SDF = new SimpleDateFormat("mm:ss");

        // Buttons
        private final JButton ADD_BUTTON = new JButton("Add");
        private final JButton REMOVE_BUTTON = new JButton("Remove Selected Row");
        private final JButton CLEAR_BUTTON = new JButton("Clear Fields");
        //private final ArrayList<JButton> REMOVE_BUTTON = new ArrayList();
        private final JButton EXPORT_BUTTON = new JButton("Export");

        // JMenu
        private JMenu menu = new JMenu();
        private JMenuBar menuBar = new JMenuBar();
        private JMenuItem newItem;
        private JMenuItem openItem;
        private JMenuItem saveItem;
        private JMenuItem saveAsItem;
        private JMenuItem exitItem;
        
        private JMenu advance = new JMenu("Advance");
        private JMenu debug = new JMenu("Debug");
        private JMenuItem killTimers;
        private JMenuItem forceAdd;

        // JList (testing)
        //Application Stuff
        private DefaultListModel<Session> sessionListModel = new DefaultListModel<>();

        //Saving stuff
        private final String FILE_EXTENSION = ".CSV";
        private int startingSize = 0;
        private File file;
        private PrintStream out;
        private boolean isModifed = false;
        
        //JTable
        private final JTable SESSION_TABLE;
        private final JTable APPOINTMENT_TABLE;
        private final int ROW_HEIGHT = 25;
        
        SessionTableModel sessionTableModel = new SessionTableModel();
        AppointmentTableModel appointmentTableModel = new AppointmentTableModel();
        

        public TutorManagement() {
            //this.setIconImage(new ImageIcon("Sample.png").getImage());
           

            // draws components onto the table
            TableCellRenderer sessionButtonRenderer;
            TableCellRenderer sessionLabelRenderer;
            TableCellRenderer appointmentButtonRenderer;

            sessionTableModel.generateData();
            this.appointmentTableModel.generateData();
            SESSION_TABLE = new JTable(sessionTableModel);
            this.SESSION_TABLE.setRowHeight(ROW_HEIGHT);
            this.APPOINTMENT_TABLE= new JTable(this.appointmentTableModel);
            this.APPOINTMENT_TABLE.setRowHeight(ROW_HEIGHT);
            this.SESSION_TABLE.setEditingColumn(1);

            sessionButtonRenderer = SESSION_TABLE.getDefaultRenderer(JButton.class);
            sessionLabelRenderer = SESSION_TABLE.getDefaultRenderer(JLabel.class);
            appointmentButtonRenderer = this.APPOINTMENT_TABLE.getDefaultRenderer(JButton.class);

            SESSION_TABLE.setDefaultRenderer(JButton.class,
                    new ComponentRenderer(sessionButtonRenderer));
            SESSION_TABLE.setDefaultRenderer(JLabel.class,
                    new ComponentRenderer(sessionLabelRenderer));
            
            this.APPOINTMENT_TABLE.setDefaultRenderer(JButton.class, appointmentButtonRenderer);

            // Adjusts SESSION_TABLE
            //SESSION_TABLE.setPreferredScrollableViewportSize(new Dimension(400, 200));
            SESSION_TABLE.addMouseListener(new SessionTableMouseListener(SESSION_TABLE));
            this.APPOINTMENT_TABLE.addMouseListener(new AppointmentTableMouseListener(this.APPOINTMENT_TABLE));

            //Frame stuff
            this.setTitle(TITLE);
            this.setSize(INITAL_WIDTH, INITAL_HEIGHT);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.addWindowListener(new CloseWindowListener());

            // Layout
            this.setLayout(new BorderLayout());

            buildPanels();
            this.makeMenuBar();
            //this.add(buttonPanel, BorderLayout.EAST);

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
            sessionPane = new JScrollPane(SESSION_TABLE);
            
            //this.add(this.tabbedPane);
            this.setLayout(new GridLayout(2,1));
            this.topLeftSidePane.addTab("Sessions",this.sessionPane);
            //this.add(this.sessionPlaceHolderPanel);
            this.sessionPlaceHolderPanel.setLayout(new GridLayout(1,2));
            
            this.sessionPane.setAutoscrolls(true);
            //this.sessionPane.setBounds(0, 0, this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT);
            //this.sessionPane.setPreferredSize(new Dimension(this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT));
            //this.sessionPane.setBounds(0, 0, this.getWidth() /2, this.getHeight() /2);
            //this.sessionPane.setPreferredSize(new Dimension(this.getWidth() /2, this.getHeight() /2));
            this.sessionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.sessionPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            
            //Removed Appointment table
            //this.sessionPlaceHolderPanel.add(this.topLeftSidePane);
            this.add(this.topLeftSidePane);
        }

        private void buildPanels() {
            this.buildJTable();
            this.makeButtonPanel();
            this.add(this.addSessionPlaceHolder);
            this.makeAppointmentPanel();
            //this.sessionPanel.add(new JLabel("Current Sessions",JLabel.CENTER));
            //this.sessionPanel.add(this.topLeftSidePane);
            //this.appointmentPanel.add(new JLabel("Appointments",JLabel.CENTER));
            //this.appointmentPanel.add(this.topRightSidePane);
            this.topRightSidePane.addTab("Appointments", this.appointmentPane);
            this.topRightSidePane.addTab("Completed Sessions", new JLabel("HI"));
            this.ADD_BUTTON.setForeground(Color.BLUE);
            this.makeClearField();
        }
        
        private void makeButtonPanel()
        {
            //Button Panel
            //this.buttonPanel.setPreferredSize(new Dimension(this.getWidth() - BUTTON_PANEL_OFFSET_WIDTH, this.getHeight()-this.BUTTON_PANEL_OFFSET_HEIGHT));
            //this.buttonPanel.setBounds(0, 0, this.getWidth() - BUTTON_PANEL_OFFSET_WIDTH, this.getHeight()- this.BUTTON_PANEL_OFFSET_HEIGHT);
            
            this.buttonPanel.setLayout(new GridLayout(10, 2, 30, 10));
            
            
            this.buttonPanel.add(this.fLabel);
            this.fName.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.fName);

            this.buttonPanel.add(this.lLabel);
            this.lName.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.lName);

            this.buttonPanel.add(this.idLabel);
            this.iD.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.iD);

            this.buttonPanel.add(this.courseLabel);
            this.course.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.course);

            this.buttonPanel.add(this.instructorLabel);
            this.instructor.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.instructor);

            this.buttonPanel.add(this.tutorLabel);
            this.tutor.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.tutor);
            
            this.buttonPanel.add(this.commentsLable);
            this.comments.setColumns(COL_WIDTH);
            this.buttonPanel.add(this.comments);
            //Removed for appointment tabel
            //this.buttonPanel.add(this.appointmentLable);
            //this.buttonPanel.add(this.appointment);
            this.buttonPanel.add(this.sessionLenLabel);
            this.sessionLength.setColumns(COL_WIDTH);
            this.sessionLength.setText("30");
            this.buttonPanel.add(this.sessionLength);

            this.ADD_BUTTON.addActionListener(new AddButtonListener());
            //buttonPanel.add(ADD_BUTTON);
            
            
            this.addSessionPlaceHolder.add(this.buttonPanel);
        }
        
        private void makeAppointmentPanel()
        {
            this.appointmentPane = new JScrollPane(this.APPOINTMENT_TABLE);
            
            this.topRightSidePane.addTab("Appointments",this.appointmentPane);
            this.appointmentPane.setAutoscrolls(true);
            //this.appointmentPane.setBounds(0, 0, this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT);
            //this.appointmentPane.setPreferredSize(new Dimension(this.getWidth() - SCROLL_PANE_OFFSET_WIDTH, this.getHeight() - SCROLL_PANE_OFFSET_HEIGHT));
            //this.appointmentPane.setBounds(0, 0, this.getWidth()/2, this.getHeight()/2);
            //this.appointmentPane.setPreferredSize(new Dimension(this.getWidth()/2, this.getHeight()/2));
            this.appointmentPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.appointmentPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            this.sessionPlaceHolderPanel.add(this.topRightSidePane);
        }

        private void makeMenuBar() {
            //Make menu bar
            this.menuBar = new JMenuBar();
            this.menu = new JMenu("File");
            this.setJMenuBar(menuBar);
            this.menuBar.add(this.menu);
            this.menuBar.add(this.advance);
            this.advance.add(this.debug);

            //Make MenuItems
            this.newItem = new JMenuItem("New");
            this.openItem = new JMenuItem("Open");
            this.saveItem = new JMenuItem("Export");
            this.saveAsItem = new JMenuItem("Export as");
            this.exitItem = new JMenuItem("Exit");
            
            this.killTimers = new JMenuItem("Kill All Timers");
            this.forceAdd = new JMenuItem("Force Add");

            //Make MenuItems Accelerators
            this.newItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            this.openItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            this.saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_DOWN_MASK, true));

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
            this.killTimers.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent ae) {
                    if(JOptionPane.showConfirmDialog(INSTANCE,"This will stop all timers!\nAre you sure?", "Warning",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                    {
                        for(Timer t : INSTANCE.TIMERS)
                        {
                            t.stop();
                        }
                    }
                }
            });
            this.forceAdd.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel timeLabel = new JLabel("00:00");
                    JButton startButton = new JButton("Start");
                    JButton stopButton = new JButton("Stop");
                    int seconds = 0; 
                    
                    sessionTableModel.addData(INSTANCE.fName.getText().trim());
                    sessionTableModel.addData(INSTANCE.lName.getText().trim());
                    if(!INSTANCE.iD.getText().matches("@[0-9]{8}"))
                        sessionTableModel.addData("@" + INSTANCE.iD.getText().trim());
                    else
                        sessionTableModel.addData(INSTANCE.iD.getText().trim());
                    sessionTableModel.addData(INSTANCE.course.getText().trim());
                    sessionTableModel.addData(INSTANCE.instructor.getText().trim());
                    sessionTableModel.addData(INSTANCE.tutor.getText().trim());
                    sessionTableModel.addData(timeLabel);
                    sessionTableModel.addData(startButton);
                    sessionTableModel.addData(stopButton);
                    sessionTableModel.addData(INSTANCE.comments.getText().trim());
                    stopButton.addActionListener(new stopButtonListener());
                    Timer timer = new Timer(seconds, new TimerListener(INSTANCE.tutor.getText().trim(),Integer.parseInt(INSTANCE.sessionLength.getText().trim())));

                    // start time
                    timer.setDelay(1000);
                    timer.start();

                    // store timer into TIMERS arraylist
                    TIMERS.add(timer);

                    // Reset TextFields
                    INSTANCE.fName.setText("");
                    INSTANCE.lName.setText("");
                    INSTANCE.iD.setText("");
                    INSTANCE.course.setText("");
                    INSTANCE.instructor.setText("");
                    INSTANCE.tutor.setText("");
                    INSTANCE.comments.setText("");
                    INSTANCE.sessionLength.setText("30");
                }
            });

            //Add to Menu
            this.menu.add(this.newItem);
            this.menu.add(this.openItem);
            this.menu.add(this.saveItem);
            this.menu.add(this.saveAsItem);
            this.menu.add(this.exitItem);
            
            this.debug.add(this.killTimers);
            this.debug.add(this.forceAdd);

        }
        
        private void makeClearField()
        {
            this.buttonPanel.add(this.CLEAR_BUTTON);
            this.CLEAR_BUTTON.setForeground(Color.red);
            this.buttonPanel.add(this.ADD_BUTTON);
            this.buttonPanel.add(this.EXPORT_BUTTON);
            this.EXPORT_BUTTON.addActionListener(new ExportListener());
            this.CLEAR_BUTTON.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(JOptionPane.showConfirmDialog(INSTANCE, "Are You Sure you want to clear the fields?", "Question?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
                    {
                        INSTANCE.fName.setText("");
                        INSTANCE.lName.setText("");
                        INSTANCE.iD.setText("");
                        INSTANCE.course.setText("");
                        INSTANCE.instructor.setText("");
                        INSTANCE.tutor.setText("");
                        INSTANCE.comments.setText("");
                        INSTANCE.sessionLength.setText("30");
                    }
                    
                    
                }
            
        });
            this.buttonPanel.add(this.REMOVE_BUTTON);
            this.REMOVE_BUTTON.setForeground(Color.red);
            this.REMOVE_BUTTON.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent ae) {
                    final int sel = INSTANCE.SESSION_TABLE.getSelectedRow();
                    switch(JOptionPane.showConfirmDialog(INSTANCE, "Are you sure to delete row " + sel + ", with the client name\n" + INSTANCE.sessionTableModel.getValueAt(sel, 0) +"?"))
                    {
                        case JOptionPane.YES_OPTION:
                        {
                            
                            TIMERS.get(sel).stop();
                            INSTANCE.sessionTableModel.removeRow(sel);
                            
                        }
                    }
                    
                }
            });
                    }


        /**
         * This class allows us to listen to button events - primarily when it's
         * pressed.
         */
        class SessionTableMouseListener implements MouseListener {

            private final JTable TABLE;
            final int STOP_BUTTON_COL = 8;

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

                if (column == STOP_BUTTON_COL) {
                    TIMERS.get(row).stop();
                    INSTANCE.sessionListModel.get(row).setEndTime(System.currentTimeMillis());

                } else {
                    TIMERS.get(row).start();
                }


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

            @Override
            public void mouseClicked(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //this.forwardEventToButton(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //this.forwardEventToButton(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                this.forwardEventToButton(e);
            }
        }
        
        class AppointmentTableMouseListener implements MouseListener {

            private final JTable TABLE;
            final int STOP_BUTTON_COL = 8;

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
                for (int i = 0; i < INSTANCE.APPOINTMENT_TABLE.getColumnCount(); i++) {
                    if(i==5)
                    {
                        sessionTableModel.addData(new JLabel("00:00"));

                    }
                    else if(i == 6)
                    {
                        sessionTableModel.addData(new JButton("Start"));
                    }
                    else if(i == 7)
                    {
                        JButton stopButton = new JButton("Stop");
                        sessionTableModel.addData(stopButton);
                        stopButton.addActionListener(new stopButtonListener());
                    }
                    else
                        INSTANCE.sessionTableModel.addData(INSTANCE.appointmentTableModel.getValueAt(row, i));
                }
                
                INSTANCE.sessionListModel.addElement(new Session(System.currentTimeMillis(),
                     INSTANCE.fName.getText().trim(),
                     INSTANCE.lName.getText().trim(),
                     INSTANCE.iD.getText().trim(),
                     INSTANCE.course.getText().trim(),
                     INSTANCE.instructor.getText().trim(),
                     INSTANCE.tutor.getText().trim(),
                     INSTANCE.comments.getText().trim()));
                
                    int seconds = 0;

                    Timer timer = new Timer(seconds, new TimerListener(INSTANCE.tutor.getText().trim(),Integer.parseInt(INSTANCE.sessionLength.getText().trim())));

                    // start time
                    timer.setDelay(1000);
                    timer.start();

                    // store timer into TIMERS arraylist
                    TIMERS.add(timer);
                    
                    INSTANCE.appointmentTableModel.fireTableRowsDeleted(row, row);
                /*
                columnNames.add("First Name");
                columnNames.add("Last Name");
                columnNames.add("ID");
                columnNames.add("Course");
                columnNames.add("Instructor");
                columnNames.add("Tutor");
                columnNames.add("Appointment Time");
                columnNames.add("Start");
                columnNames.add("Comments");
                
                columnNames.add("First Name");
                columnNames.add("Last Name");
                columnNames.add("ID");
                columnNames.add("Course");
                columnNames.add("Instructor");
                columnNames.add("Tutor");
                columnNames.add("Elapsed Time");
                columnNames.add("Start");
                columnNames.add("Stop");
                columnNames.add("Comments");
                */


                /* 
                 This is necessary so that when a button is pressed and released
                 it gets rendered properly.  Otherwise, the button may still appear
                 pressed down when it has been released.
                 */
                TABLE.repaint();
            }

            public AppointmentTableMouseListener(JTable table) {
                this.TABLE = table;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //this.forwardEventToButton(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //this.forwardEventToButton(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.forwardEventToButton(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                this.forwardEventToButton(e);
            }
        }
       

        /**
         * Purpose of this class is to draw a in a cell when called button.
         */
        private class ComponentRenderer implements TableCellRenderer {

            private final TableCellRenderer defaultRenderer;

            public ComponentRenderer(TableCellRenderer renderer) {
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

        class SessionTableModel extends AbstractTableModel  {

            protected Vector sessionRow = new Vector();
            protected Vector columnNames = new Vector();

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
                columnNames.add("Comments");
                /*
                 JLabel timeLabel = new JLabel("0");
                 int seconds = 0;                                        
                    
                 sessionRow.add("test");
                 sessionRow.add("test");
                 sessionRow.add("test");
                 sessionRow.add("test");
                 sessionRow.add("test");
                 sessionRow.add("test");
                 sessionRow.add(timeLabel);
                 sessionRow.add(new JButton("Re/Start"));
                 sessionRow.add(new JButton("Stop"));
                 //System.out.println("listener " + sessionTableModel.sessionRow); // debugging aid
                    
                 Timer timer = new Timer(seconds, new TimerListener());
                 //timer.setCoalesce(false);
                 //timer.
                 //timer.isRepeats(false)
                 //timer.setRepeats(false);
                 timer.setDelay(1000);
                 timer.start();
                 */
            }

            public void addData(Object o) {
                sessionRow.add(o);
                this.fireTableRowsInserted(0, this.getRowCount());
                //System.out.println("here");
                // System.out.println("class " + sessionRow); // debugging aid
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
                return !(column == 6 || column == 7 || column == 8);
            }

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
            
            public void removeRow(int row)
            {
                this.sessionRow.remove(row);
                this.fireTableRowsDeleted(row, row);
                this.fireTableDataChanged();
            }
        }
        
        class AppointmentTableModel extends AbstractTableModel {

            protected Vector sessionRow = new Vector();
            protected Vector columnNames = new Vector();

            protected void generateData() {
                columnNames.add("First Name");
                columnNames.add("Last Name");
                columnNames.add("ID");
                columnNames.add("Course");
                columnNames.add("Instructor");
                columnNames.add("Tutor");
                columnNames.add("Appointment Time");
                columnNames.add("Start");
                columnNames.add("Comments");
            }

            public void addData(Object o) {
                sessionRow.add(o);
                this.fireTableRowsInserted(0, this.getRowCount());
                //System.out.println("here");
                // System.out.println("class " + sessionRow); // debugging aid
            }

            @Override
            public String getColumnName(int column) {
                return columnNames.get(column).toString();
            }

            @Override
            public int getRowCount() {
                //return sessionRow.size() / columnNames.size();
                return sessionRow.size();
            }

            @Override
            public int getColumnCount() {
                return columnNames.size();
            }

            @Override
            public Object getValueAt(int row, int column) {

                //return sessionRow.get((row * getColumnCount()) + column);
                return sessionRow.get(row * column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column==8);
            }

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        }

        private class TimerListener implements ActionListener {

            int colValue = 0;
            int row = sessionTableModel.getRowCount()-1;
            final String tutor;
            final int sessionLength;
            final String client;
            boolean moreSessions = false;
            
            public TimerListener(String s, int time)
            {
                super();
                this.tutor = s;
                this.sessionLength = (time*60)-5;
                this.client = (String) sessionTableModel.getValueAt(row, 0);
            }

            private void getTimePanelPos() {
                // local variable to avoid global changes                
                for (; colValue < sessionTableModel.getColumnCount(); colValue++) {
                    // increment colValue if the column doesn't hold a JLabel
                    if (sessionTableModel.getValueAt(row, colValue) instanceof JLabel) {
                        return;
                    }
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                getTimePanelPos();
                JLabel timeLabel = (JLabel) sessionTableModel.getValueAt(row, colValue);
                String formattedTime = timeLabel.getText();
                String[] timeArray = formattedTime.split(":");
                int time = Integer.parseInt(timeArray[0])*60 + Integer.parseInt(timeArray[1]);
                if(time == this.sessionLength || (time%1795== 0 && this.moreSessions))
                {
                    switch(JOptionPane.showConfirmDialog(INSTANCE,"Remind,\n" +this.tutor+ " with "+ this.client +"\nHas 5 Minutes left in the Session\nAdditional Session?"  , "5 Minute Warning", JOptionPane.YES_NO_OPTION))
                    {
                        case JOptionPane.YES_OPTION:
                            this.moreSessions = true; break;
                        default:
                            this.moreSessions = false;
                            break;
                    }
                }
                timeLabel.setText(SDF.format((time+1)*1000));
                repaint();
                //System.out.println("time: + " + time );
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
                        || INSTANCE.tutor.getText().isEmpty()
                        || INSTANCE.sessionLength.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "A Field is Missing Information", "Error", JOptionPane.ERROR_MESSAGE);
                } else if(!INSTANCE.iD.getText().matches("@?[0-9]{8}")) JOptionPane.showMessageDialog(INSTANCE, "ID is invalid", "Error", JOptionPane.ERROR_MESSAGE);
                else if(!INSTANCE.sessionLength.getText().matches("[0-9]+")) JOptionPane.showMessageDialog(INSTANCE, "Session Length Invalid", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    
                    if(INSTANCE.appointment.isSelected())
                    {
                    JButton startButton = new JButton("Start");
                        
                    // System.out.println("outer from: " + e.getSource()); // debugging aid
                    appointmentTableModel.addData(INSTANCE.fName.getText().trim());
                    appointmentTableModel.addData(INSTANCE.lName.getText().trim());
                    if(!INSTANCE.iD.getText().matches("@[0-9]{8}"))
                        appointmentTableModel.addData("@" + INSTANCE.iD.getText().trim());
                    else
                        appointmentTableModel.addData(INSTANCE.iD.getText().trim());
                    appointmentTableModel.addData(INSTANCE.course.getText().trim());
                    appointmentTableModel.addData(INSTANCE.instructor.getText().trim());
                    appointmentTableModel.addData(INSTANCE.tutor.getText().trim());
                    appointmentTableModel.addData(startButton);
                    appointmentTableModel.addData(INSTANCE.comments.getText().trim());
                    //System.out.println("listener " + sessionTableModel.sessionRow); // debugging aid
                    

                    
                    }
                    else {
                    JLabel timeLabel = new JLabel("00:00");
                    JButton startButton = new JButton("Start");
                    JButton stopButton = new JButton("Stop");
                    int seconds = 0;
                        
                    // System.out.println("outer from: " + e.getSource()); // debugging aid
                    sessionTableModel.addData(INSTANCE.fName.getText().trim());
                    sessionTableModel.addData(INSTANCE.lName.getText().trim());
                    if(!INSTANCE.iD.getText().matches("@[0-9]{8}"))
                        sessionTableModel.addData("@" + INSTANCE.iD.getText().trim());
                    else
                        sessionTableModel.addData(INSTANCE.iD.getText().trim());
                    sessionTableModel.addData(INSTANCE.course.getText().trim());
                    sessionTableModel.addData(INSTANCE.instructor.getText().trim());
                    sessionTableModel.addData(INSTANCE.tutor.getText().trim());
                    sessionTableModel.addData(timeLabel);
                    sessionTableModel.addData(startButton);
                    sessionTableModel.addData(stopButton);
                    sessionTableModel.addData(INSTANCE.comments.getText().trim());
                    stopButton.addActionListener(new stopButtonListener());
                    //System.out.println("listener " + sessionTableModel.sessionRow); // debugging aid
                    
                    //Add to sessionListModel
                    
                    INSTANCE.sessionListModel.addElement(new Session(System.currentTimeMillis(),
                     INSTANCE.fName.getText().trim(),
                     INSTANCE.lName.getText().trim(),
                     INSTANCE.iD.getText().trim(),
                     INSTANCE.course.getText().trim(),
                     INSTANCE.instructor.getText().trim(),
                     INSTANCE.tutor.getText().trim(),
                     INSTANCE.comments.getText().trim()));

                    
                    Timer timer = new Timer(seconds, new TimerListener(INSTANCE.tutor.getText().trim(),Integer.parseInt(INSTANCE.sessionLength.getText().trim())));

                    // start time
                    timer.setDelay(1000);
                    timer.start();

                    // store timer into TIMERS arraylist
                    TIMERS.add(timer);

                    
                    }
                    // Reset TextFields
                    INSTANCE.fName.setText("");
                    INSTANCE.lName.setText("");
                    INSTANCE.iD.setText("");
                    INSTANCE.course.setText("");
                    INSTANCE.instructor.setText("");
                    INSTANCE.tutor.setText("");
                    INSTANCE.comments.setText("");
                    INSTANCE.sessionLength.setText("30");
                    
                }
            }

        }// end AddButtonListener

        private class stopButtonListener implements ActionListener {

            int colValue = 9;
            int row = sessionTableModel.getRowCount() - 1;

            private void getStopPos() {
                // local variable to avoid global changes                
                for (; colValue < sessionTableModel.getColumnCount(); colValue++) {
                    // increment colValue if the column doesn't hold a JLabel
                    if (sessionTableModel.getValueAt(row, colValue) instanceof JLabel) {
                        return;
                    }
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                getStopPos();
                JButton stopButton = (JButton) sessionTableModel.getValueAt(row, colValue);

                System.out.println(stopButton.getActionListeners());
                //int t = Integer.parseInt(timeLabel.getText());
                //timeLabel.setText((t + 1) + "");
                //repaint();
                //System.out.println("inner from: " + e.getSource());
            }

        }

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
                if (!this.file.getName().toUpperCase().endsWith(INSTANCE.FILE_EXTENSION)) {
                    this.file = new File(this.file + INSTANCE.FILE_EXTENSION);
                }
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
         *////*
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
         //*/

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
    String fName, lName, iD, course, instructor, tutor, comments;

    public Session(Long start, String fName, String lName, String iD, String course, String instructor, String tutor, String comments) {
        this.start = start;
        this.fName = fName;
        this.lName = lName;
        this.iD = iD;
        this.course = course;
        this.instructor = instructor;
        this.tutor = tutor;
        this.comments = comments;
    }

    public void calcTime() {
        this.end = (System.nanoTime() - start) / ((long) Math.pow(10, 9));
    }
    
    public void calcTime(long time)
    {
        this.end = (time - start) / ((long) Math.pow(10, 9));
    }
    public void setEndTime(long time)
    {
        this.end = time;
    }

    @Override
    public String toString() {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(new Date(this.start)) + "," + this.lName + "," + this.fName + "," + this.iD + "," + this.course + "," + this.instructor + "," + this.tutor + "," + timeFormat.format(new Date(this.start)) + "," + timeFormat.format(new Date(this.end) + ","+ this.comments) ;
    }
}