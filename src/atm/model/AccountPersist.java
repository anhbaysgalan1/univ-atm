
package atm.model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
class AccountPersist implements AccountManager {

    /** Ficheiro onde estão armazenados os dados */
    private File data;

    /**
     * Constructor.
     * Não deve ser conhecido fora da camada do modelo.
     *
     * @see AtmClient
     *
     * @param data  ficheiro onde estão armazenados os dados
     */
    AccountPersist(File data) {
        this.data = data;
        if (!data.exists()) {
            createDataFile();
        }
    }

    /** Tenta criar ficheiro, apontado pelo File passado para o construtor */
    private void createDataFile() {
        try {
            data.createNewFile();
        } catch (java.io.IOException e) {
            throw new RuntimeException(
                "Não foi possível criar novo ficheiro de dados."
            );
        }
    }

    /**
     * Guarda (serializa) o objecto num ficheiro
     *
     * @param account  objecto do tipo Account para guardar
     */
    @Override
    public void save(Account account) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(data));
            out.writeObject(account);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Problema ao guardar ficheiro.");
        }
    }

    /**
     * Carrega os dados do ficheiro para a conta.
     *
     * @param account  objecto do tipo Account para onde carregar os dados
     */
    @Override
    public void load(Account account) {
        Account restored = restore();
        if (restored != null) {
            account.setBalance(restored.getBalance());
            for (Transaction trans : restored.getTransactions()) {
                account.addTransaction(trans);
            }
        }
    }

    /** Restaura uma conta serializada do ficheiro de dados  */
    public Account restore() {
        Account account = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(data));
            account = (Account) in.readObject();
            in.close();
        } catch (ClassNotFoundException e) {
        } catch (EOFException e) {
        } catch (IOException e) {
            throw new RuntimeException("Problema ao recuperar dados.");
        }
        return account;
    }
}