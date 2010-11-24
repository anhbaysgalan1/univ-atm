
package atm.model;

import java.util.ArrayList;

/**
 * Representa uma conta bancária, segundo o ponto de vista
 * de uma caixa multibanco.
 *
 * @author heldercorreia
 */
public class Account {

    /** O número de conta */
    private String number;

    /** O nome do cliente */
    private String client;

    /** Objecto de persistência, com a fonte de dados */
    private AccountPersist data;

    /** Indica se os dados já foram carregados da fonte de dados */
    private boolean loaded = false;

    /** Saldo da conta */
    private double balance = 0.0;

    /** Colecção com os movimentos */
    private ArrayList<Transaction> transactions;

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
    Account(String number, String client, AccountPersist data) {
        this.number = number;
        this.client = client;
        this.data   = data;
    }

    /** Retorna o número de conta passado para o construtor */
    public String getNumber() {
        return number;
    }

    /** Retorna o nome do cliente passado para o construtor */
    public String getClient() {
        return client;
    }

    /**
     * Carrega os dados da persistência de dados.
     * Permite ler o ficheiro apenas quando necessário (lazy loading).
     */
    private void load() throws java.io.IOException {
        balance      = data.getBalance();
        transactions = data.getTransactions();
        loaded       = true;
    }

    /**
     * Define o saldo, sem ler do ficheiro de dados.
     *
     * Usado nos testes para evitar depender da leitura do ficheiro de dados.
     *
     * @see AccountTest
     *
     * @param balance  o saldo a definir
     */
    void setBalance(double balance) {
        this.balance = balance;
        this.loaded  = true;
    }

    /**
     * Retorna o saldo, retirando o valor da persistência de dados, caso
     * não tenha ainda sido carregada.
     */
    public double getBalance() throws java.io.IOException {
        if (!loaded) {
            load();
        }
        return balance;
    }

    /**
     * Retorna uma lista (ArrayList) com os movimentos, se já tiverem
     * sido carregados, ou carregando-os da persistência caso contrário,
     * e assumindo que não há erros de leitura.
     */
    ArrayList<Transaction> getTransactions() throws java.io.IOException {
        if (!loaded) {
            load();
        }
        return (ArrayList<Transaction>) transactions.clone();
    }

    /**
     * Efectua um depósito.
     *
     * @param d  valor a depositar
     */
    public void deposit(double d) {
        balance += d;
    }

    /**
     * Efectua um levantamento
     *
     * @param d  valor a levantar
     */
    public void withdraw(double d) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
