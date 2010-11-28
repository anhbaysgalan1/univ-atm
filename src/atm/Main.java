
package atm;

import java.util.Scanner;
import atm.model.Account;
import atm.model.AtmClient;
import java.io.IOException;

/**
 *
 * @author heldercorreia
 */
public class Main {
static Scanner input=new Scanner(System.in);
static AtmClient accountB=new AtmClient();

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
            System.exit(1);
        }
    }

    public static Account login() throws IOException {
        Account account = null;
        AtmClient aBroker=new AtmClient();
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
                servicesPayment(account);
                break;

            case 5:
                System.out.println("---DEPÓSITOS---");
                System.out.println("Indique o montante do deposito: ");
                int dep=input.nextInt();
                account.deposit(dep);
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
                System.out.println("Valor de levantamento: ");value=input.nextInt();
                account.withdraw(value);
                }catch (IllegalArgumentException e){
                    printErrorMessage(e.getMessage());
                    withdrawOther(account);
                }
    }

    public static void servicesPayment(Account account) throws IOException{
        System.out.println("---PAGAMENTOS DE SERVIÇOS---");
        System.out.println("1. Conta de Electricidade");
        System.out.println("2. Conta da Água");
        System.out.println("3. Carregamento Telemóvel");

        askInput("\n> ");

        switch (getOption()){
            case 1:
                payBill(account);
                break;
            case 2:
                payBill(account);
                break;
            case 3:
                payCell(account);
                break;
            default:
                printErrorMessage("Opção inválida");

        }
        printNewLine();
        userMenu(account);
    }


/*
 *Metodo para efectuar o pagamento de contas de electricidade e agua
 *Em falta integraçao com o registo de movimentos de conta
 */
    private static void payBill(Account account) throws IOException{
        int ent, ref;
        double payment;

        System.out.print("Entidade:");
        ent=input.nextInt();
        while(ent<10000 && ent>99999){
            System.out.println("Entidade inválida");
            System.out.print("Entidade:");
            ent=input.nextInt();
        }
        printNewLine();
        System.out.print("Referência: ");ref=input.nextInt();
        while(ref<100000000 && ref>999999999){
            System.out.println("Referência inválida");
            System.out.print("Referência: ");ref=input.nextInt();
        }
        printNewLine();

        System.out.println("Valor do pagamento: ");payment=input.nextDouble();
        account.paymentBill(payment);//Em falta registar este movimento ao movimento de conta


    }
    /*
     Metodo para efectuar carregamentos de telemovel
     Em falta registo no movimento de dados
     */
    public static void payCell(Account account) throws IOException{
        int cellRef,valueOp;
        System.out.print("Referência telemóvel: ");cellRef=input.nextInt();
        printNewLine();
        while(cellRef<900000000 && cellRef>999999999){
            System.out.println("Referência inválida");
            System.out.print("Referência telemóvel: ");cellRef=input.nextInt();
        }
        printNewLine();
        System.out.println("Selecione o montante do carregamento");
        System.out.println("1. 5€    2. 10€    3. 20€");
        askInput("\n>");
        switch (getOption()){
            case 1:
                account.paymentBill(5);
                break;
            case 2:
                account.paymentBill(10);
                break;
            case 3:
                account.paymentBill(20);
                break;
            default:
                printErrorMessage("Opção inválida");

        }
        printNewLine();
        userMenu(account);

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
