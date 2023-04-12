package com.GabrielGollo;

import javax.swing.*;
import java.awt.event.*;

public class MainInterface extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JTextField vpnHostField;
    private JButton selectOpenVPNFileButton;
    private JPasswordField secretOtpCode;
    private String _opVpnFileLocation;
    EventHandler eventHandler;

    public MainInterface(VpnConfigs initialConfigurations, EventHandler _eventHandler) {
        eventHandler = _eventHandler;

        String vpnHost = initialConfigurations.vpnHost();

        vpnHostField.setText(vpnHost);
        usernameTextField.setText(initialConfigurations.username());
        passwordTextField.setText(initialConfigurations.password());
        secretOtpCode.setText(initialConfigurations.secretOtp());
        _opVpnFileLocation = initialConfigurations.opVpnFileLocation();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        selectOpenVPNFileButton.addActionListener(this::onClickVpnFileButton);

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onClickVpnFileButton(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        if(response == JFileChooser.APPROVE_OPTION){
            _opVpnFileLocation = fileChooser.getSelectedFile().toString();
        }
    }

    @SuppressWarnings("deprecation")
    private VpnConfigs getFields(){
        return new VpnConfigs(vpnHostField.getText(), usernameTextField.getText(), passwordTextField.getText(), _opVpnFileLocation, secretOtpCode.getText());
    }

    private void onOK() {
        eventHandler.onOk(this, getFields());
    }

    public void enableStartButton(){
        buttonOK.setEnabled(true);
    }

    public void disableStartButton(){
        buttonOK.setEnabled(false);
    }

    private void onCancel() {
        eventHandler.onCancel(this, getFields());
    }
}
