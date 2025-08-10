package com.example.SqlCopilot.Service;

import com.example.SqlCopilot.Entity.DatabaseType;
import com.example.SqlCopilot.config.AIConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OllamaAiService implements AiService {

    private final AIConfig aiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public OllamaAiService(AIConfig aiConfig) {
        this.aiConfig = aiConfig;
    }

    @Override
    public String generateSql(String prompt, DatabaseType dbType) {
        // Build body for Ollama API (adapt if your Ollama version expects different format)
        String url = aiConfig.getOllamaUrl() + "/api/generate";

        Map<String, Object> body = Map.of(
                "model", aiConfig.getModel(),
                "prompt", prompt,
                "max_tokens", 1024
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> resp = restTemplate.postForEntity(url, request, Map.class);
            if (resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null) {
                // Response parsing is model/server dependent. Ollama may return "text" or nested fields.
                Object text = resp.getBody().get("text");
                if (text != null) return text.toString();
                // fallback: try "response" or "result"
                Object r = resp.getBody().get("response");
                if (r != null) return r.toString();
                return resp.getBody().toString();
            } else {
                return "/* Error: LLM did not return a result */";
            }
        } catch (Exception ex) {
            return "/* LLM call failed: " + ex.getMessage() + " */";
        }
    }
}

