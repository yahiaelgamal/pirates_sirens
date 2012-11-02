import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.Scanner;

public class ADTs
{
    public static synchronized void print(String str){
        try {
//	    System.out.println(str);
            FileWriter fw = new FileWriter(new File("Logger.txt"), true);
            fw.write(System.currentTimeMillis() + "\t" + str + "\n");
            fw.flush();
            fw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        
        FileWriter fw = new FileWriter(new File("Logger.txt"));
        fw.write("");
        fw.close();

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the number of Pirates.");
        int numPirates = sc.nextInt();
        
        System.out.println("Please enter the number of Sirens.");
        int numSirens  = sc.nextInt();

        if ((numPirates + numSirens)%4 == 0 && numPirates%4 != 1
                && numSirens%4 != 1) 
        {
            for (int i = 0; i < numPirates; i++)
            {
                Pirate p = new Pirate();
                p.start();
            }
            
            for (int i = 0; i < numSirens; i++) {
                Siren s = new Siren();
                s.start();
            }
        }
        else
            System.out.println("This combinations of Pirates/Sirens"
                +  " is not valid.");
            
    }
}

// ------------------- ********** ------------------
// ------------------- Semaphores ------------------
// ------------------- ********** ------------------
class Semaphore{
    private int count 	= 0;
    private String name = "";
    
    public Semaphore(int startingCount, String name) {
        this.count = startingCount;
        this.name  = name;
    }
    
    // return boolean just for the sake of consistency 
    // with the down()
    public synchronized boolean up() {
        String currentThread = Thread.currentThread().toString();
        
        notify();
        
        count++;
        
        ADTs.print(currentThread + " Called up on " + 
                        this.toString());
        
        return true;
    }
    
    // return true if the process which is calling
    // the method should sleep, false otherwise
    public synchronized void down() {
        try {
            String currentThread = Thread.currentThread().toString();
            
            count--;
            ADTs.print(currentThread + " Called down on " + 
                        this.toString());
            if (count < 0) {
                wait();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public String toString() {
        return name  +"(" + count + ")";
    }
}


// ------------------- ********** ------------------
// --------------------- Barrier -------------------
// ------------------- ********** ------------------
class Barrier{
    private int maxWaitingNum; // the waiting number after which waiting
                               // processes will be freed.
    
    private int currentWaiting = 0; //current number of waiting processes
    private String name;
    
    public Barrier(int num, String name) {
        maxWaitingNum = num;
        this.name = name;
    }
    
    public synchronized void approach()
                        throws InterruptedException, IOException {
        String currentThread = Thread.currentThread().toString();
        
        ADTs.print(currentThread + " approached that was " + 
                        this.toString() + " full");
        
        if (++currentWaiting == maxWaitingNum)
        {
            notifyAll();
            currentWaiting = 0;
            ADTs.print(this + " have released all");
        }
        else
            wait();
    }
    
    public String toString() {
        return name + " " + currentWaiting + " /"  + maxWaitingNum;
    }
}

class MyVector extends Vector<Character>{
    public boolean add(Character e) {
        boolean temp = super.add(e);
        ADTs.print(e + " add to the Vector .. " + temp);
        return temp;
    }
}
