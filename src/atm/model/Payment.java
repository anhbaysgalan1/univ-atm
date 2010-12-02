
package atm.model;

/**
 * Representa um pagamento de serviços
 */
public class Payment {

    /** Entidade */
    private String entity;

    /** Referência */
    private String reference;

    /** Montante */
    private double ammount;

    /**
     * Construtor
     *
     * @param entity     entidade
     * @param reference  referência
     * @param ammount    montante
     */
    public Payment(String entity, String reference, double ammount) {
        this.entity    = entity;
        this.reference = reference;
        this.ammount   = ammount;
    }

    /** Retorna a entidade passada ao construtor */
    public String getEntity() {
        return entity;
    }

    /** Retorna a referência passada ao construtor */
    public String getReference() {
        return reference;
    }

    /** Retorna o montante passado ao construtor */
    public double getAmmount() {
        return ammount;
    }
}