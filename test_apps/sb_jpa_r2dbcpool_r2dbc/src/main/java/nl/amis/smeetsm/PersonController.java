package nl.amis.smeetsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/{id}")
    private Optional<Person> getPersonById(@PathVariable Long id) {
        return personRepository.findById(id).subscribeOn(Schedulers.boundedElastic()).blockOptional();
    }

    @GetMapping
    private List<Person> getAllPersons() {
        return personRepository.findAll().subscribeOn(Schedulers.boundedElastic()).buffer().blockLast();
    }

    @PostMapping
    public Person save(@RequestBody Person person) {
        return personRepository.save(person).subscribeOn(Schedulers.boundedElastic()).block();
    }

    @DeleteMapping("/{id}")
    public void findById(@PathVariable Long id) {
        personRepository.deleteById(id).block();
        return;
    }
}
