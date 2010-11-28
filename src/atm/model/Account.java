
package atm.model;

import java.util.ArrayList;

/**
 * Representa uma conta bancária, segundo o ponto de vista
 * de uma caixa multibanco.
 */
public class Account {

    /** O número de conta */
    private String number;

    /** O nome do cliente */
    private String client;

    /** Objecto de persistência, com a fonte de dados */
    private AccountManager manager;

    /** Saldo da conta */
    private double balance = 0.0;

    /** Colecção com os movimentos */
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    /**
     * Construtor por defeito.
     *
     * Objectos deste tipo apenas devem ser instanciados
     * pelo AccountBroker, ou pelos testes.
     *
     * @see AccountBroker.getAccountWithPin(String)
     *
     * @param number  número de conta
     * @param client  nome do cliente
     * @param data    objecto de persistência, com a fonte dos dados
     */
    Account(String number, String client, AccountManager manager) {
        this.number  = number;
        this.client  = client;
        this.manager = manager;
        load();
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
    public ArrayList<Transaction> getTransactions() {
        return (ArrayList<Transaction>) transactions.clone();
    }

    /** Limpa os movimentos. Útil para retornar a um estado inicial */
    public void emptyTransactions() {
        transactions.clear();
    }

    /**
     * Regista um movimento de conta
     *
     * @param transaction  objecto de movimento de conta Transaction
     */
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    /** Carrega os dados da persistência de dados. */
    private void load() {
        manager.load(this);
    }

    /** Guarda os dados na persistência de dados. */
    public void save() {
        manager.save(this);
    }

    /**
     * Efectua um depósito.
     *
     * @param d  valor a depositar
     */
    public void deposit(double d) {
            balance += Math.abs(d);
    }

    /**
     * Efectua um levantamento
     *
     * @param d  valor a levantar
     */
    public void withdraw(double d) {
        d = Math.abs(d);
        if (d < 10 || d > 200) {
            throw new IllegalArgumentException(
                "O valor do levantamento tem que ser entre 10€ e 200€"
            );
        }
        if (d % 5 != 0) {
            throw new IllegalArgumentException(
                "O valor que introduziu tem que ser múltiplo de 5"
            );
        }
        if (d > balance) {
            throw new IllegalArgumentException("Sem saldo suficiente");
        }
        balance -= d;
    }
    
    /*Metodo para efectuar pagamento de serviços. 
     * @param d valor do levantamento
     * Levantamente só é realizado caso haja saldo disponivel   
     */
    public void paymentBill(double d) {
        d = Math.abs(d);
        if (d > balance) {
            throw new IllegalArgumentException("Sem saldo suficiente");
        }
        else{
            balance -= d;
            System.out.println("Pagamento efectuado com sucesso");
        }
    }
}
