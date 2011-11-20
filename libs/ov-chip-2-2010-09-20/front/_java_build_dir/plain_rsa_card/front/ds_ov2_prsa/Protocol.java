//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol.java"
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
// Created 10.8.08 by Hendrik
// 
// protocol record (consisting of a list of protocol steps)
// 
// $Id: Protocol.java,v 1.10 2009-05-20 11:02:38 tews Exp $

//# 1 "./config" 1
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
// Created 5.11.08 by Hendrik
// 
// preprocessor config directives
// 
// $Id: config,v 1.16 2010-02-18 12:40:38 tews Exp $
//# 55 "./config"
  // XXX the applet id is also hardwired in Makefile!
//# 200 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol.java" 2


  package ds_ov2_prsa;





/** 
 * Runtime representation of a protocol of the OV-chip protocol layer.
 * One protocol consists of a fixed list of steps (of type {@link
 * Protocol_step}) that must be executed in the given order. 
 * Besides the protocol steps every protocol instance must contain its
 * protocol identification number. This identification number must be
 * identical to the index at which this instance is stored in the
 * protocol array. Normally this is initialized in the right way by
 * {@link Registered_protocols#set_protocols
 * Registered_protocols.set_protocols}.
 * <P>
 *
 * Under normal circumstances the protocol instances are created and
 * referenced in the *_description classes, which are generated by the
 * IDL compiler. These classes are shared between the card and the
 * host driver. It is important that the protocol instances on the
 * host and the card are identical, otherwise the protocol layer will
 * not work.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2009-05-20 11:02:38 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 */
 class Protocol {

    // For explanations see Protocol_step.java.

    /**
     * 
     * The protocol identification number. Will be send as INS byte in
     * all APDU's for this protocol. Must be identical to the index at
     * which this protocol is stored in the protocol array. Under
     * normal circumstances this is initialized in 
     * {@link Registered_protocols#set_protocols
     * Registered_protocols.set_protocols}. In the constructor of this
     * class the protocol id is initialized to -1, to provoke an error
     * in the {@link Host_protocol#Host_protocol Host_protocol
     * constructor} if the protocol is used without being linked in
     * some protocols array.
     */
    public byte protocol_id; // will be sent as INS


    /**
     * 
     * All steps of this protocol.
     */
    public final Protocol_step[] steps;


    /**
     * 
     * Create a new protocol instance, consisting of the steps in
     * {@code steps}. The field {@link #protocol_id} is initialized to
     * the invalid value -1. It must receive its value later, normally
     * in {@link Registered_protocols#set_protocols
     * Registered_protocols.set_protocols}.
     * 
     * @param steps the steps
     */
    public Protocol(Protocol_step[] steps) {
        protocol_id = (byte)-1;
        this.steps = steps;
    }


    /**
     * 
     * Update the cached length of the declared results in all steps.
     * This is only necessary if the length of some result changes,
     * for instance in test frames.
     * 
     */
    public void set_result_sizes() {
        for(short i = 0; i < steps.length; i++)
            steps[i].set_result_size();
    }
}
