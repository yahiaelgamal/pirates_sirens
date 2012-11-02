
##Synchronisation problems in Sirens and Pirates
	⁃	There are a number of synchronisation issues in SnP (Sirens and Pirates). 
	1.	How the sirens and pirates checks if it's valid for them to enter the boat. 
	⁃	###Analysis 
	⁃	Pirates and Sirens tries to enter the boat. Sometimes it's not valid for Pirates or sirens to enter the boat because of the current pirates/sirens numbers in the boat at a certain round. This problem must be solved without busy waiting.

	2.	Assume that 4 characters entered the boat, who will row. And how to satisfy the constraint that only one character in the boat can row and the rest shouldn't do anything. 
	⁃	###Analysis 
	⁃	This problem is a little bit different than the first one. Given that the boat is already loaded with 4 characters, those 4 characters should somehow checks if the position of the rower is empty or not. If it's empty, it should be taken by one and only one character. 

	3.	After rowing, the pirates/sirens who couldn't enter the boat should have the chance to try again. 
	⁃	###Analysis
	⁃	Assuming that the first problem was solved, another concern must raises. How will we ensure that pirates/sirens aren't just waiting for ever and give them the chance to check the validity of the boat again. 
	⁃	We also must must ensure that after the load is complete, no pirate/siren enters the boat unless a pirate/siren that is on the boat calls the method rowBoat();

##Main Classes
	⁃	My code consists of two files but several classes. The two files are ADTs.java and Pirate_Siren.java
	⁃	The main classes are 
	⁃	Pirate_Siren in Pirate_Siren.java, This class contains only variables that the Pirates, Sirens will use such as
	⁃	Two Semaphore for mutual exclusion  (mutex1, mutex2)
	⁃	One semaphore for Pirates/Sirens that couldn't enter the boat at a certain round. (not_valid)
	⁃	One Barrier to ensure that the boat is fully loaded (boatLoadBarrier)
	⁃	Three integers, 
	⁃	cp which counts the number of Pirates that are currently in the boat, 
	⁃	cs which counts the number of Sirens that are currently in the boat. 
	⁃	finishedCounter which counts the number of Pirates/Sirens that got out of the boat. 
	⁃	One boolean,  rowed which indicates whether someone is responsible of rowing or not.

	⁃	Character, It has all the methods that are common between Pirates and Sirens (All of them methods). This design decision was taken to ensure potential for extensions to the functionalities of Pirates and Sirens.
	⁃	Pirate
	⁃	Siren
	⁃	ADTs 

##How Semaphore was implemented. 
	⁃	The Semaphore class contains the following 
	⁃	private int counter to count which is equal always ups - downs
	⁃	private String name to help in debugging
	⁃	public synchronized boolean up() is used to up a semaphore. This is done by using notify() and then incrementing he counter. 
	⁃	public synchronized boolean down() is used to down a semaphore, this is done by decrementing the counter and waiting if the counter is less than 0. 

##How Barrier was implemented
	⁃	The Barrier class contains the following 
	⁃	private int maxWaiting This contains the number of threads that must be sleeping on the barrier before all of them get released. 
	⁃	private int currentWaiting This contains the number of threads that are currently sleeping on the barrier. 
	⁃	private String name used for debugging purposes. 
	⁃	public synchronized void approach() This is called whenever a threads wants to approach the barrier. This is done by incrementing the currentWaiting and checking if it is equal to the maxWaiting. If it is, notifyAll() will be called to release all threads that are waiting on the barrier. 

##How the problem was solved using semaphore and barriers.
	⁃	I have simulated the boat with boatLoadBarrier with maxWaitingNum equal to 4. This way I will ensure that the boat won't leave the shore unless it's fully loaded. 
	⁃	I have defined three critical regions. The first one is the check of availability on the boat and boarding. This was protected by mutex Semaphore. 
	⁃	The second critical region resides in the check whether the board is rowing or not, and rowing. This is protected by mutex2 Semaphore. 
	⁃	The last critical regions resides when the roundtrip of the boat finishes and parameters are reset. This is protected by the same semaphore that protects the first critical region mutex.
	⁃	The basic idea was that each siren/pirate do the following and semaphores/barriers will handle synchronization. 
	while (true){
		if (ValidToBoard) then{
			Board()
			if(noOneIsRowing) then{
				rowBoat();
				noOneIsRowing=false;
			}

			break;
		}
		else {
			waitForTheNextRound();
		}
	}

	// exiting the boat. 
	if (iAmTheLastPersonToLeave) then{ 
		resetBoatState();
		endBoatRound();
	}

##How to run the program. 
If using the terminal on a unix machine. Go to the directory of the project and compile the code using 
javac -d bin/ src/*
Then use the following command should be used to run the main method of the project. (there is only one main method and it resides in ADTs.java)
java -cp bin/ ADTs
you will be asked to enter the number of pirates and sirens for the run. Afterwards, the output will be of the following format 

The code assumes the following assumption. If those assumption aren't satisfied, an error message will appear and the program will terminate. 
Let P, S be the number of Pirates, Sirens respectively 
P + S = 0 (mod 4)
P != 1 (mod 4)
S != 1 (mod 4) 

X HAS BOARDED
Y HAS BOARDED
Z HAS BOARDED
W Has BOARDED

[XYZW] IS ROWING 
X, Y, Z, W  have reached their destination. 
----------

X HAS BOARDED
Y HAS BOARDED
Z HAS BOARDED
W Has BOARDED

[XYZW] IS ROWING 
X, Y, Z, W  have reached their destination. 
----------

.
.
.

The code assumes two assumption about the entered numbers, first
let P, S be the number of Pirates, Siren respectively
P + S = 0 (mod 4)
P ≠ 1 (mod 4)
S ≠ 1 (mod 4)

The code produces an absolutely detailed logger Logger.txt in the home directory of the project. 




	