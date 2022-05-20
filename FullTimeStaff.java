public class FullTimeStaff extends Staff {
    //Attributes
    protected int baseSalary;
    protected double bonusRate;

    //Constructors
    public FullTimeStaff (String sID, String sName, int baseSalary, double bonusRate){
        super(sID, sName);
        this.baseSalary = baseSalary;
        this.bonusRate = bonusRate;
    }

    //Getter & Setter
    public int getBaseSalary(){return this.baseSalary;}
    public void setBaseSalary(int baseSalary){this.baseSalary = baseSalary;}
    public double getBonusRate(){return this.bonusRate;}
    public void setBonusRate(double bonusRate){this.bonusRate = bonusRate;}

    //Methods
    @Override // Override the method in the abstract class
    public double paySalary(int workedDays){
        if (workedDays > 21){
            int extraDays = workedDays - 21;
            return this.baseSalary * this.bonusRate + (extraDays*100000);
        }
        else{
            return this.baseSalary * this.bonusRate;
        }
        
    }

    @Override
    public String toString(){
        return String.format("%s_%s_%s_%s",this.sID,this.sName,String.valueOf(this.bonusRate),String.valueOf(this.baseSalary));
    }
    
}
