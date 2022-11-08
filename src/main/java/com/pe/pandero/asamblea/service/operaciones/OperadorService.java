package com.pe.pandero.asamblea.service.operaciones;

import com.pe.pandero.asamblea.service.PanListaAsociadosService;
import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;
import com.pe.pandero.asamblea.web.rest.vm.BalotasVm;
import com.pe.pandero.asamblea.web.rest.vm.SorteoVM;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OperadorService {

    private final Logger log = LoggerFactory.getLogger(OperadorService.class);

    private final PanListaAsociadosService panListaAsociadosService;

    public OperadorService(PanListaAsociadosService panListaAsociadosService) {
        this.panListaAsociadosService = panListaAsociadosService;
    }

    public Mono<List<PanListaAsociadosDTO>> realizarSorteo() {
        log.debug("Inicia proceso para realizar el sorted de asociados");
        Collection<Integer> numbers = Stream.of((int) (Math.random() * (75 - 25 + 1) + 25)).collect(Collectors.toList());
        return panListaAsociadosService
            .findAll()
            .collectList()
            .map(
                balotasBase -> {
                    balotasBase.sort(Comparator.comparing(PanListaAsociadosDTO::getPosicionId));
                    Collections.shuffle(balotasBase);
                    balotasBase.remove(0);
                    balotasBase.remove(5);
                    balotasBase.remove(10);
                    balotasBase.remove(50);
                    balotasBase.remove(23);
                    balotasBase.remove(18);
                    balotasBase.remove(22);
                    balotasBase.remove(26);
                    balotasBase.remove(30);
                    balotasBase.remove(63);
                    return balotasBase;
                }
            );
    }

    public Mono<List<SorteoVM>> playTheDraw(List<BalotasVm> balotas) {
        return Flux
            .fromIterable(balotas)
            .flatMap(
                v ->
                    panListaAsociadosService
                        .findByPosicionIdAndEstadoHabil(v.getNumero())
                        .map(valor -> new SorteoVM(v.getPosicion(), valor))
            )
            .mapNotNull(
                sorteoVM ->
                    (sorteoVM.getOrden() == 10 && sorteoVM.getAsociados().getEstadoHabil().equals("true"))
                        ? sorteoVM
                        : (sorteoVM.getAsociados().getEstadoHabil().contains("true") ? sorteoVM : null)
            )
            .sort(Comparator.comparing(SorteoVM::getOrden))
            .collectList();
    }
}
