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
// Created 2.10.08 by Hendrik
// 
// booleans with APDU_Serializable interface
// 
// $Id: APDU_boolean.java,v 1.9 2009-05-28 15:17:03 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * {@link APDU_Serializable} wrapper around a boolean. The boolean is
 * encoded as one byte, which is either 0 or 1, as every C programmer
 * would expect.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2009-05-28 15:17:03 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE<a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC<a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT<a>
 */
PUBLIC class APDU_boolean implements APDU_Serializable {

    /**
     * 
     * The boolean. It's value might freely be changed from the
     * outside. 
     */
    public boolean value;


    /**
     * 
     * Initialize {@link #value} with {@code b}.
     * 
     * @param b value for the field {@link #value}
     */
    public APDU_boolean(boolean b) {
        value = b;
        return;
    }


    /**
     * 
     * Constructor that leaves {@link #value} in an unspecified state.
     */
    public APDU_boolean() {
        this(false);
        return;
    }


    /**
     * Size of the boolean for
     * the OV-chip protocol layer, see 
     * {@link ds.ov2.util.APDU_Serializable#size APDU_Serializable.size()}.
     *
     * @return 1
     */
    public short size() {
        return 1;
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
     * This object is compatibel to any other APDU_boolean.
     *
     * @param o actual argument or result
     * @return true if {@code o} is an APDU_boolean
     */
    public boolean is_compatible_with(Object o) {
        return (o instanceof APDU_boolean);
    }


    /**
     * Serialize the boolean. See {@link 
     * ds.ov2.util.APDU_Serializable#to_byte_array 
     * APDU_Serializable.to_byte_array}.
     *
     * @param len available space in {@code byte_array}
     * @param this_index number of bytes that
     * have already been written in preceeding calls
     * @param byte_array data array to serialize the state into
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually written, except for the case 
     *           where serialization finished by writing precisely 
     *           {@code len} bytes, in this case {@code len + 1} is 
     *           returned.
     */
    public short to_byte_array(short len, short this_index, 
                               byte[] byte_array, short byte_index) {
        ASSERT(this_index == 0);
        ASSERT(len > 0);
        byte_array[byte_index] = value ? (byte)1 : (byte)0;
        if(len == 1)
            return 2;
        else
            return 1;
    }


    /**
     * Deserialize the boolean. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}.
     *
     * @param len available data in {@code byte_array}
     * @param this_index number of bytes that
     * have already been read in preceeding calls
     * @param byte_array data array to deserialize from
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually read, except for the case 
     *           where deserialization finished by reading precisely 
     *           {@code len} bytes, in this case {@code len + 1} is 
     *           returned.
     */
    public short from_byte_array(short len, short this_index,
                                 byte[] byte_array, short byte_index) {
        ASSERT(this_index == 0);
        ASSERT(len > 0);
        value = byte_array[byte_index] != (short)0;
        if(len == 1)
            return 2;
        else
            return 1;
    }
}