package dev.seiji.readme;

/** Utilitários pequenos para montar SVG como texto. */
public final class Svg {

    private Svg() {}

    public static String rect(int x, int y, int w, int h, String fill) {
        return "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\""
                + (fill == null ? "" : " fill=\"" + fill + "\"") + "/>";
    }

    /** Texto com fonte monoespaçada. {@code extra} recebe atributos como class/anchor. */
    public static String texto(int x, int y, int tamanho, String classe, String conteudo, String extra) {
        return "<text x=\"" + x + "\" y=\"" + y + "\" font-size=\"" + tamanho + "\" class=\"" + classe + "\""
                + (extra == null ? "" : " " + extra) + ">" + escapar(conteudo) + "</text>";
    }

    public static String escapar(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    public static String abrir(int w, int h) {
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + w + "\" height=\"" + h
                + "\" viewBox=\"0 0 " + w + " " + h + "\" shape-rendering=\"crispEdges\">\n";
    }
}
