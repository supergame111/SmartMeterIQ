//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/Gui_protocol_messages.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/gui/Gui_protocol_messages.java"
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
// Created 7.1.09 by Hendrik
// 
// progress messages for the GUI
// 
// $Id: Gui_protocol_messages.java,v 1.9 2009-06-16 08:16:06 tews Exp $

package ds.ov2.gui;


import java.math.BigInteger;
import javax.swing.JTextArea;
import ds.ov2.front.RSA_protocol_messages;


/** 
 * Display various progress messages in the graphical demonstrator.
 * All the progress methods in here are called from deep within the
 * terminal thread. The methods must therefore not directly access the
 * GUI but rather {@link Terminal_thread#publish publish} suitable GUI
 * actions.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2009-06-16 08:16:06 $ by $Author: tews $
 * @environment host
 */
public class Gui_protocol_messages implements RSA_protocol_messages {

    /**
     * 
     * The Terminal thread that is used to publish the progress
     * messages to the GUI.
     */
    private final Terminal_thread terminal_thread;

    /**
     * 
     * Constructor. 
     * 
     * @param terminal_thread terminal thread instance to publish the
     * GUI progress messages
     */
    public Gui_protocol_messages(Terminal_thread terminal_thread)
    {
        this.terminal_thread = terminal_thread;
    }


    /**
     * 
     * Called when starting to install an applet on a card (in {@link
     * Card_protocols#reinstall_personalize
     * Card_protocols.reinstall_personalize}).
     * 
     * @param name name of the applet
     */
    public void start_install(String name) {
        terminal_thread.gui_progress_message("install applet " + name + "\n",
                                           false);
    }



    /**
     * 
     * Called when applet loading finished during applet installation
     * (in {@link Card_protocols#install_applet_with_gps
     * Card_protocols.install_applet_with_gps}).
     */
    public void load_applet_finished() {
        terminal_thread.gui_progress_message("applet loading finished\n", false);
    }


    /**
     * 
     * Called before starting to delete an applet or a package (in
     * {@link Card_protocols#delete_applets
     * Card_protocols.delete_applets}). 
     * 
     * @param name the name of the applet/package
     */
    public void delete_applet(String name) {
        terminal_thread.gui_progress_message("delete " + name + "\n", false);
    }


    /**
     * 
     * Called when applet/package deletion failed (in {@link
     * Card_protocols#delete_applets Card_protocols.delete_applets}).
     * 
     * @param name the name of the applet/package
     */
    public void delete_applet_failure(String name) {
        terminal_thread.gui_progress_message("Deletion of " +
                                           name + " failed\n",
                                           false);
    }





    //########################################################################
    //########################################################################
    // 
    // Progress methods specified in RSA_protocol_messages
    // 
    //########################################################################
    //########################################################################

    /**
     * 
     * Called when initialization starts.
     */
    public void initialize_start() {
        terminal_thread.gui_progress_message("start card initialization\n",
                                           false);
    }


    /**
     * 
     * Called before the allocate call to the applet starts.
     */
    public void initialize_allocate_start() {
        terminal_thread.gui_progress_message("start allocation on the card\n",
                                           false);
    }


    /**
     * 
     * Called when the allocate call is finished.
     */
    public void initialize_allocate_finished() {
        terminal_thread.gui_progress_message("finished allocation on the card\n",
                                           false);
    }


    /**
     * 
     * Called when the initialize call to the applet completed and all
     * data has been copied to the applet.
     * 
     * @param blinded_a the blinded attribute expression of the new
     * applet
     */
    public void initialize_data_copied(BigInteger blinded_a) {
        terminal_thread.gui_progress_message("system parameters copied\n",
                                           false);
        terminal_thread.gui_card_message(blinded_a, "initialize applet");
    }


    /**
     * 
     * Called before the start of the resign protocol that is part of
     * applet initialization.
     */
    public void initialize_resign() {
        terminal_thread.gui_progress_message
            ("start resign protocol to complete personalization\n", false);
    }


    /**
     * 
     * Called when applet initialization is finished.
     */
    public void initialize_finished() {
        terminal_thread.gui_progress_message("finished card initialization\n",
                                           false);
    }


    /**
     * 
     * Called when the resign protocol starts. Displays the progress
     * message either in the office or the automaton tab, depending on
     * whether this resign is part of applet initialization or not. 
     */
    public void resign_start() {
        String mes = "start resign protocol\n";
        terminal_thread.gui_progress_message(mes, false);
    }

    /**
     * 
     * Called when the host received the attribute expression and the
     * signature from the appled during the resign protocol. Displays
     * the progress message either in the office or the automaton tab,
     * depending on whether this resign is part of applet
     * initialization or not.
     * 
     * @param blinded_a the blinded attribute expression of the applet
     */
    public void resign_got_signature(BigInteger blinded_a) {
        String mes = "got old signature from the card\n";
        terminal_thread.gui_progress_message(mes, false);
        terminal_thread.gui_card_message(blinded_a, "obtain new blinding");
    }


    /**
     * 
     * Called when the signature of the applet was accepted during the
     * resign protocol. Displays the progress message either in the
     * office or the automaton tab, depending on whether this resign
     * is part of applet initialization or not.
     * 
     * @param checked_signature whether the signature was actually
     * checked
     */
    public void resign_checked_signature(boolean checked_signature) {
        String mes;
        if(checked_signature)
            mes = "signature checked\n";
        else
            mes = "skip signature check\n";

        terminal_thread.gui_progress_message(mes, false);
    }


    /**
     * 
     * Called when the host received the challenge from the applet
     * during the resign protocol. Displays the progress message
     * either in the office or the automaton tab, depending on whether
     * this resign is part of applet initialization or not.
     */
    public void resign_got_challenge() {
        String mes = "got challenge from card\n";
        terminal_thread.gui_progress_message(mes, false);
    }


    /**
     * 
     * Called when the resign protocol has been finished. Displays the
     * progress message either in the office or the automaton tab,
     * depending on whether this resign is part of applet
     * initialization or not.
     * 
     * @param accepted true if the card accepted the new signature
     */
    public void resign_finished(boolean accepted) {
        String mes;
        if(accepted)
            mes = "resign completed successfully\n";
        else
            mes = "resign failed: card refused\n";

        terminal_thread.gui_progress_message(mes, false);
    }


    /**
     * 
     * Called when the entry-gate proof protocol starts.
     */
    public void entry_gate_start() {
        terminal_thread.gui_progress_message("start entry gate protocol\n",
                                          false);
    }


    /**
     * 
     * Called when the host received the commitment from the applet
     * during the proof protocol.
     * 
     * @param signature_ok true if the applet produced a valid
     * signature 
     * @param blinded_a the blinded attribute expression of the new
     * applet
     */
    public void entry_gate_committed(boolean signature_ok,
                                     BigInteger blinded_a)
    {
        terminal_thread.gui_progress_message("got card commitment\n", false);
        terminal_thread.gui_card_message(blinded_a, "entry gate");
    }


    /**
     * 
     * Called when the host received the response from the applet for
     * its challenge during the proof protocol.
     */
    public void entry_gate_got_response() {
        terminal_thread.gui_progress_message("got card response\n", false);
    }


    /**
     * 
     * Called when the entry-gate proof protocol has been finished.
     * 
     * @param result true if the response from the applet was valid
     * and the proof is therefore accepted
     */
    public void entry_gate_finished(boolean result) {
        if(result)
            terminal_thread.gui_progress_message("response is OK\n", false);
        else
            terminal_thread.gui_progress_message("response is wrong\n", false);
    }


}
