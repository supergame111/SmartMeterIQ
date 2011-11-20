// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2008, 2009
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of
// the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// General Public License in file COPYING in this or one of the
// parent directories for more details.
// 
// Created 14.9.08 by Hendrik
// 
// Bignat array with APDU_Serializable interface
// 
// $Id: Bignat_array.java,v 1.22 2009-04-06 12:51:26 tews Exp $

#include <config>

#include "bignatconfig"



#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.bignat;
#endif


#ifndef JAVACARD_APPLET
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Serializable_array;
#endif


/** 
 * 
 * {@link APDU_Serializable} interface around an array of {@link
 * Bignat Bignats}.
 * This is used inside {@link Vector} to factor out the bignat array
 * functionality. This implementation uses simply an bignat array 
 * internally. Other implementations relying on a huge byte array
 * seem possible.
 * <P>
 * 
 * Although this is a card data type, 
 * {@link Serializable_array#is_compatible_with 
 * Serializable_array.is_compatible_with()} is not overridden here. 
 * This is left for clients extending this class.
 * <P>
 *
 * The size of the array is determined by the {@code length} argument
 * of the constructor. The size stays constant for the lifetime of the
 * object. However, the array can provide the illusion of a different
 * (smaller) length. This functionality is thought for testing
 * purposes and only available if the cpp symbol VARIABLE_SIZE_BIGNATS
 * is defined, see field {@link #length} and method {@link
 * #set_length}. 
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.22 $
 * @commitdate $Date: 2009-04-06 12:51:26 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#DIGIT_TYPE">DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#VARIABLE_SIZE_BIGNATS">VARIABLE_SIZE_BIGNATS</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG(condition, tag)</a>
 *
 */
PUBLIC class Bignat_array 
    extends Serializable_array
    implements APDU_Serializable
{

    /**
     * The array of the contained bignats. Its length is determind
     * in the constructor.
     */
    private Bignat[] a;

    // length is final, in principle.
    // However, in the testframe it is handy to be able to change the 
    // length without reloading the applet.
    #ifndef VARIABLE_SIZE_BIGNATS

        protected final short length;

    #else

        /**
         * Length of the array {@link #a}. Final unless 
         * VARIABLE_SIZE_BIGNATS is defined. Can be changed via 
         * {@link #set_length set_length} to provide the illusion of
         * a shorter array.
         */
        protected short length;


        /**
         * Virtual length change. Set the {@link #length} field to provide 
         * the illusion of a different array size. 
         * <P>
         * Asserts that {@code new_length} is shorter than the real
         * length of {@link #a}.
         * <P>
         * Only available if VARIABLE_SIZE_BIGNATS is defined.
         *
         * @param new_length new length
         */
        public void set_length(short new_length) {
            ASSERT(new_length <= a.length);
            length = new_length;
        }
    #endif


    /**
     * Nonallocating constructor. This constructor allocates the
     * internal bignat array but leaves null references in there.
     * The array must then be filled by other means, for instance
     * with {@link #set set}. Useful if some of the bignats that 
     * are going to populate this array are already allocated.
     * <P>
     *
     * <a href="../../../overview-summary.html#ASSERT">ASSERT's</a>
     * that {@code length} is at least 1. Bignat arrays of length zero
     * are almost useless, because {@link #get_bignat_size} and {@link
     * #get_bignat_length} access the element at index 0.
     *
     * @param length length of the array (maximal length if 
     *          VARIABLE_SIZE_BIGNATS is defined and {@link #set_length 
     *          set_length} is available).
     */
    public Bignat_array(short length) {
        ASSERT(length >= 1);
        this.length = length;

        // It's worthwhile to consider to allocate a in RAM as well,
        // but that opens a whole can of worms, because we can only 
        // allocate Object arrays in RAM and you cannot cast an 
        // Object array into a Bignat array.
        a = new Bignat[length];
        return;
    }


    /**
     * Allocate fresh bignats for the whole array. Fills the array
     * with newly allocated, distinct bignats.
     *
     * @param bignat_size size of the bignats
     * @param in_ram if true, allocate the digit array of the 
     *     bignats in transient memory (RAM) on the card.
     */
    public void allocate(short bignat_size, boolean in_ram) {
        for(short i = 0; i < length; i++) {
            a[i] = new Bignat(bignat_size, in_ram);
        }
        return;
    }


    /**
     * Allocating constructor. Creates a fresh array with newly
     * allocated bignats.
     * <P>
     *
     * <a href="../../../overview-summary.html#ASSERT">ASSERT's</a>
     * that {@code length} is at least 1. Bignat arrays of length zero
     * are almost useless, because {@link #get_bignat_size} and {@link
     * #get_bignat_length} access the element at index 0.
     *
     *
     * @param bignat_size size of the bignats in the array
     * @param length length of the array
     * @param in_ram if true, allocate the digit array of the 
     */
    public Bignat_array(short bignat_size, short length, 
                        boolean in_ram) 
    {
        this(length);
        allocate(bignat_size, in_ram);
        return;
    }


    #ifdef VARIABLE_SIZE_BIGNATS

      /**
       * Register the array contents as short resizable bignats.
       * See {@link Resize}.
       * <P>
       * Only available if VARIABLE_SIZE_BIGNATS is defined.
       *
       */
      public void register_short_bignats() {
          for(short i = 0; i < a.length; i++)
              Resize.register_short_bignat(a[i]);
      }


      /**
       * Register the array contents as long resizable bignats.
       * See {@link Resize}.
       * <P>
       * Only available if VARIABLE_SIZE_BIGNATS is defined.
       *
       */
      public void register_long_bignats() {
          for(short i = 0; i < a.length; i++)
              Resize.register_long_bignat(a[i]);
      }
    #endif


    /**
     * Size of the contained bignats in bytes.
     * Returns {@code a[0].}{@link Bignat#size() size()}.
     *
     * @return bignat size in bytes
     */
    public short get_bignat_size() {
        return a[0].size();
    }


    /**
     * Digit length of the contained bignats in bytes.
     * Returns {@code a[0].}{@link Bignat#length() length()}.
     *
     * @return number of digits of the contained bignats
     */
    public short get_bignat_length() {
        return a[0].length();
    }


    /**
     * Return element {@code i}.
     * 
     * @param i index
     * @return bignat at index {@code i}
     * @throws ArrayIndexOutOfBoundsException if {@code i} is out of bounds 
     */
    public Bignat get(short i) {
        return a[i];
    }


    /**
     * Set element {@code i}.
     *
     * @param i index
     * @param b bignat to store at index {@code i}
     * @throws ArrayIndexOutOfBoundsException if {@code i} is out of bounds 
     */
    public void set(short i, Bignat b) {
        a[i] = b;
        return;
    }


    /**
     * Copy digit. Copy digit {@code digit} of all bignats in the 
     * array into {@code buf} such that {@code buf[0]} contains the
     * digit of {@link #get get}{@code (0)}, {@code buf[1]} of 
     * {@link #get get}{@code (1)}, and so on.
     * <P>
     * {@code buf} is an array of type DIGIT_TYPE.
     * <P>
     * Asserts that {@code buf} is at least of size {@link #length}
     * and that {@code digit} is less than the size of the bignats in 
     * this array.
     * <P>
     * Used in exponentiation in 
     * {@link Vector#exponent_mod Vector.exponent_mod}
     *
     * @param digit digit number to copy
     * @param buf of type {@code DIGIT_TYPE[]}, result array of digits
     */
    public void copy_digit(short digit, DIGIT_TYPE[] buf) {
        ASSERT(buf.length >= length);
        ASSERT(digit < get_bignat_size());
        for(short i = 0; i < length; i++) {
            buf[i] = a[i].get_digit_array()[digit];
        }
        return;
    }


    //########################################################################
    // Serializable_array support
    // 

    /**
     * Return the bignat array {@link #a} in support for abstract
     * {@link Serializable_array}.
     *
     * @return array of objects to (de-)serialize
     */
    protected APDU_Serializable[] get_array() {
        return a;
    }


    /**
     * Return {@link #length} as effective size in support for abstract
     * {@link Serializable_array}.
     *
     * @return {@link #length}
     */
    public short get_length() {
        return length;
    }


    /**
     * Size in bytes necessary to send or receive this object 
     * via the OV-chip protocol layer, see 
     * {@link ds.ov2.util.APDU_Serializable#size APDU_Serializable.size()}.
     *
     * @return size in bytes
     */
    public short size() {
        return (short)(get_length() * get_bignat_size());
    }
}
