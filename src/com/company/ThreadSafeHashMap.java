package com.company;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadSafeHashMap<K, V> extends HashMap<K, V> {
    private final HashMap<K, V> list = new HashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ThreadSafeHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ThreadSafeHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ThreadSafeHashMap() {
        super();
    }

    public ThreadSafeHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public int size() {
        return doWithReadLock(list::size);
    }

    @Override
    public boolean isEmpty() {
        return doWithReadLock(list::isEmpty);
    }

    @Override
    public V get(Object key) {
        return doWithReadLock(() -> list.get(key));
    }

    @Override
    public boolean containsKey(Object key) {
        return doWithReadLock(() -> list.containsKey(key));
    }

    @Override
    public V put(K key, V value) {
        return doWithWriteLock(() -> list.put(key, value));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        readWriteLock.writeLock().lock();
        try {
            list.putAll(m);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V remove(Object key) {
        return doWithWriteLock(() -> list.remove(key));
    }

    @Override
    public void clear() {
        readWriteLock.writeLock().lock();
        try {
            list.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return doWithReadLock(() -> list.containsValue(value));
    }

    @Override
    public Set<K> keySet() {
        return doWithWriteLock(list::keySet);
    }

    @Override
    public Collection<V> values() {
        return doWithWriteLock(list::values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return doWithWriteLock(list::entrySet);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return doWithWriteLock(() -> list.getOrDefault(key, defaultValue));
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return doWithWriteLock(() -> list.putIfAbsent(key, value));
    }

    @Override
    public boolean remove(Object key, Object value) {
        return doWithWriteLock(() -> list.remove(key, value));
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return doWithWriteLock(() -> list.replace(key, oldValue, newValue));
    }

    @Override
    public V replace(K key, V value) {
        return doWithWriteLock(() -> list.replace(key, value));
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return doWithWriteLock(() -> list.computeIfAbsent(key, mappingFunction));
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return doWithWriteLock(() -> list.computeIfPresent(key, remappingFunction));
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return doWithWriteLock(() -> list.compute(key, remappingFunction));
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return doWithWriteLock(() -> list.merge(key, value, remappingFunction));
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        readWriteLock.writeLock().lock();
        try {
            list.forEach(action);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        readWriteLock.writeLock().lock();
        try {
            list.replaceAll(function);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Object clone() {
        return doWithWriteLock(list::clone);
    }

    private <R> R doWithWriteLock(Supplier<R> supplier) {
        readWriteLock.writeLock().lock();
        try {
            return supplier.get();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private <R> R doWithReadLock(Supplier<R> supplier) {
        readWriteLock.readLock().lock();
        try {
            return supplier.get();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
