package dev.seiji.readme;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

/**
 * O personagem em pixel art (12x16). Cada letra é uma cor da paleta e '.' é transparente.
 * Quer mudar o visual? Edite as linhas abaixo.
 */
public final class Sprite {

    private static final Map<Character, String> PALETA = Map.ofEntries(
        Map.entry('k', "#1a1423"), // contorno
        Map.entry('h', "#2b2236"), // cabelo
        Map.entry('s', "#f2c79b"), // pele
        Map.entry('g', "#9ad0ff"), // lente dos óculos
        Map.entry('v', "#6DB33F"), // moletom (verde Spring)
        Map.entry('V', "#4c8a2a"), // sombra do moletom
        Map.entry('p', "#2d3a5c"), // calça
        Map.entry('b', "#3b2a1a"), // tênis
        Map.entry('m', "#8b5a2b"), // café
        Map.entry('c', "#ffffff")  // xícara
    );

    /** Quadro 1: parado / passo A. */
    public static final String[] QUADRO_A = {
        "....kkkk....",
        "...khhhhk...",
        "..khhhhhhk..",
        "..khhhhhhhk.",
        "..khssssshk.",
        "..kssgksgk..",
        "..kssssssk..",
        "...kssssk...",
        "..kvvvvvvk..",
        ".kvvvVvvvvk.",
        ".ksvvVvvvsmc",
        ".kkvvvvvvkmc",
        "...kppppk...",
        "...kp..pk...",
        "...kp..pk...",
        "..kbb..bbk..",
    };

    /** Quadro 2: pernas abertas (alternar com o quadro 1 dá a corrida). */
    public static final String[] QUADRO_B = Stream.concat(
            Arrays.stream(QUADRO_A, 0, 12),
            Stream.of("...kppppk...", "..kpk..kp...", ".kpk....kpk.", ".kbb....bbk."))
        .toArray(String[]::new);

    private Sprite() {}

    public static String desenhar(String[] quadro, int x, int y, int px) {
        var sb = new StringBuilder();
        for (int linha = 0; linha < quadro.length; linha++) {
            for (int col = 0; col < quadro[linha].length(); col++) {
                String cor = PALETA.get(quadro[linha].charAt(col));
                if (cor != null) sb.append(Svg.rect(x + col * px, y + linha * px, px, px, cor));
            }
        }
        return sb.toString();
    }

    private static final String[] MOEDA = {"..yyy..", ".yYYYy.", "yYyyyYy", "yYyyyYy", "yYyyyYy", ".yYYYy.", "..yyy.."};

    /** Moeda girando (a animação "spin" fica na folha de estilo da tela). */
    public static String moeda(int x, int y, int px) {
        var sb = new StringBuilder();
        for (int l = 0; l < MOEDA.length; l++)
            for (int c = 0; c < MOEDA[l].length(); c++) {
                char ch = MOEDA[l].charAt(c);
                if (ch != '.') sb.append(Svg.rect(c * px, l * px, px, px, ch == 'y' ? "#ffd23f" : "#e09b00"));
            }
        double meio = 7 * px / 2.0;
        return "<g transform=\"translate(" + x + " " + y + ")\"><g class=\"spin\" style=\"transform-origin:"
                + meio + "px " + meio + "px\">" + sb + "</g></g>";
    }
}
