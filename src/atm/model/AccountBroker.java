
package atm.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author heldercorreia
 */
public class AccountBroker {

    public Account getAccountWithPin(String pin) {

        File dataFile = new File("data" + File.separator + "clientes.txt");

        try {
            Scanner fileScanner = new Scanner(dataFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                if (lineScanner.hasNext() && lineScanner.next().equals(pin)) {
                    String number = lineScanner.next();
                    String client = lineScanner.next();

                    return new Account(number, client);
                }
            }
        } catch (FileNotFoundException e) {
        }

        return null;
    }

}
