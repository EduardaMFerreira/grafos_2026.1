package br.com.unipe;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // =====================================================================
        // CENÁRIO DE TESTES — LinkedIn Analyzer
        // Rede principal: Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda
        // Grupo isolado 1: Gabriel, Hugo
        // Grupo isolado 2: Igor, Juliana
        // =====================================================================

        Grafo rede = new Grafo(false, true); // não-dirigido, ponderado

        rede.adicionaVertices(
                "Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda",
                "Gabriel", "Hugo",
                "Igor", "Juliana"
        );

        // Conexões e afinidades (peso baixo = muita afinidade)
        rede.addAresta("Ana", "Bruno", 1);
        rede.addAresta("Ana", "Carlos", 2);
        rede.addAresta("Ana", "Daniela", 8);
        rede.addAresta("Bruno", "Eduardo", 1);
        rede.addAresta("Carlos", "Eduardo", 1);
        rede.addAresta("Daniela", "Fernanda", 5);
        rede.addAresta("Eduardo", "Fernanda", 1);

        // Grupo isolado 1
        rede.addAresta("Gabriel", "Hugo", 1);

        // Grupo isolado 2
        rede.addAresta("Igor", "Juliana", 1);

        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(rede);

        // ---------------------------------------------------------------
        // Missão 2 — Sugestão de conexões (amigos de 2º grau)
        // ---------------------------------------------------------------
        System.out.println("==============================");
        System.out.println("MISSAO 2 - SUGESTAO DE CONEXOES");
        System.out.println("==============================");

        Map<String, Integer> sugestoesAna = analyzer.sugerirConexoes("Ana");
        System.out.println("Sugestoes para Ana: " + sugestoesAna);
        // Esperado: Eduardo aparece com 2 amigos em comum (Bruno e Carlos),
        // já que Daniela é conexão direta e não deve ser sugerida.

        // ---------------------------------------------------------------
        // Missão 3 — Grau de separação
        // ---------------------------------------------------------------
        System.out.println("\n==============================");
        System.out.println("MISSAO 3 - GRAU DE SEPARACAO");
        System.out.println("==============================");

        System.out.println("Ana -> Bruno: " + analyzer.grauSeparacao("Ana", "Bruno"));       // esperado: 1
        System.out.println("Ana -> Eduardo: " + analyzer.grauSeparacao("Ana", "Eduardo"));   // esperado: 2
        System.out.println("Ana -> Fernanda: " + analyzer.grauSeparacao("Ana", "Fernanda")); // esperado: 2 (via Daniela)
        System.out.println("Ana -> Gabriel: " + analyzer.grauSeparacao("Ana", "Gabriel"));   // esperado: -1 (isolados)

        // ---------------------------------------------------------------
        // Missão 4 — Rota e custo de maior afinidade (Dijkstra)
        // ---------------------------------------------------------------
        System.out.println("\n==============================");
        System.out.println("MISSAO 4 - ROTA DE MAIOR AFINIDADE");
        System.out.println("==============================");

        // Demonstra que o caminho com menos "saltos" (Ana -> Daniela -> Fernanda,
        // custo 8 + 5 = 13) NÃO é o de maior afinidade. O Dijkstra deve encontrar
        // Ana -> Bruno -> Eduardo -> Fernanda, com custo 1 + 1 + 1 = 3.
        analyzer.exibirRotaMaiorAfinidade("Ana", "Fernanda");

        // Sem caminho possível (grupos isolados)
        analyzer.exibirRotaMaiorAfinidade("Ana", "Gabriel");

        // Mesmo usuário como origem e destino
        analyzer.exibirRotaMaiorAfinidade("Ana", "Ana");

        // ---------------------------------------------------------------
        // Missão 5 — Mapear grupos isolados (componentes conexos)
        // ---------------------------------------------------------------
        System.out.println("\n==============================");
        System.out.println("MISSAO 5 - GRUPOS ISOLADOS");
        System.out.println("==============================");

        analyzer.exibirGruposIsolados();
        // Esperado: 3 grupos —
        // [Ana, Bruno, Carlos, Daniela, Eduardo, Fernanda], [Gabriel, Hugo], [Igor, Juliana]

        List<List<String>> grupos = analyzer.mapearGruposIsolados();
        System.out.println("\nTotal de sub-redes isoladas: " + grupos.size());
    }
}