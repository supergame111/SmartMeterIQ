/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aod.smartmeterdevice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Main {

    public static void main(String[] args) {
        HttpClientFrame f = new HttpClientFrame();
        f.setTitle("SmartMeter Simulator");
        f.setSize(700, 500);
        f.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
        );
        f.setVisible(true);
    }

    
    public static class HttpClientFrame extends JFrame implements Simulator.Callback {
        private Simulator simulator = new Simulator();
        private DefaultTableModel model = new DefaultTableModel();

        public HttpClientFrame() {
            JPanel panInput = new JPanel(new FlowLayout());

            final JButton btnSTART = new JButton("START");
            btnSTART.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        simulator.enable();
                    }
                }
            );

            final JButton btnRESTART = new JButton("RESTART");
            btnRESTART.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        simulator.reset();
                    }
                }
            );

            final JButton btnSTOP = new JButton("STOP");
            btnSTOP.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        simulator.disable();
                    }
                }
            );

            panInput.add(btnSTART);
            panInput.add(btnRESTART);
            panInput.add(btnSTOP);

            JTable table = new JTable(model);
            JScrollPane tablePane = new JScrollPane(table);
            table.setFillsViewportHeight(true);

            model.addColumn("Time Slot");
            model.addColumn("Reading Value");
            model.addColumn("Commitment");
            model.addColumn("Randomness");

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(panInput, BorderLayout.NORTH);
            this.getContentPane().add(tablePane, BorderLayout.CENTER);

            this.simulator.setCallback(this);
            this.simulator.start();
        }

        public void handle(Reading reading) {
            model.addRow(new Object[]
                {
                    reading.getTimeSlot(),
                    reading.getValue(),
                    reading.getCommitment(),
                    reading.getRandomness()
                });
        }
    }
}