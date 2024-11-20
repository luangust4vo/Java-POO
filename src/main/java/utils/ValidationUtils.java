package utils;

import javax.swing.JOptionPane;

import utils.interfaces.Validate;

public class ValidationUtils {
    public static void validate(Validate validation) {
        try {
            validation.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Atenção!", JOptionPane.ERROR_MESSAGE);
        }
    }
}