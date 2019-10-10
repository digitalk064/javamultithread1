//Code from callicoder. Heavily modified
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class MyRunnable implements Runnable //RUNNABLE 
{
	public void run() //RUNNABLE: run() 
	{
		System.out.println("IT'S-A-ME, MARIO");
		System.out.println("Executing Task1 inside : " + Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException ex) {
			throw new IllegalStateException(ex);
		}
	}
}

class MyCallable implements Callable<Integer> //CALLABLE 
{
	@Override //MUST OVERRIDE
	public Integer call() throws Exception //CALLABLE: call() AND RETURN something AND throws Exception
	{
		System.out.println("Executing Callable Task4 inside : " + Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException ex) {
			return -1;
		}
		finally{
		return 69;
		}
	}
}

public class ExecutorsExample {
    public static void main(String[] args) {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service with a thread pool of Size 2");
        ExecutorService executorService = Executors.newFixedThreadPool(2); ///If you want all tasks to execute immediately, change this to 4
        Runnable task2 = () -> {
            System.out.println("Executing Runnable Task2 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };

        Callable<String> task3 = () -> {
            System.out.println("Executing Callable Task3 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
				return "Done";
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };


        System.out.println("Submitting the tasks for execution...");
        executorService.submit(new MyRunnable());
        executorService.submit(task2);
        Future<String> future1 = executorService.submit(task3);
		Future<Integer> future2 = executorService.submit(new MyCallable());
		
		//System.out.println(future1 + " " + future2); //NON-BLOCKING
		
		//executorService.shutdownNow(); //Very dangerous shit
		try{
			System.out.println("TASK 3,4 RESULTS: " + future1.get() + " " + future2.get()); //BLOCKING
		}
		catch(Exception e)
		{
			System.out.println("Sumting Wong: " + e);
		}
		
		//Let's try cancelling stuff mid-way
		try{
			Future<String> future3 = executorService.submit(task3); //Should wait 3 seconds...
			Thread.sleep(1000); //...but we cancel it after 1 second
			future3.cancel(true);
			System.out.println("Result: " + future3.get()); //.get() is the one throwing the exception here
		}
		catch(Exception e)
		{
			System.out.println(e); //So we get an exception. neat
		}
		executorService.shutdown(); //Non-blocking. Stop accepting tasks and will shutdown after all tasks finished
		
    }
}
