
public class InterruptExample implements Runnable {

	
	@Override
	public void run() {
		try {
			Thread.sleep(Long.MAX_VALUE);//puts sleep for 290.000 years if unbothered
		}catch(InterruptedException e){
			System.out.println("["+Thread.currentThread().getName()+"] Interrupted by exception !");
		}
		while(!Thread.interrupted()) {
			//do nothing
		}
		System.out.println("["+Thread.currentThread().getName()+"] Interrupted for the second time");
	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		Thread myThread = new Thread(new InterruptExample(),"myThread");
		myThread.start();//calls run method that we implemented
		//run method puts myThread to sleep
		
		System.out.println("["+Thread.currentThread().getName()+"] Sleeping main thread for 5s...");
		Thread.sleep(5000);//right now both threads are asleep
		
		//After 5s finished, Main thread woke up
		
		System.out.println("["+Thread.currentThread().getName()+"]Interrupting myThread");
		myThread.interrupt();//myThread (child) woke up with an interruption
		
		System.out.println("["+Thread.currentThread().getName()+"] Sleeping in main thread for 5s...");
		Thread.sleep(5000);//Main is asleep again, at the same time myThread(child) is still running
		
		//after 5s main woke up again
		
				
		System.out.println("["+Thread.currentThread().getName()+"]Interrupting myThread");
		myThread.interrupt();//myThread(child) is interrupted again
		
	}
	
}
