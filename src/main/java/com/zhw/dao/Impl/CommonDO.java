package com.zhw.dao.Impl;

import com.zhw.dao.ICommonDO;
import com.zhw.pojo.CategoryInfoPO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class CommonDO implements ICommonDO {
    @Override
    public List<CategoryInfoPO> findAll() {
        return null;
    }

    @Override
    public List<CategoryInfoPO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CategoryInfoPO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<CategoryInfoPO> findAllById(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(CategoryInfoPO categoryInfoPO) {

    }

    @Override
    public void deleteAll(Iterable<? extends CategoryInfoPO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CategoryInfoPO> S save(S s) {
        return null;
    }

    @Override
    public <S extends CategoryInfoPO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<CategoryInfoPO> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends CategoryInfoPO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<CategoryInfoPO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CategoryInfoPO getOne(Integer integer) {
        return null;
    }

    @Override
    public <S extends CategoryInfoPO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CategoryInfoPO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CategoryInfoPO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CategoryInfoPO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CategoryInfoPO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CategoryInfoPO> boolean exists(Example<S> example) {
        return false;
    }
}
