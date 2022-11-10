import it.sauronsoftware.cron4j.Scheduler;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.Callable;

public class MainInterface extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JCheckBox autoconnectCheckBox;
    private JFileChooser fileChooserField;
    private JTextField vpnHostField;
    private JButton selectOpenVPNFileButton;
    private JTextField secretOtpCode;
    private String _vpnHost;
    private String _username;
    private String _password;
    private String _opVpnFileLocation;
    private String _secretOVPNCode;
    private boolean autoConnect;
    private Scheduler runningCron;

    public MainInterface(String vpnHost, String username, String password, String opVpnFileLocation, String secretOtp) {
        _vpnHost = vpnHost;
        _username = username;
        _password = password;
        vpnHostField.setText(vpnHost);
        usernameTextField.setText(username);
        passwordTextField.setText(password);
        secretOtpCode.setText(secretOtp);
        _opVpnFileLocation = opVpnFileLocation;
        _secretOVPNCode = secretOtp;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        selectOpenVPNFileButton.addActionListener(this::onClickVpnFileButton);

        buttonCancel.addActionListener(e -> onCancel());

        vpnHostField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                _vpnHost = vpnHostField.getText();
            }
        });

        usernameTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                _username = usernameTextField.getText();
            }
        });

        passwordTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                _password = passwordTextField.getText();
            }
        });

        secretOtpCode.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                _secretOVPNCode = secretOtpCode.getText();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onClickVpnFileButton(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        if(response == JFileChooser.APPROVE_OPTION){
            String fileLocation = fileChooser.getSelectedFile().toString();
            System.out.println(fileChooser.getSelectedFile());
            _opVpnFileLocation = fileLocation;
        }
    }

    private boolean isCronRunning () {
        if(runningCron==null){
            return false;
        } else {
            return runningCron.isStarted();
        }
    }

    private void onOK() {
        boolean cronIsRunning = isCronRunning();

        if(!(_username.isBlank() || _password.isBlank() || _vpnHost.isBlank() || _opVpnFileLocation.isBlank()) && !cronIsRunning) {
            try{
                CronInfra cronJob = new CronInfra();
                Callable<Void> service = new Callable<Void>() {
                    public Void call() {
                        CronService.StartVpnAuth(_username, _password, _vpnHost, _opVpnFileLocation, _secretOVPNCode);
                        return null;
                    }
                };
                runningCron = cronJob.startJob(service);

            } catch (Exception error) {
                System.out.println(error.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null,"Please, complete all fields!");
        }

    }

    private void saveConfigs() {
        JSONObject configs = new JSONObject();
        configs.put("vpnHost", _vpnHost);
        configs.put("username", _username);
        configs.put("password", _password);
        configs.put("opVpnFileLocation", _opVpnFileLocation);
        configs.put("secretOtp", _secretOVPNCode);

        ConfigLoader configLoader = new ConfigLoader();
        configLoader.saveConfigs(configs);
    }

    private void onCancel() {
        // add your code here if necessary
        saveConfigs();
        if(runningCron != null) runningCron.stop();
        dispose();
    }
}
