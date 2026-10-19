package com.substring.docmind.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom EmbeddingModel for Google Gemini via its OpenAI-compatible endpoint.
 * Bypasses the strict 'index' field requirement of the official OpenAI Java SDK
 * which Google Gemini omits from its embedding response.
 */
@Slf4j
@Component
@Primary
public class GeminiEmbeddingModel implements EmbeddingModel {

    private final RestClient restClient;
    private final String model;
    private final int dimensions;

    public GeminiEmbeddingModel(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.base-url:https://generativelanguage.googleapis.com/v1beta/openai/}") String baseUrl,
            @Value("${spring.ai.openai.embedding.options.model:gemini-embedding-001}") String model,
            @Value("${spring.ai.openai.embedding.options.dimensions:1536}") int dimensions) {
        this.model = model;
        this.dimensions = dimensions;
        
        // Strip trailing slash if present for consistent URI mapping
        String cleanBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        
        this.restClient = RestClient.builder()
                .baseUrl(cleanBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        log.info("Initialized GeminiEmbeddingModel with baseUrl={}, model={}, dimensions={}", cleanBaseUrl, model, dimensions);
    }

    @Override
    public float[] embed(Document document) {
        String content = document.getFormattedContent(MetadataMode.EMBED);
        return embed(content);
    }

    @Override
    public float[] embed(String text) {
        EmbeddingResponse response = call(new EmbeddingRequest(List.of(text), null));
        return response.getResult().getOutput();
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> instructions = request.getInstructions();
        if (instructions == null || instructions.isEmpty()) {
            return new EmbeddingResponse(List.of());
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "input", instructions,
                "dimensions", dimensions
        );

        GeminiEmbeddingResponse responseBody = restClient.post()
                .uri("/embeddings")
                .body(body)
                .retrieve()
                .body(GeminiEmbeddingResponse.class);

        if (responseBody == null || responseBody.getData() == null) {
            throw new IllegalStateException("Empty embedding response from Gemini");
        }

        List<Embedding> results = new ArrayList<>();
        for (int i = 0; i < responseBody.getData().size(); i++) {
            GeminiEmbeddingData item = responseBody.getData().get(i);
            float[] vector = toFloatArray(item.getEmbedding());
            results.add(new Embedding(vector, i));
        }

        return new EmbeddingResponse(results);
    }

    @Override
    public int dimensions() {
        return dimensions;
    }

    private float[] toFloatArray(List<Double> list) {
        if (list == null) return new float[0];
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i).floatValue();
        }
        return arr;
    }

    @Data
    public static class GeminiEmbeddingResponse {
        private String object;
        private List<GeminiEmbeddingData> data;
    }

    @Data
    public static class GeminiEmbeddingData {
        private String object;
        private List<Double> embedding;
    }
}
