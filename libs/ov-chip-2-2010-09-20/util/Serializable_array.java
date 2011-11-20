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
// Created 17.9.08 by Hendrik
// 
// abstract base class for serializable arrays
// 
// $Id: Serializable_array.java,v 1.14 2009-06-19 20:37:37 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * Abstract class providing the {@link APDU_Serializable} interface 
 * for arrays of APDU_Serializable's.
 * This abstract class implements the {@link APDU_Serializable} interface 
 * for data types that contain several APDU_Serializable's of possibly 
 * different type or size. Clients only need to override the {@link
 * #get_array get_array()}, and the
 * {@link #is_compatible_with is_compatible_with} method. 
 * Overriding {@link #get_length get_length()} is necessary for clients 
 * that can change their size for testing purposes.
 * Overriding {@link #size size()} is optional. 
 * Overriding {@link 
 * #to_byte_array to_byte_array} and {@link #from_byte_array from_byte_array}
 * makes not much sense, execpt for special purposes, such as
 * performing side effects before serialization starts or after 
 * deserialization finshes.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.14 $
 * @commitdate $Date: 2009-06-19 20:37:37 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC abstract class Serializable_array implements APDU_Serializable {


    /**
     * Disabled constructor. Creation possible only for subclasses.
     */
    protected Serializable_array() {}


    /**
     * Array of objects to (de-)serialize. This method might get 
     * called serveral times per (de-)serializtion. The returned array
     * must contain at least one element. Further it must
     * not contain any null references. 
     * <P>
     * Clients must override this method.
     *
     * @return array of objects to (de-)serialize
     */
    protected abstract APDU_Serializable[] get_array();


    // Return the length of the array. This is needed to virtually shorten
    // the array for testing purposes. In the Test_applet in ../test, 
    // for instance, the bases and various other arrays are statically 
    // allocated with a fixed size during applet creation. At runtime
    // these arrays can then be configured to behave like a shorter array.
    /**
     * Length of the serializable array. This method must be used 
     * consistently instead of {@link #get_array get_array}{@code ().length}
     * to determine the size of the array returned from 
     * {@link #get_array()}. The returned value must be greater than zero 
     * and less than or equal to the actual array size.
     * <P>
     * The default implementation returns 
     * {@link #get_array get_array}{@code ().length}. By overriding
     * clients can provide the illusion 
     * of a shorter array without the need of reallocating this shorter
     * array.
     *
     * @return effictive size of the array returned by {@link #get_array()}
     */
    public short get_length() {
        return (short)(this.get_array().length);
    }


    /**
     * Size in bytes necessary to send or receive this object 
     * via the OV-chip protocol layer, see 
     * {@link APDU_Serializable#size APDU_Serializable.size()}.
     * The default implementation used 
     * {@link Misc#length_of_serializable_array 
     * Misc.length_of_serializable_array} to determine the size. 
     * <P>
     * Can be overriden for homegenous arrays that can determine their 
     * size more efficiently.
     *
     * @return size in bytes
     */
    public short size() {
        return Misc.length_of_serializable_array(get_array());
    }


    /**
     * Compatibility check for the OV-chip protocol layer.
     * See <a href="APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     * <P>
     * The default implementation returns false, which is the right
     * value for all host data types. Card data types must override 
     * this method.
     *
     * @param o actual argument or result
     * @return true if this (the declared argument or result) is considered 
     *         binary compatible with {@code o}.
     */
    // Clients should override this.
    public boolean is_compatible_with(Object o) {
        return false;
    }


    /**
     * Serialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#to_byte_array 
     * APDU_Serializable.to_byte_array}.
     * <P>
     * Overriding makes little sense, except when side effects must be 
     * scheduled before the start or after the end of serialization.
     *
     * @param len available space in {@code byte_array}
     * @param this_index number of bytes that
     * have already been written in preceeding calls
     * @param byte_array data array to serialize the state into
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually written, except for the case 
     * where serialization finished by writing precisely 
     * {@code len} bytes, in this case {@code len + 1} is 
     * returned.
     */
    public short to_byte_array(short len, short this_index, 
                               byte[] byte_array, short byte_index) {
        APDU_Serializable[] a = get_array();
        short a_length = get_length();
        short index = 0;
        short that_index = this_index;
        short copied = 0;

        ASSERT(a_length <= a.length);

        // System.out.format("SA to a.len %d len %d ti %d lba %d bi %d\n",
        //                a.length, len, this_index, byte_array.length,
        //                byte_index);

        while(a[index].size() <= that_index) {
            // System.out.format("SA L index %d size %d that_index %d\n",
            //                index, a[index].size(), that_index);
            that_index -= a[index].size();
            index++;
            ASSERT(index < a_length);
        }
        while(len > 0 && index < a_length) {
            short res = a[index].to_byte_array(len, that_index, 
                                               byte_array, byte_index);
            if(res == (short)(len + 1)) {
                // Finished a[index] and copied len bytes, have to return.
                // Are we finished with the whole array?
                if((short)(index + 1) == a_length)
                    // Finished whole array. Return original len + 1.
                    return (short)(copied + res);
                else
                    // Need to continue with index + 1. Return original len.
                    return (short)(copied + len);
            }
            
            if(res == len) {
                // Buffer filled, but need to continue with a[index].
                return (short)(copied + len);
            }

            // a[index] is finished, but there is still space in the buffer.
            ASSERT((short)(that_index + res) == a[index].size());
            copied += res;
            len -= res;
            byte_index += res;
            index += 1;
            that_index = 0;
        }
        return copied;
    }


    /**
     * Deserialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}.
     * <P>
     * Overriding makes little sense, except when side effects must be 
     * scheduled before the start or after the end of serialization.
     *
     * @param len available data in {@code byte_array}
     * @param this_index number of bytes that
     * have already been read in preceeding calls
     * @param byte_array data array to deserialize from
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually read, except for the case 
     * where deserialization finished by reading precisely 
     * {@code len} bytes, in this case {@code len + 1} is 
     * returned.
     */
    public short from_byte_array(short len, short this_index,
                                 byte[] byte_array, short byte_index) {
        APDU_Serializable[] a = get_array();
        short a_length = get_length();
        short index = 0;
        short that_index = this_index;
        short copied = 0;

        ASSERT(a_length <= a.length);

        while(a[index].size() <= that_index) {
            that_index -= a[index].size();
            index++;
            ASSERT(index < a_length);
        }
        while(len > 0 && index < a_length) {
            short res = a[index].from_byte_array(len, that_index,
                                                 byte_array, byte_index);
            if(res == (short)(len + 1)) {
                // Finished with a[index] and copied len bytes, need to return.
                // Are we finished with the whole array too?
                if((short)(index + 1) == a_length)
                    // Finished whole array. Return original len + 1.
                    return (short)(copied + res);
                else
                    // Need to continue with index + 1. Return original len.
                    return (short)(copied + len);
            }
            
            if(res == len) {
                // Buffer filled, but need to continue with a[index].
                return (short)(copied + len);
            }

            // a[index] is finished, but there is still space in the buffer.
            ASSERT((short)(that_index + res) == a[index].size());
            copied += res;
            len -= res;
            byte_index += res;
            index += 1;
            that_index = 0;
        }
        return copied;
    }
}
