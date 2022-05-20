import java.io.*;
import java.util.*;

public class StoreManagement {
    private ArrayList<Staff> staffs;
    private ArrayList<String> workingTime;
    private ArrayList<Invoice> invoices;
    private ArrayList<InvoiceDetails> invoiceDetails;
    private ArrayList<Drink> drinks;

    public StoreManagement(String staffPath, String workingTimePath, String invoicesPath, String detailsPath, String drinksPath) {
        this.staffs = loadStaffs(staffPath);
        this.workingTime = loadFile(workingTimePath);
        this.invoices = loadInvoices(invoicesPath);
        this.invoiceDetails = loadInvoicesDetails(detailsPath);
        this.drinks = loadDrinks(drinksPath);
    }

    public ArrayList<Staff> getStaffs() {
        return this.staffs;
    }
    
    public void setStaffs(ArrayList<Staff> staffs){
        this.staffs = staffs;
    }

    
    public ArrayList<Drink> loadDrinks(String filePath) {
        ArrayList<Drink> drinksResult = new ArrayList<Drink>();
        ArrayList<String> drinks = loadFile(filePath);

        for (String drink : drinks) {
            String[] information = drink.split(",");
            drinksResult.add(new Drink(information[0], Integer.parseInt(information[1])));
        }

        return drinksResult;
    }

    public ArrayList<Invoice> loadInvoices(String filePath) {
        ArrayList<Invoice> invoiceResult = new ArrayList<Invoice>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new Invoice(information[0], information[1], information[2]));
        }
        

        return invoiceResult;
    }

    public ArrayList<InvoiceDetails> loadInvoicesDetails(String filePath) {
        ArrayList<InvoiceDetails> invoiceResult = new ArrayList<InvoiceDetails>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new InvoiceDetails(information[0], information[1], Integer.parseInt(information[2])));
        }

        return invoiceResult;
    }

    // requirement 1
    public ArrayList<Staff> loadStaffs(String filePath){
        //Declare an ArrayList to store Staffs
        ArrayList<Staff> st = new ArrayList<>();

        //Declare a File 
        File listOfStaffs = new File(filePath);
        
        //Using try catch to throw exception
        try {
            
            //Declare a BufferedReader to read the file
            BufferedReader myReader = new BufferedReader(new FileReader(listOfStaffs));
            //Read the first line of the file
            String lines =  myReader.readLine(); 

            while(lines != null){                 
                //Split the line by ","               
                String[] information = lines.split(",");  
                                        
            //Check if the Staff is FullTimeStaff or SeasonalStaff or Manager
                // QL = Manager
                if (information[0].substring(0, 2).equals("QL")) {
                    st.add(new Manager(information[0], information[1], Integer.parseInt(information[2]), Double.parseDouble(information[3]), Integer.parseInt(information[4])));
                }
                // CT = FullTimeStaff 
                else if (information[0].substring(0, 2).equals("CT")) {
                    st.add(new FullTimeStaff(information[0], information[1], Integer.parseInt(information[2]), Double.parseDouble(information[3])));
                } 
                // TV = SeasonalStaff
                else {
                    st.add(new SeasonalStaff(information[0], information[1], Integer.parseInt(information[2])));
                }
                //Read the next line
                lines = myReader.readLine();
            }
            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return st;

    }

    
    // requirement 2
    public ArrayList<SeasonalStaff> getTopFiveSeasonalStaffsHighSalary() {
        ArrayList<String> timeSeasonalStaffs = new ArrayList<>();
        
        Map <String,Double> mapping = new TreeMap<String, Double>();
        for (String strings : this.workingTime){
            String[] information = strings.split(",");
            if (information[0].substring(0, 2).equals("TV")) {
                timeSeasonalStaffs.add(strings);
            }
        }

        for (Staff staff : staffs) {
            if (staff instanceof SeasonalStaff) {
                for (String workingTime : this.workingTime){
                    String[] information = workingTime.split(",");
                    if (staff.getsID().equals(information[0])) {
                        mapping.put(staff.getsID(), staff.paySalary(Integer.parseInt(information[1])));
                    }
                }
            }
        }
        
        //USe collections to sort the map descending
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(mapping.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        
        //Create a new LinkedHashMap to store the sorted map
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        
        //Create a new ArrayList to store the top 5 seasonal staffs from sortedMap
        ArrayList<SeasonalStaff> topFive = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            if (i < 5) {
                for (Staff staff : staffs) {
                    if (staff instanceof SeasonalStaff) {
                        if (staff.getsID().equals(entry.getKey())) {
                            topFive.add((SeasonalStaff) staff);
                        }
                    }
                }
            }
            i++;
        }
        return topFive;
        
    }

    // requirement 3
    public ArrayList<FullTimeStaff> getFullTimeStaffsHaveSalaryGreaterThan(int lowerBound) {
        ArrayList<String> fullTimeStaffs = new ArrayList<>();
        
        Map <String,Double> mapping = new LinkedHashMap<String, Double>();
        for (String strings : this.workingTime){
            String[] information = strings.split(",");
            if (information[0].substring(0, 2).equals("QL") || information[0].substring(0, 2).equals("CT")) {
                fullTimeStaffs.add(strings);
            }
        }
       
        for (Staff staff : staffs) {
            if (staff instanceof FullTimeStaff  || staff instanceof Manager ) {
                for (String workingTime : this.workingTime){
                    String[] information = workingTime.split(",");
                    if (staff.getsID().equals(information[0])) {
                        mapping.put(staff.getsID(), staff.paySalary(Integer.parseInt(information[1])));
                    }
                }
            }
        }
        
        
        //Check which one is greater than the lowerBound
        ArrayList<FullTimeStaff> fullTimeStaffsGreaterThan = new ArrayList<>();
        for (Map.Entry<String, Double> entry : mapping.entrySet()) {
            if (entry.getValue() > lowerBound) {
                for (Staff staff : staffs) {
                    if (staff instanceof FullTimeStaff || staff instanceof Manager) {
                        if (staff.getsID().equals(entry.getKey())) {
                            fullTimeStaffsGreaterThan.add((FullTimeStaff) staff);
                            
                        }
                    }
                }
            }
        }
        
        return fullTimeStaffsGreaterThan;
    }

    // requirement 4
    public double totalInQuarter(int quarter) {
        double total = 0;
        
        for (Invoice invoice : invoices){
            String[] information = invoice.getDate().split("/");

            switch(quarter){
                case 1:
                    if (Integer.parseInt(information[1]) <= 3) {
                        for (Drink drink : drinks){
                            for(InvoiceDetails inveDetails: invoiceDetails){
                                if (inveDetails.getInvoiceID().equals(invoice.getInvoiceID()) && inveDetails.getDName().equals(drink.getdName())){
                                    total += drink.getPrice() * inveDetails.getAmount();
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    if (Integer.parseInt(information[1]) > 3 && Integer.parseInt(information[1]) <= 6) {
                        for (Drink drink : drinks){
                            for(InvoiceDetails inveDetails: invoiceDetails){
                                if (inveDetails.getInvoiceID().equals(invoice.getInvoiceID()) && inveDetails.getDName().equals(drink.getdName())){
                                    total += drink.getPrice() * inveDetails.getAmount();
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    if (Integer.parseInt(information[1]) > 6 && Integer.parseInt(information[1]) <= 9) {
                        for (Drink drink : drinks){
                            for(InvoiceDetails inveDetails: invoiceDetails){
                                if (inveDetails.getInvoiceID().equals(invoice.getInvoiceID()) && inveDetails.getDName().equals(drink.getdName())){
                                    total += drink.getPrice() * inveDetails.getAmount();
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if (Integer.parseInt(information[1]) > 9) {
                        for (Drink drink : drinks){
                            for(InvoiceDetails inveDetails: invoiceDetails){
                                if (inveDetails.getInvoiceID().equals(invoice.getInvoiceID()) && inveDetails.getDName().equals(drink.getdName())){
                                    total += drink.getPrice() * inveDetails.getAmount();
                                }
                            }
                        }
                    }
                    break;
            }
            
        }
        return total;
    }
    
    // requirement 5
    public Staff getStaffHighestBillInMonth(int month) {
        Staff maxStaff = null;
        List<String> staffID = new ArrayList<>();
        List<Double> total = new ArrayList<>();
        for (Invoice inve : invoices){
            double prices =0;
            String[] information = inve.getDate().split("/");
            staffID.add(inve.getStaffID());
            if (Integer.parseInt(information[1]) == month) {
                for (InvoiceDetails inveDetails : invoiceDetails){
                    if (inveDetails.getInvoiceID().equals(inve.getInvoiceID())){
                        for (Drink drink : drinks){
                            if (inve.getInvoiceID().equals(inveDetails.getInvoiceID()) && inveDetails.getDName().equals(drink.getdName())){
                                prices += drink.getPrice() * inveDetails.getAmount();
                            }
                        }
                    }
                }

            }

            total.add(prices);
        }
        //Loop through the two list to find sum which one is similar
        Map<String,Double> mapping = new LinkedHashMap<String, Double>();
        for(Staff staff : staffs){
            double max = 0;
            for (int i = 0; i < staffID.size(); i++){
                if (staff.getsID().equals(staffID.get(i))){
                    max += total.get(i);
                }
            }
            mapping.put(staff.getsID(),max);
        }
        //Find the max value
        double max = 0;
        for (Map.Entry<String, Double> entry : mapping.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                for (Staff staff : staffs) {
                    if (staff.getsID().equals(entry.getKey())) {
                        maxStaff = staff;
                    }
                }
            }
        }
        
        return maxStaff;
    }
    

    // load file as list
    public static ArrayList<String> loadFile(String filePath) {
        String data = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader fReader = new BufferedReader(reader);

            while ((data = fReader.readLine()) != null) {
                list.add(data);
            }

            fReader.close();
            reader.close();

        } catch (Exception e) {
            System.out.println("Cannot load file");
        }
        return list;
    }

    public void displayStaffs() {
        for (Staff staff : this.staffs) {
            System.out.println(staff);
        }
    }

    public <E> boolean writeFile(String path, ArrayList<E> list) {
        try {
            FileWriter writer = new FileWriter(path);
            for (E tmp : list) {
                writer.write(tmp.toString());
                writer.write("\r\n");
            }

            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }

    public <E> boolean writeFile(String path, E object) {
        try {
            FileWriter writer = new FileWriter(path);

            writer.write(object.toString());
            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }
}
