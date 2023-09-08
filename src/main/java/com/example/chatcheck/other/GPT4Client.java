package com.example.chatcheck.other;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;

public class GPT4Client {
    public static void main(String[] args) {
        String apiEndpoint = "https://api.openai.com/v1/engines/davinci-codex/completions";
        String prompt = "Hello, ChatGPT4!";
        String apiKey = "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";

        String response = HttpRequest.post(apiEndpoint)
                .header(Header.CONTENT_TYPE, "application/json")
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                .body("{\"prompt\":\"" + prompt + "\",\"max_tokens\":50}")
                .timeout(20000)
                .execute()
                .body();

        System.out.println(response);
    }
}