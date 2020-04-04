package nl.amis.smeetsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/{id}")
    private Mono<Optional<Person>> getPersonById(@PathVariable Long id) {
        return Mono.fromCallable(() -> personRepository.findById(id)).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    private Flux<Person> getAllPersons() {
        return Flux.fromIterable(() -> personRepository.findAll().iterator()).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping
    public Mono<Person> save(@RequestBody Person person) {
        return Mono.fromCallable(() -> personRepository.save(person)).subscribeOn(Schedulers.boundedElastic());

    }

    @DeleteMapping("/{id}")
    public Mono<Object> findById(@PathVariable Long id) {
        return Mono.create(s -> {
            personRepository.deleteById(id);
            s.success();
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
