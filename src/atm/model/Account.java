
package atm.model;

/**
 *
 * @author heldercorreia
 */
public class Account {

    private String number;
    private String client;

    public Account(String number, String client) {
        this.number = number;
        this.client = client;
    }

    public String getNumber() {
        return number;
    }

    public String getClient() {
        return client;
    }

}
