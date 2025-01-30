package br.com.ccs.threadlocalexemplo.services;

import br.com.ccs.threadlocalexemplo.core.ApplicationIdHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppService {

    public String getApplicationId() {
        log.info("Valor de ApplicationId: " + ApplicationIdHolder.get());
        return ApplicationIdHolder.get();
    }
}
