package dev.seiji.readme;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Ponto de entrada.
 *
 * <pre>
 *   javac -d out $(find src -name "*.java")
 *   java -cp out dev.seiji.readme.GeradorReadme [perfil.properties] [pasta-de-saida]
 * </pre>
 */
public final class GeradorReadme {

    public static void main(String[] args) throws Exception {
        Path config = Path.of(args.length > 0 ? args[0] : "perfil.properties");
        Path saida = Path.of(args.length > 1 ? args[1] : "assets");

        Perfil perfil = Perfil.carregar(config);
        Files.createDirectories(saida);

        escrever(saida.resolve("titulo.svg"), TelaTitulo.gerar(perfil));
        escrever(saida.resolve("ficha.svg"), FichaPersonagem.gerar(perfil));

        System.out.println("""
            ╔════════════════════════════════╗
            ║   PLAYER 1 READY — SVGs gerados ║
            ╚════════════════════════════════╝""");
    }

    private static void escrever(Path arquivo, String conteudo) throws Exception {
        Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8);
        System.out.println("  ✔ " + arquivo);
    }
}
