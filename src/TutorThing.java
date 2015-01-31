
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tyler Atiburcio, Lawrence Ruffin
 */
public class TutorThing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TutorManagement();
    }

    private static class TutorManagement  extends JFrame{

        public TutorManagement() {
            
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
