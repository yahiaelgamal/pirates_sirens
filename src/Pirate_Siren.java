import java.io.IOException;

public class Pirate_Siren{
    static int cp, cs; // count_pirates, count_sirens; 
    static Semaphore notValid   = new Semaphore(0, "not_valid");
    static int notValidNum 	= 0; 
    static Semaphore mutex    	= new Semaphore(1, "mutex");
    static Semaphore mutex2 	= new Semaphore(1, "mutex2");
    static boolean rowed 	= false;
    static int finishedCounter  = 4;
    
    // for in and out
    static Barrier boatLoadBarrier = new Barrier(4, "Boat_number_barrier");
    static MyVector threadsInBoat = new MyVector();

}


// ------------------- *********** ------------------
// -------------------- Character -------------------
// ------------------- *********** ------------------
abstract class Character extends Thread{
    String ID;
    static boolean dumb = false;
    
    public void run() {
        try
        {
            while (true) {
                Pirate_Siren.mutex.down(); // will be upped 
                // in the else part or in the board(); 
                if (checkValid()) { 
                        //checks if it's a valid combination
                    
                    board();
                    //sleepDumb(); // to test that the rowing is random
                    
                    Pirate_Siren.mutex2.down();

                    if (!Pirate_Siren.rowed) {
                        rowBoat();
                        Pirate_Siren.rowed = true;
                    }
                    
                    Pirate_Siren.mutex2.up();
            
                    break;
                }
                else
                {
                    Pirate_Siren.notValidNum++;
                    Pirate_Siren.mutex.up();
                    Pirate_Siren.notValid.down();
                }
            }
                
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        
        Pirate_Siren.mutex.down();
        Pirate_Siren.finishedCounter--;
        if (Pirate_Siren.finishedCounter == 0) {
            Pirate_Siren.rowed = false;
            Pirate_Siren.finishedCounter = 4;

            Pirate_Siren.cp = 0;
            Pirate_Siren.cs = 0;
            
            String temp = "";
            for (Character c : Pirate_Siren.threadsInBoat)
                    temp += c.toString() + ", "; 
            
            temp += "have reached their destination";
            ADTs.print(temp);
            System.out.println(temp);
            System.out.println("----------\n\n");
            ADTs.print("----------\n\n");

            Pirate_Siren.threadsInBoat.clear();
            int tempi = Pirate_Siren.notValidNum;
            for (int i = 0; i < tempi; i++){
                    Pirate_Siren.notValid.up();
                    Pirate_Siren.notValidNum--;
            }
        }
        Pirate_Siren.mutex.up();
        ADTs.print(Thread.currentThread() + " HAS FINISHED EXCUTION");

//  	System.out.println(Thread.currentThread() +
//	" HAS FINISHED EXCUTION");
    }

    public synchronized void sleepDumb(){
        try{
            dumb = !dumb;
            if (dumb){
                System.out.println(Thread.currentThread() + " is Zzzz");
                ADTs.print(Thread.currentThread() + " is Zzzz");
                sleep(4000);
            }
        }catch(Exception e){ 
            e.printStackTrace();
        }
    }
    
    public boolean checkValid() {
//      System.out.println(Thread.currentThread().toString()
//      + " CHECK\tcp is " + Pirate_Siren.cp
//	+  "   cs is " + Pirate_Siren.cs);
        int cptemp = Pirate_Siren.cp;
        int cstemp = Pirate_Siren.cs;
        
        if (this instanceof Siren)
            cstemp++;
        else
            cptemp++;
                
        boolean temp = cptemp + cstemp < 5;
        boolean temp2 = (cstemp==1 && cptemp==3) ||
                        (cptemp == 1 && cstemp==3);
        
        return temp && !temp2;
    }
    
    public synchronized void incrementMyType() {
        if (this instanceof Siren)
            Pirate_Siren.cs++;
        else
            Pirate_Siren.cp++;
    }
    
    /**
     * which is executed by each siren/pirate as they board the boat.
     * You should ensure that all four threads from each boatload call
     * this method before any of the threads from the next boatload do. 
     */
    public synchronized void board() {
        try {
            incrementMyType();
            Pirate_Siren.threadsInBoat.add(this);
            System.out.println(Thread.currentThread()
                    + " HAS BOARDED");
            Pirate_Siren.mutex.up();
            ADTs.print(Thread.currentThread() + " HAS BOARDED");
            Pirate_Siren.boatLoadBarrier.approach();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * which is execut/ed by exactly one of the threads in a boatload,
     * indicating that this thread is in charge of the oars. 
     * Once this call is made, you may assume that the boat empties
     * its load onto the rainy island and magically returns back for
     * the next load
     * @throws IOException 
     */
    public synchronized void rowBoat() throws IOException {
        try
        {
            System.out.println(Thread.currentThread() + " IS ROWING");
            ADTs.print(Thread.currentThread() + " IS ROWING");
    
            
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public String toString() {
            return ID;
    }
}


// ------------------- ********** ------------------
// --------------------- Pirate --------------------
// ------------------- ********** ------------------
class Pirate extends Character{
    static int count = 0; //simple counter to identify pirates.
    
    public Pirate() {
            super.ID = "PI" + count++;
    }
    
}

// ------------------- ********** ------------------
// --------------------- Siren ---------------------
// ------------------- ********** ------------------
class Siren extends Character{
    static int count = 0; // simple counter to identify Sirens
    
    public Siren() {
            super.ID = "SI" + count++;
    }
    
}
