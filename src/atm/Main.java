
package atm;

import java.util.Scanner;
import atm.model.Account;
import atm.model.AccountBroker;
import java.io.IOException;

/**
 *
 * @author heldercorreia
 */
public class Main {
static Scanner input=new Scanner(System.in);
static AccountBroker accountB=new AccountBroker();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            userMenu(login());
        } catch (IOException e) {
            printErrorMessage(
                "Erro do Sistema. Dirija-se ao multibanco mais próximo."
            );
            printErrorMessage(e.getMessage());
            System.exit(1);
        }
    }

    public static Account login() throws IOException {
        Account account = null;
        AccountBroker aBroker=new AccountBroker();
        askInput("PIN: ");
        String password = input.nextLine();
        
        account = aBroker.getAccountWithPin(password);
        printNewLine();

        if (account == null) {
            printErrorMessage("Pin inválido!");
            return login();
        }

        return account;
    }
    public static void userMenu(Account account) throws IOException {

        System.out.println("1. Levantamentos");
        System.out.println("2. Consulta de saldo de conta");
        System.out.println("3. Consulta de movimentos de conta");
        System.out.println("4. Pagamentos de serviços");
        System.out.println("5. Depósitos");

        askInput("\n> ");

        switch (getOption()) {
            case 1:
                withdrawMenu(account);

                break;

            case 2:
                System.out.println("Saldo Actual: "+account.getBalance());

                break;

            case 3:
                //Consulta de movimentos de conta
                break;

            case 4:
                //Pagamentos de Serviços
                break;

            case 5:
                //Depósitos
                break;
            default:
                printErrorMessage("Opção inválida");
        }

        printNewLine();
        userMenu(account);
    }

    public static void withdrawMenu(Account account) throws IOException{
        System.out.println("---LEVANTAMENTOS---");
        System.out.println("1. 20       2. 50");
        System.out.println("3. 100      4. 150");
        System.out.println("5. 200      6. Outros Valores");


        askInput("\n> ");

        switch (getOption()) {
            case 1:
                account.withdraw(20);

                break;

            case 2:
                account.withdraw(50);
                break;

            case 3:
                account.withdraw(100);
                break;

            case 4:
                account.withdraw(150);
                break;

            case 5:
                account.withdraw(200);
                break;
            case 6:
                withdrawOther(account);
                break;
            
            default:
                printErrorMessage("Opção inválida");
        }
        
        printNewLine();
        userMenu(account);
    }

    public static void withdrawOther(Account account){
        int value;
                try{
                System.out.println("Valor de levantamento");value=input.nextInt();
                account.withdraw(value);
                }catch (IllegalArgumentException e){
                    printErrorMessage(e.getMessage());
                    withdrawOther(account);
                }
    }


    

    //Help methods
    private static void askInput(String msg) {
        System.out.print(msg);
    }


     private static void printStatusMessage(String msg) {
        System.out.flush();
        System.out.println(msg);
        System.out.println();
    }

    private static void printErrorMessage(String msg) {
        System.out.flush();
        System.err.println(msg);
        System.err.println();
        System.err.flush();
    }

    private static void printNewLine() {
        System.out.println();
    }
    private static short getOption() {
        short option = input.nextShort();
        if (input.hasNextLine()) {
            input.nextLine();
        }
        printNewLine();
        return option;
    }

}
