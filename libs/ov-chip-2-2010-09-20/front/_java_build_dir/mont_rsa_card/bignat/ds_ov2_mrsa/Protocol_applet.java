//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol_applet.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol_applet.java"
// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009
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
// Created 21.3.09 by Hendrik
// 
// General functionality for the main applet class
// 
// $Id: Protocol_applet.java,v 1.3 2009-06-19 20:37:36 tews Exp $

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
//# 62 "./config"
  // XXX the applet id is also hardwired in Makefile!
//# 200 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Protocol_applet.java" 2


  package ds_ov2_mrsa;




import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;


/**
 * 
 * General functionality for the main applet instance for applets
 * using the OV-Chip protocol layer. In the applet specific code there
 * must be one class extending this class and overriding the {@link
 * #install install} method. In addition to the usual tasks (creation
 * of one instance and calling {@link #register register} at the end)
 * this install method must do the following things (in conjunction
 * with the constructor of the extending class): <OL> <LI>allocate and
 * populate the array of registered protocols (which has type {@link
 * Protocol}{@code []}). <LI>Create one instance of {@link
 * Registered_protocols}, initialize the registered protocols there
 * (via {@link Registered_protocols#set_protocols
 * Registered_protocols.set_protocols}) <LI>register the instance of
 * {@link Registered_protocols} in the main applet instance (i.e., the
 * only instance of the class that extends this class). This can be
 * done with with {@link #set_registered_protocols
 * set_registered_protocols}. </OL>
 *
 * Note that the registered protocols are also needed in the host
 * driver. It is therefore recommended that points 1 and 2 in the
 * above list are factored out in a separate class, say XXX_protocols
 * while point 3 is done in the constructor of the class extending
 * this class. This way the class XXX_protocols remains independent of
 * {@link javacard.framework.Applet} and can be compiled directly into
 * the host driver without too much hassle. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.3 $
 * @commitdate $Date: 2009-06-19 20:37:36 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 *
 */
 class Protocol_applet extends Applet {

    /**
     * 
     * Card protocol instance. Responsible for running arbitrary
     * protocols. Initialized in constructor. If a protocol is active
     * then {@code card_protocol.}{@link Card_protocol#protocol
     * protocol} != null and {@link Card_protocol#protocol_running}
     * returns true.
     * 
     */
    private Card_protocol card_protocol = null;


    /**
     * 
     * Registered protocols instance. This instance maintains the
     * registered protocols. New protocols are selected via this
     * instance. <STRONG>The main applet instance that extends this
     * class must manually initialize this field via {@link
     * #set_registered_protocols set_registered_protocols}. This must
     * happen at initialization time before the applet is selected the
     * first time.</STRONG>
     */
    private Registered_protocols registered_protocols = null;


    /**
     * 
     * Initialize the {@link #registered_protocols} field.
     * <STRONG>This must happen at initialization time before the
     * applet is selected the first time.</STRONG>
     * 
     * @param registered_protocols Registered_protocols instance
     */
    public void set_registered_protocols(Registered_protocols
                                         registered_protocols) {
        this.registered_protocols = registered_protocols;
    }


    /**
     * 
     * Constructor. Initializes {@link #card_protocol}.
     * 
     */
    protected Protocol_applet() {
        card_protocol = new Card_protocol();
        return;
    }


    /**
     * 
     * Applet selection. Called from Java Card runtime when the applet
     * is selected. Resets the protocol layer.
     * 
     * @return true because the applet is always ready to be selected
     */
    public boolean select() {
        // Clean up.
        card_protocol.clear_protocol();
        return true;
    }


    /**
     * 
     * Process an incoming APDU. If no protocol is running select a
     * new protocol according to the INS byte of the incoming APDU.
     * Otherwise continue the currently selected protocol. In any case
     * the APDU is delegated to {@link Card_protocol#process
     * Card_protocol.process} for processing.
     * 
     * @param apdu application protocol data unit
     */
    public void process(APDU apdu) {
        // Return 9000 on SELECT
        if (selectingApplet()) {
            return;
        }

        Misc.myassert(registered_protocols != null, (short)(0));

        // init parameters debugging
        // ASSERT_TAG(false, init_array[19]);

        byte[] buf = apdu.getBuffer();

        if(buf[ISO7816.OFFSET_CLA] != 0)
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);

        if(card_protocol.protocol_running()) {
            // If we are in a protocol, continue it!
            card_protocol.process(apdu, buf);
        }
        else {
            // Select a new protocol.
            short ins = (short)(buf[ISO7816.OFFSET_INS] & 0xff);
            Protocol p = registered_protocols.get_protocol(ins);

            if(p != null) {
                card_protocol.set_protocol(p);
                card_protocol.process(apdu, buf);
            }
            else {
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }
        }
    }
}
