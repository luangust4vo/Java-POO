package utils;

import java.util.concurrent.Callable;

public class ValidationUtils {
    public static <R> R execute(Callable<R> validation) {
        try {
            return validation.call();
        } catch (Exception e) {
        	System.out.println("Erro: " + e.getMessage());
            return null;
        }
       
    }
}