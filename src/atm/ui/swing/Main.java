
package atm.ui.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import atm.model.*;
import javax.swing.border.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        final double funds =
                (args.length > 0) ? Double.parseDouble(args[0]) : 500;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main(funds).setVisible(true);
            }
        });
    }

    Atm atm;
    Account account;

    JButton confirm;
    JButton abort;

    public Main(double startingFunds) {
        atm = new Atm(startingFunds);
        setTitle("Multibanco");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(300, 300));
        reset();
    }

    private void reset() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(operations(), BorderLayout.PAGE_END);
        root.add(loginScreen(), BorderLayout.CENTER);
        root.revalidate();
        setContentPane(root);
        account = null;
        pack();
    }

    private void updateContent(JComponent component) {
        JPanel root = (JPanel) getContentPane();
        BorderLayout layout = (BorderLayout) root.getLayout();
        root.remove(layout.getLayoutComponent(root, BorderLayout.CENTER));
        root.add(component, BorderLayout.CENTER);
        root.revalidate();
    }

    private JComponent operations() {
        confirm = new JButton("Confirmar");
        abort   = new JButton("Abortar");

        abort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        Box buttonbox = Box.createHorizontalBox();
        buttonbox.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonbox.add(Box.createHorizontalGlue());
        buttonbox.add(confirm);
        buttonbox.add(Box.createHorizontalGlue());
        buttonbox.add(abort);
        buttonbox.add(Box.createHorizontalGlue());
        return buttonbox;
    }

    private JComponent loginScreen() {
        JLabel lblPin = new JLabel("Introduza o seu PIN:");
        lblPin.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPasswordField pwdPin = new JPasswordField(7);
        pwdPin.setMaximumSize(pwdPin.getPreferredSize());
        pwdPin.setHorizontalAlignment(JTextField.CENTER);
        pwdPin.setAlignmentX(Component.CENTER_ALIGNMENT);

        ActionListener loginListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pin = String.valueOf(pwdPin.getPassword());
                account = atm.getAccountWithPin(pin);
                if (account == null) {
                    JOptionPane.showMessageDialog(Main.this,
                        "Pin inválido. Tente de novo.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                    pwdPin.setText("");
                    pwdPin.grabFocus();
                } else {
                    updateContent(mainMenu());
                    pwdPin.removeActionListener(this);
                    confirm.setEnabled(false);
                    abort.setText("Sair");
                }
            }
        };
        pwdPin.addActionListener(loginListener);
        confirm.addActionListener(loginListener);

        Box login = Box.createVerticalBox();
        login.setPreferredSize(new Dimension(300, 220));

        login.add(Box.createVerticalGlue());
        login.add(lblPin);
        login.add(Box.createRigidArea(new Dimension(0, 5)));
        login.add(pwdPin);
        login.add(Box.createVerticalGlue());

        pwdPin.requestFocusInWindow();
        return login;
    }

    private JComponent mainMenu() {
        JButton withdrawals  = new JButton("Levantamentos");
        JButton checkbalance = new JButton("Consulta de saldo da conta");
        JButton transactions = new JButton("Consulta de movimentos de conta");
        JButton payments     = new JButton("Pagamento de serviços");
        JButton deposits     = new JButton("Depósitos");

        withdrawals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Levantamentos!");
            }
        });

        checkbalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateContent(balanceScreen());
                confirm.setEnabled(true);
                confirm.addActionListener(new MenuListener());
            }
        });

        transactions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateContent(transactionsScreen());
                confirm.setEnabled(true);
                confirm.addActionListener(new MenuListener());
            }
        });

        payments.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pagamentos!");
            }
        });

        deposits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Depósitos!");
            }
        });

        confirm.setText("Outras operações");
        abort.setText("Sair");

        Box menu = Box.createVerticalBox();

        JButton[] order = {
            withdrawals,
            checkbalance,
            transactions,
            payments,
            deposits
        };

        menu.add(screenTitle("Menu Principal"));
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        for (int i = 0; i < order.length; i++) {
            menu.add(Box.createRigidArea(new Dimension(0, 5)));
            menu.add(centerComponent(order[i]));
        }
        menu.add(Box.createVerticalGlue());

        return menu;
    }

    private JComponent balanceScreen() {
        Box screen = Box.createVerticalBox();

        screen.add(screenTitle("Saldo de Conta"));
        screen.add(Box.createVerticalGlue());

        screen.add(centerComponent(boldLabel("Conta Número")));
        screen.add(Box.createRigidArea(new Dimension(0, 5)));
        screen.add(centerComponent(
            new JLabel(account.getNumber()))
        );
        screen.add(Box.createVerticalGlue());

        screen.add(centerComponent(boldLabel("Saldo Actual")));
        screen.add(Box.createRigidArea(new Dimension(0, 5)));
        screen.add(centerComponent(
            new JLabel(formatCurrency(account.getBalance()))
        ));
        screen.add(Box.createVerticalGlue());

        return screen;
    }

    private JComponent transactionsScreen() {
        java.util.List transactions = account.getLatestTransactions(10);

        String[] columns = {"Data", "Descrição", "Tipo", "Valor"};
        Object[][] data  = new Object[transactions.size()][4];

        for (int i = 0; i < transactions.size(); i++) {
            Transaction trans = (Transaction) transactions.get(i);
            data[i] = new Object[] {
                trans.getDateString(),
                trans.getDescription(),
                trans.getTypeString(),
                trans.getAmount()
            };
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        Box screen = Box.createVerticalBox();
        screen.add(screenTitle("Movimentos"));
        screen.add(Box.createRigidArea(new Dimension(0, 10)));
        screen.add(centerComponent(new JLabel(
            "<html><b>Saldo actual:</b> "+formatCurrency(account.getBalance())+"</html>"
        )));
        screen.add(Box.createRigidArea(new Dimension(0, 10)));
        screen.add(scrollPane);
        screen.add(Box.createVerticalGlue());

        return screen;
    }

    class MenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            updateContent(mainMenu());
            confirm.removeActionListener(this);
            confirm.setEnabled(false);
        }
    }

    private JComponent centerComponent(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        return component;
    }

    private JLabel screenTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private JLabel boldLabel(String text) {
        JLabel newLabel = new JLabel(text);
        newLabel.setFont(newLabel.getFont().deriveFont(Font.BOLD));
        return newLabel;
    }

    private String formatCurrency(double amount) {
        return String.format("%.2f euros", amount);
    }
}