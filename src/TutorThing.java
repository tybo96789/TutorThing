
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Tyler Atiburcio, Lawrence Ruffin
 * @version 0
 */
public class TutorThing {

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
        
        //Panel Stuff
        private ArrayList<Container> containers = new ArrayList<Container>();
        private JPanel panel = new JPanel();
        
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

            //Used to center the Window to the center of the screen no matter what computer you are using
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setMaximizedBounds(null);
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

            //To adjust the size of the text area when the frame size is adjusted
            //this.addComponentListener(new JFrameComponentAdaptor());            

            //Container stuff
            this.makeContainers();
            for (Container c : this.containers) {
                this.panel.add(c);
            }
            this.add(this.panel);

            //make it visable to the user
            this.setVisible(true);
            
        }

        private void makeContainers() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
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
