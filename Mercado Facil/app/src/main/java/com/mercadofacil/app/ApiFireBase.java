import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class FirestoreApi {

    private static final String SERVICE_ACCOUNT_FILE = "../sample_data/serviceAccountKey.json";
    private static final String PROJECT_ID = "mercado-facil-a07ec";
    private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static String getAccessToken() throws Exception {
        try (FileInputStream serviceAccountStream = new FileInputStream(SERVICE_ACCOUNT_FILE)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped("https://www.googleapis.com/auth/datastore");
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        }
    }

    private static HttpRequest.Builder createRequestBuilder(String url) throws Exception {
        String token = getAccessToken();
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
    }

    public static ObjectNode consultarDocumento(String collection, String documentId) throws Exception {
        String url = String.format("%s/%s/%s", BASE_URL, collection, documentId);
        HttpRequest request = createRequestBuilder(url).GET().build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return (ObjectNode) objectMapper.readTree(response.body());
        } else {
            throw new Exception("Erro ao consultar documento: " + response.statusCode() + " - " + response.body());
        }
    }

    public static ObjectNode inserirDocumento(String collection, String documentId, Map<String, Object> data) throws Exception {
        String url = String.format("%s/%s?documentId=%s", BASE_URL, collection, documentId);
        ObjectNode firestoreData = converterParaFirestoreFormat(data);

        HttpRequest request = createRequestBuilder(url)
                .POST(HttpRequest.BodyPublishers.ofString(firestoreData.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return (ObjectNode) objectMapper.readTree(response.body());
        } else {
            throw new Exception("Erro ao inserir documento: " + response.statusCode() + " - " + response.body());
        }
    }

    public static ObjectNode atualizarDocumento(String collection, String documentId, Map<String, Object> data) throws Exception {
        String url = String.format("%s/%s/%s", BASE_URL, collection, documentId);
        ObjectNode firestoreData = converterParaFirestoreFormat(data);

        HttpRequest request = createRequestBuilder(url)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(firestoreData.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return (ObjectNode) objectMapper.readTree(response.body());
        } else {
            throw new Exception("Erro ao atualizar documento: " + response.statusCode() + " - " + response.body());
        }
    }

    public static String deletarDocumento(String collection, String documentId) throws Exception {
        String url = String.format("%s/%s/%s", BASE_URL, collection, documentId);
        HttpRequest request = createRequestBuilder(url).DELETE().build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return "Documento deletado com sucesso";
        } else {
            throw new Exception("Erro ao deletar documento: " + response.statusCode() + " - " + response.body());
        }
    }

    private static ObjectNode converterParaFirestoreFormat(Map<String, Object> data) {
        ObjectNode fieldsNode = objectMapper.createObjectNode();

        data.forEach((key, value) -> {
            ObjectNode valueNode = objectMapper.createObjectNode();

            if (value instanceof Boolean) {
                valueNode.put("booleanValue", (Boolean) value);
            } else if (value instanceof Integer) {
                valueNode.put("integerValue", String.valueOf(value));
            } else if (value instanceof Long) {
                valueNode.put("integerValue", String.valueOf(value));
            } else if (value instanceof Double || value instanceof Float) {
                valueNode.put("doubleValue", ((Number) value).doubleValue());
            } else {
                valueNode.put("stringValue", String.valueOf(value));
            }

            fieldsNode.set(key, valueNode);
        });

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("fields", fieldsNode);
        return rootNode;
    }

    // Teste rápido
    public static void main(String[] args) {
        try {
            var dados = Map.of(
                    "nome", "Produto Exemplo",
                    "preco", 19.99,
                    "disponivel", true
            );

            System.out.println("Inserindo documento...");
            var docInserido = inserirDocumento("Produtos", "abc123", dados);
            System.out.println(docInserido.toPrettyString());

            System.out.println("\nConsultando documento...");
            var docConsultado = consultarDocumento("Produtos", "abc123");
            System.out.println(docConsultado.toPrettyString());

            System.out.println("\nAtualizando documento...");
            var dadosAtualizados = Map.of(
                    "nome", "Produto Atualizado",
                    "preco", 29.99,
                    "disponivel", false
            );
            var docAtualizado = atualizarDocumento("Produtos", "abc123", dadosAtualizados);
            System.out.println(docAtualizado.toPrettyString());

            // Descomente para deletar
            // System.out.println("\nDeletando documento...");
            // String resultado = deletarDocumento("Produtos", "abc123");
            // System.out.println(resultado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}