import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test void createsValidTransaction(){
        Transaction t=new Transaction("Groceries",LocalDate.of(2026,11,17),42.50,"Food shopping",Transaction.Type.EXPENSE);
        assertEquals("Groceries",t.getCategory()); assertEquals(42.50,t.getAmount());
        assertEquals(Transaction.Type.EXPENSE,t.getType());
    }
    @Test void rejectsNonPositiveAmount(){
        assertThrows(IllegalArgumentException.class,()->new Transaction("Rent",LocalDate.now(),0,"",Transaction.Type.EXPENSE));
    }
    @Test void roundsToCents(){
        Transaction t=new Transaction("Pay",LocalDate.now(),10.129,"",Transaction.Type.INCOME);
        assertEquals(10.13,t.getAmount(),0.001);
    }
}
