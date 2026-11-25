package dev.seiji.readme;

import java.text.Normalizer;
import java.util.Map;

/** Fonte bitmap 5x7 desenhada à mão. Cada letra vira um monte de quadradinhos. */
public final class PixelFont {

    private static final Map<Character, String[]> GLIFOS = Map.ofEntries(
        Map.entry('A', new String[]{"01110","10001","10001","11111","10001","10001","10001"}),
        Map.entry('B', new String[]{"11110","10001","10001","11110","10001","10001","11110"}),
        Map.entry('C', new String[]{"01110","10001","10000","10000","10000","10001","01110"}),
        Map.entry('D', new String[]{"11110","10001","10001","10001","10001","10001","11110"}),
        Map.entry('E', new String[]{"11111","10000","10000","11110","10000","10000","11111"}),
        Map.entry('F', new String[]{"11111","10000","10000","11110","10000","10000","10000"}),
        Map.entry('G', new String[]{"01110","10001","10000","10111","10001","10001","01111"}),
        Map.entry('H', new String[]{"10001","10001","10001","11111","10001","10001","10001"}),
        Map.entry('I', new String[]{"01110","00100","00100","00100","00100","00100","01110"}),
        Map.entry('J', new String[]{"00111","00010","00010","00010","00010","10010","01100"}),
        Map.entry('K', new String[]{"10001","10010","10100","11000","10100","10010","10001"}),
        Map.entry('L', new String[]{"10000","10000","10000","10000","10000","10000","11111"}),
        Map.entry('M', new String[]{"10001","11011","10101","10101","10001","10001","10001"}),
        Map.entry('N', new String[]{"10001","11001","10101","10011","10001","10001","10001"}),
        Map.entry('O', new String[]{"01110","10001","10001","10001","10001","10001","01110"}),
        Map.entry('P', new String[]{"11110","10001","10001","11110","10000","10000","10000"}),
        Map.entry('Q', new String[]{"01110","10001","10001","10001","10101","10010","01101"}),
        Map.entry('R', new String[]{"11110","10001","10001","11110","10100","10010","10001"}),
        Map.entry('S', new String[]{"01111","10000","10000","01110","00001","00001","11110"}),
        Map.entry('T', new String[]{"11111","00100","00100","00100","00100","00100","00100"}),
        Map.entry('U', new String[]{"10001","10001","10001","10001","10001","10001","01110"}),
        Map.entry('V', new String[]{"10001","10001","10001","10001","10001","01010","00100"}),
        Map.entry('W', new String[]{"10001","10001","10001","10101","10101","10101","01010"}),
        Map.entry('X', new String[]{"10001","10001","01010","00100","01010","10001","10001"}),
        Map.entry('Y', new String[]{"10001","10001","01010","00100","00100","00100","00100"}),
        Map.entry('Z', new String[]{"11111","00001","00010","00100","01000","10000","11111"}),
        Map.entry('0', new String[]{"01110","10001","10011","10101","11001","10001","01110"}),
        Map.entry('1', new String[]{"00100","01100","00100","00100","00100","00100","01110"}),
        Map.entry('2', new String[]{"01110","10001","00001","00010","00100","01000","11111"}),
        Map.entry('3', new String[]{"11110","00001","00001","01110","00001","00001","11110"}),
        Map.entry('4', new String[]{"00010","00110","01010","10010","11111","00010","00010"}),
        Map.entry('5', new String[]{"11111","10000","11110","00001","00001","10001","01110"}),
        Map.entry('6', new String[]{"00110","01000","10000","11110","10001","10001","01110"}),
        Map.entry('7', new String[]{"11111","00001","00010","00100","01000","01000","01000"}),
        Map.entry('8', new String[]{"01110","10001","10001","01110","10001","10001","01110"}),
        Map.entry('9', new String[]{"01110","10001","10001","01111","00001","00010","01100"}),
        Map.entry('.', new String[]{"00000","00000","00000","00000","00000","01100","01100"}),
        Map.entry(':', new String[]{"00000","01100","01100","00000","01100","01100","00000"}),
        Map.entry('!', new String[]{"00100","00100","00100","00100","00100","00000","00100"}),
        Map.entry('?', new String[]{"01110","10001","00001","00010","00100","00000","00100"}),
        Map.entry('-', new String[]{"00000","00000","00000","11111","00000","00000","00000"}),
        Map.entry('/', new String[]{"00001","00010","00010","00100","01000","01000","10000"}),
        Map.entry('>', new String[]{"01000","00100","00010","00001","00010","00100","01000"})
    );

    private PixelFont() {}

    /** Remove acentos e passa para maiúsculas ("SÃO" -> "SAO"). */
    static String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase();
    }

    public static int largura(String texto, int px) {
        return normalizar(texto).length() * 6 * px - px;
    }

    /** Desenha o texto como um grupo SVG de retângulos a partir de (x, y). */
    public static String desenhar(String texto, int x, int y, int px, String cor) {
        var sb = new StringBuilder("<g fill=\"").append(cor).append("\">");
        int cursor = x;
        for (char ch : normalizar(texto).toCharArray()) {
            String[] glifo = GLIFOS.get(ch);
            if (glifo != null) {
                for (int linha = 0; linha < glifo.length; linha++) {
                    for (int col = 0; col < glifo[linha].length(); col++) {
                        if (glifo[linha].charAt(col) == '1') {
                            sb.append(Svg.rect(cursor + col * px, y + linha * px, px, px, null));
                        }
                    }
                }
            }
            cursor += 6 * px;
        }
        return sb.append("</g>").toString();
    }

    /** Desenha centralizado horizontalmente numa tela de largura {@code larguraTela}. */
    public static String centralizado(String texto, int larguraTela, int y, int px, String cor) {
        return desenhar(texto, (larguraTela - largura(texto, px)) / 2, y, px, cor);
    }
}
