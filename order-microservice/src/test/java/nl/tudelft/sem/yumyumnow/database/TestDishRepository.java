package nl.tudelft.sem.yumyumnow.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestDishRepository implements DishRepository {

    private final List<Dish> dishes;
    private final List<String> methodCalls;

    public TestDishRepository() {
        this.dishes = new ArrayList<>();
        this.methodCalls = new ArrayList<>();
    }

    public List<String> getMethodCalls() {
        return methodCalls;
    }

    private void call(String method) {
        this.methodCalls.add(method);
    }

    @Override
    public List<Dish> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public void deleteAll(Iterable<? extends Dish> entities) {

    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        this.dishes.clear();
    }

    @Override
    public Optional<Dish> findById(Long id) {
        call("findById");
        return this.dishes.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return this.dishes.stream().anyMatch(x -> x.getId().equals(id));
    }

    @Override
    public void flush() {

    }

    @Override
    public Dish getOne(Long id) {
        return null;
    }

    @Override
    public <S extends Dish> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public List<Dish> findAll() {
        call("findAll");
        return dishes;
    }

    @Override
    public List<Dish> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Dish> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Dish> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Dish> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Dish> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Dish> long count(Example<S> example) {
        return 0;
    }

    @Override
    public long count() {
        call("count");
        return this.dishes.size();
    }

    @Override
    public <S extends Dish> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public void deleteById(Long id) {
        call("deleteById");
        this.dishes.removeIf(x -> x.getId().equals(id));
    }

    @Override
    public void delete(Dish entity) {
        call("delete");
        this.dishes.remove(entity);
    }

    @Override
    public <S extends Dish> S save(S entity) {
        call("save");
        long id = this.dishes.size();
        if (id > 0) {
            id = Math.max(id, this.dishes.get((int) id - 1).getId());
        }
        entity.setId(id);
        this.dishes.add(entity);
        return entity;
    }

    @Override
    public <S extends Dish> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Dish> entities) {
        call("deleteInBatch");
    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public <S extends Dish> S saveAndFlush(S entity) {
        return null;
    }

}
