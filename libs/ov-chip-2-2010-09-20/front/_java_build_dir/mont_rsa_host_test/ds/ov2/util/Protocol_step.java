//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol_step.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol_step.java"
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
// Created 25.8.08 by Hendrik
// 
// protocol step records
// 
// $Id: Protocol_step.java,v 1.13 2009-04-09 10:42:17 tews Exp $

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
//# 200 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol_step.java" 2




  package ds.ov2.util;



/** 
 * Runtime instance of one step of a protocol of the OV-chip protocol
 * layer. Every such {@link Protocol} consists of several steps that
 * must be executed exactly in a specific order. Every protocol step
 * transfers some data to the applet, executes some method there and
 * transfers some data back. Instances of this class only describe the
 * protocol steps. The machinery to execute the steps is contained in
 * {@link Host_protocol} and {@link Card_protocol}. 
 * <P>
 *
 * The step instances are created and maintained in the *_description
 * classes, which are generated by the IDL compiler. The description
 * instances and the protocols and steps therein are shared between
 * the host and the card. For the protocol layer to work it is
 * important that the shared instances of protcols and steps are
 * identical up to one exception. The exception is the {@link #method}
 * field, which links the code to be executed on the card. This field
 * and its constructor argument are guarded by <a
 * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 * and <a
 * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>
 * and are therefore only present on the card and in the applet test
 * frame. 
 * <P>
 *
 * The arguments and results here are the <EM>declared</EM> arguments
 * and results. On the cards they are used as actual arguments and
 * result. This means that incoming data on the card is directly
 * copied into the arguments here. The methods on the card have to
 * store their results in the result objects here, whose contents is
 * then copied back to the host. 
 * <P>
 *
 * On the host usually separate arrays of {@link APDU_Serializable}
 * are used as arguments and results. It is therefore possible to
 * perform some data type conversion (by for instance supplying an
 * {@link ds.ov2.bignat.APDU_BigInteger} where an {@link
 * ds.ov2.bignat.Bignat} is expected). On the host the declared
 * argument and results in instances of this class function only as a
 * type and size reference, see the <A
 * HREF="../util/APDU_Serializable.html#apdu_compatibility">compatibility
 * check.</A>
 * <P>
 *
 * Instances of this class cache the total length of the declared
 * results in the field {@link #result_size}. When the size of the
 * results is changed (for instance in a test frame) this field must
 * be explicitely updated by calling {@link #set_result_size} here or
 * {@link Protocol#set_result_sizes} on the protocol that contains
 * this step.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.13 $
 * @commitdate $Date: 2009-04-09 10:42:17 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>
 */
public class Protocol_step {


    /**
     * 
     * The number of this step. The first step in a protocol must have
     * number 0, the second number 1, and so on. Normally the IDL
     * compiler generates the code for creating the protocol steps and
     * the generated code will contain the right step numbers.
     */
    public final byte step_identifier; // will be sent as P1


    /**
     * 
     * The declared arguments.
     */
    public APDU_Serializable[] arguments;



        /**
         * 
         * The code to run on the card when the arguments have been
         * received. 
         * <P>
         *
         * Only available when <a
         * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
         * or <a
         * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>
         * is defined. 
         */
        public final Void_method method;



    /**
     * 
     * The declared results.
     */
    public APDU_Serializable[] results;


    /**
     * 
     * Total length of the declared results in bytes. This value is
     * cached here to reduce to overhead of the LE field checking on
     * the card (in {@link Card_protocol#process_send
     * Card_protocol.process_send}). This field is initialized in the
     * constructor. When the size of the results changes afterwards
     * (for instance in a test frame) this field must be explicitely
     * updated by calling {@link #set_result_size} here or {@link
     * Protocol#set_result_sizes} on the protocol that contains this
     * step.
     */
    private short result_size;


    /**
     * 
     * Update the {@link #result_size} cache.
     */
    public void set_result_size() {
        result_size = Misc.length_of_serializable_array(results);
        return;
    }


    /**
     * 
     * Return the total length of the {@link #results}.
     * 
     * @return lenght of the results in bytes
     */
    public short get_result_size() {
        return result_size;
    }


    /**
     * 
     * Create a new protocol step with the supplied arguments. The
     * total length of the {@code results} will be cached in {@link
     * #result_size}. 
     * <P>
     *
     * The {@code method} argument is only available on the card and
     * in the <a
     * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>.
     * On the host this constructor has only 3 arguments.
     * 
     * @param step_identifier the number of this step
     * @param arguments the declared arguments
     * @param method the code to execute on the card; this argument is
     * not present on the host.
     * @param results the declared results
     */
    public Protocol_step(
               byte step_identifier,
               APDU_Serializable[] arguments,

                    Void_method method,

               APDU_Serializable[] results)
    {

        this.step_identifier = step_identifier;
        this.arguments = arguments;


            this.method = method;


        this.results = results;
        set_result_size();
        return;
    }
}
