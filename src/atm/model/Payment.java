
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
    /** Referencia */
    private final int refSize=9;
    /** Entidade */
    private final int entSize=5;
    /**
     * Construtor
     *
     * @param entity     entidade
     * @param reference  referência
     * @param ammount    montante
     */
    public Payment(String entity, String reference, double ammount) {

        verifyService(entity,entSize);
        verifyService(reference,refSize);

        this.entity    = entity;
        this.reference = reference;
        this.ammount   = Math.abs(ammount);
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

    public void verifyService(String numberService, int value){
        //Testa a referencia/entidade caso acho caracteres invalidos
        try{
            Integer.parseInt(numberService);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Não é um número");
            }

        //Verifica se a referencia/entidade introduzida é inferior ou superior a 9 digitos
        if(numberService.length()<value || numberService.length()>value)
                throw new IllegalArgumentException(
                    "Referencia inválida"
                    );

        
    }
}