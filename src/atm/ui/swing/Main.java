
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

    private Font BIGGER_FONT = new Font(Font.SERIF, Font.PLAIN, 20);

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

        Box menu = Box.createVerticalBox();

        JButton[] order = {
            withdrawals,
            checkbalance,
            transactions,
            payments,
            deposits
        };

        JLabel title = new JLabel("Menu Principal");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(BIGGER_FONT);

        menu.add(title);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        for (int i = 0; i < order.length; i++) {
            order[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            menu.add(Box.createRigidArea(new Dimension(0, 5)));
            menu.add(order[i]);
        }
        menu.add(Box.createVerticalGlue());

        return menu;
    }
}