package com.example.SqlCopilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.ai")
public class AIConfig {
    /**
     * provider: "ollama" or "openai"
     * model: model name used for Ollama/OpenAI
     * ollama.url: base url for local Ollama server (e.g. http://localhost:11434)
     */
    private String provider = "ollama";
    private String model = "mistral";
    private String ollamaUrl = "http://localhost:11434";

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getOllamaUrl() { return ollamaUrl; }
    public void setOllamaUrl(String ollamaUrl) { this.ollamaUrl = ollamaUrl; }
}
