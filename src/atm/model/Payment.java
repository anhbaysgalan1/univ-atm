
package atm.model;

/**
 *
 */
public class Payment {
    private String ent;
    private String ref;
    private double ammount;

    public Payment(String ent, String ref, double ammount) {
        this.ent = ent;
        this.ref = ref;
        this.ammount = ammount;
    }

    public double getAmmount() {
        return ammount;
    }

    public String getEnt() {
        return ent;
    }

    public String getRef() {
        return ref;
    }
    

}
