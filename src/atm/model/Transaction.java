
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
    private Double value;

    /**
     * Construtor.
     *
     * Apenas deve ser usado pelos testes, e AccountPersist,
     * para poder definir uma data específica.
     *
     * @param date         data do movimento
     * @param description  descrição do movimento
     * @param type         tipo do movimento
     * @param value        valor movimentado
     */
    Transaction(Date date, String description, Type type, double value) {
        this.date = (Date) date.clone();
        this.description = description;
        this.type = type;
        this.value = new Double(value);
    }

    /**
     * Cria um movimento de crédito
     *
     * @param description  descrição do movimento
     * @param value        valor movimentado
     * @return             novo objecto de movimento Transaction
     */
    public static Transaction newCredit(String description, double value) {
        return new Transaction(new Date(), description, Type.CREDIT, value);
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
    public double getAmmount() {
        return value.doubleValue();
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
             df.format(date), description, type, value.doubleValue()
        );
    }

    /**
     * Verifica se um objecto é igual a este.
     * Utilizado nos testes, com assertEquals().
     *
     * @see TransactionTest
     *
     * @param obj  a referência do objecto para comparar
     * @return     true se este objecto é igual ao argumento obj;
     *             false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transaction other = (Transaction) obj;
        if (this.date != other.date &&
                (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        if ((this.description == null)
                ? (other.description != null)
                : !this.description.equals(other.description)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.value != other.value &&
                (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    /**
     * Retorna um código único para este objecto.
     * Deve ser implementado sempre que se faz o override do equals(Object).
     *
     * @see equals(Object)
     *
     * @return  um código único para este objecto
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 59 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 59 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

}
