public class Contact {
    private String homeAddress="", phone="", email="";
    public Contact() {}
    public Contact(String homeAddress, String phone, String email) {
        setHomeAddress(homeAddress); setPhone(phone); setEmail(email);
    }
    public String getHomeAddress(){ return homeAddress; }
    public void setHomeAddress(String v){ homeAddress=v==null?"":v.trim(); }
    public String getPhone(){ return phone; }
    public void setPhone(String v){ phone=v==null?"":v.trim(); }
    public String getEmail(){ return email; }
    public void setEmail(String v){ email=v==null?"":v.trim(); }
}
