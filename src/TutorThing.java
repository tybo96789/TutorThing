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




class Session{
	double adjustment = (Math.pow(10,9));
	long a = (long)adjustment;
	Long start, end;
	String f, l, i, c, t;
    
	Session(String fname, String lname, String id, String c, String i, String tu, Long start){
    	f = fname;
    	l = lname;
    	i = id;
    	this.c = c;
    	this.i = i;
    	t = tu;
   	 
    	this.start = start;
	}
    
    
    
	public Long calcTime(){
   	 
    	Long e = System.nanoTime();
    	System.out.println("end = "+ e );
    	System.out.println("a = " + a);
    	return ((e - start))/a;
           	 
   	 
	}
    
}
