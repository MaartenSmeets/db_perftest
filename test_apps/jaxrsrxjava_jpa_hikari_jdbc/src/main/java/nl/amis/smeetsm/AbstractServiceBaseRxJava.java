package nl.amis.smeetsm;

import io.reactivex.rxjava3.core.Observable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for services that has operations for creating, reading,
 * updating and deleting entities.
 * This implementation uses RxJava.
 *
 * @param <E> Entity type.
 * @author Ivan Krizsan
 */
@Transactional
public class AbstractServiceBaseRxJava<E> {
    /* Constant(s): */

    /* Instance variable(s): */
    protected CrudRepository<E,Long> mRepository;

    /**
     * Creates a service instance that will use the supplied repository for entity persistence.
     *
     * @param inRepository Entity repository.
     */
    public AbstractServiceBaseRxJava(final CrudRepository<E,Long> inRepository) {
        mRepository = inRepository;
    }

    /**
     * Saves the supplied entity.
     *
     * @param inEntity Entity to save.
     * @return Observable that will receive the saved entity, or exception if error occurs.
     */
    public Observable<E> save(final E inEntity) {
        return Observable.create(inSource -> {
            try {
                final E theSavedEntity = mRepository.save(inEntity);
                inSource.onNext(theSavedEntity);
                inSource.onComplete();
            } catch (final Exception theException) {
                inSource.onError(theException);
            }
        });
    }

    /**
     * Updates the supplied entity.
     *
     * @param inEntity Entity to update.
     * @return Observable that will receive the updated entity, or exception if error occurs.
     */
    public Observable<E> update(final E inEntity) {
        return Observable.create(inSource -> {
            try {
                final E theUpdatedEntity = mRepository.save(inEntity);
                inSource.onNext(theUpdatedEntity);
                inSource.onComplete();
            } catch (final Exception theException) {
                inSource.onError(theException);
            }
        });
    }

    /**
     * Finds the entity having supplied id.
     *
     * @param inEntityId Id of entity to retrieve.
     * @return Observable that will receive the found entity, or exception if
     * error occurs or no entity is found.
     */
    @Transactional(readOnly = true)
    public Observable<E> find(final Long inEntityId) {
        return Observable.create(inSource -> {
            try {
                final Optional<E> theEntityOptional = mRepository.findById(inEntityId);
                if (theEntityOptional.isPresent()) {
                    inSource.onNext(theEntityOptional.get());
                    inSource.onComplete();
                } else {
                    inSource.onError(new Error("Cannot find entity with id " + inEntityId));
                }
            } catch (final Exception theException) {
                inSource.onError(theException);
            }
        });
    }

    /**
     * Finds all the entities.
     *
     * @return Observable that will receive a list of entities, or exception if error occurs.
     */
    @Transactional(readOnly = true)
    public Observable<List<E>> findAll() {
        return Observable.fromIterable(mRepository.findAll()).toList().toObservable();
    }

    /**
     * Deletes the entity having supplied id.
     *
     * @param inId Id of entity to delete.
     * @return Observable that will receive completion, or exception if error occurs.
     */
    public Observable delete(final Long inId) {
        return Observable.create(inSource -> {
            try {
                mRepository.deleteById(inId);
                inSource.onComplete();
            } catch (final Exception theException) {
                inSource.onError(theException);
            }
        });
    }

    /**
     * Deletes all entities.
     *
     * @return Observable that will receive completion, or exception if error occurs.
     */
    public Observable deleteAll() {
        return Observable.create(inSource -> {
            try {
                mRepository.deleteAll();
                inSource.onComplete();
            } catch (final Exception theException) {
                inSource.onError(theException);
            }
        });
    }
}

