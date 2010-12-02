
package atm;

import java.util.Scanner;
import atm.model.Atm;
import atm.model.Account;
import atm.model.Transaction;

public class Main {

    private static Scanner input = new Scanner(System.in);
    private static Atm atm = new Atm(500);

    public static void main(String[] args) {
        try {
            userMenu(login());
        } catch (RuntimeException e) {
            printErrorMessage(
                "Erro do Sistema. Dirija-se ao multibanco mais próximo.\n"
              + "Diagnóstico: " + e.getMessage()
            );
            System.exit(1);
        }
    }

    /** Autentica o utilizador com um pin, retornando uma conta válida */
    public static Account login() {

        askInput("PIN: ");
        String pin = input.nextLine();
        
        Account account = atm.getAccountWithPin(pin);
        printNewLine();

        if (account == null) {
            printErrorMessage("Pin inválido!");
            return login();
        }

        return account;
    }

    /** Menu de entrada ao utilizador */
    public static void userMenu(Account account) {

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
                printStatusMessage(
                    "Saldo Actual: " + formatCurrency(account.getBalance())
                );
                pause();
                break;

            case 3:
                printHeader("Movimentos de conta");
                for (Transaction latest : account.getLatestTransactions(10)) {
                    System.out.println(latest);
                }
                pause();
                break;

            case 4:
                printStatusMessage("Serviço em desenvolvimento, poderá visualizar erros...");
                pause();
                servicesPayment(account);
                break;

            case 5:
                printHeader("Depósito");
                askInput("Montante: ");
                int dep = input.nextInt();
                clearInput();

                atm.deposit(dep, account);
                printStatusMessage("Obrigado pelo seu depósito.");
                break;

            default:
                printErrorMessage("Opção inválida");
        }

        printNewLine();
        userMenu(login());
    }

    /** Menu dos levantamentos */
    public static void withdrawMenu(Account account) {

        printHeader("Levantamentos");

        System.out.println("1. 20       2. 50");
        System.out.println("3. 100      4. 150");
        System.out.println("5. 200      6. Outros valores");

        askInput("\n> ");

        try {
            switch (getOption()) {
                case 1: atm.withdraw(20, account); break;
                case 2: atm.withdraw(50, account); break;
                case 3: atm.withdraw(100, account); break;
                case 4: atm.withdraw(150, account); break;
                case 5: atm.withdraw(200, account); break;
                case 6: withdrawOther(account); break;
                default:
                    printErrorMessage("Opção inválida");
            }
        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
        }
        
        printNewLine();
        userMenu(account);
    }

    /** Levantamento de outras importâncias
     *
     */
    public static void withdrawOther(Account account) {
        try{
            askInput("Montante: ");
            int ammount = input.nextInt();
            clearInput();
            atm.withdraw(ammount, account);

        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
            withdrawOther(account);
        }
    }
/**Pagamento de serviços
 * Conta de electricidade
 * Conta da agua
 * Carregamento de telemovel
 */
    public static void servicesPayment(Account account) {
        printHeader("Pagamentos de Serviços");
        System.out.println("1. Conta de Electricidade");
        System.out.println("2. Conta da Água");
        System.out.println("3. Carregamento Telemóvel");

        askInput("\n> ");

        try {
        switch (getOption()){
            case 1:
                System.out.println("Em desenvolvimento");
                pause();
                break;
            case 2:
                System.out.println("Em desenvolvimento");
                pause();
                break;
            case 3:
                System.out.println("Em desenvolvimento");
                pause();
                break;
            default:
                printErrorMessage("Opção inválida");

             }
         }catch(IllegalArgumentException e){
          printErrorMessage(e.getMessage());
        }
        printNewLine();
        
    }


    

    // Helper methods
    private static void printHeader(String header) {
        System.out.println("\n\n--- "+header.toUpperCase()+" ---");
    }

    public static void pause() {
        printStatusMessage("\n<Prima ENTER para continuar…>\n");
        input.nextLine();
    }

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

    private static String formatCurrency(double ammount) {
        return String.format("%.2f", ammount);
    }

    private static short getOption() {
        short option = input.nextShort();
        clearInput();
        printNewLine();
        return option;
    }

    private static void clearInput() {
        if (input.hasNextLine()) {
            input.nextLine();
        }
    }
}
