package com.example.chatcheck.util;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class OpenAIStreamAPI {

    private String apiKey;
    private String model;
    private String prompt;
    private String temperature;
    private String maxTokens;
    private String frequencyPenalty;
    private String presencePenalty;

    public OpenAIStreamAPI(String apiKey, String model, String prompt, String temperature, String maxTokens, String frequencyPenalty, String presencePenalty) {
        this.apiKey = apiKey;
        this.model = model;
        this.prompt = prompt;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.frequencyPenalty = frequencyPenalty;
        this.presencePenalty = presencePenalty;
    }

    public void start() throws Exception {


        // 启用系统代理

        // 获取系统默认代理选择器
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        // 设置请求URL和参数
        // 设置请求URL和参数


        String url = "https://api.openai.com/v1/engines/davinci-codex/completions";
        JSONObject json = new JSONObject();
        json.put("prompt", prompt);
        json.put("model", model);
        json.put("temperature", temperature);
        json.put("max_tokens", maxTokens);
        json.put("frequency_penalty", frequencyPenalty);
        json.put("presence_penalty", presencePenalty);

        HttpResponse httpResponse = HttpRequest.post(url).setProxy(proxy)
                .header("Content-Type", "application/json")
                .header("cookie", "_ga=GA1.1.1124744148.1679380203; intercom-device-id-dgkjq2bp=7da5adc6-f0c1-4746-96ea-1b8d24b8f7e4; _ga_GLYMMY7CH1=GS1.1.1679532387.2.0.1679532387.0.0.0; cf_clearance=tsm8eOjVIm_YgsjWTIJj9kv6Xl2mjz6eGwkWdkayaAs-1681441836-0-1-b8944206.e1e9096a.ef951de6-250; __Host-next-auth.csrf-token=adf2d4719043a43cd2a5e05f64df64b9b07babf4ac8c196d0f554458cadc0ce3%7C1ca482a45e379eb3a32586a3ffb8aa0daaa3a78aaffb3cdae3e91bb6cab6d403; __Secure-next-auth.callback-url=https%3A%2F%2Fchat.openai.com%2F; _ga_9YTZJE58M9=GS1.1.1681442938.5.1.1681443017.0.0.0; _cfuvid=3hjqq1K.TfLBmdwMDNgfUsCbMafjT5pR9oUgBarCBXA-1681445663360-0-604800000; __cf_bm=pAzzlq9cxNaZTCwGnUCsDbe5Yuzwv2gn1o0b6xur.UA-1681447120-0-AUn54xjThdvn3VgWZ4Ch+h39mY6lTFY8iVTLxmgX9lI/R1sxldi/5OlyWn95zAI2yEphM6irRq6YccJSEsLOYRM8xWStSQ5jP3fHWK8K5rUnEAjYyol2DmHKi0CtVHJSt6WHboeLzkdLa76hbZHMgk7xRM1bJCLZU6XFQpDr/rLg; cf_clearance=JrQWS2VYIzka68yYLMC32fqomYqZrb8J9XpcdBj7w80-1681447122-0-1-280a678b.75371e19.7f0b386b-250; _puid=user-xNrTC12791kT1SWbXXTbGvW5:1681447272-iJzY8wbqIZI3tXQmK4019C7zJFboIdct6ezVtWFgsbA%3D; __Secure-next-auth.session-token=eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..VFivPrCJGGKpblQP.zWIXSIviG3_23YOgQcPdB_7dkNMEW3VHO7mOKUG07UqGG24NZJGM-U-zI8WQAGkgM7c20Obm0HNBtVva5c7tnyt7hsXIB8o0ZcXptlElV74_htghNdbRhjBIf_z3P-LV_eBoVRZ5Xj7nP1HCxhUkLj9Khm42GcmEwSY7UoVGiznwga1hJOoA8KiN1G8AA4q90GtCrnz0nAzzgZyJLVMl1odAGafj7n5RE0mz6ro3fiRJEhCHRF9YQPaG2Pm4YtI0fdwqJBHj8xOezXeGBdcXxdimP4VUyZbDuWo_dvpx4FJvfRw3Qgf8TqeUkrp0whMK-R3fN1VC_Iyl81cIrkUJ2xhtB8U6eu7o2OxIVFsk-okcfftqQqj-Vl0gsA4Luoxiw_bR84nW8vZvJdOlZ0UlRNba1byrWilwcxNReSXKxIKjqr2M9mYd2ATPKCanWMP3eR9QR6xgGVwEo4T9VIDSzq4wLIkpzn5CK_33uZUk1bM50CFNoDS2dLF4OOf9N3JtMbwCW4qq9SpSg9tjPYF-s-9J9Dqx2-eG33ISS9hBJMa83R4e2FqaQ092HbMWiHpAMiUWmZisWpARPgcmKmTWQonsYBNKgZcEtvkW8DHKlDiGkQegN44YzJnb5EwPYpzGgOuP9p97nvp96qsSiLuuP1KlPjOMFP5eORdGa3gAMHIUFYHzJmxocOZXYFnRsDLeTMNzKZy22bYXwrHofkwekHqYzXOMxNB9Xnr53Vs6M9LP7EUG8XMFrQFp6ES8SHipPbU3hOkHKhyIvqhZEj7TdOTigcBhuohAJMq1T81R4VXik5P_PZIJGxLRg-FLqu96Q-MhXvrVLezUxVmFeGNguG2q9KtcaLL6d9iSdAbADddl1zaYq39EUlJ4TvXuUQJm6q11TRTJqwQbdOWLu-GmBmL3XZgC61LuOg3FkbALc8iPfhJp_d_56pzkSRidXyBECWGsj0oO5O99RABK1MxLgMyxT4RSEYhX5EOm2HRda_lV5TjRQRPm6Q_nM6C0-ASNuyPGibWUudpDx222Ji52AuTNGmxC47cK_ohDnsbS0ps5-mAJnEV9VWN0uUf2ChjcvGJZC8XvRKj_H2vpx_J0_I5WyQPxBYUOEvU7g9aE6lhkAw7aHiTPJgpW5qeOTA3yEea94XbyI-6m5iA5IiBprPszoGS01gWjuTEuzPg7DHfomUhryMHfRRQRVrbuAl_nIMHcwbvoLhQyo8M0NzRF54mnyVrGuBgMqQVUj5x_d4V4o6QKBkQeGC6qZWG-WJDDoClKg0rTl_6gnejueWoT6psoDM4y19JmPYSZDzVlvRPHF4YXKR5xSYJf-5OCAoBVMabF68hwM_OinnthifDZpUaO2DJoJD_e-1FdE6-kyJ4VWb3g0xFB-VYMdiVbMUBtUsSmTUdqxSqwzXBPKum8bCzUhvXo2i7mu6UTMhKciyxbd2JNXS0PdMl7oSzodATRcEpeP1HKiTGRypw19CEZROpW-uZG5ZKS01U5EfGmHOdBpUP0D9YnhsSHGAxW6zeFj7w78zgT6XqAy4XXF7KQ2C4XQJEkrrTb94iJYuBB9iMLVl8h-fpvzop_l6S5yJSsjPMLTIDVUZzpKwy5jgW75OHY0WdIfvP9aJQPmCAYzwAfrckuUS6aM5v0XkXo7-F9fnq_Q5b8vqmPc6gM-3PZVa3Alon3bYuntjGqfc3cssXDcr5hxKp_HHlIF1O3V8K_MgPMKOMZC7b7aTXUeIjmFSwVFyf_DAPY7e-3IBbtqqSZRPusxvMrh3slEKzYxxiAJhbhGl4vdiXaLYft9HylmrzgLiqFM4LjYzzEFOOzzvujMhXOvUE7GHyOFqV7FyaR0p_rfINJcsm6SImCLxcjT8b1rjUNaaiCbCTO-qU1sAkd4EVzHoI2Odu8WmmdHn5CLwDPPkKU3mBwru8pHUqnJxtkWw10HIJ56XEuIbLMf6yDgYxkxSW_7CV-J6mgDEaPJ22agZwBcu3-gtKMz1dbMfW8D5H1UK-RN4PBNv2O-3l3SYSV-iEgqZoZuN7nCpZIqpajEajYz4-3OoRlQOV4d8qQ63TRKtnIvmpzuZ4khvCnJrsWX739BzOefslS1N1_gMypmpwccqLa2TtDB5W_SDWVIk9idg2qIuerqiQTy6nGJabeMby0p7UiBmO8w5inUhgP-nRae_Ic46fOP8GaFDgdFgJ8vGQLrYT2QzoBfzXragX54oHXInsfgizqxL0UTKG0-44dpJUKxsApN-u6CMVvVpMFNyesSdwfWy4FAErprLSVgBLTiHajuCYRCSInNenBV1ae2p1atSvmZ9NvSzqYOKU32M63Tz2rmavaMNHM3tfDLLqv2N34J2udVNRuGcVFvau3w4MhYGSIb9YaY-q2Ja0AWHZUFKRukpNB37VAmXn6k5pcnemddFzzw_--aE-5Vw4yqQanQ-SUdqj6LKtToWamga9Yh_ZJ0DsfjDt-nkWC9KVgEBIoSIs-fgQgJVvQWANBM4YOUD7XoIyiAy5hhinT7LmXT7r60FT-oxes4RA0bSKdv4cEKIOHl5Lv3lV4oH7UcIVW0OO-MzBV5L9wAYuX8umW7AP7MrvT4JftgW-v3FuCMbFOMcml7nhyypBcsa_3iDVA.hgO9TTrdCRpKQXlkJXIGZw; intercom-session-dgkjq2bp=Z0VpSk5pZDQ4TjR4RC9CMXE2NDRRQzZYSmY2U0FFT0VaZURkbStyK3NCYVdMTm91TUE3S05vT3ZrckwrMklBby0tazFSRXpmczVpSUpqL3IzeU9DbERqZz09--66cc5fe3bcb8bff0eccc1c62fe24de1050342dbb; __cf_bm=Vqn_yG8moxfdIIAgQi9dMOG5oSM7mPwm8URKrMa92so-1681447594-0-AeCHJdA93Mg0wRkieSQh+9wc/FQVMAJBnPuUiLUYl7kMETcIG2mvCiSQOCMvD4k3eBvIXLJMfBE5s3Gdv3dZi3A=")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJxYTEyNDMwNzY0NDZAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWV9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXIteE5yVEMxMjc5MWtUMVNXYlhYVGJHdlc1In0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwMzk1NzY1NjcxNDQwODE2NzgxNiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2ODE0NDE4ODQsImV4cCI6MTY4MjY1MTQ4NCwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.TFcxSgeAYjalWPfHT-wz9s3GsAo26FSqGUdfI7W4xQkLsVH1VQcahg8XACZzEEAww05YKUbNE2Xv9VYwnn8zHz2aNe0YyWXFMtnhgq7x2Zi8LPgjNSA-CqbiBL5_itQ7_1Qa9l0zvTpXLwTI_qoUVo4WLiSkGEbYKlOZUEtqgeY1zu1rI0ZKqR3gEAPsuiP8GWmSZ-Q4WyTvNSeUOlcgpyqMhUEkG0EQc6804WWHeiJiHAG7HJ51wfBBmWKIH2wZlbEHEm2mrhonhPlRNWvomWCR8LmX5yVnk_-d4WKVv14cPymWA8JlyWuo3owvTSXqri1ACq5CTP1JEELNLWIwtg")
                .header("origin", "https://chat.openai.com")
                .header("referer", "https://chat.openai.com/")
                .body(json.toString())
                .execute();

        if(httpResponse.getStatus() != 200) {
            throw new Exception("Failed to get response from OpenAI API.");
        }

        String responseStr = httpResponse.body();
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        if (responseJson.containsKey("choices")) {
            String text = responseJson.getJSONArray("choices").getJSONObject(0).getString("text");
            System.out.println(text);
        }
    }

    public static void main(String[] args) {
        String apiKey = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJxYTEyNDMwNzY0NDZAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWV9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXIteE5yVEMxMjc5MWtUMVNXYlhYVGJHdlc1In0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwMzk1NzY1NjcxNDQwODE2NzgxNiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2ODE0NDE4ODQsImV4cCI6MTY4MjY1MTQ4NCwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.TFcxSgeAYjalWPfHT-wz9s3GsAo26FSqGUdfI7W4xQkLsVH1VQcahg8XACZzEEAww05YKUbNE2Xv9VYwnn8zHz2aNe0YyWXFMtnhgq7x2Zi8LPgjNSA-CqbiBL5_itQ7_1Qa9l0zvTpXLwTI_qoUVo4WLiSkGEbYKlOZUEtqgeY1zu1rI0ZKqR3gEAPsuiP8GWmSZ-Q4WyTvNSeUOlcgpyqMhUEkG0EQc6804WWHeiJiHAG7HJ51wfBBmWKIH2wZlbEHEm2mrhonhPlRNWvomWCR8LmX5yVnk_-d4WKVv14cPymWA8JlyWuo3owvTSXqri1ACq5CTP1JEELNLWIwtg";
        String model = "davinci-codex";
        String prompt = "{\"text\":\"\",\"max_tokens\":50,\"temperature\":0.7}";
        String temperature = "0.7";
        String maxTokens = "50";
        String frequencyPenalty = "0";
        String presencePenalty = "0";

        OpenAIStreamAPI api = new OpenAIStreamAPI(apiKey, model, prompt, temperature, maxTokens, frequencyPenalty, presencePenalty);
        try {
            api.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

