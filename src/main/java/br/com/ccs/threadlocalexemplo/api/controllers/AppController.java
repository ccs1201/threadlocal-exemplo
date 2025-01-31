package br.com.ccs.threadlocalexemplo.api.controllers;

import br.com.ccs.threadlocalexemplo.core.ApplicationIdHolder;
import br.com.ccs.threadlocalexemplo.services.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class AppController {

    private final AppService service;

    @RequestMapping
    @ResponseStatus(OK)
    public String getApplicationId() {
        logger("síncrona");
        /*
        Aqui estou alterando o applicationID concatenando com um UUID
        para mostrar que o mesmo é alterado para cada chamada, e não deve
        afetar chamadas para outros endpoints. Pois o ApplicationId é setado
        no filtro que é chamado a cada requisição que chega, e cada thread tem o
        seu próprio ApplicationId. Tradicionalmente em JAVA uma Thread por request.
         */
        ApplicationIdHolder.set(ApplicationIdHolder.get() + " - " + UUID.randomUUID());
        return service.getApplicationId();
    }

    @RequestMapping("/async_sem_appid")
    @ResponseStatus(BAD_REQUEST)
    public CompletableFuture<String> asyncSemAppId() {
        logger("assíncrona - async_sem_appid");
        log.info("Valor do ApplicationId antes de iniciar a nova Thread: " + ApplicationIdHolder.get());
        /*
        Aqui não copiamos o ApplicationId do Thread atual para setar na nova Thread que será criada
        pelo CompletableFuture
         */
        return CompletableFuture.supplyAsync(service::getApplicationId)
                .thenApply(s -> {
                    if (Objects.isNull(s))
                        return "ApplicationId é nulo.";
                    return "ApplicationId deveria ser nulo mas é: " + s;
                });
    }


    @RequestMapping("/async_com_appid")
    @ResponseStatus(OK)
    public CompletableFuture<String> asyncComAppId() {
        logger("assíncrona - async_com_appid");

        /*
        Aqui nos copiamos o ApplicationId do Thread atual para setar na nova Thread que será criada
        pelo CompletableFuture
         */
        var appid = ApplicationIdHolder.get();

        return CompletableFuture.supplyAsync(() -> {
                    ApplicationIdHolder.set(appid);
                    return service.getApplicationId();
                })
                .thenApply(s -> Objects.requireNonNull(s, "ApplicationId não deveria ser nulo."));
    }

    private static void logger(String tipo) {
        log.info("Iniciando chamada {}", tipo);
    }
}
