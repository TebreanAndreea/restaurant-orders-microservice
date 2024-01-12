package nl.tudelft.sem.yumyumnow.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestRatingRepository implements RatingRepository {

    private final List<Rating> ratings;
    private final List<String> methodCalls;

    public TestRatingRepository() {
        this.ratings = new ArrayList<>();
        this.methodCalls = new ArrayList<>();
    }

    /**
     * Getter for the methodCalls list attribute.
     *
     * @return a list of names of the methods that have been called on this instance.
     */
    public List<String> getMethodCalls() {
        return methodCalls;
    }

    /**
     * Adds a method name to the methodCalls list.
     *
     * @param method the method that is being called
     */
    private void call(String method) {
        this.methodCalls.add(method);
    }


    @Override
    public List<Rating> findAll() {
        return null;
    }

    @Override
    public List<Rating> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Rating> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Rating> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Rating entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Rating> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Rating> S save(S entity) {
        call("save");
        long id = this.ratings.size();
        if (id > 0) {
            id = Math.max(id, this.ratings.get((int) id - 1).getId());
        }
        entity.setId(id);
        this.ratings.add(entity);
        return entity;
    }

    @Override
    public <S extends Rating> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Rating> findById(Long id) {
        call("findById");
        return this.ratings.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Rating> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Rating> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Rating getOne(Long id) {
        return null;
    }

    @Override
    public <S extends Rating> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Rating> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Rating> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Rating> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Rating> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Rating> boolean exists(Example<S> example) {
        return false;
    }
}
