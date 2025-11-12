import java.util.Scanner; 
 
public class firstfit { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
 
        // Input memory blocks 
        System.out.print("Enter number of memory blocks: "); 
        int m = sc.nextInt(); 
        int blockSize[] = new int[m]; 
 
        System.out.println("Enter sizes of each memory block:"); 
        for (int i = 0; i < m; i++) { 
            blockSize[i] = sc.nextInt(); 
        } 
 
        // Input processes 
        System.out.print("Enter number of processes: "); 
        int n = sc.nextInt(); 
        int processSize[] = new int[n]; 
 
        System.out.println("Enter sizes of each process:"); 
        for (int i = 0; i < n; i++) { 
            processSize[i] = sc.nextInt(); 
        } 
 
        // Array to store block allocation for each process 
        int allocation[] = new int[n]; 
        for (int i = 0; i < n; i++) { 
            allocation[i] = -1; // -1 means not allocated 
        } 
 
        // First Fit Allocation 
        for (int i = 0; i < n; i++) { // process loop 
            for (int j = 0; j < m; j++) { // block loop 
                if (blockSize[j] >= processSize[i]) { 
                    allocation[i] = j; 
                    blockSize[j] -= processSize[i]; 
                    break; // Move to next process 
                } 
            } 
        }
           // Output results 
        System.out.println("\nProcess No.\tProcess Size\tBlock No."); 
        for (int i = 0; i < n; i++) { 
            System.out.print((i + 1) + "\t\t" + processSize[i] + "\t\t"); 
            if (allocation[i] != -1) 
                System.out.println(allocation[i] + 1); 
            else 
                System.out.println("Not Allocated"); 
        } 
        sc.close(); 
    }
}