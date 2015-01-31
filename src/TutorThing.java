/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tyler
 */
public class TutorThing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Session s = new Session(System.nanoTime());

        System.out.println("start = " + s.start + "\ndifference = " + s.calcTime());

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
        /*
         Long e = System.nanoTime();
         System.out.println("end = "+ e );
         System.out.println("a = " + a);
         return ((e - start))/a;
         */
        return (System.nanoTime() - start) / ((long) Math.pow(10, 9));
    }

}
