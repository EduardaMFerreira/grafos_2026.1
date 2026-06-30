package br.com.unipe;


public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(true, true);

        grafo.adicionaVertices("1", "2", "3", "4", "5", "6", "7");

        grafo.addAresta("5", "6", 1);
        grafo.addAresta("5", "7", 2);

        grafo.addAresta("6", "7", 1);
        grafo.addAresta("7", "6", 1);

        grafo.addAresta("6", "2", 3);
        grafo.addAresta("7", "4", 2);

        grafo.addAresta("2", "4", 1);
        grafo.addAresta("4", "2", 1);

        grafo.addAresta("1", "2", 2);
        grafo.addAresta("4", "1", 3);

        grafo.addAresta("2", "3", 2);
        grafo.addAresta("1", "3", 1);
        grafo.addAresta("4", "3", 4);

        Grafo rede = new Grafo();
        rede.adicionaVertices("Ana", "Bruno", "Eduardo");
        rede.addAresta("Ana", "Bruno");
        rede.addAresta("Bruno", "Eduardo");

        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(rede);

        System.out.println(analyzer.grauSeparacao("Ana", "Eduardo")); // esperado: 2
        System.out.println(analyzer.grauSeparacao("Ana", "Gabriel")); // esperado: -1

        System.out.println(analyzer.grauSeparacao("Bruno", "Eduardo")); // esperado: 1
        System.out.println(analyzer.grauSeparacao("Eduardo", "Ana"));   // esperado: 2
        
        System.out.println(grafo.greedySearch("1", "5"));

        System.out.println("\n==============================");
        System.out.println("TESTE - ROTA DE MAIOR AFINIDADE");
        System.out.println("==============================");

        // Grafo de rede social (ponderado)
        Grafo redeAfinidade = new Grafo(false, true);

        redeAfinidade.adicionaVertices(
            "Ana",
            "Bruno",
            "Carlos",
            "Eduardo",
            "Fernanda",
            "Gabriel"
        );

        // Afinidades (quanto menor o peso, maior a afinidade)
        redeAfinidade.addAresta("Ana", "Bruno", 1);
        redeAfinidade.addAresta("Ana", "Carlos", 4);
        redeAfinidade.addAresta("Bruno", "Eduardo", 2);
        redeAfinidade.addAresta("Carlos", "Eduardo", 1);
        redeAfinidade.addAresta("Eduardo", "Fernanda", 1);

        // Gabriel fica isolado
        LinkedInAnalyzer analyzerAfinidade = new LinkedInAnalyzer(redeAfinidade);

        // Caminho encomtrado
        analyzerAfinidade.exibirRotaMaiorAfinidade("Ana", "Fernanda");

        // Sem caminho
        analyzerAfinidade.exibirRotaMaiorAfinidade("Ana", "Gabriel");

        // Mesmo usuário
        analyzerAfinidade.exibirRotaMaiorAfinidade("Ana", "Ana");

        System.out.println("\n==============================");
        System.out.println("TESTE - SUGESTÃO DE CONEXÕES");
        System.out.println("==============================");

        Grafo redeSugestoes = new Grafo();

        redeSugestoes.adicionaVertices(
                "Ana",
                "Bruno",
                "Carlos",
                "Daniela",
                "Eduardo",
                "Fernanda"
        );

        redeSugestoes.addAresta("Ana", "Bruno");
        redeSugestoes.addAresta("Ana", "Carlos");
        redeSugestoes.addAresta("Ana", "Daniela");

        redeSugestoes.addAresta("Bruno", "Eduardo");
        redeSugestoes.addAresta("Carlos", "Eduardo");
        redeSugestoes.addAresta("Daniela", "Fernanda");

        LinkedInAnalyzer analyzerSugestoes = new LinkedInAnalyzer(redeSugestoes);

        System.out.println(analyzerSugestoes.sugerirConexoes("Ana"));

            
        }
    }