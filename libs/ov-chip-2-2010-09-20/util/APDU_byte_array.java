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
// Created 1.11.08 by Hendrik
// 
// APDU_Serializable interface for byte arrays
// 
// $Id: APDU_byte_array.java,v 1.9 2009-05-28 15:17:03 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif



/** 
 * {@link APDU_Serializable} wrapper around a fixed size byte array. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2009-05-28 15:17:03 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class APDU_byte_array implements APDU_Serializable {

    /**
     * 
     * The byte array. The byte array can be freely changed, but this
     * reference itself must stay constant.
     */
    public final byte[] buf;


    /**
     * 
     * Create a new wrapped byte array of length {@code size}.
     * 
     * @param size length of the wrapped byte array
     */
    public APDU_byte_array(short size) {
        buf = new byte[size];
        return;
    }


    /**
     * 
     * Create a APDU_byte_array by wrapping an existing byte array.
     * 
     * @param buf the byte array to wrap
     */
    public APDU_byte_array(byte buf[]) {
        this.buf = buf;
    }


    //########################################################################
    // APDU_Serializable interface
    // 


    /**
     * Size in bytes necessary to send or receive this object 
     * via the OV-chip protocol layer, see 
     * {@link ds.ov2.util.APDU_Serializable#size APDU_Serializable.size()}.
     *
     * @return length of the {@link #buf} byte array
     */
    public short size() {
        return (short)buf.length;
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
     * Objects of this class are only compatible to objects of this
     * class of the same size.
     *
     * @param o actual argument or result
     * @return true if this {@code o} is an APDU_byte_array of the
     * same size
     */
    public boolean is_compatible_with(Object o) {
        if(o instanceof APDU_byte_array) {
            return this.size() == ((APDU_byte_array)o).size();
        }
        return false;
    }


    /**
     * Serialize the byte array. See {@link 
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
        ASSERT(this_index < buf.length);
        short max = 
            (short)(this_index + len) <= buf.length ? 
                        len : (short)(buf.length - this_index);
        Misc.array_copy(buf, this_index, byte_array, byte_index, max);
        if((short)(this_index + len) == buf.length)
            return (short)(len + 1);
        else
            return max;
    }


    /**
     * Deserialization the byte array. See {@link 
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
        ASSERT(this_index < buf.length);
        short max = 
            (short)(this_index + len) <= buf.length ? 
                      len : (short)(buf.length - this_index);
        Misc.array_copy(byte_array, byte_index, buf, this_index, max);
        if((short)(this_index + len) == buf.length)
            return (short)(len + 1);
        else
            return max;
    }
}
