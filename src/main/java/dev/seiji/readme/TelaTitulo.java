package dev.seiji.readme;

import java.util.Random;

/** Gera a tela de título: céu estrelado, cidade passando, chão rolando e o personagem correndo. */
public final class TelaTitulo {

    private static final int W = 1000, H = 420;

    private TelaTitulo() {}

    public static String gerar(Perfil perfil) {
        var sb = new StringBuilder(Svg.abrir(W, H));
        sb.append("""
            <style>
            .tw{animation:tw 2.4s steps(2) infinite} @keyframes tw{50%{opacity:.15}}
            .blink{animation:bl 1s steps(1) infinite} @keyframes bl{50%{opacity:0}}
            .ground{animation:gr .6s linear infinite} @keyframes gr{to{transform:translateX(-32px)}}
            .city{animation:ct 30s linear infinite} @keyframes ct{to{transform:translateX(-LARGURApx)}}
            .fa{animation:fa .36s steps(1) infinite} .fb{animation:fb .36s steps(1) infinite}
            @keyframes fa{50%{opacity:0}} @keyframes fb{0%{opacity:0}50%{opacity:1}}
            .spin{animation:sp 1.2s steps(6) infinite} @keyframes sp{0%,100%{transform:scaleX(1)}50%{transform:scaleX(.15)}}
            .bob{animation:bo 1.8s ease-in-out infinite} @keyframes bo{50%{transform:translateY(-6px)}}
            text{font-family:'Courier New',Courier,monospace;font-weight:700;fill:#fff}
            .rod{fill:#d9c7ff}
            </style>
            """.replace("LARGURA", String.valueOf(W)));

        sb.append(Svg.rect(0, 0, W, H, "#120d2b")).append(Svg.rect(0, 0, W, 200, "#1b1340"));
        sb.append(estrelas());
        String cidade = cidade();
        sb.append("<g class=\"city\"><g>").append(cidade)
          .append("</g><g transform=\"translate(").append(W).append(" 0)\">").append(cidade).append("</g></g>");
        sb.append("<g class=\"ground\">").append(chao()).append("</g>");

        // título com sombra
        int px = 9;
        int tx = (W - PixelFont.largura(perfil.nome(), px)) / 2;
        sb.append("<g class=\"bob\">")
          .append(PixelFont.desenhar(perfil.nome(), tx + 6, 72, px, "#000"))
          .append(PixelFont.desenhar(perfil.nome(), tx, 66, px, "#ffd23f"))
          .append("</g>");
        sb.append(PixelFont.centralizado(perfil.subtitulo(), W, 150, 3, "#9ad0ff"));
        sb.append("<g class=\"blink\">").append(PixelFont.centralizado("PRESS START", W, 205, 4, "#ffffff")).append("</g>");

        sb.append(Sprite.moeda(330, 250, 5)).append(Sprite.moeda(470, 230, 5)).append(Sprite.moeda(610, 250, 5));

        sb.append("<g transform=\"translate(140 260)\">")
          .append("<g class=\"fa\">").append(Sprite.desenhar(Sprite.QUADRO_A, 0, 0, 5)).append("</g>")
          .append("<g class=\"fb\">").append(Sprite.desenhar(Sprite.QUADRO_B, 0, 0, 5)).append("</g>")
          .append("</g>");

        sb.append(Svg.texto(20, 30, 16, "", "1UP  002026", null));
        sb.append(Svg.texto(W / 2, 30, 16, "", "HI-SCORE  " + perfil.hiscore(), "text-anchor=\"middle\""));
        sb.append(Svg.texto(W - 20, 30, 16, "", "LV %02d".formatted(perfil.nivel()), "text-anchor=\"end\""));
        sb.append(Svg.texto(W / 2, 408, 13, "rod", perfil.rodape(), "text-anchor=\"middle\""));
        return sb.append("</svg>\n").toString();
    }

    private static String estrelas() {
        var rnd = new Random(21); // semente fixa: mesma imagem toda vez
        var sb = new StringBuilder();
        int[] tamanhos = {2, 2, 3, 4};
        for (int i = 0; i < 70; i++) {
            int s = tamanhos[rnd.nextInt(tamanhos.length)];
            sb.append("<rect x=\"").append(rnd.nextInt(W)).append("\" y=\"").append(rnd.nextInt(250))
              .append("\" width=\"").append(s).append("\" height=\"").append(s)
              .append("\" fill=\"#fff\" class=\"tw\" style=\"animation-delay:")
              .append(String.format(java.util.Locale.ROOT, "%.2f", rnd.nextDouble() * 3)).append("s\"/>");
        }
        return sb.toString();
    }

    private static String cidade() {
        var rnd = new Random(5);
        var sb = new StringBuilder();
        int[] larguras = {40, 56, 72}, vaos = {0, 6, 10};
        for (int x = 0; x < W; ) {
            int w = larguras[rnd.nextInt(3)], h = 50 + rnd.nextInt(91);
            sb.append(Svg.rect(x, 340 - h, w, h, "#241b45"));
            for (int wy = 340 - h + 10; wy < 330; wy += 16)
                for (int wx = x + 8; wx < x + w - 8; wx += 14)
                    if (rnd.nextDouble() < .35)
                        sb.append("<rect x=\"").append(wx).append("\" y=\"").append(wy)
                          .append("\" width=\"5\" height=\"6\" fill=\"#ffd23f\" opacity=\".7\"/>");
            x += w + vaos[rnd.nextInt(3)];
        }
        return sb.toString();
    }

    private static String chao() {
        var sb = new StringBuilder();
        for (int i = 0; i < W + 64; i += 32) {
            sb.append(Svg.rect(i, 340, 32, 8, "#6DB33F"))
              .append(Svg.rect(i, 348, 32, 72, "#7a4a2a"))
              .append(Svg.rect(i + 4, 358, 8, 8, "#5e3820"))
              .append(Svg.rect(i + 20, 378, 8, 8, "#5e3820"))
              .append(Svg.rect(i, 340, 4, 8, "#4c8a2a"));
        }
        return sb.toString();
    }
}
