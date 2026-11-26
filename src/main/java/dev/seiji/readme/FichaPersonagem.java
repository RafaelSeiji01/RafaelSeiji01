package dev.seiji.readme;

import java.util.List;

/** Gera a ficha de personagem estilo RPG: retrato, barras, atributos, conquistas e missão. */
public final class FichaPersonagem {

    private static final int W = 1000, H = 575;

    private FichaPersonagem() {}

    public static String gerar(Perfil p) {
        var sb = new StringBuilder(Svg.abrir(W, H));
        sb.append("""
            <defs><linearGradient id="win" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0" stop-color="#2a3aa8"/><stop offset="1" stop-color="#0d1352"/>
            </linearGradient></defs>
            <style>
            text{font-family:'Courier New',Courier,monospace;font-weight:700}
            .t{fill:#fff} .d{fill:#b9c3ff} .y{fill:#ffd23f}
            .xp{animation:xp 4s ease-out infinite;transform-box:fill-box;transform-origin:left center} @keyframes xp{0%{transform:scaleX(0)}70%,100%{transform:scaleX(1)}}
            .blink{animation:bl 1s steps(1) infinite} @keyframes bl{50%{opacity:0}}
            .bob{animation:bo 1.6s steps(2) infinite} @keyframes bo{50%{transform:translateY(-4px)}}
            </style>
            """);
        sb.append(Svg.rect(0, 0, W, H, "#06081f"));

        painelPersonagem(sb, p);
        painelAtributos(sb, p.atributos());
        painelConquistas(sb, p.conquistas());
        painelMissao(sb, p);

        return sb.append("</svg>\n").toString();
    }

    private static void painelPersonagem(StringBuilder sb, Perfil p) {
        sb.append(janela(10, 10, 370, 345));
        sb.append("<rect x=\"40\" y=\"40\" width=\"130\" height=\"150\" fill=\"#0b0f2e\" stroke=\"#fff\" stroke-width=\"2\"/>");
        sb.append("<g class=\"bob\">").append(Sprite.desenhar(Sprite.QUADRO_A, 59, 47, 9)).append("</g>");

        String[] partes = p.nome().split(" ", 2);
        sb.append(Svg.texto(190, 62, 22, "y", partes[0], null));
        if (partes.length > 1) sb.append(Svg.texto(190, 88, 22, "y", partes[1], null));
        sb.append(Svg.texto(190, 118, 14, "d", "CLASSE", null));
        sb.append(Svg.texto(190, 138, 16, "t", p.classe(), null));
        sb.append(Svg.texto(190, 164, 14, "d", "GUILDA", null));
        sb.append(Svg.texto(190, 184, 16, "t", p.guilda(), null));

        sb.append(Svg.texto(40, 228, 18, "t", "LV  %02d".formatted(p.nivel()), null));
        sb.append(Svg.texto(140, 228, 13, "d", p.nivelDescricao(), null));
        sb.append(Svg.texto(40, 262, 16, "t", "HP", null)).append(barra(90, 250, 260, p.hp(), "#4ade80", ""));
        sb.append(Svg.texto(40, 290, 16, "t", "MP", null)).append(barra(90, 278, 260, p.mp(), "#60a5fa", ""));
        sb.append(Svg.texto(355, 304, 11, "d", p.mpNota(), "text-anchor=\"end\""));
        sb.append(Svg.texto(40, 326, 16, "t", "XP", null)).append(barra(90, 314, 260, p.xp(), "#ffd23f", "xp"));
    }

    private static void painelAtributos(StringBuilder sb, List<Perfil.Atributo> atributos) {
        sb.append(janela(390, 10, 600, 345));
        sb.append(Svg.texto(420, 58, 20, "y", "ATRIBUTOS", null));
        sb.append(Svg.texto(960, 58, 14, "d", "pontos livres: ∞", "text-anchor=\"end\""));
        int passo = Math.min(33, 230 / Math.max(1, atributos.size()));
        for (int i = 0; i < atributos.size(); i++) {
            var a = atributos.get(i);
            int y = 92 + i * passo;
            sb.append(Svg.texto(420, y + 12, 16, "t", a.nome(), null));
            for (int s = 0; s < 10; s++) {
                sb.append("<rect x=\"").append(600 + s * 18).append("\" y=\"").append(y)
                        .append("\" width=\"14\" height=\"14\" fill=\"").append(s < a.nivel() ? a.cor() : "#0b0f2e")
                        .append("\" stroke=\"#fff\" stroke-width=\"1.5\"/>");
            }
            sb.append(Svg.texto(790, y + 12, 16, "t", "%3d".formatted(a.nivel() * 10), null));
        }
    }

    private static void painelConquistas(StringBuilder sb, List<Perfil.Conquista> conquistas) {
        sb.append(janela(10, 365, 490, 200));
        sb.append(Svg.texto(40, 411, 20, "y", "CONQUISTAS", null));
        for (int i = 0; i < Math.min(4, conquistas.size()); i++) {
            var c = conquistas.get(i);
            int y = 445 + i * 32;
            sb.append(Svg.texto(40, y, 16, "t", c.texto(), null));
            sb.append(Svg.texto(470, y, 13, "d", c.detalhe(), "text-anchor=\"end\""));
        }
    }

    private static void painelMissao(StringBuilder sb, Perfil p) {
        sb.append(janela(510, 365, 480, 200));
        sb.append(Svg.texto(540, 411, 20, "y", "MISSÃO ATUAL", null));
        sb.append(Svg.texto(540, 447, 16, "t", p.missaoTitulo(), null));
        for (int i = 0; i < Math.min(2, p.missaoDetalhes().size()); i++)
            sb.append(Svg.texto(540, 473 + i * 22, 14, "d", "  " + p.missaoDetalhes().get(i), "xml:space=\"preserve\""));
        sb.append(Svg.texto(540, 531, 15, "t", p.missaoRecompensa(), null));
        sb.append(Svg.texto(960, 549, 16, "t blink", "▼", "text-anchor=\"end\""));
    }

    /** Janela azul com borda dupla, no estilo dos menus de RPG antigos. */
    private static String janela(int x, int y, int w, int h) {
        return "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\" fill=\"url(#win)\"/>"
                + "<rect x=\"" + (x + 3) + "\" y=\"" + (y + 3) + "\" width=\"" + (w - 6) + "\" height=\"" + (h - 6)
                + "\" fill=\"none\" stroke=\"#fff\" stroke-width=\"4\"/>"
                + "<rect x=\"" + (x + 9) + "\" y=\"" + (y + 9) + "\" width=\"" + (w - 18) + "\" height=\"" + (h - 18)
                + "\" fill=\"none\" stroke=\"#7d8bd6\" stroke-width=\"2\"/>";
    }

    private static String barra(int x, int y, int w, int pct, String cor, String classe) {
        int cheio = w * pct / 100;
        return Svg.rect(x, y, w, 14, "#0b0f2e")
                + "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + cheio + "\" height=\"14\" fill=\"" + cor + "\" class=\"" + classe + "\"/>"
                + "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + cheio + "\" height=\"4\" fill=\"#fff\" opacity=\".35\" class=\"" + classe + "\"/>"
                + "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"14\" fill=\"none\" stroke=\"#fff\" stroke-width=\"2\"/>";
    }
}