
package atm;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-gui")) {
            atm.ui.swing.Main.main(new String[]{});
        } else {
            atm.ui.Console.run();
        }
    }
}
