package nl.tudelft.sem.yumyumnow.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestOrderRepository implements OrderRepository {

    private final List<Order> orders;
    private final List<String> methodCalls;

    /**
     * Mocks an Order Repository for testing purposes.
     */
    public TestOrderRepository() {
        this.orders = new ArrayList<>();
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
    public long count() {
        call("count");
        return this.orders.size();
    }

    @Override
    public <S extends Order> long count(Example<S> example) {
        return 0;
    }

    @Override
    public void deleteById(Long id) {
        call("deleteById");
        this.orders.removeIf(x -> x.getOrderId().equals(id));
    }

    @Override
    public void delete(Order entity) {
        call("delete");
        this.orders.remove(entity);
    }

    @Override
    public <S extends Order> S save(S entity) {
        call("save");
        long id = this.orders.size();
        if (id > 0) {
            id = Math.max(id, this.orders.get((int) id - 1).getOrderId());
        }
        entity.setOrderId(id);
        this.orders.add(entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        call("findById");
        return this.orders.stream().filter(x -> x.getOrderId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return this.orders.stream().anyMatch(x -> x.getOrderId().equals(id));
    }

    @Override
    public void flush() {

    }

    /**
     * Getter for the orders list attribute.
     *
     * @return the list of Orders stored.
     */
    @Override
    public List<Order> findAll() {
        call("findAll");
        return orders;
    }

    @Override
    public List<Order> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Order> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public List<Order> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        this.orders.clear();
    }

    @Override
    public void deleteAll(Iterable<? extends Order> entities) {

    }

    @Override
    public <S extends Order> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Order> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Order> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Order getOne(Long id) {
        return null;
    }

    @Override
    public <S extends Order> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Order> boolean exists(Example<S> example) {
        return false;
    }
}
