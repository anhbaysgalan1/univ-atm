
package atm.model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Persistência de dados das contas.
 *
 * Esta classe não deve ser usada fora do modelo (package private),
 * e é usada para separação de responsabilidades,
 * o que facilita uma futura manutenção.
 *
 * Em vez de armazenar os dados num ficheiro, pode ser desejado usar
 * uma base de dados ou chamadas pela rede (RPC), como faz um
 * multibanco verdadeiro. As alterações são só precisas aqui.
 */
class AccountMapper {

    /** Ficheiro onde estão armazenados os dados */
    private File data;

    /**
     * Constructor.
     * Não deve ser conhecido fora da camada do modelo.
     *
     * @see Atm.parseValidAccount(String, String)
     *
     * @param data  ficheiro onde estão armazenados os dados
     */
    AccountMapper(File data) {
        this.data = data;
        if (!data.exists()) {
            try {
                data.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(
                    "Não foi possível criar novo ficheiro de dados."
                );
            }
        }
    }

    /**
     * Guarda o saldo e movimentos de conta no ficheiro
     *
     * @param account  objecto do tipo Account para guardar
     */
    public void save(Account account) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(data));
            out.writeDouble(account.getBalance());
            out.writeObject(account.getTransactions());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Problema ao guardar dados.");
        }
    }

    /**
     * Carrega o saldo e movimentos de conta do ficheiro
     *
     * @param account  objecto do tipo Account para onde carregar os dados
     */
    public void load(Account account) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(data));
            account.setBalance(in.readDouble());
            for (Transaction trans : (List<Transaction>) in.readObject()) {
                account.addTransaction(trans);
            }
            in.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Objecto desconhecido ao carregar dados."
            );
        } catch (EOFException e) {
        } catch (IOException e) {
            throw new RuntimeException("Problema ao recuperar dados.");
        }
    }
}