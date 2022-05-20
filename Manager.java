public class Manager extends FullTimeStaff {
    //Attributes
    private int allowance;

    //Constructors
    public Manager (String sID, String sName, int baseSalary, double bonusRate, int allowance){
        super(sID, sName, baseSalary, bonusRate);
        this.allowance = allowance;
    }

    //Getter
    public int getAllowance(){return this.allowance;}
    
    //Check if FullTimeStaff is Manager or not, and override paySalary method
    @Override
    public double paySalary(int workedHours){
        return super.paySalary(workedHours) + this.allowance;
    }
    @Override
    public String toString(){
        return String.format("%s_%s_%s_%s_%s",this.sID,this.sName,String.valueOf(this.getBonusRate()),String.valueOf(this.getBaseSalary()),this.allowance);
    }
}
