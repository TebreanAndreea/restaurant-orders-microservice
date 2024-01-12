package nl.tudelft.sem.yumyumnow.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.model.Admin;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestAdminRepository implements AdminRepository {

    private final List<Admin> admins;
    private final List<String> methodCalls;

    /**
     * Mocks an Admin Repository for testing purposes.
     */
    public TestAdminRepository() {
        this.admins = new ArrayList<>();
        this.methodCalls = new ArrayList<>();
    }

    /**
     * Getter for the methodCalls list attribute.
     *
     * @return a list of names of the methods that have been called on this instance.
     */
    private void call(String method) {
        this.methodCalls.add(method);
    }

    @Override
    public long count() {
        call("count");
        return this.admins.size();
    }

    @Override
    public <S extends Admin> long count(Example<S> example) {
        return 0;
    }

    @Override
    public void deleteById(Long id) {
        call("deleteById");
        this.admins.removeIf(x -> x.getId().equals(id));
    }

    @Override
    public void delete(Admin entity) {
        call("delete");
        this.admins.remove(entity);
    }

    @Override
    public <S extends Admin> S save(S entity) {
        call("save");
        long id = this.admins.size();
        if (id > 0) {
            id = Math.max(id, this.admins.get((int) id - 1).getId());
        }
        entity.setId(id);
        this.admins.add(entity);
        return entity;
    }

    @Override
    public Optional<Admin> findById(Long id) {
        call("findById");
        return this.admins.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return this.admins.stream().anyMatch(x -> x.getId().equals(id));
    }

    @Override
    public void flush() {

    }

    @Override
    public List<Admin> findAll() {
        call("findAll");
        return this.admins;
    }

    @Override
    public List<Admin> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Admin> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Admin> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Admin> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Admin> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public List<Admin> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public void deleteAll(Iterable<? extends Admin> entities) {

    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        this.admins.clear();
    }

    @Override
    public <S extends Admin> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Admin> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Admin> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Admin getOne(Long id) {
        return null;
    }

    @Override
    public <S extends Admin> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Admin> boolean exists(Example<S> example) {
        return false;
    }
}
