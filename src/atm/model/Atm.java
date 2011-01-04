
package atm.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Objecto que representa uma caixa de multibanco. Responsável por
 * autenticar o utilizador com base no seu pin, e efectuar operações de
 * serviços que pertençam a um multibanco.
 */
public class Atm {

    /** Ficheiro de dados com as contas de clientes */
    private final String DATA_SOURCE = "clientes.txt";

    /** Quantia mínima que se pode levantar */
    private final int MINIMAL_WITHDRAWAL = 10;

    /** Quantia máxima que se pode levantar */
    private final int MAXIMAL_WITHDRAWAL = 200;

    /** Dinheiro disponível no cofre */
    private double funds;

    /**
     * Permite abstracção na localização dos ficheiros de dados
     *
     * @param filename  nome do ficheiro de dados
     * @return          objecto File
     */
    public static File getFile(String filename) {
        return new File("data" + File.separator + filename);
    }

    /**
     * Construtor
     *
     * @param startingFunds  quantidade disponível no cofre
     */
    public Atm(double startingFunds) {
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
        account.processTransaction(
            Transaction.newCredit("Depósito MB", d)
        );
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
        account.processTransaction(
            Transaction.newDebit("Levantamento MB", d)
        );
        funds -= d;
    }

    /**
     * Pagamento de serviços Água
     *
     * @param payment  objecto de um pagamento de serviços
     * @param account  objecto de conta
     */
    public void payWaterBill(Payment payment, Account account) {
        payBill("Pagamento de serviços Água", payment, account);

    }

    /**
     * Pagamento de serviços Electricidade
     *
     * @param payment  objecto de um pagamento de serviços
     * @param account  objecto de conta
     */
    public void payElectricityBill(Payment payment, Account account) {
        payBill("Pagamento de serviços Electricidade", payment, account);

    }

    /**
     * Pagamento de serviços Telemóvel
     *
     * @param payment  objecto de um pagamento de serviços
     * @param account  objecto de conta
     */
    public void payPhoneBill(Payment payment, Account account) {
       payBill("Pagamento de serviços Telemóvel", payment, account);

    }

    /**
     * Pagamento de um serviço.
     *
     * @param description  descrição a registar nos movimentos de conta
     * @param payment      objecto do pagamento de serviços
     * @param account      objecto de conta
     */
    private void payBill(String description, Payment payment, Account account) {
        // É possível, posteriormente, fazer algo com o objecto do pagamento
        account.processTransaction(
            Transaction.newDebit(description, payment.getAmount())
        );
    }

    /**
     * Retorna uma entidade para um número de telemóvel em operadora conhecida.
     *
     * @param phone  número de telemóvel
     * @return       entidade para ser usada num pagamento de serviços
     */
    public String getPhoneEntity(String phone) {
        if (Payment.isValidReference(phone)) {
            switch (phone.charAt(1)){
                case '1': return "10158";  // vodafone
                case '3': return "20638";  // optimus
                case '6': return "10559";  // tmn
            }
        }
        throw new IllegalArgumentException(
            "Número inválido ou operadora desconhecida."
        );
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
        File dataFile = getFile(DATA_SOURCE);
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
            String nmbr = tokens[1];
            String name = tokens[2];
            String data = tokens[3];
            AccountMapper datamapper = new AccountMapper(getFile(data));
            account = new Account(nmbr, name, datamapper);
        }
        return account;
    }
}
