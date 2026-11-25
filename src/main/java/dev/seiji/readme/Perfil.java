package dev.seiji.readme;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/** Tudo que aparece nas telas, lido do arquivo perfil.properties. */
public record Perfil(
        String nome, String subtitulo, String hiscore, String rodape,
        int nivel, String nivelDescricao, String classe, String guilda,
        int hp, int mp, String mpNota, int xp,
        List<Atributo> atributos, List<Conquista> conquistas,
        String missaoTitulo, List<String> missaoDetalhes, String missaoRecompensa) {

    /** Ex.: "JAVA:8:#ED8B00" -> nome JAVA, nível 8 de 10, cor laranja. */
    public record Atributo(String nome, int nivel, String cor) {
        public Atributo {
            if (nivel < 0 || nivel > 10) throw new IllegalArgumentException("Nível de " + nome + " deve ser 0..10");
        }
    }

    /** Ex.: "🏆 1º FIAP Hackathon 2026|Auto-Lab" -> texto à esquerda | detalhe à direita. */
    public record Conquista(String texto, String detalhe) {}

    public static Perfil carregar(Path arquivo) throws IOException {
        var p = new Properties();
        try (Reader r = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            p.load(r);
        }
        return new Perfil(
                req(p, "nome"), req(p, "subtitulo"), req(p, "hiscore"), req(p, "rodape"),
                Integer.parseInt(req(p, "nivel")), p.getProperty("nivel.descricao", ""),
                req(p, "classe"), req(p, "guilda"),
                pct(p, "hp"), pct(p, "mp"), p.getProperty("mp.nota", ""), pct(p, "xp"),
                Arrays.stream(req(p, "atributos").split(","))
                        .map(String::trim)
                        .map(s -> s.split(":"))
                        .map(a -> new Atributo(a[0].trim(), Integer.parseInt(a[1].trim()), a[2].trim()))
                        .toList(),
                lista(p, "conquista").stream()
                        .map(s -> s.split("\\|", 2))
                        .map(a -> new Conquista(a[0].trim(), a.length > 1 ? a[1].trim() : ""))
                        .toList(),
                req(p, "missao.titulo"), lista(p, "missao.detalhe"), req(p, "missao.recompensa"));
    }

    private static String req(Properties p, String chave) {
        String v = p.getProperty(chave);
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Faltou a chave '" + chave + "' no perfil.properties");
        return v.trim();
    }

    private static int pct(Properties p, String chave) {
        int v = Integer.parseInt(req(p, chave));
        if (v < 0 || v > 100) throw new IllegalArgumentException(chave + " deve ser 0..100");
        return v;
    }

    /** Lê chave.1, chave.2, ... até a primeira que faltar. */
    private static List<String> lista(Properties p, String prefixo) {
        var itens = new ArrayList<String>();
        for (int i = 1; p.getProperty(prefixo + "." + i) != null; i++) itens.add(p.getProperty(prefixo + "." + i).trim());
        return itens;
    }
}
