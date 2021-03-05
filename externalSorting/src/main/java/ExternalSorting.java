import java.util.ArrayList;

/* external sorting of a large input file with random data */
public class ExternalSorting {
    private static final String fDir = "./"; // for storing and merging temporary files
    static final String fPath = "app.txt"; // large input file

    public static void main (String[] args) {

        /* generating a large input file with random data */
        int minLength = 5; // minimum line length
        int maxLength = 10; // maximum line length
        int linesFile = 100000; // total number of lines in the large input file
        GenerateRandomDataFile generateRandomDataFile = new GenerateRandomDataFile();
        generateRandomDataFile.generateRandomFile(minLength, maxLength, linesFile);

        /* external sorting */
        // splitting a large input file into chunks as temporary files
        ArrayList<Thread> splitThreads = new ArrayList<>();
        int countThreads = 8; // number of threads
        int linesThread = linesFile / countThreads; // total number of lines in one thread
        for (int i = 1; i <= countThreads; i++) {
            int startLine = i == 1 ? i : (i - 1) * linesThread + 1;
            int endLine = i * linesThread;
            SplitLargeFile mapThreads = new SplitLargeFile(fDir + "op" + i, startLine, endLine);
            splitThreads.add(mapThreads);
            mapThreads.start();
        }
        splitThreads.forEach(t -> {
            try {
                t.join();
            } catch (Exception ignored) {}
        });
        // merging generated temporary files
        int treeHeight = (int) (Math.log(countThreads) / Math.log(2));
        for (int i = 0; i < treeHeight; i++) {
            ArrayList<Thread> mergeThreads = new ArrayList<>();
            for (int j = 1, itr = 1; j <= countThreads / (i + 1); j += 2, itr++) {
                int offset = i * 100;
                String tempFile1 = fDir + "op" + (j + offset);
                String tempFile2 = fDir + "op" + ((j + 1) + offset);
                String opFile = fDir + "op" + (itr + ((i + 1) * 100));
                MergeTemporaryFiles reduceThreads = new MergeTemporaryFiles(tempFile1, tempFile2, opFile);
                mergeThreads.add(reduceThreads);
                reduceThreads.start();
            }
            mergeThreads.forEach(t -> {
                try {
                    t.join();
                } catch (Exception ignored) {}
            });
        }
    }
}