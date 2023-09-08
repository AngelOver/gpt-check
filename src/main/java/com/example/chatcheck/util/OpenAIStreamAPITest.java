package com.example.chatcheck.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OpenAIStreamAPITest {

    public static void main(String[] args) {
        String kes = "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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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,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";

        Set<String> set = new HashSet<>(Arrays.asList(kes.split(",")));
        System.out.println(String.join(",", set));
        System.out.println(set.size());
    }

//    public static void main(String[] args) {
//        String apiKey = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJxYTEyNDMwNzY0NDZAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWV9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXIteE5yVEMxMjc5MWtUMVNXYlhYVGJHdlc1In0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwMzk1NzY1NjcxNDQwODE2NzgxNiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2ODE0NDE4ODQsImV4cCI6MTY4MjY1MTQ4NCwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.TFcxSgeAYjalWPfHT-wz9s3GsAo26FSqGUdfI7W4xQkLsVH1VQcahg8XACZzEEAww05YKUbNE2Xv9VYwnn8zHz2aNe0YyWXFMtnhgq7x2Zi8LPgjNSA-CqbiBL5_itQ7_1Qa9l0zvTpXLwTI_qoUVo4WLiSkGEbYKlOZUEtqgeY1zu1rI0ZKqR3gEAPsuiP8GWmSZ-Q4WyTvNSeUOlcgpyqMhUEkG0EQc6804WWHeiJiHAG7HJ51wfBBmWKIH2wZlbEHEm2mrhonhPlRNWvomWCR8LmX5yVnk_-d4WKVv14cPymWA8JlyWuo3owvTSXqri1ACq5CTP1JEELNLWIwtg";
//        String model = "text-davinci-002-render-sha";
//        String prompt = "{\"action\":\"next\",\"messages\":[{\"id\":\"1c1d5427-49d6-45e7-947f-78fb267d99c9\",\"author\":{\"role\":\"user\"},\"content\":{\"content_type\":\"text\",\"parts\":[\"我挺累的\"]}}],\"parent_message_id\":\"719ec710-a483-4712-b4a6-e7a1362032a5\",\"model\":\"text-davinci-002-render-sha\",\"timezone_offset_min\":-480}";
//        String temperature = "0.5";
//        String maxTokens = "50";
//        String topP = "1";
//        String frequencyPenalty = "0";
//        String presencePenalty = "0";
//
//        OpenAIStreamAPI api = new OpenAIStreamAPI(apiKey, model, prompt, temperature, maxTokens, topP, frequencyPenalty, presencePenalty);
//        try {
//            api.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
