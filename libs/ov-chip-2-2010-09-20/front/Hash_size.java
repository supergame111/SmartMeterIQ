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
// Created 28.11.08 by Hendrik
// 
// Common part of Signature and Host_signature
// 
// $Id: Hash_size.java,v 1.10 2010-02-16 10:26:07 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.front;
#endif


/** 
 * Computes the size of the hash that is used in both {@link
 * Signature} and {@link Host_signature}. The underlying digest SHA-1
 * produces always 160 bit hashes. For using smaller RSA keys (for
 * instance in the host test frame or in the Montgomerizing applet)
 * this means that hash does not fit into existing variables. To avoid
 * complications in these cases (where security is irrelevant anyways)
 * I restrict the hash size appropriately. This is done both in {@link
 * Signature} and {@link Host_signature} by using only some last bytes
 * of the digest. This class computes the hash size. See {@link
 * Signature} for a more elaborated discussion of the hash size
 * problem. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2010-02-16 10:26:07 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class Hash_size {

    /**
     * 
     * Static class, object construction disabled.
     */
    protected Hash_size() {}


    /**
     * 
     * The output size that the SHA-1 digest produces when used in
     * {@link Signature} and {@link Host_signature}.
     */
    public final static short digest_output_size = 20;


    /**
     * 
     * Compute the hash size, depending on the size for short
     * (exponent) bignats. Normally the size should not exceed 
     * {@code 2 * short_bignat_size -1}, but for the Montgomerizing
     * applet it is restricted to {@code short_bignat_size}. 
     * 
     * @param short_bignat_size the size of short (exponent) bignats
     * in bytes
     * @param applet_id the applet ID, see {@link RSA_data#applet_id}.
     * @return size of the hash in bytes
     */
    public static short hash_size(short short_bignat_size, byte applet_id) {
        short x;
        switch(applet_id) {
        case RSA_data.MONT_RSA_APPLET:
            x = short_bignat_size;
            break;
        case RSA_data.PLAIN_RSA_APPLET:
        case RSA_data.SQUARED_RSA_APPLET:
        case RSA_data.SQUARED4_RSA_APPLET:
            x = (short)(2 * short_bignat_size -1);
            break;
        default:
            x = 0;
            ASSERT(false);
        }
        return x > digest_output_size ? digest_output_size : x;
    }
}


