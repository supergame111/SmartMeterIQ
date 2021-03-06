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
// wrap an java.security.MessageDigest object with the javacard api
// 
// $Id: Message_digest_wrapper.java,v 1.6 2009-04-09 10:42:17 tews Exp $


package ds.ov2.util;


import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;


/** 
 * Wrap an {@link java.security.MessageDigest
 * java.security.MessageDigest} instance into an {@link
 * javacard.security.MessageDigest javacard.security.MessageDigest}
 * interface. With this wrapper code that is shared between host and
 * card does not have to care about the different interfaces of
 * MessageDigest objects. The abstraction is achieved in the following
 * way: Shared code should declare its MessageDigest objects with <a
 * href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>,
 * which will expand to {@link javacard.security.MessageDigest} on the
 * card and {@link Message_digest_wrapper} on the host. New digest
 * objects can be created with {@link Misc#get_message_digest}. On the
 * host this will return an instance of this class. <P>
 *
 * This class is only for SHA-1 160 bit digests.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.6 $
 * @commitdate $Date: 2009-04-09 10:42:17 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed, however, other code usually
 * refers through <a
 * href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>
 * to this class
 */
public class Message_digest_wrapper {

    /**
     * 
     * The wrapped {@link java.security.MessageDigest} instance.
     */
    private MessageDigest digest;


    /**
     * 
     * Create an instance that wraps a SHA-1 160 bit message digest
     * object. 
     */
    public Message_digest_wrapper()
    {
        try {
            digest = MessageDigest.getInstance("SHA");
        }
        catch(NoSuchAlgorithmException e) {
            System.err.println("Message digest SHA not available");
            System.exit(1);
        }
        return;
    }


    /**
     * 
     * Fed the digest with {@code len} bytes from {@code in_buf},
     * starting at {@code offset}.
     * 
     * @param in_buf data buffer 
     * @param offset starting offset in {@code in_buf}
     * @param len number of bytes to digest
     * @see javacard.security.MessageDigest#update
     * javacard.security.MessageDigest.update
     * @see java.security.MessageDigest#update
     * java.security.MessageDigest.update
     */
    public void update(byte[] in_buf, short offset, short len) {
        digest.update(in_buf, offset, len);
    }


    /**
     * 
     * Fed the digest with {@code in_len} bytes from {@code in_buff},
     * starting at {@code in_off}. Afterwards finish the hash
     * computation and copy the digest into {@code out_buff} at index
     * {@code out_off}. <P>
     *
     * If there is not enough place in {@code out_buff} (less then 20
     * bytes starting at {@code out_off}) an {@link
     * IndexOutOfBoundsException} is thrown.
     * 
     * 
     * @param in_buff data buffer 
     * @param in_off starting offset in {@code in_buf}
     * @param in_len number of bytes to digest
     * @param out_buff buffer for the digest
     * @param out_off starting index to copy the result digest to
     * @throws IndexOutOfBoundsException if there is not enough place
     * in {@code out_buff}
     * @see javacard.security.MessageDigest#doFinal
     * javacard.security.MessageDigest.doFinal 
     * @see java.security.MessageDigest#update
     * java.security.MessageDigest.update
     * @see java.security.MessageDigest#digest
     * java.security.MessageDigest.digest 
     */
    public void doFinal(byte[] in_buff, short in_off, short in_len, 
                        byte[] out_buff, short out_off)
    {
        digest.update(in_buff, in_off, in_len);
        byte[] hash = digest.digest();
        System.arraycopy(hash, 0, out_buff, out_off, hash.length);
        return;
    }
}
