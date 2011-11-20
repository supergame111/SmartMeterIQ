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
// Created 1.11.08 by Hendrik
// 
// actions for the bases selection dialog
// 
// $Id: Bases_actions.java,v 1.7 2009-05-11 21:44:30 tews Exp $

package ds.ov2.gui;


/** 
 * Actions for the bases selection dialog.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.7 $
 * @commitdate $Date: 2009-05-11 21:44:30 $ by $Author: tews $
 * @environment host
 */
public class Bases_actions {

    /**
     * 
     * Empty constructor.
     */
    public Bases_actions() {}


    /**
     * 
     * OK button action. Closes the dialog and marks {@link
     * Base_selection#finished_ok} with true.
     * 
     * @param bs the base selection dialog instance
     */
    public void ok_button(Base_selection bs) {
        System.out.println("bases selection ok");
        bs.dispose();
        bs.finished_ok = true;
    }


    /**
     * 
     * Dismiss button action. Close the dialog, leaving {@link
     * Base_selection#finished_ok} at false.
     * 
     * @param bs the base selection dialog instance
     */
    public void dismiss_button(Base_selection bs) {
        System.out.println("bases selection dismiss");
        bs.dispose();
    }
}
