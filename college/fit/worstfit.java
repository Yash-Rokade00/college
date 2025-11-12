import java.util.Scanner; 
 
public class worstfit { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
 
        // Input memory blocks 
        System.out.print("Enter number of memory blocks: "); 
        int memoryBlockCount = sc.nextInt(); 
        int[] memoryBlocks = new int[memoryBlockCount]; 
        System.out.println("Enter the sizes of memory blocks:"); 
        for (int i = 0; i < memoryBlockCount; i++) { 
            memoryBlocks[i] = sc.nextInt(); 
        } 
 
        // Input processes 
        System.out.print("Enter number of processes: "); 
        int processCount = sc.nextInt(); 
        int[] processes = new int[processCount]; 
        System.out.println("Enter the sizes of processes:"); 
        for (int i = 0; i < processCount; i++) { 
            processes[i] = sc.nextInt(); 
        } 
 
        int[] allocation = new int[processCount]; 
        for (int i = 0; i < allocation.length; i++) { 
            allocation[i] = -1; // initialize as not allocated 
        } 
 
        // Worst Fit allocation 
        for (int i = 0; i < processes.length; i++) { 
            int worstIdx = -1; 
            for (int j = 0; j < memoryBlocks.length; j++) { 
                if (memoryBlocks[j] >= processes[i]) { 
                    if (worstIdx == -1 || memoryBlocks[j] > memoryBlocks[worstIdx]) { 
                        worstIdx = j; 
                    } 
                } 
            } 
 
            if (worstIdx != -1) { 
                allocation[i] = worstIdx; 
                memoryBlocks[worstIdx] -= processes[i]; 
            } 
        } 
 
        // Print allocation results 
        System.out.println("\nProcess No.\tProcess Size\tBlock No."); 
        for (int i = 0; i < processes.length; i++) { 
            System.out.print((i + 1) + "\t\t" + processes[i] + "\t\t"); 
            if (allocation[i] != -1) { 
                System.out.println((allocation[i] + 1)); 
            } else { 
                System.out.println("Not Allocated"); 
            } 
        } 
 
        sc.close(); 
    } 
}