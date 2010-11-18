
package atm.model;

/**
 * Excepção geral da aplicação.
 *
 * Erros deste tipo não devem impedir o funcionamento corrente da aplicação.
 * O intuito é mostrar a mensagem de erro ao utilizador, quando excepções
 * deste tipo acontecem.
 *
 * @author heldercorreia
 */
public class ApplicationException extends Exception {

    /**
     * Cria uma instância de <code>ApplicationException</code>
     * com mensagem de detalhe.
     *
     * @param msg  A mensagem de detalhe
     */
    public ApplicationException(String msg) {
        super(msg);
    }
}
