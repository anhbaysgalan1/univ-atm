
package atm.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Representa um movimento de conta.
 */
public class Transaction implements java.io.Serializable {

    /**
     * Versão de serialização
     *
     * @see http://www.mkyong.com/java-best-practices/understand-the-serialversionuid/
     */
    private static final long serialVersionUID = 1L;

    /** Tipo enumerado com os tipos de movimentos */
    enum Type {
        DEBIT  { @Override public String toString() { return "Débito"; } },
        CREDIT { @Override public String toString() { return "Crédito"; } };
    }

    /** Data (e hora) do movimento */
    private Date date;

    /** Descrição do movimento */
    private String description;

    /** Tipo de movimento */
    private Type type;

    /** Saldo */
    private Double amount;

    /**
     * Construtor.
     *
     * Apenas deve ser usado por AccountPersist,
     * para poder definir uma data específica.
     *
     * @param date         data do movimento
     * @param description  descrição do movimento
     * @param type         tipo do movimento
     * @param amount       valor movimentado
     */
    Transaction(Date date, String description, Type type, double amount) {
        this.date        = (Date) date.clone();
        this.description = description;
        this.type        = type;
        this.amount      = new Double(amount);
    }

    /**
     * Cria um movimento de crédito
     *
     * @param description  descrição do movimento
     * @param amount       valor movimentado
     * @return             novo objecto de movimento Transaction
     */
    public static Transaction newCredit(String description, double amount) {
        return new Transaction(new Date(), description, Type.CREDIT, amount);
    }

    /**
     * Cria um movimento de débito
     * 
     * @param description  descrição do movimento
     * @param value        valor movimentado
     * @return             novo objecto de movimento Transaction
     */
    public static Transaction newDebit(String description, double value) {
        return new Transaction(new Date(), description, Type.DEBIT, value);
    }

    /** Retorna a descrição do movimento */
    public String getDescription() {
        return description;
    }

    /** Retorna o valor do movimento */
    public double getAmount() {
        return amount.doubleValue();
    }

    /** Retorna o tipo do movimento */
    Type getType() {
        return type;
    }

    /**
     * String formatada que representa este objecto.
     * Usado para mostrar os movimentos no ecrã.
     */
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return String.format("%s   %-35s   %-7s   %.2f",
             df.format(date), description, type, amount.doubleValue()
        );
    }
}
