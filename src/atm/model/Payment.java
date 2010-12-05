
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
    private double amount;

    /** Valida se uma entidade tem 5 dígitos */
    public static boolean isValidEntity(String entity) {
        return validate(entity, 5);
    }

    /** Valida se uma referência tem 9 dígitos */
    public static boolean isValidReference(String reference) {
        return validate(reference, 9);
    }

    /**
     * Construtor
     *
     * @param entity     entidade
     * @param reference  referência
     * @param amount     montante
     */
    public Payment(String entity, String reference, double amount) {
        StringBuilder error = new StringBuilder();
        if (!isValidEntity(entity)) {
            error.append("Entidade inválida. ");
        }
        if (!isValidReference(reference)) {
            error.append("Referência inválida.");
        }
        if (error.length() != 0) {
            throw new IllegalArgumentException(error.toString());
        }
        this.entity    = entity;
        this.reference = reference;
        this.amount    = Math.abs(amount);
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
    public double getAmount() {
        return amount;
    }

    /**
     * Valida se uma string é um número com size dígitos
     *
     * @param numeric  número a validar
     * @param size     número de dígitos para verificar
     * @return         true se numeric tiver size dígitos;
     *                 false caso contrário
     */
    private static boolean validate(String numeric, int size) {
        java.util.Scanner sc = new java.util.Scanner(numeric);
        return sc.hasNextInt() && numeric.length() == size;
    }
}