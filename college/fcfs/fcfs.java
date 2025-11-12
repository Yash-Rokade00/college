import java.util.*; 
 
class Process { 
    int id, at, bt, ct, tat, wt, rt, priority, remaining; 
} 
 
public class fcfs { 
    static Scanner sc = new Scanner(System.in); 
 
    public static void main(String[] args) { 
        System.out.print("Enter number of processes: "); 
        int n = sc.nextInt(); 
        Process[] p = new Process[n]; 
 
        // Input 
        for (int i = 0; i < n; i++) { 
            p[i] = new Process(); 
            p[i].id = i + 1; 
            System.out.print("Arrival Time P" + p[i].id + ": "); 
            p[i].at = sc.nextInt(); 
            System.out.print("Burst Time P" + p[i].id + ": "); 
            p[i].bt = p[i].remaining = sc.nextInt(); 
            System.out.print("Priority P" + p[i].id + ": "); 
            p[i].priority = sc.nextInt(); 
        } 
 
        System.out.print("Enter Time Quantum for Round Robin: "); 
        int tq = sc.nextInt(); 
 
        // Run all algorithms one by one 
        fcfs(cloneProcesses(p), n);                // FCFS 
        sjfPreemptive(cloneProcesses(p), n);       // SJF (Preemptive) 
        priorityNonPreemptive(cloneProcesses(p), n); // Priority (Non-Preemptive) 
        roundRobin(cloneProcesses(p), n, tq);      // Round Robin 
    } 
 
    // Clone process array to avoid modifying original data 
    static Process[] cloneProcesses(Process[] p) { 
        Process[] clone = new Process[p.length]; 
        for (int i = 0; i < p.length; i++) { 
            clone[i] = new Process(); 
            clone[i].id = p[i].id; 
            clone[i].at = p[i].at; 
            clone[i].bt = p[i].bt; 
            clone[i].priority = p[i].priority; 
            clone[i].remaining = p[i].bt; 
        } 
        return clone; 
    } 
 
    // ---------- 1. FCFS ---------- 
    static void fcfs(Process[] p, int n) { 
        Arrays.sort(p, Comparator.comparingInt(a -> a.at)); 
        int time = 0; 
        for (Process pr : p) { 
            time = Math.max(time, pr.at) + pr.bt; 
            pr.ct = time; 
            pr.tat = pr.ct - pr.at; 
            pr.wt = pr.tat - pr.bt; 
        } 
        display(p, "FCFS"); 
    } 
 
    // ---------- 2. SJF (Preemptive) ---------- 
    static void sjfPreemptive(Process[] p, int n) { 
        int completed = 0, time = 0; 
        while (completed < n) { 
            Process curr = null; 
            for (Process pr : p) 
                if (pr.at <= time && pr.remaining > 0) 
                    if (curr == null || pr.remaining < curr.remaining) 
                        curr = pr; 
            if (curr == null) { time++; continue; } 
            curr.remaining--; 
            time++; 
            if (curr.remaining == 0) { 
                curr.ct = time; 
                curr.tat = curr.ct - curr.at; 
                curr.wt = curr.tat - curr.bt; 
                completed++; 
            } 
        } 
        display(p, "SJF (Preemptive)"); 
    } 
 
    // ---------- 3. Priority (Non-Preemptive) ---------- 
    static void priorityNonPreemptive(Process[] p, int n) { 
        boolean[] done = new boolean[n]; 
        int time = 0, completed = 0; 
        while (completed < n) { 
            Process curr = null; 
            for (int i = 0; i < n; i++) 
                if (!done[i] && p[i].at <= time) 
                    if (curr == null || p[i].priority < curr.priority) 
                        curr = p[i]; 
            if (curr == null) { time++; continue; } 
            time += curr.bt; 
            curr.ct = time; 
            curr.tat = curr.ct - curr.at; 
            curr.wt = curr.tat - curr.bt; 
            done[curr.id - 1] = true; 
            completed++; 
        } 
        display(p, "Priority (Non-Preemptive)"); 
    } 
 
    // ---------- 4. Round Robin ---------- 
    static void roundRobin(Process[] p, int n, int tq) { 
        Queue<Process> q = new LinkedList<>(); 
        int time = 0, completed = 0; 
        Arrays.sort(p, Comparator.comparingInt(a -> a.at)); 
        q.add(p[0]); 
        int index = 1; 
 
        while (!q.isEmpty()) { 
            Process curr = q.poll(); 
            if (curr.remaining > tq) { 
                time += tq; 
                curr.remaining -= tq; 
            } else { 
                time += curr.remaining; 
                curr.remaining = 0; 
                curr.ct = time; 
                curr.tat = curr.ct - curr.at; 
                curr.wt = curr.tat - curr.bt; 
                completed++; 
            } 
            while (index < n && p[index].at <= time) q.add(p[index++]); 
            if (curr.remaining > 0) q.add(curr); 
            if (q.isEmpty() && completed < n && index < n && p[index].at > time) 
                time = p[index].at; 
        } 
        display(p, "Round Robin"); 
    } 
 
    // ---------- Display ---------- 
    static void display(Process[] p, String algo) { 
        System.out.println("\n--- " + algo + " ---"); 
        System.out.println("PID\tAT\tBT\tCT\tTAT\tWT"); 
        double avgTAT = 0, avgWT = 0; 
        for (Process pr : p) { 
            System.out.println(pr.id + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + 
pr.wt); 
            avgTAT += pr.tat; avgWT += pr.wt; 
        } 
        System.out.printf("Average TAT: %.2f\n", avgTAT / p.length); 
        System.out.printf("Average WT: %.2f\n", avgWT / p.length); 
    } 
} 