public class SeasonalStaff extends Staff{
    //Attributes
    private int hourlyWage;

    //Constructors
    public SeasonalStaff (String sID, String sName, int hourlyWage){
        super(sID, sName);
        this.hourlyWage = hourlyWage;
    }

    //Getter & Setter
    public int getHourlyWage(){return this.hourlyWage;}
    public void setHourlyWage(int hourlyWage){this.hourlyWage = hourlyWage;}
    
    // Override the method in the abstract class
    @Override 
    public double paySalary(int workedHours){
        return this.hourlyWage * workedHours;
    }
    
    @Override
    public String toString(){
        return String.format("%s_%s_%s",this.sID,this.sName,String.valueOf(this.hourlyWage));
    }

	
}