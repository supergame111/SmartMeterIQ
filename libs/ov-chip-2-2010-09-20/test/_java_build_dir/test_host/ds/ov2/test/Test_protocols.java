//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/Test_protocols.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/Test_protocols.java"
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
// Created 20.3.09 by Hendrik
// 
// registered protocols for the test applet
// 
// $Id: Test_protocols.java,v 1.14 2010-02-18 11:42:49 tews Exp $

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
// Created 26.7.08 by Hendrik
// 
// preprocessor config directives
// 
// $Id: config,v 1.17 2010-02-16 10:26:10 tews Exp $
//# 91 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/Test_protocols.java" 2




  package ds.ov2.test;



  import ds.ov2.util.APDU_short;
  import ds.ov2.util.APDU_byte_array;
  import ds.ov2.util.Registered_protocols;
  import ds.ov2.util.Protocol;



/** 
 * Central point of the test applet. There is precisely one instance
 * of this class in the test applet. This instance bundles all the
 * functionality of the applet in the array of registered protocols.
 * This class is also responsible for creating/initializing the
 * complete object structure of the test applet. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.14 $
 * @commitdate $Date: 2010-02-18 11:42:49 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 *
 */
public class Test_protocols {

    /**
     * 
     * Collection of all Bignats, Vectors and RSA exponents.
     */
    public final Bignats bignats;

    /**
     * 
     * Misc protocol description instance.
     */
    public final Misc_protocols_description misc_protocols;

    /**
     * 
     * Data protocol description instance.
     */
    public final Data_protocol_description data_protocol;

    /**
     * 
     * Protocol description for various Bignat (performance) tests.
     */
    public final Bignat_protocols_description bignat_protocols;

    /**
     * 
     * Protocol description for vector exponent.
     */
    public final Exponent_perf_description exponent_protocols;


    /**
     * 
     * Service instance for protocol arrays.
     */
    public final Registered_protocols registered_protocols;


    /**
     * 
     * Central creation/allocation point of the test applet. This
     * constructor creates all protocol descriptions (and thereby all
     * test applet objects) and allocates and initializes the protocol
     * array, which is then registered in {@link
     * #registered_protocols}.
     * 
     * @param short_bignat_size size in bytes for the (short) exponent
     * Bignats 
     * @param long_bignat_size size in bytes for the (long) base
     * Bignats 
     * @param double_bignat_size size in bytes for the double-sized
     * Bignats
     * @param max_vector_length initialization length for the base and
     * exponent vectors.
     * @param cap_creation_time applet cap file creation time as
     * returned by {@link java.io.File#lastModified}
     */
    public Test_protocols(APDU_short short_bignat_size,
                          APDU_short long_bignat_size,
                          APDU_short double_bignat_size,
                          APDU_short max_vector_length,
                          APDU_byte_array cap_creation_time)
    {
        bignats = new Bignats(short_bignat_size.value,
                              long_bignat_size.value,
                              double_bignat_size.value,
                              max_vector_length.value);

        misc_protocols = new Misc_protocols_description(this,
                                                        short_bignat_size,
                                                        long_bignat_size,
                                                        double_bignat_size,
                                                        max_vector_length,
                                                        cap_creation_time);
        data_protocol = new Data_protocol_description(this);
        bignat_protocols = new Bignat_protocols_description(bignats);
        exponent_protocols =
            new Exponent_perf_description(this, bignats);

        Protocol[] protocols = new Protocol[]{
            misc_protocols.ping_protocol,
            misc_protocols.status_protocol,
            misc_protocols.mem_size_protocol,
            misc_protocols.set_size_protocol,
            data_protocol.check_data_protocol,
            data_protocol.set_size_protocol,
            data_protocol.data_performance_receive_protocol,
            data_protocol.data_performance_send_protocol,
            data_protocol.data_perf_proof_protocol,
            bignat_protocols.mont_mult_protocol,
            bignat_protocols.demontgomerize_protocol,
            bignat_protocols.div_protocol,
            exponent_protocols.vector_length_protocol,
            exponent_protocols.vector_exp_protocol,
            bignat_protocols.rsa_exp_protocol,
            bignat_protocols.squared_mult_protocol,
            bignat_protocols.short_squared_mult_protocol,
            bignat_protocols.squared_mult_4_protocol,
            bignat_protocols.short_square_4_mult_protocol,
            bignat_protocols.add_protocol,
            bignat_protocols.subtract_protocol,
            bignat_protocols.mult_protocol
        };

        registered_protocols = new Registered_protocols(protocols);
    }


    /**
     * 
     * Recompute the cached total result size in all steps in all
     * protocols. Needs to be done after each resizing operation.
     * 
     */
    public void set_result_sizes() {
        for(short i = 0;
            i < registered_protocols.get_max_protocol_index(); i++)
            registered_protocols.get_protocols()[i].set_result_sizes();
    }
}
