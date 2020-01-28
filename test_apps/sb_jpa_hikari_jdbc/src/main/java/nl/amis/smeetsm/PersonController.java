package nl.amis.smeetsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/{id}")
    private Optional<Person> getPersonById(@PathVariable Long id) {
        return personRepository.findById(id);
    }

    @GetMapping
    private Iterable<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @PostMapping
    public Person save(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @DeleteMapping("/{id}")
    public void findById(@PathVariable Long id) {
        personRepository.deleteById(id);
        return;
    }
}
