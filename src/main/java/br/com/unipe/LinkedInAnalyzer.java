package br.com.unipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class LinkedInAnalyzer {

    private final Grafo rede;

    public LinkedInAnalyzer(Grafo rede) {
        this.rede = rede;
    }

    public Map<String, Integer> sugerirConexoes(String usuario) {

        Vertice usuarioVertice = rede.encontraVertice(usuario).orElse(null);

        if (usuarioVertice == null) {
            return new LinkedHashMap<>();
        }

        Set<Vertice> amigos = new HashSet<>(usuarioVertice.getAdjacencias());
        Map<String, Integer> sugestoes = new HashMap<>();

        for (Vertice amigo : amigos) {

            for (Vertice amigoDoAmigo : amigo.getAdjacencias()) {

                // Não sugerir o próprio usuário
                if (amigoDoAmigo.equals(usuarioVertice)) {
                    continue;
                }

                // Não sugerir quem já é amigo
                if (amigos.contains(amigoDoAmigo)) {
                    continue;
                }

                sugestoes.put(
                        amigoDoAmigo.getNome(),
                        sugestoes.getOrDefault(amigoDoAmigo.getNome(), 0) + 1
                );
            }
        }

        return sugestoes.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public int grauSeparacao(String origem, String destino) {
        Queue<String> fila = new LinkedList<>();
        Map<String, Integer> visitados = new HashMap<>();

        fila.add(origem);
        visitados.put(origem, 0);

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            int passos = visitados.get(atual);

            if (atual.equals(destino)) {
                return passos;
            }

            Vertice verticeAtual = rede.encontraVertice(atual).orElse(null);
            if (verticeAtual == null) continue;

            for (Vertice vizinho : verticeAtual.getAdjacencias()) {
                if (!visitados.containsKey(vizinho.getNome())) {
                    visitados.put(vizinho.getNome(), passos + 1);
                    fila.add(vizinho.getNome());
                }
            }
        }

        return -1; // sem conexão
    }

    public ResultadoCaminho rotaMaiorAfinidade(String origem, String destino) {
        if (origem == null || destino == null) {
            return new ResultadoCaminho();
        }

        if (origem.isBlank() || destino.isBlank()) {
            return new ResultadoCaminho();
        }

        return rede.dijkstra(origem, destino);
    }

    public void exibirRotaMaiorAfinidade(String origem, String destino) {
        ResultadoCaminho resultado = rotaMaiorAfinidade(origem, destino);

        System.out.println("\n===== ROTA DE MAIOR AFINIDADE =====");

        if (!resultado.existeCaminho()) {
            System.out.println("Nao existe caminho entre " + origem + " e " + destino);
            return;
        }

        System.out.println("Origem: " + origem);
        System.out.println("Destino: " + destino);

        imprimirCaminho(resultado);

        System.out.println("Custo total: " + resultado.getCustoTotal());
    }

    private void imprimirCaminho(ResultadoCaminho resultado) {
        System.out.println("\nMelhor caminho:");

        System.out.println(
                String.join(" -> ", resultado.getCaminho())
        );
    }

    // -------------------------------------------------------------------------
    // Missão 5 — Mapear Grupos Isolados (componentes conexos via DFS)
    // -------------------------------------------------------------------------

    /**
     * Percorre a rede inteira e agrupa os usuários que estão conectados entre
     * si, mas totalmente isolados dos outros grupos (componentes conexos).
     *
     * @return Uma lista de grupos, onde cada grupo é uma lista com os nomes
     *         dos usuários daquela sub-rede.
     */
    public List<List<String>> mapearGruposIsolados() {
        List<List<String>> grupos = new ArrayList<>();
        Set<Vertice> visitados = new HashSet<>();

        for (Vertice vertice : rede.getVertices()) {
            if (!visitados.contains(vertice)) {
                List<Vertice> grupoAtual = new ArrayList<>();
                dfsComponente(vertice, visitados, grupoAtual);
                grupos.add(grupoAtual.stream().map(Vertice::getNome).toList());
            }
        }

        return grupos;
    }

    /**
     * DFS recursiva que acumula, em {@code grupoAtual}, todos os vértices
     * alcançáveis a partir de {@code atual}.
     */
    private void dfsComponente(Vertice atual, Set<Vertice> visitados, List<Vertice> grupoAtual) {
        visitados.add(atual);
        grupoAtual.add(atual);

        for (Vertice vizinho : atual.getAdjacencias()) {
            if (!visitados.contains(vizinho)) {
                dfsComponente(vizinho, visitados, grupoAtual);
            }
        }

        // Como o grafo social é não-direcionado, os "adjacentes" (quem chega até
        // 'atual') também precisam ser visitados para garantir que o componente
        // seja encontrado independentemente de qual vértice disparou a busca.
        for (Vertice vizinho : atual.getAdjacentes()) {
            if (!visitados.contains(vizinho)) {
                dfsComponente(vizinho, visitados, grupoAtual);
            }
        }
    }

    public void exibirGruposIsolados() {
        List<List<String>> grupos = mapearGruposIsolados();

        System.out.println("\n===== GRUPOS ISOLADOS (SUB-REDES) =====");
        System.out.println("Total de grupos encontrados: " + grupos.size());

        for (int i = 0; i < grupos.size(); i++) {
            System.out.println("Grupo " + (i + 1) + ": " + String.join(", ", grupos.get(i)));
        }
    }
}