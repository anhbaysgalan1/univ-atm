
package atm;

import java.util.Scanner;
import atm.model.Atm;
import atm.model.Account;
import atm.model.Payment;
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
        System.out.println("4. Pagamento de serviços");
        System.out.println("5. Depósitos");
        System.out.println("6. Abortar");

        askInput("\n> ");

        switch (getOption()) {
            case 1:
                withdrawMenu(account);
                break;

            case 2:
                printHeader("Saldo de conta");
                printStatusMessage(
                    "Conta número: " + account.getNumber() + "\n" +
                    "Saldo Actual: " + formatCurrency(account.getBalance())
                );
                break;

            case 3:
                printHeader("Movimentos de conta");
                printStatusMessage(
                    "Saldo Actual: " + formatCurrency(account.getBalance())
                );
                for (Transaction latest : account.getLatestTransactions(10)) {
                    System.out.println(latest);
                }
                break;

            case 4:
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

            case 6:
                break;

            default:
                printErrorMessage("Opção inválida");
        }

        printNewLine();
        userMenu(login());
    }

    /** Menu dos levantamentos */
    public static void withdrawMenu(Account account) {

        printHeader("Levantamento");

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
    }

    /** Levantamento de outras importâncias */
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

    /** Menu do pagamento de serviços */
    public static void servicesPayment(Account account) {
        printHeader("Pagamento de Serviços");
        System.out.println("1. Conta de Electricidade");
        System.out.println("2. Conta da Água");
        System.out.println("3. Carregamento Telemóvel");

        askInput("\n> ");
        switch (getOption()){
            case 1: atm.payElectricityBill(getPayment(), account); break;
            case 2: atm.payWaterBill(getPayment(), account);       break;
            case 3: atm.payPhoneBill(getPhonePayment(), account);  break;
            default:
                printErrorMessage("Opção inválida");
                servicesPayment(account);
        }

        printStatusMessage("Pagamento efectuado com sucesso");
        printNewLine();
    }

    /** Retorna um objecto de pagamento de serviço */
    public static Payment getPayment() {
        try {
            askInput("Entidade: ");
            String entity = input.nextLine();
            askInput("Referência: ");
            String reference = input.nextLine();
            askInput("Montante: ");
            double ammount = input.nextDouble();
            clearInput();
            printNewLine();
            return new Payment(entity, reference, ammount);
        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
            return getPayment();
        }
    }

    /** Retorna um objecto de pagamento de serviço, para um telemóvel */
    public static Payment getPhonePayment() {
        try {
            askInput("Telemóvel: ");
            String phone = input.nextLine();
            String entity = atm.getPhoneEntity(phone);
            double amount = getPhonePaymentAmount();
            printNewLine();
            return new Payment(entity, phone, amount);
        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
            return getPhonePayment();
        }
    }

    /** Menu com quantias de carregamento do telemóvel */
    public static double getPhonePaymentAmount(){
        printHeader("Montante");
        System.out.println("1. 5 euros");
        System.out.println("2. 10 euros");
        System.out.println("3. 20 euros");

        askInput("\n> ");
        switch (getOption()) {
            case 1: return 5;
            case 2: return 15;
            case 3: return 20;
            default:
                printErrorMessage("Opção inválida");
                return getPhonePaymentAmount();
        }
    }


    // Helper methods
    private static void printHeader(String header) {
        System.out.println("\n\n| "+header+" |\n");
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
        return String.format("%.2f euros", ammount);
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
