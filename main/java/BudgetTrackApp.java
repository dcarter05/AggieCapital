import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class BudgetTrackApp extends JFrame {
    private final TransactionRepository repo;
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Date","Type","Category","Amount","Description"},0){
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);
    private final JLabel summary=new JLabel(" ");
    private final JTextField month=new JTextField(7);
    public BudgetTrackApp(TransactionRepository repo){
        super("BudgetTrack | Personal Finance Manager"); this.repo=repo;
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(950,600); setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add=new JButton("Add transaction"), edit=new JButton("Edit selected"), delete=new JButton("Delete selected"), refresh=new JButton("Refresh"), report=new JButton("Monthly report");
        month.setText(YearMonth.now().toString());
        top.add(add);top.add(edit);top.add(delete);top.add(refresh);top.add(new JLabel("Month (YYYY-MM):"));top.add(month);top.add(report);
        add(top,BorderLayout.NORTH); add(new JScrollPane(table),BorderLayout.CENTER);
        summary.setBorder(BorderFactory.createEmptyBorder(8,12,8,12)); add(summary,BorderLayout.SOUTH);
        add.addActionListener(e->editTransaction(null)); edit.addActionListener(e->editSelected());
        delete.addActionListener(e->deleteSelected()); refresh.addActionListener(e->load());
        report.addActionListener(e->showReport());
        load();
    }
    private void load(){
        try{
            model.setRowCount(0);
            for(Transaction t:repo.findAll()) model.addRow(new Object[]{t.getId(),t.getDate(),t.getType(),t.getCategory(),String.format("$%.2f",t.getAmount()),t.getDescription()});
            updateSummary();
        }catch(Exception ex){error(ex);}
    }
    private void updateSummary(){
        try{
            String m=month.getText().trim();
            double in=repo.total(Transaction.Type.INCOME,m), out=repo.total(Transaction.Type.EXPENSE,m);
            summary.setText(String.format("%s  |  Income: $%.2f  |  Expenses: $%.2f  |  Net: $%.2f",m,in,out,in-out));
        }catch(Exception ex){summary.setText("Enter month as YYYY-MM to view totals.");}
    }
    private void editSelected(){
        int row=table.getSelectedRow(); if(row<0){JOptionPane.showMessageDialog(this,"Select a transaction first.");return;}
        try{
            long id=(long)model.getValueAt(row,0);
            Transaction original=repo.findAll().stream().filter(t->t.getId()==id).findFirst().orElseThrow();
            editTransaction(original);
        }catch(Exception ex){error(ex);}
    }
    private void editTransaction(Transaction old){
        JTextField date=new JTextField(old==null?LocalDate.now().toString():old.getDate().toString());
        JTextField category=new JTextField(old==null?"":old.getCategory());
        JTextField amount=new JTextField(old==null?"":String.valueOf(old.getAmount()));
        JTextField description=new JTextField(old==null?"":old.getDescription());
        JComboBox<Transaction.Type> type=new JComboBox<>(Transaction.Type.values()); if(old!=null)type.setSelectedItem(old.getType());
        JPanel p=new JPanel(new GridLayout(0,2,6,6));
        p.add(new JLabel("Date (YYYY-MM-DD)"));p.add(date);p.add(new JLabel("Type"));p.add(type);
        p.add(new JLabel("Category"));p.add(category);p.add(new JLabel("Amount"));p.add(amount);
        p.add(new JLabel("Description"));p.add(description);
        if(JOptionPane.showConfirmDialog(this,p,old==null?"Add transaction":"Edit transaction",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            try{
                Transaction t=new Transaction(old==null?0:old.getId(),category.getText(),LocalDate.parse(date.getText().trim()),
                    Double.parseDouble(amount.getText().trim()),description.getText(),(Transaction.Type)type.getSelectedItem());
                if(old==null)repo.add(t);else repo.update(t);load();
            }catch(DateTimeParseException|IllegalArgumentException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Invalid input",JOptionPane.WARNING_MESSAGE);}
            catch(Exception ex){error(ex);}
        }
    }
    private void deleteSelected(){
        int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a transaction first.");return;}
        if(JOptionPane.showConfirmDialog(this,"Delete the selected transaction?","Confirm delete",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try{repo.delete((long)model.getValueAt(row,0));load();}catch(Exception ex){error(ex);}
        }
    }
    private void showReport(){
        try{
            String m=month.getText().trim(); YearMonth.parse(m);
            Map<String,Double> expenses=repo.totalsByCategory(Transaction.Type.EXPENSE,m);
            Map<String,Double> income=repo.totalsByCategory(Transaction.Type.INCOME,m);
            double in=repo.total(Transaction.Type.INCOME,m),out=repo.total(Transaction.Type.EXPENSE,m);
            StringBuilder s=new StringBuilder("MONTHLY BUDGET REPORT — "+m+"\n\nINCOME BY CATEGORY\n");
            income.forEach((k,v)->s.append(String.format("%-24s $%10.2f%n",k,v)));
            s.append(String.format("%nTotal income: $%.2f%n%nEXPENSES BY CATEGORY%n",in));
            expenses.forEach((k,v)->s.append(String.format("%-24s $%10.2f%n",k,v)));
            s.append(String.format("%nTotal expenses: $%.2f%nNet cash flow: $%.2f%n",out,in-out));
            JTextArea area=new JTextArea(s.toString(),22,55);area.setEditable(false);area.setFont(new Font(Font.MONOSPACED,Font.PLAIN,13));
            JOptionPane.showMessageDialog(this,new JScrollPane(area),"Monthly report",JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"Enter a valid month in YYYY-MM format.");}
    }
    private void error(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"BudgetTrack error",JOptionPane.ERROR_MESSAGE);}
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            try{
                Database db=new Database("data/budgettrack.db");db.initialize();
                TransactionRepository repo=new TransactionRepository(db);
                if(repo.findAll().isEmpty() && java.nio.file.Files.exists(java.nio.file.Path.of("budgetdata.txt"))){
                    int n=LegacyImporter.importTransactions(java.nio.file.Path.of("budgetdata.txt"),repo);
                    System.out.println("Imported "+n+" legacy transactions.");
                }
                new BudgetTrackApp(repo).setVisible(true);
            }catch(Exception ex){JOptionPane.showMessageDialog(null,ex.getMessage(),"Startup error",JOptionPane.ERROR_MESSAGE);}
        });
    }
}
