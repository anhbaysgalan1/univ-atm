
package atm.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Objecto que representa uma caixa de multibanco. Responsável por
 * autenticar o utilizador com base no seu pin, e efectuar operações de
 * serviços que pertençam a um multibanco.
 */
public class AtmClient {

    /** Ficheiro de dados com as contas de clientes */
    private final String DATA_SOURCE = "clientes.txt";

    /** Quantia mínima que se pode levantar */
    private final int MINIMAL_WITHDRAWAL = 10;

    /** Quantia máxima que se pode levantar */
    private final int MAXIMAL_WITHDRAWAL = 200;

    /** Dinheiro disponível no cofre */
    private double funds;

    /**
     * Construtor
     *
     * @param startingFunds  quantidade disponível no cofre
     */
    public AtmClient(double startingFunds) {
        funds = Math.abs(startingFunds);
    }

    /** Retorna a quantidade disnpinível no cofre */
    double getFunds() {
        return funds;
    }

    /**
     * Verifica se tem dinheiro suficiente no cofre
     * para efectuar um levantamento
     */
    public boolean hasEnoughWithdrawalFunds() {
        return funds >= MINIMAL_WITHDRAWAL;
    }

    /**
     * Deposita dinheiro numa conta de cliente
     *
     * @param d        quantidade a depositar
     * @param account  conta para onde depositar o dinheiro
     */
    public void deposit(double d, Account account) {
        account.deposit(Math.abs(d));
    }

    /**
     * Faz um levantamento, desde que haja dinheiro no cofre e:
     *
     * - tem que ser no mínimo 10 e no máximo 200
     * - tem que uma quantia possível de se juntar com notas
     *   (múltiplo de 5)
     *
     * @param d        quantia a levantar
     * @param account  conta de onde levantar o dinheiro
     */
    public void withdraw(double d, Account account) {
        d = Math.abs(d);
        if (d > funds) {
            throw new IllegalArgumentException(
                "Montante não disponível. Levante até "+funds+
                " ou dirija-se à caixa de multibanco mais próxima."
            );
        }
        if (d < MINIMAL_WITHDRAWAL || d > MAXIMAL_WITHDRAWAL) {
            throw new IllegalArgumentException(
                "Mínimo "+MINIMAL_WITHDRAWAL+", máximo "+MAXIMAL_WITHDRAWAL+"."
            );
        }
        if (d % 5 != 0) {
            throw new IllegalArgumentException(
                "Não existem notas de "+(d%5)+" euros."
            );
        }
        account.withdraw(d);
        funds -= d;
    }

    /**
     * Efectua o "login", procurando uma conta com base no seu pin,
     * partindo do princípio que o ficheiro de clientes está bem
     * formatado de acordo com as especificações.
     *
     * @see parseAccount(String)
     *
     * @param pin  o pin da conta, para autenticar
     * @return     objecto de conta se encontrada, null caso contrário
     */
    public Account getAccountWithPin(String pin) {
        Account fetchedAccount = null;
        File dataFile = Factory.getFile(DATA_SOURCE);

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
