
package atm.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Persistência de dados das contas.
 *
 * Normalmente seria este objecto a compor um do tipo Account,
 * não o contrário, mas não queria que o Main tivesse conhecimento
 * da persistência, para simplificar.
 *
 * Deste modo, esta classe não deve ser usada fora do modelo
 * (package private), e é usada para separação de responsabilidades,
 * o que facilita uma futura manutenção.
 *
 * Em vez de armazenar os dados num ficheiro, pode ser desejado usar
 * uma base de dados ou chamadas pela rede (RPC), como faz um
 * multibanco verdadeiro. As alterações são só precisas aqui.
 *
 * @author heldercorreia
 */
class AccountPersist {

    /** Mensagem de erro quando o ficheiro está mal formatado */
    private final String BAD_FORMAT_ERR =
            "Ficheiro com dados do cliente formatado incorrectamente";

    /** Ficheiro onde estão armazenados os dados */
    private File data;

    /**
     * Saldo.
     * Como objecto, para poder comparar null
     * como não tendo sido carregado (lazy loading).
     */
    private Double balance;

    /**
     * Constructor.
     * Não deve ser conhecido fora da camada do modelo.
     *
     * @see AccountBroker
     *
     * @param data  ficheiro onde estão armazenados os dados
     */
    AccountPersist(File data) {
        this.data = data;
    }

    /**
     * Retorna o saldo, se já tiver sido carregado, ou carrega-o do
     * ficheiro caso contrário, e assumindo que não há erros de
     * leitura.
     *
     * @return  o saldo
     */
    double getBalance() throws IOException {
        if (balance == null) {
            load();
        }
        return balance.doubleValue();
    }

    /**
     * Carrega os dados do ficheiro para este objecto,
     * assumindo que não há erros de leitura.
     *
     * Formato do ficheiro:
     * [saldo]
     * [linhas que representam movimentos]
     * .
     * .
     * .
     *
     * É criado um ficheiro novo, com dados a "zero" se ele não existir.
     */
    private void load() throws IOException {
        try {
            Scanner fileScanner = new Scanner(data);
            if (!fileScanner.hasNextDouble()) {
                throw new IOException(BAD_FORMAT_ERR);
            }
            balance = new Double(fileScanner.nextDouble());
            fileScanner.nextLine(); // discard the leftover enter

            while (fileScanner.hasNextLine()) {
                parseLine(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            data.createNewFile();
            balance = new Double(0.0);
        }
    }

    /**
     * Carrega movimentos de conta lidos pelo load()
     *
     * Formato de cada linha de movimento de conta:
     * [data],[descrição do movimento],[tipo de movimento],[valor movimentado]
     *
     * Formato da data: YYYYMMDDHHIISS
     * Tipos de movimento: Débito|Crédito
     *
     * @see load()
     *
     * @param line  linha de movimento de conta do ficheiro de dados
     */
    private void parseLine(String line) {
        String[] tokens = line.split(",");
        if (tokens.length == 4) {
            // @todo: implement loading Transaction[] here
        }
    }
}
