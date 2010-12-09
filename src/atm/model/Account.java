
package atm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma conta bancária.
 */
public class Account {

    /** O número de conta */
    private String number;

    /** O nome do cliente */
    private String client;

    /** Saldo da conta */
    private double balance = 0.0;

    /** Colecção com os movimentos */
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    /** Objecto de persistência, com a fonte de dados (não serializado) */
    private transient AccountMapper mapper;

    /**
     * Construtor por defeito.
     *
     * Objectos deste tipo apenas devem ser instanciados
     * pelo Atm, ou pelos testes.
     *
     * @see Atm.getAccountWithPin(String)
     *
     * @param number  número de conta
     * @param client  nome do cliente
     * @param mapper  objecto de persistência, com a fonte dos dados
     */
    Account(String number, String client, AccountMapper mapper) {
        this.number = number;
        this.client = client;
        this.mapper = mapper;
        load();
    }

    /** Carrega os dados da persistência */
    private void load() {
        mapper.load(this);
    }

    /** Retorna o número de conta passado para o construtor */
    public String getNumber() {
        return number;
    }

    /** Retorna o nome do cliente passado para o construtor */
    public String getClient() {
        return client;
    }

    /** Retorna o saldo */
    public double getBalance() {
        return balance;
    }

    /**
     * Define o saldo. Usado pelos testes e pela persistência de dados
     *
     * @param balance  o saldo a definir
     */
    void setBalance(double balance) {
        this.balance = balance;
    }

    /** Retorna os movimentos de conta */
    ArrayList<Transaction> getTransactions() {
        return (ArrayList<Transaction>) transactions.clone();
    }

    /**
     * Regista um movimento de conta
     *
     * @param transaction  objecto de movimento de conta Transaction
     */
    void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    /** Retorna o último movimento adicionado */
    Transaction getLastTransaction() {
        return !transactions.isEmpty()
                    ? transactions.get(transactions.size()-1)
                    : null;
    }

    /**
     * Retorna os últimos max movimentos,
     * por ordem do mais recente ao mais antigo
     */
    public List<Transaction> getLatestTransactions(int max) {
        if (transactions.size() < max) {
            max = transactions.size();
        }

        List<Transaction> latest = new ArrayList<Transaction>(max);
        for (int i = max-1; i >= 0; i--) {
            latest.add(transactions.get(i));
        }

        return latest;
    }

    /**
     * Processa um movimento, debitando ou creditando da conta o valor
     * movimentado, assim como registando o movimento. Por último,
     * se tudo tiver corrido bem, os dados são gravados.
     *
     * @param transaction  objecto de movimento de conta
     */
    public void processTransaction(Transaction transaction) {
        double amount = transaction.getAmount();
        switch (transaction.getType()) {
            case CREDIT:
                credit(amount);
                break;
            case DEBIT:
                debit(amount);
                break;
        }
        addTransaction(transaction);
        mapper.save(this);
    }

    /**
     * Coloca dinheiro na conta.
     *
     * @param d  valor a creditar
     */
    public void credit(double d) {
        balance += Math.abs(d);
    }

    /**
     * Retira dinheiro da conta, tipicamente para um levantamento ou
     * pagamento de serviços.
     *
     * @param d  valor a debitar
     */
    public void debit(double d) {
        d = Math.abs(d);
        if (d > balance) {
            throw new IllegalArgumentException("Não tem saldo suficiente");
        }
        balance -= d;
    }
}