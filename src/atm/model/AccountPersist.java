
package atm.model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    /** Colecção com os movimentos */
    private ArrayList<Transaction> transactions;

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
     * @see load()
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
     * Define o saldo.

     * @param balance  o saldo a definir
     */
    void setBalance(double ammount) {
        balance = new Double(ammount);
    }

    /**
     * Define os movimentos de conta, usando uma lista de movimentos
     *
     * @param transactions  lista com os movimentos do tipo Transaction
     */
    void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = (ArrayList<Transaction>) transactions.clone();
    }

    /**
     * Adiciona um movimento de conta
     *
     * @param transaction  movimento de conta a adicionar
     */
    void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<Transaction>();
        }
        transactions.add(transaction);
    }

    /**
     * Retorna uma lista (ArrayList) com os movimentos, se já tiverem
     * sido carregados, ou carregando-os do ficheiro caso contrário, e
     * assumindo que não há erros de leitura.
     *
     * @see load()
     *
     * @return  colecção com os movimentos (objectos do tipo Transaction)
     */
    ArrayList<Transaction> getTransactions() throws IOException {
        if (transactions == null) {
            load();
        }
        return (ArrayList<Transaction>) transactions.clone();
    }

    /**
     * Guarda os dados no ficheiro.
     */
    void save() throws IOException {
        if (!data.exists()) {
            data.createNewFile();
        }
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(data), "UTF8")
        );
        out.write(balance.toString());

        for (Transaction transaction : transactions) {
            out.write(transaction.toString());
        }
        out.close();
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
        balance = new Double(0.0);
        transactions = new ArrayList<Transaction>();

        try {
            Scanner fileScanner = new Scanner(data, "UTF8");
            balance = Double.parseDouble(fileScanner.nextLine());
            while (fileScanner.hasNextLine()) {
                parseTransaction(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            data.createNewFile();
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
    private void parseTransaction(String line) {
        String[] tokens = line.split(",");
        if (tokens.length == 4) {
            try {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

                Date date             = df.parse(tokens[0]);
                String description    = tokens[1];
                Transaction.Type type = parseTransactionType(tokens[2]);
                double ammount        = Double.parseDouble(tokens[3]);

                transactions.add(new Transaction(
                    date, description, type, ammount
                ));
            } catch (ParseException ex) {
            }
        }
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