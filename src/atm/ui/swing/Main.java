
package atm.ui.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import atm.model.*;
import javax.swing.border.*;

public class Main extends JFrame {
    Atm atm;
    Account account;

    JButton confirm;
    JButton abort;
    JPanel content;

    public static void main(String[] args) {
        final double funds =
                (args.length > 0) ? Double.parseDouble(args[0]) : 500;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main(funds).setVisible(true);
            }
        });
    }

    public Main(double startingFunds) {
        atm = new Atm(startingFunds);
        setTitle("Multibanco");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JPasswordField pwdPin = new JPasswordField(7);
        pwdPin.setMaximumSize(pwdPin.getPreferredSize());
        pwdPin.setHorizontalAlignment(JTextField.CENTER);
        pwdPin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        ActionListener loginListener = new LoginListener(pwdPin);
        pwdPin.addActionListener(loginListener);
        confirm.addActionListener(loginListener);

        JPanel loginPane = new JPanel();
        loginPane.setLayout(new BoxLayout(loginPane, BoxLayout.Y_AXIS));
        loginPane.setPreferredSize(new Dimension(300, 300));

        loginPane.add(Box.createVerticalGlue());
        loginPane.add(lblPin);
        loginPane.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPane.add(pwdPin);
        loginPane.add(Box.createVerticalGlue());

        pwdPin.requestFocusInWindow();
        return loginPane;
    }

    class LoginListener implements ActionListener {
        JPasswordField pwdPin;
        LoginListener(JPasswordField pwdPin) {
            this.pwdPin = pwdPin;
        }
        @Override
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
            }
        }
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
                System.out.println("Saldo!");
            }
        });

        transactions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Movimentos!");
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

        JPanel menu = new JPanel();
        menu.add(withdrawals);
        menu.add(checkbalance);
        menu.add(transactions);
        menu.add(payments);
        menu.add(deposits);

        return menu;
    }
}