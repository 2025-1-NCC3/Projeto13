package criptografia;

public class criptografiaApp {

    private static final int CHAVE = 3;

    // Método auxiliar para aplicar o deslocamento
    private static char deslocarCaracter(char c, int chave, int base, int limite) {
        return (char) ((c - base + chave + limite) % limite + base);
    }

    // Método genérico para criptografar ou descriptografar
    private static String processarTexto(String texto, int chave) {
        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                resultado.append(deslocarCaracter(c, chave, 'A', 26)); // Desloca letras maiúsculas
            } else if (Character.isLowerCase(c)) {
                resultado.append(deslocarCaracter(c, chave, 'a', 26)); // Desloca letras minúsculas
            } else if (Character.isDigit(c)) {
                resultado.append(deslocarCaracter(c, chave, '0', 10)); // Desloca dígitos
            } else {
                resultado.append(c); // Outros caracteres permanecem iguais
            }
        }

        return resultado.toString();
    }

    // Criptografar o texto
    public static String criptografar(String texto) {
        return processarTexto(texto, CHAVE);
    }

    // Descriptografar o texto
    public static String descriptografar(String texto) {
        return processarTexto(texto, -CHAVE);
    }
}
