import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArraySumTask implements Callable<Integer> {
    private final int[] array;
    private final int startIndex;
    private final int endIndex;

    public ArraySumTask(int[] array, int startIndex, int endIndex) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        int[] array = new int[1000000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        int numThreads = 4;
        int chunkSize = array.length / numThreads;
        int startIndex = 0;
        int endIndex = chunkSize - 1;
        Future<Integer>[] futures = new Future[numThreads];
        for (int i = 0; i < numThreads; i++) {
            ArraySumTask task = new ArraySumTask(array, startIndex, endIndex);
            futures[i] = executor.submit(task);
            startIndex += chunkSize;
            endIndex += chunkSize;
        }

        int sum = 0;
        for (Future<Integer> future : futures) {
            sum += future.get();
        }

        System.out.println("Sum of array elements: " + sum);

        executor.shutdown();
    }
}
