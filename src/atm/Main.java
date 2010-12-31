
package atm;

public class Main {

    public static void main(String[] args) {
        double startingFunds = 500;

        if (args.length > 0) {
            try {
                startingFunds = Double.parseDouble(args[args.length-1]);
            } catch (NumberFormatException e) {}

            if (args[0].equals("-gui")) {
                atm.ui.Swing.run(startingFunds);
                return;
            }
        }

        atm.ui.Console.run(startingFunds);
    }
}
