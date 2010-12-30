
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

    private final String CONFIRM = "Confirmar";
    private final String OTHEROP = "Outras operações";
    private final String ABORT_T = "Abortar";

    private Atm atm;
    private Account account;

    private JButton confirm;
    private JButton abort;

    public Main(double startingFunds) {
        atm = new Atm(startingFunds);
        setTitle("Multibanco");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 350));
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
        confirm = new JButton(CONFIRM);
        abort   = new JButton(ABORT_T);

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

        final JPasswordField pwdPin = new JPasswordField(7);
        pwdPin.setMaximumSize(pwdPin.getPreferredSize());
        pwdPin.setHorizontalAlignment(JTextField.CENTER);

        ActionListener loginListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pin = String.valueOf(pwdPin.getPassword());
                account = atm.getAccountWithPin(pin);
                if (account != null) {
                    showMainMenu();
                } else {
                    showError("Pin inválido. Tente de novo.", pwdPin);
                }
            }
        };

        pwdPin.addActionListener(loginListener);
        confirm.addActionListener(loginListener);

        Box login = Box.createVerticalBox();
        login.setPreferredSize(new Dimension(300, 220));

        login.add(Box.createVerticalGlue());
        login.add(centerComponent(lblPin));
        login.add(Box.createRigidArea(new Dimension(0, 5)));
        login.add(centerComponent(pwdPin));
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
                updateContent(withdrawalScreen());
                confirm.setEnabled(true);
                confirm.addActionListener(new MenuListener());
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
                updateContent(depositScreen());
                confirm.setText(CONFIRM);
                confirm.setEnabled(true);
            }
        });

        confirm.setText(OTHEROP);

        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 3));
        grid.add(withdrawals);
        grid.add(checkbalance);
        grid.add(transactions);
        grid.add(payments);
        grid.add(deposits);

        Box menu = Box.createVerticalBox();
        menu.add(screenTitle("Menu Principal"));
        menu.add(Box.createRigidArea(new Dimension(0, 15)));
        menu.add(grid);
        return menu;
    }

    private JComponent withdrawalScreen() {
        ActionListener withdrawalListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                int amount = Integer.parseInt(source.getActionCommand());
                if (amount != 0) {
                    atm.withdraw(amount, account);
                    showMainMenu();
                } else {
                    updateContent(otherWithdrawalScreen());
                }
            }
        };

        JButton btn;
        String[] order = {"20", "50", "100", "150", "200", "0"};

        JPanel grid = new JPanel(new GridLayout(0, 2));
        for (int i = 0; i < order.length; i++) {
            btn = new JButton(
                order[i].equals("0") ? "Outros valores" : order[i]
            );
            btn.setActionCommand(order[i]);
            btn.addActionListener(withdrawalListener);

            grid.add(btn);
        }

        Box screen = Box.createVerticalBox();
        screen.add(screenTitle("Levantamento"));
        screen.add(Box.createRigidArea(new Dimension(0, 15)));
        screen.add(grid);
        screen.add(Box.createVerticalGlue());
        return screen;
    }

    private JComponent otherWithdrawalScreen() {
        confirm.setText(CONFIRM);
        removeActionListeners(confirm);

        final JTextField withdrawal = new JTextField(6);
        withdrawal.setMaximumSize(withdrawal.getPreferredSize());
        withdrawal.setHorizontalAlignment(JTextField.CENTER);

        ActionListener otherWithdrawalListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(withdrawal.getText());
                    atm.withdraw(amount, account);
                    showMainMenu();
                } catch (NumberFormatException ex) {
                    showError("Valor inválido. Tente novamente.", withdrawal);
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage(), withdrawal);
                }
            }
        };

        withdrawal.addActionListener(otherWithdrawalListener);
        confirm.addActionListener(otherWithdrawalListener);

        Box screen = Box.createVerticalBox();
        screen.add(screenTitle("Levantamento"));
        screen.add(screenTitle("de outras importâncias"));
        screen.add(Box.createVerticalGlue());
        screen.add(centerComponent(withdrawal));
        screen.add(Box.createVerticalGlue());
        return screen;
        
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
            "<html><b>Saldo actual:</b> "
            +formatCurrency(account.getBalance())+"</html>"
        )));
        screen.add(Box.createRigidArea(new Dimension(0, 10)));
        screen.add(scrollPane);
        screen.add(Box.createVerticalGlue());

        return screen;
    }

    private JComponent depositScreen() {
        final JTextField deposit = new JTextField(6);
        deposit.setMaximumSize(deposit.getPreferredSize());
        deposit.setHorizontalAlignment(JTextField.CENTER);

        ActionListener depositListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(deposit.getText());
                    atm.deposit(amount, account);
                    showMainMenu();
                } catch (NumberFormatException ex) {
                    showError("Valor inválido. Tente novamente.", deposit);
                }
            }
        };

        deposit.addActionListener(depositListener);
        confirm.addActionListener(depositListener);

        Box screen = Box.createVerticalBox();
        screen.add(screenTitle("Depósito"));
        screen.add(Box.createVerticalGlue());
        screen.add(centerComponent(deposit));
        screen.add(Box.createVerticalGlue());
        return screen;
    }

    class MenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            showMainMenu();
        }
    }

    private void showMainMenu() {
        updateContent(mainMenu());
        removeActionListeners(confirm);
        confirm.setEnabled(false);
    }

    private void removeActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
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

    private void showError(String error, JTextField[] reset) {
        JOptionPane.showMessageDialog(Main.this, error, "Erro",
            JOptionPane.ERROR_MESSAGE
        );
        for (int i = 0; i < reset.length; i++) {
            reset[i].setText("");
        }
        reset[0].grabFocus();
    }

    private void showError(String error, JTextField reset) {
        showError(error, new JTextField[] {reset});
    }

    private String formatCurrency(double amount) {
        return String.format("%.2f euros", amount);
    }
}