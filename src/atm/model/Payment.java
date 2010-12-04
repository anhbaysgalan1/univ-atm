
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

        ammount=Math.abs(ammount);
        //Testa a referencia caso acho caracteres invalidos
        try{
            Integer.parseInt(reference);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Referencia inválido");
            }

        //Verifica se a referencia introduzida é inferior a 9 digitos
        if(reference.length()<9)
                throw new IllegalArgumentException(
                    "Referencia inválida - Caracteres em falta"
                    );

        //Verifica se a referencia introduzida é superior a 9 digitos
        if(reference.length()>9)
                throw new IllegalArgumentException(
                    "Referencia inválida - Caracteres em excesso"
                    );
        //Testa a entidade caso acho caracteres invalidos
        try{
            Integer.parseInt(entity);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Entidade inválido");
            }

        //Verifica se a entidade introduzida é inferior a 5 digitos
        if(entity.length()<5)
                throw new IllegalArgumentException(
                    "Entidade inválida - Caracteres em falta"
                    );

        //Verifica se a entidade introduzida é superior a 5 digitos
        if(entity.length()>5)
                throw new IllegalArgumentException(
                    "Entidade inválida - Caracteres em excesso"
                    );


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