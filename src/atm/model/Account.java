
package atm.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author heldercorreia
 */
public class Account {

    private String number;
    private String client;
    private File data;
    private double balance = 0.0;

    public Account(String number, String client) {
        this.number = number;
        this.client = client;
    }

    void load(File data) throws IOException {
        this.data = data;

        try {
            Scanner fileScanner = new Scanner(data);
            if (fileScanner.hasNextDouble()) {
                balance = fileScanner.nextDouble();
            }

        } catch (FileNotFoundException e) {
            data.createNewFile();
        }
    }

    public String getNumber() {
        return number;
    }

    public String getClient() {
        return client;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double d) {
        balance += d;
    }

}
