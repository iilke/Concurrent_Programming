import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsumerProducer {
    private static final Queue queue = new ConcurrentLinkedQueue();
    private static final long startMillis = System.currentTimeMillis();

    public static class Consumer implements Runnable{

        @Override
        public void run() {
            while(System.currentTimeMillis() < (startMillis + 100)){
                synchronized (queue){
                    try{
                        queue.wait();
                        System.out.println("[" +Thread.currentThread().getName() + "]: " + "is waiting");
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("[" + Thread.currentThread().getName() + "]: " + " wakes up");
                if(!queue.isEmpty()){
                    Integer integer = (Integer) queue.poll();
                    System.out.println("[ " + Thread.currentThread().getName() + "]: " + integer);
                }
            }
        }
    }

    public static class Producer implements Runnable{
        @Override
        public void run() {
            int i=0;
            while(System.currentTimeMillis() < (startMillis + 100)){
                queue.add(i++);
                synchronized (queue){
                    queue.notify();
                    System.out.println("[" + Thread.currentThread().getName() + "]: notified : " + (i-1));
                }
                try{
                    Thread.sleep(5);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            synchronized (queue){
                queue.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        Thread[] consumerThreads = new Thread[5];
        for(int i=0;i< consumerThreads.length;i++){
            consumerThreads[i] = new Thread(new Consumer() , "consumer-" + i);
            consumerThreads[i].start();
        }

        Thread producerThread = new Thread(new Producer(), "producer");
        producerThread.start();

        for(int i=0 ; i< consumerThreads.length ; i++){
            consumerThreads[i].join();
        }
        producerThread.join();
    }

}
