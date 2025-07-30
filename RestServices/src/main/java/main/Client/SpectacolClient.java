package main.Client;

import Domain.Spectacol;
import main.Exception.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class SpectacolClient {
    public static final String URL = "http://localhost:8080/festival/spectacole";
    private final RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Spectacol[] getAll(){
        return execute(()->restTemplate.getForObject(URL, Spectacol[].class));
    }

    public Spectacol getById(Integer id){
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Spectacol.class));
    }

    public Spectacol create(Spectacol user) {
        return execute(() -> restTemplate.postForObject(URL, user, Spectacol.class));
    }

    public void update(Spectacol user) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, user.getId()), user);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
