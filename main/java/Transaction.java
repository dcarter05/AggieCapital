import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    public enum Type { INCOME, EXPENSE }
    private long id;
    private String category, description;
    private LocalDate date;
    private double amount;
    private Type type;

    public Transaction(long id, String category, LocalDate date, double amount, String description, Type type) {
        setId(id); setCategory(category); setDate(date); setAmount(amount); setDescription(description); setType(type);
    }
    public Transaction(String category, LocalDate date, double amount, String description, Type type) {
        this(0, category, date, amount, description, type);
    }
    public long getId(){ return id; }
    public void setId(long id){ if(id<0) throw new IllegalArgumentException("ID cannot be negative"); this.id=id; }
    public String getCategory(){ return category; }
    public void setCategory(String v){ if(v==null || v.isBlank()) throw new IllegalArgumentException("Category is required"); category=v.trim(); }
    public LocalDate getDate(){ return date; }
    public void setDate(LocalDate v){ date=Objects.requireNonNull(v, "Date is required"); }
    public double getAmount(){ return amount; }
    public void setAmount(double v){ if(!Double.isFinite(v) || v<=0) throw new IllegalArgumentException("Amount must be greater than zero"); amount=Math.round(v*100.0)/100.0; }
    public String getDescription(){ return description; }
    public void setDescription(String v){ description=v==null ? "" : v.trim(); }
    public Type getType(){ return type; }
    public void setType(Type v){ type=Objects.requireNonNull(v, "Type is required"); }
}
