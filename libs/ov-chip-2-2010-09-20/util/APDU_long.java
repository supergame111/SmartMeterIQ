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
// Created 29.10.08 by Hendrik
// 
// APDU_Serializable interface for long. There are no longs on the 
// card. This is only used in the testframe with BIGNAT_USE_INT.
// 
// $Id: APDU_long.java,v 1.9 2009-06-19 20:37:36 tews Exp $

package ds.ov2.util;


/** 
 * {@link APDU_Serializable} wrapper around long. There are no long's
 * on Java Card, so this is not used for communication with the card.
 * This class is only used in the bignat testframe for testing the
 * int/long configuration.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2009-06-19 20:37:36 $ by $Author: tews $
 * @environment host, host test environment
 * @CPP no cpp preprocessing needed
 */
public class APDU_long implements APDU_Serializable {

    /**
     * 
     * The wrapped long value.
     */
    public long value;


    /**
     * 
     * Empty constructor. Default initialize {@link #value}.
     */
    public APDU_long() {
        // nothing to do
        return;
    }


    /**
     * 
     * Initialize {@link #value} to {@code l}.
     * 
     * @param l long to wrap
     */
    public APDU_long(long l) {
        // ASSERT(Short.MIN_VALUE <= s && s <= Short.MAX_VALUE);
        value = l;
        return;
    }


    /**
     * Size in bytes necessary to send or receive this object 
     * via the OV-chip protocol layer, see 
     * {@link ds.ov2.util.APDU_Serializable#size APDU_Serializable.size()}.
     *
     * @return 8
     */
    public short size(){
        return (short)8;
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
     * This object is only compatible with instances of this class. 
     *
     * @param o actual argument or result
     * @return true if {@code o} is an APDU_long instance.
     */
    public boolean is_compatible_with(Object o){
        if(o instanceof APDU_long) {
            return true;
        }
        return false;
    }


    /**
     * Serialization this long. See {@link 
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
        assert(this_index < 8);
        assert(len > 0);
        short count = 0;
        while(count < len && this_index + count < 8) {
            int shift = (7 - this_index - count) * 8;
            byte_array[byte_index] = (byte)
                ((value & (0xffL << shift)) >> shift);
            count++;
            byte_index++;
        }
        if(count == len && this_index + count == 8)
            return (short)(count + 1);
        else
            return count;
    }


    /**
     * Deserialize this long. See {@link 
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
        assert(this_index < 8);
        assert(len > 0);

        if(this_index == 0)
            value = 0L;

        short count = 0;
        while(count < len && this_index + count < 8) {
            int shift = (7 - this_index - count) * 8;
            value |= (byte_array[byte_index] & 0xffL) << shift;
            count++;
            byte_index++;
        }
        if(count == len && this_index + count == 8)
            return (short)(count + 1);
        else
            return count;
    }
}
