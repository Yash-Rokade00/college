import java.io.*;
import java.util.*;

public class pass2 {
    public static void main(String[] args) throws IOException {

        // --- File Readers ---
        BufferedReader icFile = new BufferedReader(new FileReader("C:/Users/yashr/Desktop/college/pass2/ic.txt"));
        BufferedReader symFile = new BufferedReader(new FileReader("C:/Users/yashr/Desktop/college/pass2/symbol.txt"));
        BufferedReader litFile = new BufferedReader(new FileReader("C:/Users/yashr/Desktop/college/pass2/littab.txt"));

        FileWriter output = new FileWriter("C:/Users/yashr/Desktop/college/pass2/pass2.txt");

        // --- Symbol and Literal Tables ---
        HashMap<Integer, String> symAddr = new HashMap<>();
        HashMap<Integer, String> litAddr = new HashMap<>();

        String line;
        int index = 1;

        // --- Read symbol table ---
        while ((line = symFile.readLine()) != null) {
            String[] parts = line.trim().split("\\s+"); // split by spaces/tabs
            if (parts.length >= 2) {
                symAddr.put(index++, parts[1]);
            }
        }

        // --- Read literal table ---
        index = 1;
        while ((line = litFile.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 2) {
                litAddr.put(index++, parts[1]);
            }
        }

        // --- Read Intermediate Code ---
        while ((line = icFile.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // --- AD,01 etc ---
            if (line.contains("AD,01") || line.contains("AD,02") || line.contains("AD,03") || line.contains("AD,04") || line.contains("AD,05")) {
                output.write("\n"); // assembler directives -> no machine code
                continue;
            }

            // --- IS,00 ---
            if (line.contains("IS,00")) {
                output.write("+ 00 0 000\n");
                continue;
            }

            // --- IS instructions ---
            if (line.contains("IS")) {
                // Example: (IS,04) (S,03)
                String[] parts = line.split("\\s+");
                String opcode = line.substring(line.indexOf("IS,") + 3, line.indexOf("IS,") + 5);
                output.write("+ " + opcode + " ");

                String registerPart = "0";
                String addressPart = "000";

                if (line.contains("(RG,")) {
                    // (RG,1) etc.
                    registerPart = line.replaceAll(".*\\(RG,([0-9]+)\\).*", "$1");
                } else if (line.contains("(C,")) {
                    registerPart = "0";
                } else if (line.contains("(-1)")) {
                    registerPart = "-1"; // for missing reg
                }

                output.write(registerPart + " ");

                if (line.contains("(S,")) {
                    int num = Integer.parseInt(line.replaceAll(".*\\(S,([0-9]+)\\).*", "$1"));
                    addressPart = symAddr.getOrDefault(num, "000");
                } else if (line.contains("(L,")) {
                    int num = Integer.parseInt(line.replaceAll(".*\\(L,([0-9]+)\\).*", "$1"));
                    addressPart = litAddr.getOrDefault(num, "000");
                } else if (line.contains("(C,")) {
                    addressPart = line.replaceAll(".*\\(C,([^\\)]+)\\).*", "$1");
                    if (addressPart.length() == 1)
                        addressPart = "00" + addressPart;
                    else if (addressPart.length() == 2)
                        addressPart = "0" + addressPart;
                } else {
                    addressPart = "000";
                }

                output.write(addressPart + "\n");
                continue;
            }

            // --- DL,01 (Define Constant) ---
            if (line.contains("DL,01")) {
                String constant = line.replaceAll(".*\\(C,([^\\)]+)\\).*", "$1");
                String value = String.format("%03d", Integer.parseInt(constant.replaceAll("'", "")));
                output.write("+ 00 0 " + value + "\n");
                continue;
            }

            // --- DL,02 (Reserve Space) ---
            if (line.contains("DL,02")) {
                output.write("+ 00 0 000\n");
                continue;
            }
        }

        icFile.close();
        symFile.close();
        litFile.close();
        output.close();

        System.out.println("✅ Pass-2 completed. Output written to pass2.txt");
    }
}
