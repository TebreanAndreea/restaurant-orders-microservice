package nl.tudelft.sem.yumyumnow.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.model.Customer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestCustomerRepository implements CustomerRepository{

    private final List<Customer> customers;

    private final List<String> methodCalls;

    /**
     * Mocks an Customer Repository for testing purposes.
     */
    public TestCustomerRepository() {
        this.customers = new ArrayList<>();
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
    public List<Customer> findAll() {
        call("findAll");
        return this.customers;
    }

    @Override
    public List<Customer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Customer> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        call("count");
        return this.customers.size();
    }

    @Override
    public void deleteById(Long id) {
        call("deleteById");
        this.customers.removeIf(x -> x.getId().equals(id));
    }

    @Override
    public void delete(Customer entity) {
        call("delete");
        this.customers.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> entities) {

    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        this.customers.clear();
    }

    @Override
    public <S extends Customer> S save(S entity) {
        call("save");
        long id = this.customers.size();
        if(id > 0){
            id = Math.max(id, this.customers.get((int) id - 1).getId());
        }
        entity.setId(id);
        this.customers.add(entity);
        return entity;
    }

    @Override
    public <S extends Customer> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        call("findById");
        return this.customers.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return this.customers.stream().anyMatch(x -> x.getId().equals(id));
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Customer> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Customer> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Customer getOne(Long id) {
        return null;
    }

    @Override
    public <S extends Customer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Customer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Customer> boolean exists(Example<S> example) {
        return false;
    }
}
