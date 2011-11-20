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
// Created 29.11.08 by Hendrik
// 
// Debug code for the RSA ov2 applet
// 
// $Id: RSA_card_debug.java,v 1.24 2009-06-18 11:57:38 tews Exp $

#include <config>

package ds.ov2.front;


import java.io.PrintWriter;
import java.util.Random;
import java.math.BigInteger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;

import ds.ov2.util.Misc_host;
import ds.ov2.util.BigIntUtil;
import ds.ov2.front.RSA_DEBUG_PROTOCOL_STUBS.Get_result;
import ds.ov2.front.RSA_DEBUG_PROTOCOL_STUBS.Mem_size_result;


/** 
 * Host driver code for the debug protocols of the RSA applets. The
 * debug protocols should not be available in a real system, because
 * they extract all attribute values from the applet. However, for the
 * current version of the test frame and the graphical demonstrator
 * the debug protocols are necessary, because for the random attribute
 * updates the host driver needs to know the real attribute values.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.24 $
 * @commitdate $Date: 2009-06-18 11:57:38 $ by $Author: tews $
 * @environment host
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#RSA_DEBUG_PROTOCOL_STUBS">RSA_DEBUG_PROTOCOL_STUBS<a>
 */
public class RSA_card_debug {

    /**
     * 
     * Registered protocols instance.
     */
    private final Front_protocols front_protocols;    

    /**
     * 
     * Stubs instance for the protocol steps. Declared to be of type
     * <a
     * href="../../../overview-summary.html#RSA_DEBUG_PROTOCOL_STUBS">RSA_DEBUG_PROTOCOL_STUBS<a>. 
     */
    public final RSA_DEBUG_PROTOCOL_STUBS stubs;

    /**
     * 
     * Randomness source.
     */
    private final Random rand = new Random();

    /**
     * 
     * Output channel for debug messages.
     */
    private final PrintWriter out;

    /**
     * 
     * Verbosity level.
     */
    private final int verbosity;


    /**
     * 
     * Constructor. Initialize all fields.
     * 
     * @param front_protocols protocol array
     * @param stubs of type <a
     * href="../../../overview-summary.html#RSA_DEBUG_PROTOCOL_STUBS">RSA_DEBUG_PROTOCOL_STUBS<a>,
     * stubs for protocol steps
     * @param out output channel
     * @param verbosity verbosity level
     */
    public RSA_card_debug(Front_protocols front_protocols,
                          RSA_DEBUG_PROTOCOL_STUBS stubs, 
                          PrintWriter out,
                          int verbosity) 
    {
        this.front_protocols = front_protocols;
        this.stubs = stubs;
        this.out = out;
        this.verbosity = verbosity;
        return;
    }


    /**
     * 
     * Host side initialization for delayed protocols. 
     * <P>
     *
     * Asserts that the host side initialization for the delayed
     * protocols of {@link #front_protocols} has been done before
     * calling this method.
     * 
     */
    // Essential parts of the RMI layer depend on the parameters that 
    // we install in the first step on the card. After the first step
    // we must therefore initialize the RMI layer on the host side.
    // (For the card this happens in the allocate call.)
    // See also RSA_host_card.host_side_init.
    public void host_side_init() {
        // RSA_host_card.host_side_init must have been called before!
        assert front_protocols.rsa_description.data.n != null;

        front_protocols.rsa_debug.delayed_init();

        // If the applet and the host driver data structures have only
        // been reset via the reset_applet_state protocol then it is
        // necessary to update argument and result arrays in the host
        // data structures to make them point to the newly allocated
        // variables.
        front_protocols.rsa_debug.update_all();

        front_protocols.init_protocols();
        stubs.delayed_init();
    }


    /**
     * 
     * Print the status from the status protocol.
     * 
     * @param out output channel
     * @param s result record from the status protocol
     */
    public void print_card_status(PrintWriter out, Get_result s) {
        out.format("card status response\n" +
                   "  applet ID = 0x%02X (%s)\n" +
                   "  n = %s\n" +
                   "    = %s\n" +
                   "  v = %s\n" +
                   "    = %s\n" +
                   "  h = %s\n" +
                   "    = %s\n",
                   s.data_applet_id, 
                   Applet_type.from_byte(s.data_applet_id), 
                   s.data_n.m,
                   BigIntUtil.to_byte_hex_string(s.data_n.m),
                   s.data_v,
                   BigIntUtil.to_byte_hex_string(s.data_v),
                   s.data_ptls_key,
                   BigIntUtil.to_byte_hex_string(s.data_ptls_key));
        BigIntUtil.print_array(out, "", " g", s.data_bases.a);
        BigIntUtil.print_array(out, "current attributes\n", "  a",
                               s.data_current_attributes.a);
        BigIntUtil.print_array(out, "new attributes\n", "  a",
                               s.data_new_attributes.a);
        out.format("  current b = %s\n" +
                   "            = %s\n" +
                   "      new b = %s\n" +
                   "            = %s\n" +
                   "  current A = %s\n" +
                   "            = %s\n" +
                   "      new A = %s\n" +
                   "            = %s\n" +
                   "  curr sig c = %s\n" +
                   "  curr sig r = %s\n" +
                   "             = %s\n" +
                   "   new sig c = %s\n" +
                   "   new sig r = %s\n" +
                   "             = %s\n",
                   s.data_current_blinding,
                   BigIntUtil.to_byte_hex_string(s.data_current_blinding),
                   s.data_new_blinding,
                   BigIntUtil.to_byte_hex_string(s.data_new_blinding),
                   s.data_current_blinded_a,
                   BigIntUtil.to_byte_hex_string(s.data_current_blinded_a),
                   s.data_new_blinded_a,
                   BigIntUtil.to_byte_hex_string(s.data_new_blinded_a),
                   Misc_host.to_byte_hex_string(
                                     s.data_current_signature.hash),
                   s.data_current_signature.number,
                   BigIntUtil.to_byte_hex_string(
                                s.data_current_signature.number),
                   Misc_host.to_byte_hex_string(
                                     s.data_new_signature.hash),
                   s.data_new_signature.number,
                   BigIntUtil.to_byte_hex_string(
                                s.data_new_signature.number));
        BigIntUtil.print_array(out, "%d montgomery corrections\n",
                               "  corr", 
                               s.data_montgomery_corrections.a);
    }


    /**
     * 
     * Run the status protocol to retrieve the applet status.
     * 
     * @param card_channel channel to the applet
     * @return result record of the status protocol
     * @throws CardChannel on communication errors
     */
    public Get_result get_status(CardChannel card_channel) 
        throws CardException
    {
        return stubs.get_call(card_channel);
    }


    /**
     * 
     * Run the status protocol to retrieve and print the applet
     * status. Convenience method combining {@link #get_status
     * get_status} and {@link #print_card_status print_card_status}.
     * 
     * @param out output channel, maybe null to supress output
     * @param level required verbosity level; only print status if
     * {@code level} is greater or equal the configured {@link
     * #verbosity}. 
     * @return result record of the status protocol
     * @throws CardException for communication errors
     */
    public Get_result get_and_print_status(PrintWriter out, 
                                           int level,
                                           CardChannel card_channel) 
        throws CardException
    {
        Get_result s = get_status(card_channel);
        if(out != null && verbosity >= level)
            print_card_status(out, s);

                
        return s;
    }


    /**
     * 
     * Run the mem_size protocol to retrieve and print the available
     * memory and whether assertions are compiled in the applet.
     * 
     * @param out output channel
     * @param card_channel channel to the applet
     * @throws CardException on communication errors
     */
    public void print_applet_memory(PrintWriter out, CardChannel card_channel)
        throws CardException
    {
        Mem_size_result r = stubs.mem_size_call(card_channel);
        out.format("### Free applet memory: %d persistent, %d trans reset, " +
                   "%d trans deselect\n" +
                   "### Assertions %s\n",
                   r.mem_persistent,
                   r.mem_transient_reset,
                   r.mem_transient_deselect,
                   r.assertions_on ? "ON" : "OFF");
    }


    /**
     * 
     * Run the reset-applet-state protocol to reset the applet into
     * state {@link RSA_data#UNALLOCTED}. Do also reset the mirroring
     * host data structures.
     * 
     * @param card_channel channel to the applet
     * @throws CardException on communication errors
     */
    public void reset_applet(CardChannel card_channel) 
        throws CardException
    {
        stubs.reset_call(card_channel);
        // Reset the host side too.
        front_protocols.rsa_description.data.state = RSA_data.UNALLOCTED;
    }


    /**
     * 
     * Invent random attribute updates. The resign protocol fails when
     * one of the attribute updates produces an under- or overflow.
     * Therefore this method queries the card status in order to
     * retrieve the current attribute values of the applet before
     * determining the random updates. 
     * <P>
     *
     * The random updates excercise border conditions as follows. With
     * a probability of 5% the update is chosen such that the
     * attribute gets the maximal possible attribute value. Also with
     * 5% the attribute get value 0. With both 40% a random value is
     * added or subtracted without producing an overflow. 
     * 
     * @param params current PTLS parameter set
     * @param card_channel channel to the applet
     * @return array with {@link PTLS_rsa_parameters#attribute_number
     * params.attribute_number} updates in the range -{@link
     * PTLS_rsa_parameters#v params.v} + 1 .. {@link
     * PTLS_rsa_parameters#v params.v} -1, inclusive
     * @throws CardException on communication errors
     */
    public BigInteger[] invent_attribute_updates(PTLS_rsa_parameters params,
                                              CardChannel card_channel) 
        throws CardException
    {
        // The updates array will be returned
        BigInteger[] updates = new BigInteger[params.attribute_number];
        
        Get_result s = get_status(card_channel);

        assert s.data_current_attributes.a.length - 1 == 
            params.attribute_number;

        BigInteger[] current_attributes = 
            new BigInteger[params.attribute_number];
        System.arraycopy(s.data_current_attributes.a, 0,
                         current_attributes, 0, params.attribute_number);

        boolean[] multiplication = new boolean[params.attribute_number];

        for(int i = 0; i < updates.length; i++) {
            float die = rand.nextFloat();
            if(die < 0.05) {
                updates[i] = 
                    params.v.subtract(current_attributes[i]).
                    subtract(BigInteger.ONE);
            }
            else if(die < 0.10) {
                updates[i] = current_attributes[i].negate();
            }
            else {
                boolean choose_positive_value = die < 0.55;
                int max_bit_length = -1;
                if(choose_positive_value) {
                    max_bit_length = 
                        params.v.subtract(current_attributes[i]).
                        bitLength();
                    if(max_bit_length <= 3)
                        choose_positive_value = false;
                }
                if(!choose_positive_value) {
                    max_bit_length = current_attributes[i].bitLength();
                    if(max_bit_length <= 3) {
                        max_bit_length =
                            params.v.subtract(current_attributes[i]).
                            bitLength();
                        choose_positive_value = true;
                    }
                }
                BigInteger val = new BigInteger(max_bit_length -1, rand);
                if(!choose_positive_value)
                    val = val.negate();
                updates[i] = val;
            }                   
        }
        return updates;
    }
}
