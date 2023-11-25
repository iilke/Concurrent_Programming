import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AtomicAssignment implements Runnable {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm: ss:SSS ");
    private static Map<String, String> configuration = new HashMap<>();
    @Override
    public void run() {
        for(int i=0; i<5; i++){
            Map<String, String> currConfig = configuration; //ATOMIC STEP 1/2  //pointing at same map
            System.out.println("Atomic assignment run[" + Thread.currentThread().getName() + "]" + currConfig + "\n");
            String value1 = currConfig.get("key-1");
            String value2 = currConfig.get("key-2");
            String value3 = currConfig.get("key-3");
            if(!(value1.equals(value2) && value2.equals(value3))){
                throw new IllegalStateException("Values are not equal.");
            }
            try{
                Thread.sleep(1);
            }
            catch (InterruptedException e){//if a problem occurs while sleeping
                e.printStackTrace();
            }
        }
    }
    public static void readConfig(){//creates content inside configuration
        Map<String, String> newConfig = new HashMap<>();
        Date now = new Date();
        newConfig.put("key-1", sdf.format(now));
        newConfig.put("key-2", sdf.format(now));
        newConfig.put("key-3", sdf.format(now));
        configuration = newConfig; //ATOMIC STEP 2/2
        System.out.println("readConfig is called " + "[" + Thread.currentThread().getName() + "]" + configuration + "\n");
    }


    public static void main(String[] args) throws InterruptedException {
        readConfig();

        Thread configThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0 ; i<5 ; i++){
                    readConfig();
                }
                try{
                    Thread.sleep(10);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"configuration-thread");

        configThread.start();

        Thread[] threads = new Thread[3];
        for(int i=0; i<threads.length; i++){
            threads[i] = new Thread(new AtomicAssignment(),"thread-" + i);
            threads[i].start();
        }
        for(int i=0; i<threads.length; i++){
            threads[i] = new Thread(new AtomicAssignment(),"thread-" + i);
            threads[i].join();
        }
        configThread.join();
        System.out.println("[" + Thread.currentThread().getName() + "] All threads have finished");
    }
}
