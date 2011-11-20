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
// Created 25.11.08 by Hendrik
// 
// ONLY USE THIS FOR TESTING: an unsecure RSA key generator
// 
// $Id: Unsecure_rsa_key.java,v 1.5 2009-04-09 10:42:17 tews Exp $

package ds.ov2.util;


import java.util.Random;
import java.math.BigInteger;

/** 
 * Unsecure RSA key generation. Do not use this class for real keys.
 * But for key sizes below 512 bits, where {@link
 * java.security.KeyPairGenerator#generateKeyPair} refuses to work,
 * this class can be used. RSA Key sizes below 512 bits are unsecure
 * anyway. 
 * <P>
 *
 * Static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.5 $
 * @commitdate $Date: 2009-04-09 10:42:17 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Unsecure_rsa_key {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Unsecure_rsa_key() {}
    

    /**
     * 
     * Unsecure RSA key generation. Use this method for unsecure key
     * sizes below 512 bits for testing only, where {@link
     * java.security.KeyPairGenerator#generateKeyPair} refuses to
     * work. 
     * 
     * @param key_size desired size of the RSA modulus 
     * @param certainty certainty parameter for probabilistic prime
     * generation, see second argument of {@link
     * BigInteger#BigInteger(int, int, Random)}
     * @return an array of length 3, containing the RSA modulus and
     * the two factors.
     */
    public static BigInteger[] generate(int key_size,
                                        int certainty,
                                        Random rand) 
    {
        int p_len = key_size / 2;
        int q_len = key_size - p_len;
        BigInteger p, q, n;

        do {
            p = new BigInteger(p_len, certainty, rand);
            q = new BigInteger(q_len, certainty, rand);
            n = p.multiply(q);
        } while(n.bitLength() != key_size);

        return new BigInteger[]{n, p, q};
    }
}
