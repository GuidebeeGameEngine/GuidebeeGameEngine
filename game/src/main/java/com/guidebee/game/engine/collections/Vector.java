/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//--------------------------------- PACKAGE ------------------------------------
package com.guidebee.game.engine.collections;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * The class provides J2SE version alike Vector.
 *
 * @author James Shen.
 */
public class Vector extends java.util.Vector {

    //[------------------------------ CONSTRUCTOR -----------------------------]


    /**
     * Constructs an empty vector.
     */
    public Vector() {
        super();
    }


    /**
     * Constructs an empty vector with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the vector
     */
    public Vector(int initialCapacity) {
        super(initialCapacity);
    }


    /**
     * Constructs an empty vector with the specified initial capacity.
     *
     * @param initialCapacity   the initial capacity of the vector
     * @param capacityIncrement the amount by which the capacity is increased
     *                          when the vector overflows
     */
    public Vector(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
    }


    /**
     * <DD>Inserts the specified element at the specified position in this Vector.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     * <P><DD>
     *
     * @param index   at which the specified element is to be inserted.
     * @param element element to be inserted.
     */
    public void add(int index, Object element) {
        insertElementAt(element, index);
    }


    /**
     * Appends the specified element to the end of this Vector.
     *
     * @param o element to be appended to this Vector..
     */
    public boolean add(Object o) {
        addElement(o);
        return true;
    }


    /**
     * Removes all of the elements from this Vector. The Vector will be
     * empty after this call returns (
     */
    public void clear() {
        removeAllElements();
    }


    /**
     * Returns the element at the specified position in this Vector.
     *
     * @param index index of element to return.
     * @return object at the specified index.
     */
    public Object get(int index) {
        return elementAt(index);
    }


    /**
     * <DD>Returns an array containing all of the elements in this Vector in the
     * correct order; the runtime type of the returned array is that of the
     * specified array.  If the Vector fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this Vector.<p>
     * If the Vector fits in the specified array with room to spare
     * (i.e., the array has more elements than the Vector),
     * the element in the array immediately following the end of the
     * Vector is set to null.  This is useful in determining the length
     * of the Vector <em>only</em> if the caller knows that the Vector
     * does not contain any null elements.<P><DD>.
     *
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
        Object[] retArray = new Object[size()];
        copyInto(retArray);
        return retArray;
    }


    /**
     * <DD>Returns an array containing all of the elements in this Vector in the
     * correct order; the runtime type of the returned array is that of the
     * specified array.  If the Vector fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this Vector.<p>
     * If the Vector fits in the specified array with room to spare
     * (i.e., the array has more elements than the Vector),
     * the element in the array immediately following the end of the
     * Vector is set to null.  This is useful in determining the length
     * of the Vector <em>only</em> if the caller knows that the Vector
     * does not contain any null elements.<P><DD>.
     *
     * @param a an array to copy to.
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray(Object[] a) {
        int elementCounts = size();
        if (a.length < elementCounts) {
            return (Object[]) Arrays.copyOf(elementData, elementCounts);
        }

        System.arraycopy(elementData, 0, a, 0, elementCounts);

        if (a.length > elementCounts) {
            a[elementCounts] = null;
        }

        return a;


    }


}
