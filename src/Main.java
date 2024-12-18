import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        executorService.submit(() -> {
            long counter = 0;
            for (int i = 0; i <= 1_000_000; i++) {
                counter += i;
            }
            try {
                Files.write(Path.of("Files/first.txt"), String.valueOf(counter).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.submit(() -> {
            long counter1 = 0;
            for (int i = 1_000_000; i <= 2_000_000; i++) {
                counter1 += i;
            }
            try {
                Files.write(Path.of("Files/second.txt"), String.valueOf(counter1).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.submit(() -> {
            long counter2 = 0;
            for (int i = 2_000_000; i <= 3_000_000; i++) {
                counter2 += i;
            }
            try {
                Files.write(Path.of("Files/third.txt"), String.valueOf(counter2).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            executorService.shutdown();
            long zeroToMillion = Long.parseLong(Files.readString(Path.of("Files/first.txt")));
            long millionTo2Million = Long.parseLong(Files.readString(Path.of("Files/second.txt")));
            long twoMillionTo3Million = Long.parseLong(Files.readString(Path.of("Files/third.txt")));
            System.out.println("Total:" + (zeroToMillion + millionTo2Million + twoMillionTo3Million));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}