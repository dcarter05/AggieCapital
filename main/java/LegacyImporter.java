import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class LegacyImporter {
    private LegacyImporter(){}
    /** Imports the original budgetdata.txt transaction section once; malformed rows are skipped. */
    public static int importTransactions(Path file, TransactionRepository repo) throws IOException, java.sql.SQLException {
        List<String> lines=Files.readAllLines(file);
        int marker=-1;
        for(int i=5;i<lines.size();i++) if(lines.get(i).trim().equals("0")){ marker=i; break; }
        if(marker<0) throw new IOException("Legacy file does not contain the transaction-section marker.");
        int imported=0;
        for(int i=marker+1;i+2<lines.size();i+=3){
            String category=lines.get(i).trim(), details=lines.get(i+1).trim();
            String[] parts=details.split("\\s+",3);
            if(parts.length<2) continue;
            try{
                LocalDate date=parseDate(parts[0]);
                double amount=Double.parseDouble(parts[1]);
                // Original file uses category names to distinguish income and expenses.
                Transaction.Type type=category.equalsIgnoreCase("Work Check") || category.equalsIgnoreCase("Rental Income")
                    ? Transaction.Type.INCOME : Transaction.Type.EXPENSE;
                String description=i+2<lines.size()?lines.get(i+2).trim():"";
                repo.add(new Transaction(category,date,amount,description,type)); imported++;
            }catch(RuntimeException ignored){ /* Skip malformed legacy records. */ }
        }
        return imported;
    }
    private static LocalDate parseDate(String value){
        for(DateTimeFormatter f:List.of(DateTimeFormatter.ofPattern("M/d/yyyy"),DateTimeFormatter.ofPattern("M/d/yy"))){
            try{return LocalDate.parse(value,f);}catch(DateTimeParseException ignored){}
        }
        throw new DateTimeParseException("Invalid date",value,0);
    }
}
