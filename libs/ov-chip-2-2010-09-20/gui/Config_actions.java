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
// Created 5.1.09 by Hendrik
// 
// actions for the config dialog
// 
// $Id: Config_actions.java,v 1.14 2009-06-22 21:09:39 tews Exp $

package ds.ov2.gui;


import java.io.File;
import java.security.NoSuchAlgorithmException;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ds.ov2.util.Card_terminal;


/**
 * Actions for the config window.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.14 $
 * @commitdate $Date: 2009-06-22 21:09:39 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Config_actions {

    /**
     * 
     * Default empty constructor.
     */
    Config_actions() {}


    //########################################################################
    //########################################################################
    // 
    // Card reader actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Generate an array of all connected PCSC card readers. 
     * 
     * @return list of all PCSC card terminals
     */
    public CardTerminal[] get_pcsc_readers() {
        CardTerminal[] pcsc_terminals = new CardTerminal[0];
        try {
            pcsc_terminals = 
                Card_terminal.get_all_pcsc_terminals().toArray(pcsc_terminals);
        }
        catch(NoSuchAlgorithmException e) {}
        catch(CardException e) {}

        return pcsc_terminals;
    }


    /**
     * 
     * Generate an array of all accessible jcop emulators. Returns an
     * empty array if the appropriate libraries are not present at
     * runtime.
     * 
     * @return list of all accessible jcop emulators
     */
    public CardTerminal[] get_jcop_readers() {
        int[] ports = new int[Gui_state.jcop_ports.size()];
        for(int i = 0; i < ports.length; i++)
            ports[i] = Gui_state.jcop_ports.get(i);

        CardTerminal[] jcop_emulators = new CardTerminal[0];
        try {
            jcop_emulators =
                Card_terminal.get_all_jcop_emulators(ports)
                    .toArray(jcop_emulators);
        }
        catch(NoSuchAlgorithmException e) {}
        catch(CardException e) {}

        return jcop_emulators;     
    }


    /**
     * 
     * Generate an array of all connected card readers. Used to
     * initialize the model of the card terminal combo box. Collects
     * all the PC/SC terminals and adds an entry for the jcop emulator
     * at the end if the appropriate libraries are present at runtime.
     * 
     * @return list of all card terminals
     */
    public CardTerminal[] get_all_readers() {
        CardTerminal[] pcsc_terminals = get_pcsc_readers();
        CardTerminal[] jcop_emulators = get_jcop_readers();

        CardTerminal[] res = new CardTerminal[pcsc_terminals.length +
                                              jcop_emulators.length];

        System.arraycopy(pcsc_terminals, 0, res, 0, pcsc_terminals.length);
        System.arraycopy(jcop_emulators, 0, res, pcsc_terminals.length,
                         jcop_emulators.length);
        return res;     
    }


    /**
     * 
     * Choose a default reader among all connected ones. Used during
     * initialization in {@link Gui_actions#default_initialization
     * Gui_actions.default_initialization}. If no reader can be found
     * (because no real reader is connected and the libraries for the
     * jcop emulator are not present) display an error message and
     * exit the program.
     * 
     * @param gui the GUI window
     * @return default card reader
     */
    public CardTerminal get_default_reader(Ov_demo_gui gui) {
        CardTerminal[] terminals;

        if(Gui_state.default_jcop.ref) {
            terminals = get_jcop_readers();
        }
        else {
            terminals = get_all_readers();
        }

        if(terminals != null && 
           terminals.length > Gui_state.default_reader_number.ref) 
            {
                return terminals[Gui_state.default_reader_number.ref];
            }

        JOptionPane.showMessageDialog(gui,
            "Cannot select a suitable default Card Terminal!\n" +
            "Exit now.",
            "Default terminal selection error",
            JOptionPane.ERROR_MESSAGE);

        System.exit(1);
        return null;
    }


    /**
     * 
     * Method to be called when the OK button is pressed in the config
     * dialog. 
     * 
     * @param cw the config window
     */
    public void ok_button(Config_window cw) {
        System.out.println("config ok");
        cw.dispose();
        cw.finished_ok = true;
    }


    /**
     * 
     * Method to be called when the dismiss button is pressed in the
     * config dialog. 
     * 
     * @param cw the config window
     */
    public void dismiss_button(Config_window cw) {
        System.out.println("config dismiss");
        cw.dispose();
    }



    //########################################################################
    //########################################################################
    // 
    // Applet actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Method to be called when one of the browse buttons for the
     * applet cap files in the config window is pressed. Displays an
     * appropriate file chooser and records the result.
     * 
     * @param cw the config window
     * @param applet_name_text_field the text field to be modified as
     * result 
     */
    void applet_browse_button(Config_window cw,
                              JTextField applet_name_text_field) 
    {
        System.out.println("browsing for applet");

        String applet_name = applet_name_text_field.getText();
        File applet = new File(applet_name);
        Gui_state.out.format("init file chooser with %s\n", applet.getPath());
        final JFileChooser fc = new JFileChooser(applet);

        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter("Applet CAP Files", "cap");
        fc.setFileFilter(filter);

        int ret = fc.showOpenDialog(cw);

        if(ret == JFileChooser.APPROVE_OPTION) {
            String new_name = fc.getSelectedFile().getPath();
            Gui_state.out.format("Selected applet %s\n", new_name);
            applet_name_text_field.setText(new_name);
        }
    }
}