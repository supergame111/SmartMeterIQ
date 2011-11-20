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
// Created 18.03.09 by Hendrik
// 
// abstract base for registered protocols
// 
// $Id: Registered_protocols.java,v 1.3 2009-04-03 07:28:44 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * Service class for the registered protocols of one applet. This
 * class provides the common functionality for the array of protocols
 * of an applet. The applet specific code must allocate and maintain
 * the protocols array. Everything else (consistency checking,
 * protocol ID setting, protocol lookup) is done here. 
 * <P>
 * 
 * Every applet should have precisely one instance of this class
 * (through this is not enforced). In the applet this instance must be
 * registered with {@link Protocol_applet#set_registered_protocols
 * Protocol_applet.set_registered_protocols}. Newly selected protocols
 * will then be looked up with {@link #get_protocol get_protocol} of
 * this instance. <STRONG>The registration with {@link
 * Protocol_applet#set_registered_protocols
 * Protocol_applet.set_registered_protocols} must be done manually at
 * initialization time, before the applet is selected.</STRONG>
 * <P>
 *
 * During initialization or whenever the list of protocol changes
 * (because of delayed protocol initialization, for instance) the
 * applet specific code calls {@link #set_protocols set_protocols} on
 * the only instance of this class. The instance then stores an alias
 * of the externally allocated protocols array for its operation and
 * computes the necessary data to use this new protocols array. <P>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.3 $
 * @commitdate $Date: 2009-04-03 07:28:44 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 *
 */
PUBLIC class Registered_protocols {

    /**
     * 
     * Normal, empty constructor.
     */
    public Registered_protocols() {}
    

    /**
     * 
     * Convenience constructor, incorporating a call to {@link
     * #set_protocols set_protocols}.
     * 
     * @param protocols the array of registered protocols, as for
     * {@link #set_protocols set_protocols}
     */
    public Registered_protocols(Protocol[] protocols) {
        this();
        set_protocols(protocols);
    }


    /**
     * 
     * Alias to the array of all supported protocols. The array itself
     * must be allocated and maintained outside. Direct assignment to
     * this field is impossible, because clients must use {@link
     * #set_protocols set_protocols} to register a new or changed
     * array. 
     *
     * @see #get_protocols
     */
    private Protocol[] protocols;


    /**
     * 
     * Return the protocols array. For those applets that do not store
     * it themselves. Changing the array is permitted under the
     * condition that the currently running protocol does not change
     * its position in the array. After changing, {@link #set_protocols
     * set_protocols} must be called before the next protocol is
     * selected. This is necessary even if the array itself stays the
     * same and only fields in the array are changed.
     */
    public Protocol[] get_protocols() {
        return protocols;
    }


    /**
     * 
     * The first nonaccessible protocol index. In the beginning this
     * can be lesser then protocols.length if not all protocols have
     * been initialized yet. Computed in {@link #set_protocols
     * set_protocols}. 
     *
     * @see #get_max_protocol_index
     */
    private short max_protocol_index = 0;


    /**
     * 
     * Return {@link #max_protocol_index}.
     */
    public short get_max_protocol_index() {
        return max_protocol_index;
    }


    /**
     * 
     * Consistency check for the protocol array. Enforces that
     * <UL>
     * <LI>all elements below {@link #max_protocol_index} are non-null
     * <LI>the protocol_id is the same as the array index
     * <LI>steps and all its elements are be nonnull
     * <LI>in all steps for the arguments and results arrays the
     *   following must hold: Either the array is be null or all its
     * elements are nonnull 
     * </UL>
     * Validity of these properties is enforced with assertions (see
     * the ASSERT macro). 
     * 
     */
    private void check_protocols() {
        for(short i = 0; i < max_protocol_index; i++) {
            ASSERT(protocols[i] != null);
            Protocol p = protocols[i];
            ASSERT(p.protocol_id == i);
            ASSERT(p.steps != null);
            for(short j = 0; j < p.steps.length; j++) {
                Protocol_step s = p.steps[j];
                ASSERT(s != null);
                if(s.arguments != null) {
                    for(short k = 0; k < s.arguments.length; k++) {
                        ASSERT(s.arguments[k] != null);
                    }
                }
                if(s.results != null) {
                    for(short k = 0; k < s.results.length; k++) {
                        ASSERT(s.results[k] != null);
                    }
                }
            }
        }
        return;
    }


    /**
     * 
     * (Re-)Initialize the protocols array. The protocols array itself
     * must be allocated and filled outside of this class in the
     * client. This method computes the protocol ID's, performs
     * consitency checks and stores an alias of the array in {@link
     * #protocols}. In addition, this method determins {@link
     * #max_protocol_index} as the maximal index such that all entries
     * less than {@link #max_protocol_index} in {@link #protocols} are
     * non-null. 
     * <P>
     *
     * This method can be called several times, for instance to make
     * delayed protocols available at a later stage. 
     * <P>
     *
     * Internally the {@code protocols} argument is not copied, only
     * an alias is stored. Changing the protocols array is permitted
     * under the condition that the currently running protocol does
     * not change its position in the array and remains itself
     * unchanged.
     * After changing, {@link
     * #set_protocols set_protocols} must be called before the next
     * protocol is selected. This is necessary even if the array
     * itself stays the same and only fields in the array are changed.

     *
     * @param protocols the protocols array, must be non-null but may
     * contain null references. Only protocols before the first null
     * reference get activated.
     */
    public void set_protocols(Protocol[] protocols) {
        this.protocols = protocols;
        max_protocol_index = (short)protocols.length;
        for(short i = 0; i < protocols.length; i++) {
            if(protocols[i] == null) {
                max_protocol_index = i;
                break;
            }
            protocols[i].protocol_id = (byte)i;
        }

        check_protocols();
        return;
    }


    /**
     * 
     * Select protocol. Return the protocol with the index {@code ins}
     * or null if {@code ins} is greater than {@link
     * #max_protocol_index}. (The argument {@code ins} is the INS byte
     * of the first APDU of the first step of a newly selected
     * protocol.) 
     */
    public Protocol get_protocol(short ins) {
        if(ins < max_protocol_index) {
            return protocols[ins];
        }
        else {
            return null;
        }
    }
}
