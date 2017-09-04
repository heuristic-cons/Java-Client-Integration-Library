// --------------------------------------------------------------------------------------------------------------------
// <copyright file="Packs.cs" company="Solidsoft Reply Ltd.">
//   (c) 2017 Solidsoft Reply Ltd.
// </copyright>
// <summary>
//   Represents a collection of client credentials.
// </summary>
// --------------------------------------------------------------------------------------------------------------------
package com.reply.solidsoft.nbs.integration.sample.console;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Iterator;

/**
 * Represents a collection of packs.
 */
public class Packs implements List<PackIdentifier> {

    /**
     * The inner list of packs.
     */
    private final List<PackIdentifier> packList = new ArrayList<>();

    /**
     * Gets the number of elements contained in the pack list.
     *
     * @return The number of elements contained in the pack list.
     */
    public int getCount() {
        return this.packList.size();
    }

    /**
     * Gets the element at the specified index.
     *
     * @param index The zero-based index of the element to get or set.
     * @return The element at the specified index.
     */
    @Override
    public PackIdentifier get(int index) {
        return this.packList.get(index);
    }

    /**
     * Sets the element at the specified index.
     *
     * @param index The zero-based index of the element to get or set.
     * @param element The element to be added.
     * @return The specified index.
     */
    @Override
    public PackIdentifier set(int index, PackIdentifier element) {
        return this.packList.set(index, element);
    }

    /**
     * Add a pack to the collection.
     *
     * @param pack The pack to be added.
     */
    @Override
    public boolean add(PackIdentifier pack) {
        return this.packList.add(pack);
    }

    /**
     * Clears the packs.
     */
    @Override
    public void clear() {
        this.packList.clear();
    }

    /**
     * Returns a value indicating whether the collection contains a given pack.
     *
     * @param pack The pack to be tested.
     * @return True, if the collection contains the pack; otherwise false.
     */
    public boolean contains(PackIdentifier pack) {
        return this.packList.contains(pack);
    }

    /**
     * Copies the entire pack list to a compatible one-dimensional array,
     * starting at the specified index of the target array.
     */
    @Override
    public Object[] toArray() {
        return this.packList.toArray();
    }

    /**
     * Copies the entire pack list to a compatible one-dimensional array,
     * starting at the specified index of the target array.
     *
     * @param array The one-dimensional Array that is the destination of the
     * elements copied from pack list. The Array must have zero-based indexing.
     * @return A compatible one-dimensional array containing the entire pack
     * list.
     */
    public PackIdentifier[] toArray(PackIdentifier[] array) {
        return this.packList.toArray(array);
    }

    /**
     * Returns an enumerator for the packs.
     *
     * @return An enumerator for the client credentials.
     */
    @Override
    public Iterator<PackIdentifier> iterator() {
        return this.packList.iterator();
    }

    /**
     * Determines the index of a specific item in the pack list.
     *
     * @param pack The object to locate in the pack list.
     * @return The index of item if found in the list; otherwise, -1.
     */
    public int indexOf(PackIdentifier pack) {
        return this.packList.indexOf(pack);
    }

    /**
     * Inserts an item to the pack list at the specified index.
     *
     * @param index The zero-based index at which item should be inserted.
     * @param pack The object to insert into the pack list.
     */
    @Override
    public void add(int index, PackIdentifier pack) {
        this.packList.add(index, pack);
    }

    /**
     * Removes the first occurrence of a specific object from the pack list.
     *
     * @param pack The object to remove from the pack list. The value can be
     * null.
     * @return True if the pack is successfully removed; otherwise, false. This
     * method also returns false if the pack was not found in the pack list.
     */
    public boolean remove(PackIdentifier pack) {
        return this.packList.remove(pack);
    }

    /**
     * Removes the pack list item at the specified index.
     *
     * @param index The zero-based index of the item to remove.
     */
    public void Remove(int index) {
        this.packList.remove(index);
    }

    /**
     * Returns the size of the list.
     * 
     * @return The size of the list.
     */
    @Override
    public int size() {
        return this.packList.size();
    }

    /**
     * Gets a value indicating whether the list is empty.
     * 
     * @return A value indicating whether the list is empty.
     */
    @Override
    public boolean isEmpty() {
        return this.packList.isEmpty();
    }

    /**
     * Gets a value indicating whether the list contains an object. 
     * 
     * @param o The object to be compared.
     * @return A value indicating whether the list contains an object.
     */
    @Override
    public boolean contains(Object o) {
        return this.packList.contains(o);
    }

    /**
     * An array of pack identifiers.
     * 
     * @param <T> PackIdentifier.
     * @param a The array to populate.
     * @return The populated array.
     */
    @Override
    @SuppressWarnings("SuspiciousToArrayCall")
    public <T> T[] toArray(T[] a) {
        return this.packList.toArray(a);
    }

    /**
     * Removes an object from the list.
     * 
     * @param o The object to be removed.
     * @return A value indicating whether the object was found and removed from the list.
     */
    @Override
    public boolean remove(Object o) {
        return this.packList.remove(o);
    }

    /**
     * Gets a value indicating whether the list contains all the elements in a given collection.
     * 
     * @param c The collection.
     * @return True, if all the elements of the collection are contained in the list; otherwise false.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return this.packList.containsAll(c);
    }

    /**
     * Adds all the elements of a collection to the list.
     * 
     * @param c The collection of elements.
     * @return True, if all the elements were added successfully; otherwise false.
     */
    @Override
    public boolean addAll(Collection<? extends PackIdentifier> c) {
        return this.packList.addAll(c);
    }

    /**
     * Adds all the elements of a collection to the list.
     * 
     * @param index The index at which to insert the first element from the specified collection
     * @param c The collection of elements.
     * @return True, if all the elements were added successfully; otherwise false.
     */
    @Override
    public boolean addAll(int index, Collection<? extends PackIdentifier> c) {
        return this.packList.addAll(c);
    }

    /**
     * Removes all the items in a collections from the list.
     * @param c The collection of items to be removed.
     * @return True, if all the items in the collection were successfully removed; otherwise false.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return this.packList.removeAll(c);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection (optional operation).  In other words, removes
     * from this list all of its elements that are not contained in the
     * specified collection.
     * 
     * @param c The collection.
     * @return True, if the elements that are not in the collection were successfully removed; otherwise false.
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return this.packList.retainAll(c);
    }

    /**
     * Removes an element from the list at the given index.
     * 
     * @param index The index of the element to be removed.
     * @return The removed element.
     */
    @Override
    public PackIdentifier remove(int index) {
        return this.packList.remove(index);
    }

    /**
     * Gets the index of a given object.
     * 
     * @param o The object.
     * @return The index of the object within the list.
     */
    @Override
    public int indexOf(Object o) {
        return this.packList.indexOf(o);
    }

    /**
     * Gets the index of the last occurrence of an object within the list.
     * @param o The object.
     * @return The index of the last occurrence of the object within the list.
     */
    @Override
    public int lastIndexOf(Object o) {
        return this.packList.lastIndexOf(o);
    }

    /**
     * Returns a list iterator for the list.
     * 
     * @return A list iterator.
     */
    @Override
    public ListIterator<PackIdentifier> listIterator() {
        return this.packList.listIterator();
    }

    /**
     * Returns a list iterator for the list.  Starts at the given index.
     * @param index The index at which iteration will start.
     * @return The list iterator.
     */
    @Override
    public ListIterator<PackIdentifier> listIterator(int index) {
        return this.packList.listIterator(index);
    }

    /**
     * Gets a sublist of elements in the list between two given index positions.
     * 
     * @param fromIndex The index of the first element (inclusive).
     * @param toIndex The index of the last element (exclusive).
     * @return A sublist of elements.
     */
    @Override
    public List<PackIdentifier> subList(int fromIndex, int toIndex) {
        return this.packList.subList(fromIndex, toIndex);
    }
}

