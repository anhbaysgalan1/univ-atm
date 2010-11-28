
package atm.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Objecto responsável por gerir as várias contas, principalmente
 * autenticar o utilizador com base no seu pin de conta.
 */
public class AccountBroker {

    /**
     * Efectua o "login", procurando uma conta com base no seu pin,
     * partindo do princípio que o ficheiro de clientes está bem
     * formatado de acordo com as especificações.
     *
     * @see parseLine(String)
     *
     * @param pin  o pin da conta, para autenticar
     * @return     objecto de conta se encontrada, null caso contrário
     */
    public Account getAccountWithPin(String pin) {
        Account fetchedAccount = null;
        File dataFile = Factory.getFile("clientes.txt");

        try {
            Scanner fileScanner = new Scanner(dataFile, "UTF8");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                /*
                 * Não há necessidade de guardar o pin no objecto de conta.
                 * Será usado apenas para autenticação por este meio.
                 */
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                if (lineScanner.hasNext() && lineScanner.next().equals(pin)) {
                    fetchedAccount = parseAccount(line);
                    return fetchedAccount;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Ficheiro de dados não encontrado!");
        } catch (IOException e) {
            throw new RuntimeException("Erro de leitura do ficheiro de dados");
        }

        return fetchedAccount;
    }

    /**
     * Interpreta uma linha do ficheiro de clientes para um
     * objecto do tipo Account, partindo do princípio que
     * o ficheiro está bem formatado.
     *
     * Formato de cada linha:
     * [pin],[número da conta],[nome do cliente],[ficheiro de dados da conta]
     *
     * @param line  linha formatada do ficheiro de clientes
     * @return      objecto de conta
     */
    private Account parseAccount(String line) throws IOException {
        String[] tokens = line.split(",");
        if (tokens.length != 4) {
            throw new IOException(
                "Ficheiro com dados de contas formatado incorrectamente"
            );
        }
        String number = tokens[1];
        String client = tokens[2];
        String source = tokens[3];
        AccountPersist data = new AccountPersist(Factory.getFile(source));
        return new Account(number, client, data);
    }
}
