
package atm.model;

/**
 * Duplo que permite testar isoladamente Account,
 * sem necessitar ler ou escrever o sistema de ficheiros.
 *
 * @see AccountTest
 */
public class MockAccountPersist implements AccountManager {

    @Override
    public void load(Account account) {
        account.setBalance(600);
    }

    @Override
    public void save(Account account) {}

}
