package com.demidovn.fruitbounty.game.services;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;

import java.util.Arrays;

/**
 * A set of methods to simplify working with 'fastutil' collections.
 */
public class FastUtilAssistant {
    /**
     * Method to get iterator over {@link Int2LongMap#int2LongEntrySet()}.
     * If possible, the answer will be a fast version of the iterator
     * that reuses the object {@link Int2LongMap.Entry}.
     */
    public static ObjectIterator<Int2IntMap.Entry> entrySetIterator(Int2IntMap map) {
        ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        if (entries instanceof Int2IntMap.FastEntrySet) {
            return ((Int2IntMap.FastEntrySet) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static ObjectIterator<Int2LongMap.Entry> entrySetIterator(Int2LongMap map) {
        ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
        if (entries instanceof Int2LongMap.FastEntrySet) {
            return ((Int2LongMap.FastEntrySet) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static <K, V>ObjectIterator<Object2ObjectMap.Entry<K, V>> entrySetIterator(Object2ObjectMap<K, V> map) {
        ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        if (entries instanceof Object2ObjectMap.FastEntrySet) {
            return ((Object2ObjectMap.FastEntrySet<K, V>) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static <T> ObjectIterator<Long2ObjectMap.Entry<T>> entrySetIterator(Long2ObjectMap<T> map) {
        ObjectSet<Long2ObjectMap.Entry<T>> entries = map.long2ObjectEntrySet();
        if (entries instanceof Int2ObjectMap.FastEntrySet) {
            return ((Long2ObjectMap.FastEntrySet<T>) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static <T> ObjectIterator<Int2ObjectMap.Entry<T>> entrySetIterator(Int2ObjectMap<T> map) {
        ObjectSet<Int2ObjectMap.Entry<T>> entries = map.int2ObjectEntrySet();
        if (entries instanceof Int2ObjectMap.FastEntrySet) {
            return ((Int2ObjectMap.FastEntrySet<T>) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static <T> ObjectIterator<Short2ObjectMap.Entry<T>> entrySetIterator(Short2ObjectMap<T> map) {
        ObjectSet<Short2ObjectMap.Entry<T>> entries = map.short2ObjectEntrySet();
        if (entries instanceof Int2ObjectMap.FastEntrySet) {
            return ((Short2ObjectMap.FastEntrySet<T>) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static ObjectIterator<Int2DoubleMap.Entry> entrySetIterator(Int2DoubleMap map) {
        ObjectSet<Int2DoubleMap.Entry> entries = map.int2DoubleEntrySet();
        if (entries instanceof Int2DoubleMap.FastEntrySet) {
            return ((Int2DoubleMap.FastEntrySet) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    /**
     * Merges 2 histograms into the first one, sums counters for shared keys.
     * */
    public static void appendIntToDouble(Int2DoubleMap dest, Int2DoubleMap source){
        ObjectIterator<Int2DoubleMap.Entry> it = FastUtilAssistant.entrySetIterator(source);
        while (it.hasNext()) {
            Int2DoubleMap.Entry en =it.next();
            double count =dest.get(en.getIntKey());
            count+=en.getDoubleValue();
            dest.put(en.getIntKey(),count);
        }
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <T> ObjectIterator<Object2LongMap.Entry<T>> entrySetIterator(Object2LongMap<T> map) {
        ObjectSet<Object2LongMap.Entry<T>> entries = map.object2LongEntrySet();
        if (entries instanceof Object2LongMap.FastEntrySet) {
            return ((Object2LongMap.FastEntrySet) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <T> ObjectIterator<Object2DoubleMap.Entry<T>> entrySetIterator(Object2DoubleMap<T> map) {
        ObjectSet<Object2DoubleMap.Entry<T>> entries = map.object2DoubleEntrySet();
        if (entries instanceof Object2LongMap.FastEntrySet) {
            return ((Object2DoubleMap.FastEntrySet) entries).fastIterator();
        } else {
            return entries.iterator();
        }
    }

    public static <T> void addLongs(Object2LongMap<T> result, Object2LongMap<T> toAdd) {
        ObjectIterator<Object2LongMap.Entry<T>> it
                = FastUtilAssistant.entrySetIterator(toAdd);
        while (it.hasNext()) {
            Object2LongMap.Entry<T> entry = it.next();
            T key = entry.getKey();
            long count = entry.getLongValue();
            long prevCount = result.getLong(key);
            result.put(key, prevCount + count);
        }
    }

    public static <T> void addLongs(Object2ObjectMap<T,long[]> result, Object2ObjectMap<T,long[]> toAdd) {
        ObjectIterator<Object2ObjectMap.Entry<T,long[]>> it
                = FastUtilAssistant.entrySetIterator(toAdd);
        while (it.hasNext()) {
            Object2ObjectMap.Entry<T,long[]> entry = it.next();
            T key = entry.getKey();
            long[] count = entry.getValue();
            if (count!=null) {
                long[] prevCount = result.get(key);
                if (prevCount==null){
                    result.put(key, Arrays.copyOf(count,count.length));
                    continue;
                }
                for (int i = 0; i < count.length; i++) {
                    prevCount[i]+=count[i];
                }
            }
        }
    }

    public static void fillTo(IntCollection intCollection, int to) {
        for (int i = 0; i < to; i++) {
            intCollection.add(i);
        }
    }

}
