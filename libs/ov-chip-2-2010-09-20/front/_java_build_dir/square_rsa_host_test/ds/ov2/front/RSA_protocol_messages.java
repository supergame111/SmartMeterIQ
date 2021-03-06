//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_protocol_messages.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_protocol_messages.java"
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
// Created 7.1.09 by Hendrik
// 
// interface for progress message methods
// 
// $Id: RSA_protocol_messages.java,v 1.6 2009-06-16 08:16:05 tews Exp $

package ds.ov2.front;

import java.math.BigInteger;


/** 
 * Generate protocol progress messages. An instance of this interface
 * can be passed into several methods of {@link RSA_host_card}. At
 * certain points these method call the appropriate methods of this
 * interface to display some progress message. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.6 $
 * @commitdate $Date: 2009-06-16 08:16:05 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public interface RSA_protocol_messages {

    /**
     * 
     * Called when initialization starts in {@link
     * RSA_host_card#issue_card RSA_host_card.issue_card}
     */
    public void initialize_start();

    /**
     * 
     * Called before the allocate call to the applet starts (in {@link
     * RSA_host_card#initialize_card RSA_host_card.initialize_card}).
     */
    public void initialize_allocate_start();

    /**
     * 
     * Called when the allocate call is finished  (in {@link
     * RSA_host_card#initialize_card RSA_host_card.initialize_card}).
     */
    public void initialize_allocate_finished();

    /**
     * 
     * Called when the initialize call to the applet completed and all
     * data has been copied to the applet (in {@link
     * RSA_host_card#initialize_card RSA_host_card.initialize_card}).
     * 
     * @param blinded_a the blinded attribute expression of the new
     * applet
     */
    public void initialize_data_copied(BigInteger blinded_a);

    /**
     * 
     * Called before the start of the resign protocol that is part of
     * applet initialization (in {@link RSA_host_card#issue_card
     * RSA_host_card.issue_card}). 
     */
    public void initialize_resign();

    /**
     * 
     * Called when applet initialization is finished (in {@link
     * RSA_host_card#issue_card RSA_host_card.issue_card}).
     */
    public void initialize_finished();

    /**
     * 
     * Called when the resign protocol starts (in {@link
     * RSA_host_card#resign RSA_host_card.resign}).
     */
    public void resign_start();

    /**
     * 
     * Called when the host received the attribute expression and the
     * signature from the applet during the resign protocol (in {@link
     * RSA_host_card#resign RSA_host_card.resign}).
     * 
     * @param blinded_a the blinded attribute expression of the applet
     */
    public void resign_got_signature(BigInteger blinded_a);

    /**
     * 
     * Called when the signature of the applet was accepted during the
     * resign protocol (in {@link RSA_host_card#resign
     * RSA_host_card.resign}). The signature can be accepted either
     * because it is a valid signature or because signature checking
     * is disabled (as in the resign protocol that is part of applet
     * initialization).
     * 
     * @param checked_signature whether the signature was actually
     * checked
     */
    public void resign_checked_signature(boolean checked_signature);

    /**
     * 
     * Called when the host received the challenge from the applet
     * during the resign protocol (in {@link RSA_host_card#resign
     * RSA_host_card.resign}).
     */
    public void resign_got_challenge();

    /**
     * 
     * Called when the resign protocol has been finished (in {@link
     * RSA_host_card#resign RSA_host_card.resign}).
     * 
     * @param accepted true if the card accepted the new signature
     */
    public void resign_finished(boolean accepted);

    /**
     * 
     * Called when the entry-gate proof protocol starts (in {@link
     * RSA_host_card#check_gate RSA_host_card.check_gate}). 
     */
    public void entry_gate_start();

    /**
     * 
     * Called when the host received the commitment from the applet
     * during the proof protocol (in {@link
     * RSA_host_card#check_gate RSA_host_card.check_gate}). 
     * 
     * @param signature_ok true if the applet produced a valid
     * signature 
     * @param blinded_a the blinded attribute expression of the new
     * applet
     */
    public void entry_gate_committed(boolean signature_ok,
                                     BigInteger blinded_a);

    /**
     * 
     * Called when the host received the response from the applet for
     * its challenge during the proof protocol (in {@link
     * RSA_host_card#check_gate RSA_host_card.check_gate}). 
     */
    public void entry_gate_got_response();

    /**
     * 
     * Called when the entry-gate proof protocol has been finished (in
     * {@link RSA_host_card#check_gate RSA_host_card.check_gate}).
     * 
     * @param result true if the response from the applet was valid
     * and the proof is therefore accepted
     */
    public void entry_gate_finished(boolean result);

}
