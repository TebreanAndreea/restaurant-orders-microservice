package nl.tudelft.sem.yumyumnow.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestVendorRepository implements VendorRepository {
    private final List<Vendor> vendors;
    private final List<String> methodCalls;

    public TestVendorRepository() {
        this.vendors = new ArrayList<>();
        this.methodCalls = new ArrayList<>();
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }

    private void call(String method) {
        this.methodCalls.add(method);
    }



    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Returns the number of instances matching the given {@link Example}.
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return the number of instances matching the {@link Example}.
     */
    @Override
    public <S extends Vendor> long count(Example<S> example) {
        return 0;
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    @Override
    public void deleteById(Long id) {

    }

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public void delete(Vendor entity) {

    }

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    @Override
    public void deleteAll(Iterable<? extends Vendor> entities) {

    }

    /**
     * Deletes all entities managed by the repository.
     */
    @Override
    public void deleteAll() {

    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    @Override
    public <S extends Vendor> S save(S entity) {
        call("save");
        long id = this.vendors.size();
        if (id > 0) {
            id = Math.max(id, this.vendors.get((int) id - 1).getId());
        }
        entity.setId(id);
        this.vendors.add(entity);
        return entity;
    }

    @Override
    public <S extends Vendor> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public Optional<Vendor> findById(Long id) {

        call("findById");
        return this.vendors.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public boolean existsById(Long id) {
        return false;
    }

    /**
     * Flushes all pending changes to the database.
     */
    @Override
    public void flush() {

    }

    /**
     * Saves an entity and flushes changes instantly.
     *
     * @param entity entity
     * @return the saved entity
     */
    @Override
    public <S extends Vendor> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Deletes the given entities in a batch which means it will create a single. Assume that we will clear
     * the {@link EntityManager} after the call.
     *
     * @param entities iterable of entities
     */
    @Override
    public void deleteInBatch(Iterable<Vendor> entities) {

    }

    /**
     * Deletes all entities in a batch call.
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * Returns a reference to the entity with the given identifier. Depending on how the JPA persistence provider is
     * implemented this is very likely to always return an instance and throw an
     * {@link EntityNotFoundException} on first access. Some of them will reject invalid identifiers
     * immediately.
     *
     * @param id must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @see EntityManager#getReference(Class, Object) for details on when an exception is thrown.
     */
    @Override
    public Vendor getOne(Long id) {
        return null;
    }

    /**
     * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
     *
     * @param example must not be {@literal null}.
     * @return a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     * @throws IncorrectResultSizeDataAccessException if the Example yields more than one result.
     */
    @Override
    public <S extends Vendor> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public List<Vendor> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public List<Vendor> findAll() {
        return null;
    }

    @Override
    public List<Vendor> findAll(Sort sort) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable pageble
     * @return a page of entities
     */

    @Override
    public Page<Vendor> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Vendor> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Vendor> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
     * {@link Page} is returned.
     *
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return a {@link Page} of entities matching the given {@link Example}.
     */
    @Override
    public <S extends Vendor> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }



    /**
     * Checks whether the data store contains elements that match the given {@link Example}.
     *
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @return {@literal true} if the data store contains elements that match the given {@link Example}.
     */
    @Override
    public <S extends Vendor> boolean exists(Example<S> example) {
        return false;
    }
}
