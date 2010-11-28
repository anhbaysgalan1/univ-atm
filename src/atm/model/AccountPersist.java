
package atm.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Persistência de dados das contas.
 *
 * Esta classe não deve ser usada fora do modelo
 * (package private), e é usada para separação de responsabilidades,
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
     * @see AccountBroker
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
     * Guarda os dados no ficheiro.
     *
     * @param account  objecto do tipo Account de onde se quer guardar os dados
     */
    @Override
    public void save(Account account) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(data), "UTF8")
            );
            out.write(Double.toString(account.getBalance()));

            for (Transaction transaction : account.getTransactions()) {
                out.write(transaction.toString());
            }
        // lidar erros a partir daqui
        } catch (java.io.FileNotFoundException e) {
            createDataFile();
        } catch (java.io.IOException e) {
            throw new RuntimeException(
                "Problema ao gravar dados para o disco"
            );
        }
        // @todo: adicionar finally com out.close()
        // ignorado por agora, porque não estava a mudar de linha
    }

    /**
     * Carrega os dados do ficheiro para a conta,
     * assumindo que não há erros de leitura.
     *
     * Formato do ficheiro:
     * [saldo]
     * [linhas que representam movimentos]
     * .
     * .
     * .
     *
     * É criado um ficheiro novo, se ele não existir.
     *
     * @param account   objecto do tipo Account para onde carregar os dados
     */
    @Override
    public void load(Account account) {
        account.setBalance(0);
        account.emptyTransactions();

        try {
            Scanner fileScanner = new Scanner(data, "UTF8");

            if (fileScanner.hasNextLine()) {
                account.setBalance(Double.parseDouble(fileScanner.nextLine()));

                while (fileScanner.hasNextLine()) {
                    account.addTransaction(
                        parseTransaction(fileScanner.nextLine())
                    );
                }
            }
        } catch (java.io.FileNotFoundException e) {
            createDataFile();
        } catch (NumberFormatException e) {
            // ignora... saldo fica 0.0
            // idealmente corrigido no próximo save()
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
     * @see load(Account)
     *
     * @param line  linha de movimento de conta do ficheiro de dados
     * @return      objecto Transaction, com o movimento de conta
     */
    private Transaction parseTransaction(String line) {
        String[] tokens = line.split(",");
        if (tokens.length == 4) {
            try {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

                Date date             = df.parse(tokens[0]);
                String description    = tokens[1];
                Transaction.Type type = parseTransactionType(tokens[2]);
                double ammount        = Double.parseDouble(tokens[3]);

                return new Transaction(date, description, type, ammount);

            } catch (java.text.ParseException e) {
                // ignora
            } catch (IllegalArgumentException e) {
                // ignora
            }
        }
        return null;
    }

    /**
     * Tradução do tipo de movimento recolhido do ficheiro, para
     * o seu tipo nativo, reconhecido pela classe Transaction.
     *
     * @see Transaction
     *
     * @param type  tipo do movimento (crédito/débito)
     * @return      tipo do movimento pelo enum Transaction.Type
     */
    private Transaction.Type parseTransactionType(String type) {
        if (type.equals("Credito")) {
            return Transaction.Type.CREDIT;
        }
        if (type.equals("Debito")) {
            return Transaction.Type.DEBIT;
        }
        throw new IllegalArgumentException(
            "Unknown transaction type ("+type+")."
        );
    }
}