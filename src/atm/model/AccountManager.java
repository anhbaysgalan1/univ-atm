
package atm.model;

/**
 * Interface que declara o contrato para objectos que gerem a
 * persistência de objectos do tipo Account.
 *
 * Necessário para criar objectos duplos para teste.
 *
 * @see MockAccountPersist
 */
public interface AccountManager {
    void load(Account account);
    void save(Account account);
}
