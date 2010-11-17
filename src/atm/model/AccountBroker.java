
package atm.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author heldercorreia
 */
public class AccountBroker {

    public Account getAccountWithPin(String pin) throws IOException {

        Account fetchedAccount = null;
        File dataFile = new File("clientes.txt");

        try {
            Scanner fileScanner = new Scanner(dataFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                if (lineScanner.hasNext() && lineScanner.next().equals(pin)) {
                    String number = lineScanner.next();
                    String client = lineScanner.next();
                    String source = lineScanner.next();

                    fetchedAccount = new Account(number, client);
                    fetchedAccount.load(new File(source));
                    return fetchedAccount;
                }
            }
        } catch (FileNotFoundException e) {
        }

        return fetchedAccount;
    }

}
