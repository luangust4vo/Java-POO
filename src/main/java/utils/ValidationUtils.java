package utils;

import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class ValidationUtils {
    public static <R> R execute(Callable<R> validation) {
        try {
            return validation.call();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Atenção!", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }

    public static void execute(Runnable validation) {
        try {
            validation.run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Atenção!", JOptionPane.ERROR_MESSAGE);
        }
    }
}