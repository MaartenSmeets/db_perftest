package nl.amis.smeetsm;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends AbstractServiceBaseRxJava<Person> {

    public PersonService(final CrudRepository<Person,Long> inRepository) {
        super(inRepository);
    }
}
