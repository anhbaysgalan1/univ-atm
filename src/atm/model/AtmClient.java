
package atm.model;

import java.io.File;
import java.io.FileNotFoundException;
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

    /** Retorna a quantidade disponível no cofre */
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
        d = Math.abs(d);
        account.credit(d);
        account.addTransaction(Transaction.newCredit("Depósito MB", d));
        account.save();
    }

    /**
     * Faz um levantamento, desde que haja dinheiro no cofre e:
     *
     * - tem que ser no mínimo 10 e no máximo 200
     * - tem que ser uma quantia possível de se juntar com notas
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
        account.debit(d);
        account.addTransaction(Transaction.newDebit("Levantamento MB", d));
        account.save();
        funds -= d;
    }

    /**
     * Efectua o "login", procurando uma conta com base no seu pin,
     * e partindo do princípio que o ficheiro de clientes está bem
     * formatado de acordo com as especificações.
     *
     * @see parseValidAccount(String, String)
     *
     * @param pin  o pin da conta, para autenticar
     * @return     objecto de conta se encontrada, null caso contrário
     */
    public Account getAccountWithPin(String pin) {
        Account account = null;
        File dataFile = Factory.getFile(DATA_SOURCE);
        try {
            Scanner fileScanner = new Scanner(dataFile, "UTF8");
            while (fileScanner.hasNextLine() && account == null) {
                String client = fileScanner.nextLine();
                account = parseValidAccount(client, pin);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Ficheiro de clientes não encontrado!");
        }
        return account;
    }

    /**
     * Interpreta uma linha do ficheiro de clientes para um
     * objecto do tipo Account, partindo do princípio que
     * o ficheiro está bem formatado.
     *
     * Formato de cada linha:
     * [pin],[número da conta],[nome do cliente],[ficheiro de dados da conta]
     *
     * @param client  linha formatada do ficheiro de clientes
     * @param pin     o pin da conta, para autenticar
     * @return        objecto de conta
     */
    private Account parseValidAccount(String client, String pin) {
        Account account = null;
        String[] tokens = client.split(",");
        if (tokens.length != 4) {
            throw new RuntimeException (
                "Ficheiro de clientes formatado incorrectamente."
            );
        }
        if (tokens[0].equals(pin)) {
            String numb = tokens[1];
            String name = tokens[2];
            String data = tokens[3];
            AccountManager manager = new AccountPersist(Factory.getFile(data));
            account = new Account(numb, name, manager);
        }
        return account;
    }
}
