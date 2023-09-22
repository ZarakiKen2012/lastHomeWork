import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consumer {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String my_answer = "";
        //Создаем пользователя для сохранения
        Map<String, Object> json = new HashMap<>();// Создаем json для отправки
        json.put("id", 3);
        json.put("name", "James");
        json.put("lastName", "Brown");
        json.put("age", 12);

        String url = "http://94.198.50.185:7081/api/users";

        // Отправляем пустой Гет запрос по заданному url
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, null, String.class);

        // Извлекаем заголовки ответа
        HttpHeaders headers = responseEntity.getHeaders();

        // Извлекаем cookies из заголовков
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);

        String my_cookies = cookies.get(0);
        String sessionId = my_cookies.substring(my_cookies.indexOf("=") + 1, my_cookies.indexOf(";"));

        //Создаем свой заголовок
        HttpHeaders my_headers = new HttpHeaders();
        my_headers.set(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);

        //Сохраняем пользователя
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(json, my_headers);// Создаем запрос с установленным заголовком и пользователем
        responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, request, String.class);
        my_answer += responseEntity.getBody();

        //Изменяем пользователя
        json.put("name", "Thomas");
        json.put("lastName", "Shelby");
        request = new HttpEntity<>(json, my_headers);
        responseEntity = restTemplate.exchange(
                url, HttpMethod.PUT, request, String.class);
        my_answer += responseEntity.getBody();

        //Удаляем пользователя
        request = new HttpEntity<>(null, my_headers);
        responseEntity = restTemplate.exchange(
                url + "/3", HttpMethod.DELETE, request, String.class);
        my_answer += responseEntity.getBody();
        System.out.println(my_answer);

    }
}
