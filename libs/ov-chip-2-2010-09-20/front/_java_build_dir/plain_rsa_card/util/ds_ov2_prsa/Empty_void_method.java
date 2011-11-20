//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Empty_void_method.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Empty_void_method.java"
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
// Created 23.9.08 by Hendrik
// 
// the empty void method
// 
// $Id: Empty_void_method.java,v 1.6 2009-03-26 15:51:31 tews Exp $

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
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/../util/Empty_void_method.java" 2


  package ds_ov2_prsa;





// import javacard.framework.APDU;


/** 
 * The empty {@link Void_method}. Used for methods steps that specify
 * {@code call nothing} because they don't run a method.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.6 $
 * @commitdate $Date: 2009-03-26 15:51:31 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>
 */
 class Empty_void_method implements Void_method {

    /**
     * 
     * Empty constructor.
     */
    public Empty_void_method() {}


    // Pass APDU and APDU buffer, in case the waitExtension must be called.
    // waitExtension doesn't seem to have any effect. Save some trouble.
    // public void method(APDU apdu, byte[] buf){
    //  return;
    // }


    /**
     * 
     * Return immediately.
     */
    public void method(){
        return;
    }
}
