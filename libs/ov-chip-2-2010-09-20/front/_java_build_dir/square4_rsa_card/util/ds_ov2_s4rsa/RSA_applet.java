//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_applet.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_applet.java"
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
// Created 3.12.08 by Hendrik
// 
// Applet class for the OV-chip 2.0 applet
// 
// $Id: RSA_applet.java,v 1.8 2009-03-26 15:51:29 tews Exp $

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
//# 76 "./config"
  // XXX the applet id is also hardwired in Makefile!
//# 200 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_applet.java" 2


  package ds_ov2_s4rsa;
//# 39 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/RSA_applet.java"
/** 
 * 
 * Main class of the OV-chip RSA applet. Extends {@link Protocol_applet}
 * with specific applet installation code. 
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.8 $
 * @commitdate $Date: 2009-03-26 15:51:29 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>
 *
 */
class RSA_applet extends Protocol_applet {

    /**
     * 
     * Applet install method. Uses no applet installation arguments,
     * the actual arguments are ignored. All the work is done in the
     * constructor {@link #RSA_applet}.
     *
     * @param bytes ignored, array with installation arguments
     * @param start ignored, start offset of the installation arguments
     * @param len ignored, length of installation arguments
     */
    public static void install(byte[] bytes, short start, byte len) {
        new RSA_applet();
        return;
    }


    /**
     * 
     * Allocate/initialize everything for the OV-chip RSA applet.
     * 
     */
    RSA_applet() {

        Front_protocols front_protocols = new Front_protocols();
        set_registered_protocols(front_protocols.registered_protocols);

        register();
        return;
    }
}
