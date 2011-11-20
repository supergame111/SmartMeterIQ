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
// attribute expression signatures, consisting of a hash and a Bignat
// 
// $Id: Signature.java,v 1.11 2009-06-17 21:16:39 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.front;
#endif


#ifndef JAVACARD_APPLET
   import ds.ov2.util.Misc;
   import ds.ov2.util.APDU_Serializable;
   import ds.ov2.util.APDU_byte_array;
   import ds.ov2.util.Serializable_array;
   import ds.ov2.bignat.Bignat;
#endif


/** 
 * Signatures on blinded attribute expressions. This class only
 * provides the storage for a signature. Any computation with the
 * signature has to be done outside. During digest computation one
 * should use {@link #hash_output_buf} as output buffer. When digest
 * computation is finished one has to call {@link #commit_hash}, which
 * either does nothing or copies the restricted hash into {@link
 * #hash}. <P>
 *
 * Signature creation is done in {@link
 * RSA_squared_card#make_sig_hash} and {@link
 * RSA_squared_card#finish_signature
 * RSA_squared_card.finish_signature} and similar methods in {@link
 * RSA_plain_card} and {@link RSA_mont_card}. Signature checking is
 * done in {@link Host_signature#check_signature
 * Host_signature.check_signature}. <P>
 *
 * This is a card data type. It is compatible with
 * <UL>
 * <LI>Signature and {@link Host_signature} if the hash and the number
 * have the same sizes, respectively.</LI>
 * </UL>
 * <P>
 *
 * A signature consists of a hash {@code c} and a number {@code r}
 * such that {@code c = H(A, r^v * (h * A) ^ -c)}, where {@code H} is
 * a hash function, {@code A} is the blinded attribute expression,
 * {@code v} is the public RSA exponent (see {@link RSA_data#v} and
 * {@link PTLS_rsa_parameters#v}), and {@code h} is the public PTLS
 * key (see {@link RSA_data#ptls_key} and {@link
 * PTLS_rsa_parameters#ptls_public_key}). As you can see, the hash
 * value {@code c} is considered as a number and takes part in the
 * computation. 
 * <P>
 *
 * For simplicity the Montgomerizing applet does not demontgomerize
 * the numbers in the resign protocol before feeding numbers to the
 * hash function. This results in what I would call a montgomerized
 * signature, that is a signature where one has to montgomerize the
 * numbers before feeding them to the hash function in the signature
 * check. 
 * <P>
 *
 * During resigning the applet needs to compute {@code r * beta2 *
 * beta1 ^ c * (h A)^ q}. What is important here, is that the hash
 * {@code c} is used as an exponent on the card. In the plain and the
 * squared applets the power {@code beta1 ^ c} is computed with an
 * {@link ds.ov2.bignat.RSA_exponent}, that accepts a number of
 * (almost) arbitrary size as exponent. In these two applets I use
 * {@link RSA_plain_card#double_small} and, respectively, {@link
 * RSA_squared_card#double_small} to hold the hash and pass it on to
 * the {@link ds.ov2.bignat.RSA_exponent}. Further, for the
 * computation of {@code q} it is necessary to sum {@code c + beta3},
 * where {@code beta3} is a random less than {@code v}. This sum is
 * also computed in {@code double_small}. <P>
 *
 * Therefore in the plain and squareing applets the hash must fit into
 * a number of twice the exponent size and there must further be
 * enough room to add a number of exponent size. For the Java cards
 * that we currently have the base size is restricted to 512 - 1952
 * bits, which, according to Lenstra (see {@link
 * ds.ov2.util.Security_parameter}), corresponds to an exponent size
 * of 94 - 198 bits or 12 - 25 bytes. For the hash function {@code H}
 * I use 160 bit SHA-1 ({@link
 * javacard.security.MessageDigest#ALG_SHA}), which produces 20 byte
 * hashes. Thus, if the exponent size is chosen according to Lenstra,
 * the variable {@code double_small} is big enough.
 * <P>
 *
 * For testing in the host test frames I however permit RSA key sizes
 * below 512 bits. Then the {@code double_small} might not be big
 * enough. In such cases (where security is broken anyways) I restrict
 * the size of the hash to {@code 2 * short_bignat_size -1} for the
 * plain and the squaring applet, where {@code short_bignat_size} is
 * the size of {@code v}. The restriction is done by using only the
 * last bytes of the output of {@code H}. The computation of the hash
 * size is done in {@link Hash_size#hash_size Hash_size.hash_size}.
 * The hash of the required size is always stored in {@link #hash}.
 * For the output of {@code H} there is an additional byte array
 * {@link #hash_output_buf}. In the normal case, where the full output
 * of {@code H} is used {@link #hash_output_buf} is merely an alias of
 * {@link #hash}. If the hash size is restricted it is a separate
 * array. 
 * <P>
 *
 * In the Montgomerizing applet I use two additional {@link
 * ds.ov2.bignat.Vector Vector's} {@link RSA_mont_card#temp_base_vec}
 * and {@link RSA_mont_card#temp_exp_vec} of size two to compute
 * {@code beta1 ^ c * (h A)^ q} using simultaneous repeated squaring.
 * Until now I have not found any hints on how big {@code c} and
 * therefore {@code q} should be in relation with {@code v}. Therefore
 * I decided to use the normal exponent size for the exponent vector
 * for the computation of {@code beta1 ^ c * (h A)^ q} (which has the
 * nice side effect that the two additional vectors are perfectly
 * suited for computing {@code beta2 ^ v * (h * A) ^ beta3} for the
 * second hash input). Therefore, for the Montgomerizing applet the
 * hash is restricted to the size of the exponent (the sum {@code c +
 * beta3} is still computed in {@code double_small}, so the hash can
 * really be as long as the exponents). This means that for the
 * Montgomerizing applet the full hash is only used for RSA key sizes
 * greater than 1184 bits. For Java Card resigning takes already more
 * than one hour for 512 bits in the Montgomerizing applet, so any
 * secure RSA key sizes are out of reach any way. For smart phones
 * this point might need to be reconsidered.
 *
 * 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.11 $
 * @commitdate $Date: 2009-06-17 21:16:39 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 */
PUBLIC class Signature
    extends Serializable_array
    implements APDU_Serializable
{

    /**
     * 
     * The hash {@code c}. Size is determined by {@link
     * Hash_size#hash_size Hash_size.hash_size}.
     */
    /* package */ APDU_byte_array hash;

    /**
     * 
     * The number {@code r}. Long bignat.
     */
    /* package */ Bignat number;


    /**
     * 
     * Array of {@link #hash} and {@link #number} for {@link #get_array}.
     */
    private APDU_Serializable[] serializable_contents;


    /**
     * 
     * Output buffer for the digest algorithm. Either an alias of
     * {@link #hash} or a separate byte array. After filling it,
     * {@link #commit_hash} must be called.
     */
    /* package */ byte[] hash_output_buf;


    /**
     * 
     * Value of the first constructor argument {@code
     * short_bignat_size}. Available here for the stub code of the
     * OV-chip protocol layer, to enable it to construct properly
     * sized objects on the fly.
     */
    /* package */ final short sig_short_size;


    /**
     * 
     * Value of the second constructor argument {@code
     * short_bignat_size}. Available here for the stub code of the
     * OV-chip protocol layer, to enable it to construct properly
     * sized objects on the fly.
     */
    /* package */ final short sig_long_size;


    /**
     * 
     * Value of the third constructor argument {@code
     * applet_id}. Available here for the stub code of the
     * OV-chip protocol layer, to enable it to construct properly
     * sized objects on the fly.
     */
    /* package */ final byte applet_id;


    /**
     * 
     * Create a new Signature object. Sizes of internal numbers and
     * buffers depend on the base and exponent size as well as on the
     * applet (see discussion above and {@link Hash_size}).
     * 
     * @param short_bignat_size size of the exponent bignats in bytes
     * @param long_bignat_size size of the base bignats in bytes
     * @param applet_id the applet ID
     */
    public Signature(short short_bignat_size, short long_bignat_size,
                     byte applet_id) 
    {
        sig_short_size = short_bignat_size;
        sig_long_size = long_bignat_size;
        this.applet_id = applet_id;

        short hash_size = Hash_size.hash_size(short_bignat_size, applet_id);
        if(hash_size == Hash_size.digest_output_size) {
            hash = new APDU_byte_array(Hash_size.digest_output_size);
            hash_output_buf = hash.buf;
        }
        else {
            hash = new APDU_byte_array(hash_size);
            hash_output_buf = new byte[Hash_size.digest_output_size];
        }
        number = new Bignat(long_bignat_size, false);

        serializable_contents = new APDU_Serializable[2];
        serializable_contents[0] = hash;
        serializable_contents[1] = number;
        return;
    }


    /**
     * 
     * Produce a properly sized hash value out of the data in {@link
     * #hash_output_buf}. Does nothing if {@link #hash} has size
     * {@link Hash_size#digest_output_size} and {@link
     * #hash_output_buf} is an alias of {@link #hash}. Otherwise
     * copies the last bytes of {@link #hash_output_buf} into {@link
     * #hash}. 
     * 
     */
    void commit_hash() {
        if(hash_output_buf != hash.buf)
            Misc.array_copy(hash_output_buf, 
                            (short)(Hash_size.digest_output_size - hash.size()),
                            hash.buf, (short)0,
                            hash.size());
        return;
    }
        

    /**
     * Copies the hash value {@link #hash} into a Bignat. 
     * <P>
     *
     * Asserts that the size of {@code b} is sufficient.
     * 
     * 
     * @param b Bignat to copy the hash into
     */
    void hash_into_bignat(Bignat b) {
        ASSERT(b.size() >= hash.size());
        b.zero();
        short len = hash.size();
        short res = b.from_byte_array(len, (short)(b.size() - len), 
                                      hash.buf, (short)0);
        ASSERT(res == (short)(len + 1));
        return;
    }


    //########################################################################
    // Serializable_array support
    // 

    /**
     * Return {@link #serializable_contents} in support for abstract
     * {@link Serializable_array}.
     *
     * @return array of objects to (de-)serialize
     */
    protected APDU_Serializable[] get_array() {
        return serializable_contents;
    }


    /**
     * Return 2 as effective size in support for abstract
     * {@link Serializable_array}.
     *
     * @return 2
     */
    public short get_length() {
        return 2;
    }


    /**
     * Compatibility check for the OV-chip protocol layer. Signature
     * is compatible with 
     * <UL>
     * <LI>Signature and {@link Host_signature} if the hash and the number
     * have the same sizes, respectively.</LI>
     * </UL>
     * See <a href="../util/APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     *
     * @param o actual argument or result
     * @return true if this (the declared argument or result) is considered 
     *         binary compatible with {@code o}.
     */
    public boolean is_compatible_with(Object o) {
        if(o instanceof Signature) {
            Signature co = (Signature)o;
            return this.hash.size() == co.hash.size() &&
                this.number.size() == co.number.size();
        }
        #ifndef JAVACARD_APPLET
            else if(o instanceof Host_signature) {
                Host_signature ho = (Host_signature)o;
                return this.hash.size() == ho.get_hash_size() &&
                    this.number.size() == ho.get_number_size();
            }
        #endif
        return false;
    }
}
