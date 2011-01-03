
package atm.ui;

import atm.model.Atm;
import atm.model.Account;
import atm.model.Payment;
import atm.model.Transaction;

public class Console {

    private static java.util.Scanner input;
    private static java.io.PrintStream out;
    private static java.io.PrintStream err;
    private static Atm atm;

    public static void run(double startingFunds) {

        try { // Permitir acentuação
            out = new java.io.PrintStream(System.out, true, "UTF-8");
            err = new java.io.PrintStream(System.err, true, "UTF-8");
            input = new java.util.Scanner(System.in);
        } catch (java.io.UnsupportedEncodingException e) {
            out = System.out;
            err = System.out;
        }

        try { // Iniciar a aplicação
            atm = new Atm(startingFunds);
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
        printLineBreak();

        if (account == null) {
            printErrorMessage("Pin inválido!");
            return login();
        }

        return account;
    }

    /** Menu de entrada ao utilizador */
    public static void userMenu(Account account) {

        printMenu(
            "1. Levantamentos",
            "2. Consulta de saldo de conta",
            "3. Consulta de movimentos de conta",
            "4. Pagamento de serviços",
            "5. Depósitos"
        );
        askInput("\n> ");

        try {
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
                        out.println(latest);
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

                default:
                    printErrorMessage("Opção inválida");
            }
        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
        }

        printLineBreak();
        userMenu(login());
    }

    /** Menu dos levantamentos */
    public static void withdrawMenu(Account account) {

        printHeader("Levantamento");
        printMenu(
            "1. 20       2. 50",
            "3. 100      4. 150",
            "5. 200      6. Outros valores"
        );
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

        printLineBreak();
    }

    /** Levantamento de outras importâncias */
    public static void withdrawOther(Account account) {
        try{
            askInput("Montante: ");
            int amount = input.nextInt();
            clearInput();
            atm.withdraw(amount, account);

        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
            withdrawOther(account);
        }
    }

    /** Menu do pagamento de serviços */
    public static void servicesPayment(Account account) {
        printHeader("Pagamento de Serviços");
        printMenu(
            "1. Conta de Electricidade",
            "2. Conta da Água",
            "3. Carregamento Telemóvel"
        );
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
        printLineBreak();
    }

    /** Retorna um objecto de pagamento de serviço */
    public static Payment getPayment() {
        try {
            askInput("Entidade: ");
            String entity = input.nextLine();
            askInput("Referência: ");
            String reference = input.nextLine();
            askInput("Montante: ");
            double amount = input.nextDouble();
            clearInput();
            printLineBreak();
            return new Payment(entity, reference, amount);
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
            printLineBreak();
            return new Payment(entity, phone, amount);
        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
            return getPhonePayment();
        }
    }

    /** Menu com quantias de carregamento do telemóvel */
    public static double getPhonePaymentAmount(){
        printHeader("Montante");
        printMenu(
            "1. 5 euros",
            "2. 10 euros",
            "3. 20 euros"
        );
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

    /* Métodos de ajuda */

    private static String formatCurrency(double amount) {
        return String.format("%.2f euros", amount);
    }

    private static short getOption() {
        short option = input.nextShort();
        clearInput();
        printLineBreak();
        return option;
    }

    private static void clearInput() {
        if (input.hasNextLine()) {
            input.nextLine();
        }
    }

    /* 
     * Métodos de abstracção do output para tornar o código mais legível e
     * facilitar a manutenção ao remover uma dependência ao método de output
     */

    private static void printHeader(String header) {
        out.println("\n\n| "+header+" |\n");
    }

    private static void printMenu(String ... entries) {
        for (int i = 0; i < entries.length; i++) {
            out.println(entries[i]);
        }
    }

    private static void askInput(String msg) {
        out.print(msg);
    }

    private static void printStatusMessage(String msg) {
        out.println(msg);
        out.println();
    }

    private static void printErrorMessage(String msg) {
        err.println(msg);
        err.println();
    }

    private static void printLineBreak() {
        out.println();
    }
}
