 
import java.io.*; 
import java.util.HashMap; 
 
class symbol { 
    String sym; 
    int addr; 
} 
 
class littab { 
    String lit; 
    int addr; 
} 
 
public class pass1 { 
 
    HashMap<String, Integer> OPTAB = new HashMap<>(); 
    HashMap<String, Integer> REGTAB = new HashMap<>(); 
    HashMap<String, Integer> CONDTAB = new HashMap<>(); 
    HashMap<String, Integer> ADTAB = new HashMap<>(); 
 
    int MAX = 20; 
 
    symbol SYMTAB[] = new symbol[MAX]; 
    littab LITTAB[] = new littab[MAX]; 
 
    String buffer; 
    int lc, litcnt = 0, poolcnt = 0, proc_lit = 0, symcount = 0; 
 
    pass1() { 
        initialize_OPTAB(); 
        initialize_REGTAB(); 
        initialize_CONDTAB(); 
        initialize_ADTAB(); 
 
        for (int i = 0; i < MAX; i++) SYMTAB[i] = new symbol(); 
        for (int i = 0; i < MAX; i++) LITTAB[i] = new littab(); 
    } 
 
    public void initialize_OPTAB() { 
        OPTAB.put("STOP", 0); OPTAB.put("ADD", 1); OPTAB.put("SUB", 2); 
        OPTAB.put("MULT", 3); OPTAB.put("MOVER", 4); OPTAB.put("MOVEM", 5); 
        OPTAB.put("COMP", 6); OPTAB.put("BC", 7); OPTAB.put("DIV", 8); 
        OPTAB.put("READ", 9); OPTAB.put("PRINT", 10); 
    } 
 
    public void initialize_REGTAB() { 
        REGTAB.put("AREG", 1); REGTAB.put("BREG", 2); 
        REGTAB.put("CREG", 3); REGTAB.put("DREG", 4); 
    } 
 
    public void initialize_CONDTAB() { 
        CONDTAB.put("LT", 1); CONDTAB.put("LE", 2); CONDTAB.put("EQ", 3); 
        CONDTAB.put("GT", 4); CONDTAB.put("GE", 5); CONDTAB.put("ANY", 6); 
    } 
 
    public void initialize_ADTAB() { 
        ADTAB.put("START", 1); ADTAB.put("END", 2); 
        ADTAB.put("ORIGIN", 3); ADTAB.put("EQU", 4); 
        ADTAB.put("LTORG", 5); 
    } 
 
    public int search_OPTAB(String str) { return OPTAB.getOrDefault(str, -1); } 
    public int search_REGTAB(String str) { return REGTAB.getOrDefault(str, -1); } 
    public int search_CONDTAB(String str) { return CONDTAB.getOrDefault(str, -1); } 
    public int search_ADTAB(String str) { return ADTAB.getOrDefault(str, -1); } 
 
    public int search_symbol(String str) { 
        for (int i = 0; i < symcount; i++) 
            if (str.equals(SYMTAB[i].sym)) return i; 
        return -1; 
    } 
 
    void passone(String filename) throws IOException { 
        BufferedReader fs = new BufferedReader(new FileReader(filename)); 
        BufferedWriter ft = new BufferedWriter(new FileWriter("ic.txt")); 
 
        while ((buffer = fs.readLine()) != null) { 
            buffer = buffer.trim(); 
            if (buffer.isEmpty()) continue; 
            String[] tokens = buffer.split("\\s+|,"); 
            int n = tokens.length, i, j, p, k; 
 
            switch (n) { 
                case 1: 
                    i = search_OPTAB(tokens[0]); 
                    if (i == 0) { ft.write("(IS," + String.format("%02d)", i)); lc++; break; } 
                    i = search_ADTAB(tokens[0]); 
                    if (i == 2 || i == 5) { 
                        for (j = proc_lit; j < litcnt; j++) LITTAB[j].addr = lc++; 
                        proc_lit = litcnt; 
                        ft.write("(AD," + String.format("%02d)", i)); 
                    } 
                    break; 
 
                case 2: 
                    i = search_ADTAB(tokens[0]); 
                    if (i == 1 || i == 3) { 
                        lc = Integer.parseInt(tokens[1]); 
                        ft.write("(AD," + String.format("%02d) (C,%s)", i, tokens[1])); 
                        break; 
                    } 
                    i = search_OPTAB(tokens[0]); 
                    if (i == 9 || i == 10) { 
                        p = search_symbol(tokens[1]); 
                        if (p == -1) { 
                            SYMTAB[symcount].sym = tokens[1]; 
                            ft.write("(IS," + String.format("%02d) (S,%02d)", i, ++symcount)); 
                        } else { 
                            ft.write("(IS," + String.format("%02d) (S,%02d)", i, p + 1)); 
                        } 
                        lc++; 
                        break; 
                    } 
                    break; 
 
                case 3: 
                    i = search_OPTAB(tokens[0]); 
                    if (i >= 1 && i <= 8) { 
                        lc++; 
                        k = (i == 7) ? search_CONDTAB(tokens[1]) : search_REGTAB(tokens[1]); 
                        if (tokens[2].startsWith("='")) { 
                            String litval = tokens[2].substring(2, tokens[2].length() - 1); 
                            LITTAB[litcnt].lit = litval; 
                            ft.write("(IS," + String.format("%02d) (%d)(L,%02d)", i, k, ++litcnt)); 
                        } else { 
                            p = search_symbol(tokens[2]); 
                            if (p == -1) { 
                                SYMTAB[symcount].sym = tokens[2]; 
                                ft.write("(IS," + String.format("%02d) (%d)(S,%02d)", i, k, ++symcount)); 
                            } else { 
                                ft.write("(IS," + String.format("%02d) (%d)(S,%02d)", i, k, p + 1)); 
                            } 
                        } 
                    } else if (tokens[1].equals("DS")) { 
                        p = search_symbol(tokens[0]); 
                        if (p == -1) { 
                            SYMTAB[symcount].sym = tokens[0]; 
                            SYMTAB[symcount++].addr = lc; 
                        } else SYMTAB[p].addr = lc; 
                        ft.write("(DL,02) (C," + tokens[2] + ")"); 
                        lc += Integer.parseInt(tokens[2]); 
                    } else if (tokens[1].equals("DC")) { 
                        p = search_symbol(tokens[0]); 
                        if (p == -1) { 
                            SYMTAB[symcount].sym = tokens[0]; 
                            SYMTAB[symcount++].addr = lc; 
                        } else SYMTAB[p].addr = lc; 
                        ft.write("(DL,01) (C," + tokens[2] + ")"); 
                        lc++; 
                    } 
                    break; 
 
                case 4: 
                    i = search_OPTAB(tokens[1]); 
                    if (i >= 1 && i <= 8) { 
                        p = search_symbol(tokens[0]); 
                        if (p == -1) { 
                            SYMTAB[symcount].sym = tokens[0]; 
                            SYMTAB[symcount++].addr = lc; 
                        } else SYMTAB[p].addr = lc; 
                        k = (i == 7) ? search_CONDTAB(tokens[2]) : search_REGTAB(tokens[2]); 
                        if (tokens[3].startsWith("='")) { 
                            String litval = tokens[3].substring(2, tokens[3].length() - 1); 
                            LITTAB[litcnt].lit = litval; 
                            ft.write("(IS," + String.format("%02d) (%d)(L,%02d)", i, k, ++litcnt)); 
                        } else { 
                            p = search_symbol(tokens[3]); 
                            if (p == -1) { 
                                SYMTAB[symcount].sym = tokens[3]; 
                                ft.write("(IS," + String.format("%02d) (%d)(S,%02d)", i, k, ++symcount)); 
                            } else { 
                                ft.write("(IS," + String.format("%02d) (%d)(S,%02d)", i, k, p + 1)); 
                            } 
                        } 
                        lc++; 
                    } 
                    break; 
            } 
            ft.write("\n"); 
        } 
        ft.close(); 
        fs.close(); 
    } 
 
    void print_littab() { 
        for (int i = 0; i < litcnt; i++) 
            System.out.println(LITTAB[i].lit + "\t" + LITTAB[i].addr); 
    } 
 
    void print_symtab() { 
        for (int i = 0; i < symcount; i++) 
            System.out.println(SYMTAB[i].sym + "\t" + SYMTAB[i].addr); 
    } 
 
    void print_srcfile(String filename) throws IOException { 
        BufferedReader fs = new BufferedReader(new FileReader(filename)); 
        while ((buffer = fs.readLine()) != null) System.out.println(buffer); 
        fs.close(); 
    } 
 
    void print_icfile() throws IOException { 
        BufferedReader fs = new BufferedReader(new FileReader("ic.txt")); 
        while ((buffer = fs.readLine()) != null) System.out.println(buffer); 
        fs.close(); 
    } 
 
    public static void main(String[] args) throws IOException { 
        if (args.length == 0) { 
            System.out.println("Usage: java pass1 <inputfile>"); 
            return; 
        } 
 
        pass1 obj = new pass1(); 
        String filename = args[0]; 
 
        obj.passone(filename); 
 
        System.out.println("SOURCE CODE\n"); 
        obj.print_srcfile(filename); 
 
        System.out.println("\n\n*************************************************"); 
        System.out.println("\n\nINTERMEDIATE CODE\n"); 
        obj.print_icfile(); 
 
        System.out.println("\n\n*************************************************"); 
        System.out.println("\n\nSYMBOL TABLE"); 
        System.out.println("================="); 
        System.out.println("Symbol\tAddress"); 
        System.out.println("================="); 
        obj.print_symtab(); 
 
        System.out.println("\n\n*************************************************"); 
        System.out.println("\n\nLITERAL TABLE"); 
        System.out.println("================="); 
        System.out.println("Literal\tAddress"); 
        System.out.println("================="); 
        obj.print_littab(); 
    } 
}