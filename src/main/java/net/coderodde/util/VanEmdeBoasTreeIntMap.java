package net.coderodde.util;

import java.util.NoSuchElementException;

/**
 * This class implements a sorted map mapping integer keys to values of 
 * arbitrary type.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 9, 2017)
 * @param <V> the type of values.
 */
public final class VanEmdeBoasTreeIntMap<V> {
    
    /**
     * The minimum universe size a node of a van Emde Boas tree can hold.
     */
    private static final int MINIMUM_UNIVERSE_SIZE = 2;
    
    /**
     * The value used to denote the absence of an integer key.
     */
    private static final int NULL_KEY = -1;
    
    /**
     * Used to denote that there is an integer mapped to a {@code null} value.
     */
    private final V NULL_VALUE = (V) new Object();
    
    /**
     * This static inner class implements a node in a van Emde Boas tree.
     */
    private static final class VEBTree {
        
        /**
         * The universe size of this vEB node.
         */
        private final int universeSize;
        
        /**
         * The shift length for computing the high indices.
         */
        private final int highShift;
        
        /**
         * The mask used to compute the low indices.
         */
        private final int lowMask;
        
        /**
         * The minimum integer key in the tree starting from this node.
         */
        private int min;
        
        /**
         * The maximum integer key in the tree starting from this node.
         */
        private int max;
        
        /**
         * The summary vEB-tree.
         */
        private final VEBTree summary;
        
        /**
         * The children nodes of this vEB node.
         */
        private final VEBTree[] cluster;
        
        VEBTree(int universeSize) {
            this.universeSize = universeSize;
            
            int universeSizeLowerSquare = lowerSquare(universeSize);
            
            this.lowMask = universeSizeLowerSquare - 1;
            this.highShift = 
                    Integer.numberOfTrailingZeros(universeSizeLowerSquare);
            
            this.min = NULL_KEY;
            this.max = NULL_KEY;
            
            if (universeSize != MINIMUM_UNIVERSE_SIZE) {
                int upperUniverseSizeSquare = upperSquare(universeSize);
                int lowerUniverseSizeSquare = lowerSquare(universeSize);
                this.summary = new VEBTree(upperUniverseSizeSquare);
                this.cluster = new VEBTree[upperUniverseSizeSquare];
                
                for (int i = 0; i != upperUniverseSizeSquare; ++i) {
                    this.cluster[i] = new VEBTree(lowerUniverseSizeSquare);
                }
            } else {
                this.summary = null;
                this.cluster = null;
            }
        }
        
        int getUniverseSize() {
            return universeSize;
        }
        
        int getMinimumKey() {
            return min;
        }
        
        int getMaximumKey() {
            return max;
        }
        
        int getSuccessor(int x) {
            if (universeSize == MINIMUM_UNIVERSE_SIZE) {
                if (x == 0 && max == 1) {
                    return 1;
                }
                
                return NULL_KEY;
            }
            
            if (min != NULL_KEY && x < min) {
                return min;
            }
            
            int maximumLow = cluster[high(x)].getMaximumKey();
            
            if (maximumLow != NULL_KEY && low(x) < maximumLow) {
                int offset = cluster[high(x)].getSuccessor(low(x));
                return index(high(x), offset);
            }
            
            int successorCluster = summary.getSuccessor(high(x));
            
            if (successorCluster == NULL_KEY) {
                return NULL_KEY;
            }
            
            int offset = cluster[successorCluster].getMinimumKey();
            return index(successorCluster, offset);
        }
        
        int getPredecessor(int x) {
            if (universeSize == MINIMUM_UNIVERSE_SIZE) {
                if (min == NULL_KEY) {
                    return NULL_KEY;
                }
                
                if (x == 1 && min == 0) {
                    return 0;
                }
                
                return NULL_KEY;
            }
            
            if (max != NULL_KEY && x > max) {
                return max;
            }
            
            int minimumLow = cluster[high(x)].getMinimumKey();
            
            if (minimumLow != NULL_KEY && low(x) > minimumLow) {
                int offset = cluster[high(x)].getPredecessor(low(x));
                return index(high(x), offset);
            }
            
            int predecessorCluster = summary.getPredecessor(high(x));
            
            if (predecessorCluster == NULL_KEY) {
                if (min != NULL_KEY && x > min) {
                    return min;
                }
                
                return NULL_KEY;
            }
            
            int offset = cluster[predecessorCluster].getMaximumKey();
            return index(predecessorCluster, offset);
        }
        
        void treeInsert(int x) {
            if (min == NULL_KEY) {
                emptyTreeInsert(x);
                return;
            }
            
            if (x < min) {
                int tmp = x;
                x = min;
                min = tmp;
            }
            
            if (universeSize != MINIMUM_UNIVERSE_SIZE) {
                int minimum = cluster[high(x)].getMinimumKey();
                
                if (minimum == NULL_KEY) {
                    summary.treeInsert(high(x));
                    cluster[high(x)].emptyTreeInsert(low(x));
                } else {
                    cluster[high(x)].treeInsert(low(x));
                }
            }
            
            if (max < x) {
                max = x;
            }
        }
        
        void treeDelete(int x) {
            if (min == max) {
                min = NULL_KEY;
                max = NULL_KEY;
                return;
            }
            
            if (universeSize == MINIMUM_UNIVERSE_SIZE) {
                if (x == 0) {
                    min = 1;
                } else {
                    max = 0;
                }
                
                max = min;
                return;
            }
            
            if (min == x) {
                int firstCluster = summary.getMinimumKey();
                x = index(firstCluster, cluster[firstCluster].getMinimumKey());
                min = x;
            }
            
            cluster[high(x)].treeDelete(low(x));
            
            if (cluster[high(x)].getMinimumKey() == NULL_KEY) {
                summary.treeDelete(high(x));
                
                if (x == max) {
                    int summaryMaximum = summary.getMaximumKey();
                    
                    if (summaryMaximum == NULL_KEY) {
                        max = min;
                    } else {
                        int maximumKey = 
                                cluster[summaryMaximum].getMaximumKey();
                        max = index(summaryMaximum, maximumKey);
                    }
                }
            } else if (x == max) {
                int maximumKey = cluster[high(x)].getMaximumKey();
                max = index(high(x), maximumKey);
            }
        }
        
        private void emptyTreeInsert(int x) {
            min = x;
            max = x;
        }
        
        private int high(int x) {
            return x >>> highShift;
        }
        
        private int low(int x) {
            return x & lowMask;
        }
        
        private int index(int x, int y) {
            return (x << highShift) | (y & lowMask);
        }
    }
    
    private static int upperSquare(int number) {
        double exponent = Math.ceil(Math.log(number) / Math.log(2.0) / 2.0);
        return (int) Math.pow(2.0, exponent);
    }
    
    private static int lowerSquare(int number) {
        double exponent = Math.floor(Math.log(number) / Math.log(2.0) / 2.0);
        return (int) Math.pow(2.0, exponent);
    }
    
    private final VEBTree root;
    private final int minimumKey;
    private final int maximumKey;
    private final V[] table;
    private int size;
    
    public VanEmdeBoasTreeIntMap(int minimumKey, int maximumKey) {
        checkBounds(minimumKey, maximumKey);
        this.minimumKey = minimumKey;
        this.maximumKey = maximumKey;
        int universeSize = maximumKey - minimumKey + 1;
        universeSize = fixUniverseSize(universeSize);
        this.root = new VEBTree(universeSize);
        this.table = (V[]) new Object[universeSize];
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int getMinimumKey() {
        return size != 0 ? root.min + minimumKey : this.maximumKey + 1;
    }
    
    public int getMaximumKey() {
        return size != 0 ? root.max + minimumKey : this.minimumKey - 1;
    }
    
    public int getNextIntKey(int key) {
        checkKey(key);
        int nextKey = root.getSuccessor(key - minimumKey);
        return nextKey == NULL_KEY ?
                this.minimumKey - 1 :
                nextKey + minimumKey;
    }
    
    public int getPreviousIntKey(int key) {
        checkKey(key);
        int previousKey = root.getPredecessor(key - minimumKey);
        return previousKey == NULL_KEY ? 
                this.maximumKey + 1 : 
                previousKey + minimumKey;
    }
    
    public boolean contains(int key) {
        checkKey(key);
        return table[key - minimumKey] != null;
    }
    
    public V get(int key) {
        checkKey(key);
        V value = table[key - minimumKey];
        return (value == null || value == NULL_VALUE) ? null : value;
    }
    
    public V put(int key, V value) {
        checkKey(key);
        // Translate the key:
        key -= minimumKey;
        V currentValue = table[key];
        
        if (currentValue != null) {
            // key is present in this map.
            V oldValue = table[key];
            table[key] = value;
            return oldValue;
        } else {
            root.treeInsert(key);
            table[key] = value != null ? value : NULL_VALUE;
            size++;
            return null;
        }
    }
    
    public V remove(int key) {
        checkKey(key);
        // Translate the key:
        key -= minimumKey;
        V value = table[key];
        
        if (value != null) {
            // key is in this map.
            table[key] = null;
            root.treeDelete(key);
            size--;
            return value == NULL_VALUE ? null : value;
        } else {
            return null;
        }
    }
    
    public void clear() {
        int key = root.min;
        int nextKey;
        
        for (int i = 0; i != size; ++i) {
            nextKey = root.getSuccessor(key);
            root.treeDelete(key);
            key = nextKey;
        }
        
        size = 0;
    }
    
    public static final class IntKeyIterator<V> {
        
        private final VanEmdeBoasTreeIntMap<V> tree;
        private int iterated = 0;
        private int lastReturned;
        
        IntKeyIterator(VanEmdeBoasTreeIntMap<V> tree) {
            this.tree = tree;
        }
        
        public boolean hasNext() {
            return iterated < tree.size;
        }
        
        public int next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Nothing to iterate left.");
            }
            
            if (iterated == 0) {
                lastReturned = tree.getMinimumKey();
                iterated++;
                return lastReturned;
            } else {
                int next = tree.getNextIntKey(lastReturned);
                lastReturned = next;
                iterated++;
                return next;
            }
        }
    }
    
    public IntKeyIterator<V> keyIterator() {
        return new IntKeyIterator<>(this);
    }
    
    public static final class Mapping<V> {
        public int key;
        public V value;
        
        @Override
        public String toString() {
            return "(" + key + " -> " + value + ")";
        }
    }
    
    public static final class MappingIterator<V> {
        
        private final VanEmdeBoasTreeIntMap<V> tree;
        private int iterated = 0;
        private int lastReturned;
        
        MappingIterator(VanEmdeBoasTreeIntMap<V> tree) {
            this.tree = tree;
        }
        
        public boolean hasNext() {
            return iterated < tree.size;
        }
        
        public void next(Mapping<V> mapping) {
            if (!hasNext()) {
                throw new NoSuchElementException("Nothing to iterate left.");
            }
            
            if (iterated == 0) {
                lastReturned = tree.getMinimumKey();
                iterated++;
                mapping.key = lastReturned;
                mapping.value = tree.table[lastReturned - tree.minimumKey];
            } else {
                int next = tree.getNextIntKey(lastReturned);
                lastReturned = next;
                iterated++;
                mapping.key = lastReturned;
                mapping.value = tree.table[lastReturned - tree.minimumKey];
            }
        }
    }
    
    private void checkBounds(int minimumKey, int maximumKey) {
        if (minimumKey > maximumKey) {
            throw new IllegalArgumentException(
                    "minimumKey(" + minimumKey + ") > " +
                    "maximumKey(" + maximumKey + ")");
        }
    }
    
    private int fixUniverseSize(int requestedUniverseSize) {
        int tmp = Integer.highestOneBit(requestedUniverseSize);
        return tmp == requestedUniverseSize ?
                requestedUniverseSize :
               (tmp << 1);
    }
    
    private void checkKey(int key) {
        if (key < minimumKey) {
            throw new IllegalArgumentException(
                    "The given key (" + key + ") is too small. Must be at " +
                    "least " + minimumKey + ".");
        }
        
        if (key > maximumKey) {
            throw new IllegalArgumentException(
                    "The given key (" + key + ") is too large. Must be at " +
                    "most " + maximumKey + ".");
        }
    }
}
