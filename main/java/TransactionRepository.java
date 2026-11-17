import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TransactionRepository {
    private final Database db;
    public TransactionRepository(Database db){ this.db=db; }
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> out=new ArrayList<>();
        try(Connection c=db.connect(); PreparedStatement p=c.prepareStatement("SELECT * FROM transactions ORDER BY date DESC,id DESC"); ResultSet r=p.executeQuery()){
            while(r.next()) out.add(map(r));
        } return out;
    }
    public void add(Transaction t) throws SQLException {
        String sql="INSERT INTO transactions(category,date,amount,description,type) VALUES(?,?,?,?,?)";
        try(Connection c=db.connect(); PreparedStatement p=c.prepareStatement(sql)){
            bind(p,t); p.executeUpdate();
        }
    }
    public void update(Transaction t) throws SQLException {
        String sql="UPDATE transactions SET category=?,date=?,amount=?,description=?,type=? WHERE id=?";
        try(Connection c=db.connect(); PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,t.getCategory()); p.setString(2,t.getDate().toString()); p.setDouble(3,t.getAmount());
            p.setString(4,t.getDescription()); p.setString(5,t.getType().name()); p.setLong(6,t.getId());
            if(p.executeUpdate()==0) throw new SQLException("Transaction not found.");
        }
    }
    public void delete(long id) throws SQLException {
        try(Connection c=db.connect(); PreparedStatement p=c.prepareStatement("DELETE FROM transactions WHERE id=?")){
            p.setLong(1,id); if(p.executeUpdate()==0) throw new SQLException("Transaction not found.");
        }
    }
    public Map<String,Double> totalsByCategory(Transaction.Type type, String month) throws SQLException {
        Map<String,Double> map=new LinkedHashMap<>();
        String sql="SELECT category,SUM(amount) total FROM transactions WHERE type=? AND substr(date,1,7)=? GROUP BY category ORDER BY total DESC";
        try(Connection c=db.connect(); PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,type.name()); p.setString(2,month);
            try(ResultSet r=p.executeQuery()){ while(r.next()) map.put(r.getString(1),r.getDouble(2)); }
        } return map;
    }
    public double total(Transaction.Type type, String month) throws SQLException {
        try(Connection c=db.connect(); PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(amount),0) FROM transactions WHERE type=? AND substr(date,1,7)=?")){
            p.setString(1,type.name()); p.setString(2,month);
            try(ResultSet r=p.executeQuery()){ return r.next()?r.getDouble(1):0; }
        }
    }
    private void bind(PreparedStatement p,Transaction t)throws SQLException{
        p.setString(1,t.getCategory());p.setString(2,t.getDate().toString());p.setDouble(3,t.getAmount());
        p.setString(4,t.getDescription());p.setString(5,t.getType().name());
    }
    private Transaction map(ResultSet r)throws SQLException{
        return new Transaction(r.getLong("id"),r.getString("category"),LocalDate.parse(r.getString("date")),
            r.getDouble("amount"),r.getString("description"),Transaction.Type.valueOf(r.getString("type")));
    }
}
