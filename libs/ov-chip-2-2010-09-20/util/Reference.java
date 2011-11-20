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
// Created 29.5.09 by Hendrik
// 
// References for Java
// 
// $Id: Reference.java,v 1.1 2009-06-02 09:56:03 tews Exp $


package ds.ov2.util;

/**
 * References. Each instance stores one instance of (probably
 * immutable) E, which can be accessed and changed freely.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 *
 */
public class Reference<E> {

    /**
     * 
     * The reference.
     */
    public E ref;

    /**
     * 
     * Initializing constructor. Initialize the reference to {@code
     * e}. 
     * 
     * @param e first value of the reference
     */
    public Reference(E e) { ref = e; }

    /**
     * 
     * Noninitializing constructor. Set the reference to null.
     * 
     */
    public Reference() { this(null); }
}