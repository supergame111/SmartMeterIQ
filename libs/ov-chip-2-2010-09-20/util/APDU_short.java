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
// Created 2.9.08 by Hendrik
// 
// APDU wrapper for short
// 
// $Id: APDU_short.java,v 1.11 2009-06-19 20:37:36 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * {@link APDU_Serializable} wrapper around a short.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.11 $
 * @commitdate $Date: 2009-06-19 20:37:36 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 */
PUBLIC class APDU_short implements APDU_Serializable {

    /**
     * 
     * The wrapped short value.
     */
    public short value;


    /**
     * 
     * Default initialization.
     */
    public APDU_short() {
        // nothing to do
        return;
    }


    /**
     * 
     * Initialize to {@code s}.
     * 
     * @param s short to wrap
     */
    public APDU_short(short s) {
        // ASSERT(Short.MIN_VALUE <= s && s <= Short.MAX_VALUE);
        value = s;
        return;
    }

    #ifndef JAVACARD_APPLET
        /**
         * 
         * Initialize to {@code i}. Asserts that {@code i} fits into a
         * short.
         * <P>
         * Only available if <a
         * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
         * is undefined.
         * 
         * @param i value to wrap 
         */
        public APDU_short(int i) {
            assert Short.MIN_VALUE <= i && i <= Short.MAX_VALUE;
            value = (short)i;
            return;
        }


        /**
         * 
         * Copy {@code i} into {@link #value}. Asserts that {@code i} fits into a
         * short.
         * <P>
         * Only available if <a
         * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
         * is undefined.
         * 
         * @param i value to wrap 
         */
        public void copy(int i) {
            assert Short.MIN_VALUE <= i && i <= Short.MAX_VALUE;
            value = (short)i;
            return;
        }
    #endif


    /**
     * Size in bytes necessary to send or receive this object 
     * via the OV-chip protocol layer, see 
     * {@link ds.ov2.util.APDU_Serializable#size APDU_Serializable.size()}.
     *
     * @return 2
     */
    public short size(){
        return (short)2;
    }


    /**
     * Compatibility check for the OV-chip protocol layer.
     * See <a href="APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     * <P>
     *
     * This object is compatible with any other instance of
     * APDU_short.
     *
     * @param o actual argument or result
     * @return true if {@code o} is an instance of APDU_short
     */
    public boolean is_compatible_with(Object o){
        if(o instanceof APDU_short) {
            return true;
        }
        return false;
    }


    /**
     * Serialize this short. See {@link 
     * ds.ov2.util.APDU_Serializable#to_byte_array 
     * APDU_Serializable.to_byte_array}.
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
        ASSERT(this_index < 2);
        ASSERT(len > 0);
        if(this_index == 0 && len >= 2) {
            byte_array[byte_index] = (byte)(value >> 8);
            byte_array[(short)(byte_index + 1)] = (byte)(value);
            if(len == 2)
                return 3;
            else
                return 2;
        }
        else if(this_index == 0 && len == 1) {
            byte_array[byte_index] = (byte)(value >> 8);
            return 1;
        }
        else { // this_index == 1 && len >= 1
            byte_array[byte_index] = (byte)(value);
            if(len == 1)
                return 2;
            else
                return 1;
        }
    }


    /**
     * Deserialize this short. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}.
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
        ASSERT(this_index < 2);
        ASSERT(len > 0);
        if(this_index == 0 && len >= 2) {
            value = (short)((byte_array[byte_index] << 8) |
                            (byte_array[(short)(byte_index +1)] & 0xff));
            if(len == 2)
                return 3;
            else
                return 2;
        }
        else if(this_index == 0 && len == 1) {
            value = (short)(byte_array[byte_index] << 8);
            return 1;
        }
        else { // this_index == 1 && len >= 1
            value |= (short)(byte_array[byte_index] & 0xff);
            if(len == 1)
                return 2;
            else
                return 1;
        }
    }
}
