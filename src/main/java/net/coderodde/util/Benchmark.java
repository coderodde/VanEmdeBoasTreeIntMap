package net.coderodde.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import net.coderodde.util.VanEmdeBoasTreeIntMap.KeyIterator;
import net.coderodde.util.VanEmdeBoasTreeIntMap.KeyValueIterator;
import net.coderodde.util.VanEmdeBoasTreeIntMap.KeyValueMapping;

/**
 * This class benchmarks the {@link VanEmdeBoasTreeIntMap} against a 
 * {@link TreeMap} and {@link HashMap}.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 13, 2017)
 */
public final class Benchmark {
    
    private static final int MINIMUM_KEY = -1000_000;
    private static final int MAXIMUM_KEY = 1_000_000;
    private static final int INTEGER_ARRAY_LENGTH = 1_500_000;
    private static final Random RANDOM;
    
    static {
        long seed = System.currentTimeMillis();
        System.out.println("Seed = " + seed);
        RANDOM = new Random(seed);
    }
    
    public static void main(String[] args) {
        warmup();
        
        System.out.println();
        
        benchmark();
    }
    
    private static void benchmark() {
        Integer[] testArray = createRandomIntegerArray(INTEGER_ARRAY_LENGTH,
                                                       MINIMUM_KEY, 
                                                       MAXIMUM_KEY);
        benchmarkTreeMap(testArray);
        System.out.println();
        benchmarkHashMap(testArray);
        System.out.println();
        benchmarkVebMap(testArray);
    }
    
    private static void benchmarkTreeMap(Integer[] testArray) {
        benchmarkMap(new TreeMap<>(), testArray);
    }
    
    private static void benchmarkHashMap(Integer[] testArray) {
        benchmarkMap(new HashMap<>(), testArray);
    }
    
    private static void benchmarkVebMap(Integer[] testArray) {
        
    }
    
    private static void benchmarkMap(Map<Integer, Integer> map,
                                     Integer[] testArray) {
        System.out.println("--- " + map.getClass().getSimpleName() + " ---");
        
        long startTime;
        long endTime;
        long totalTime = 0L;
        
        // put().
        startTime = System.currentTimeMillis();
        
        for (Integer i : testArray) {
            map.put(i, i);
        }
        
        endTime = System.currentTimeMillis();
        totalTime += endTime - startTime;
        
        System.out.println(
                "put() in " + (endTime - startTime) + " milliseconds.");
        
        // Key iteration.
        startTime = System.currentTimeMillis();
        
        for (Integer i : map.keySet()) {
            
        }
        
        endTime = System.currentTimeMillis();
        totalTime += endTime - startTime;
        
        System.out.println(
                "keySet().iterator() in " + (endTime - startTime) + 
                " milliseconds.");
        
        // Entry set iteration.
        startTime = System.currentTimeMillis();
        
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            
        }
        
        endTime = System.currentTimeMillis();
        totalTime += endTime - startTime;
        
        System.out.println(
                "entrySet().iterator() in " + (endTime - startTime) + 
                " milliseconds.");
        
        // containsKey.
        startTime = System.currentTimeMillis();
        
        for (int i = MINIMUM_KEY; i <= MAXIMUM_KEY; ++i) {
            map.containsKey(i);
        }
        
        endTime = System.currentTimeMillis();
        totalTime += endTime - startTime;
        
        System.out.println(
                "containsKey() in " + (endTime - startTime) + " milliseconds.");
        
        startTime = System.currentTimeMillis();
        
        for (Integer i : testArray) {
            map.remove(i);
        }
        
        endTime = System.currentTimeMillis();
        totalTime += endTime - startTime;
        
        System.out.println(
                "remove() in " + (endTime - startTime) + " milliseconds.");
        System.out.println("Total time: " + totalTime + " milliseconds.");
    }
    
    private static void warmup() {
        System.out.println("Warming up...");
        VanEmdeBoasTreeIntMap<Integer> vebMap = 
                new VanEmdeBoasTreeIntMap<>(MINIMUM_KEY, MAXIMUM_KEY);
        
        Map<Integer, Integer> treeMap = new TreeMap<>();
        Map<Integer, Integer> hashMap = new HashMap<>();
        
        Integer[] randomIntegerArray = 
                createRandomIntegerArray(INTEGER_ARRAY_LENGTH,
                                         MINIMUM_KEY,
                                         MAXIMUM_KEY);
        
        for (Integer i : randomIntegerArray) {
            vebMap.put(i, i);
            treeMap.put(i, i);
            hashMap.put(i, i);
        }
        
        for (Integer i : randomIntegerArray) {
            vebMap.contains(i);
            treeMap.containsKey(i);
            hashMap.containsKey(i);
        }
        
        for (Map.Entry<Integer, Integer> e : treeMap.entrySet()) {
            
        }
        
        for (Integer i : treeMap.keySet()) {
            
        }
        
        for (Map.Entry<Integer, Integer> e : hashMap.entrySet()) {
            
        }
        
        for (Integer i : hashMap.keySet()) {
            
        }
        
        KeyIterator keyIterator = vebMap.treeKeyIterator();
        
        while (keyIterator.hasNextKey()) {
            keyIterator.nextKey();
        }
        
        keyIterator = vebMap.tableKeyIterator();
        
        while (keyIterator.hasNextKey()) {
            keyIterator.nextKey();
        }
        
        KeyValueMapping<Integer> mapping = new KeyValueMapping<>();
        KeyValueIterator<Integer> keyValueIterator =
                vebMap.treeKeyValueIterator();
        
        while (keyValueIterator.hasNextKeyValuePair()) {
            keyValueIterator.nextKeyValuePair(mapping);
        }
        
        keyValueIterator = 
                vebMap.tableKeyValueIterator();
        
        while (keyValueIterator.hasNextKeyValuePair()) {
            keyValueIterator.nextKeyValuePair(mapping);
        }
        
        for (Integer i : randomIntegerArray) {
            vebMap.remove(i);
            treeMap.remove(i);
            hashMap.remove(i);
        }
        
        System.out.println("Warming up done!");
    }
    
    private static Integer[] createRandomIntegerArray(int length,
                                                      int minimumKey,
                                                      int maximumKey) {
        Integer[] array = new Integer[length];
        int rangeLength = maximumKey - minimumKey + 1;
        
        for (int i = 0; i < length; ++i) {
            array[i] = RANDOM.nextInt(rangeLength) + minimumKey;
        }
        
        return array;
    }
}
