
package org.jalgo.tests.ebnf.ebnfinput.HeaderViewPanel;

import javax.swing.JPanel;

public class HeaderViewPanel extends JPanel {
    
    /** Creates new form HeaderViewPanel */
    public HeaderViewPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        HeaderViewlPanel = new javax.swing.JPanel();
        headerLabel1 = new javax.swing.JLabel();
        headerLabel2 = new javax.swing.JLabel();
        headerLabel3 = new javax.swing.JLabel();

        HeaderViewlPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerLabel1.setFont(new java.awt.Font("Tahoma", 0, 16));
        headerLabel1.setText("E = (V, \u03a3, <SimpleExpression>, R)");

        headerLabel2.setFont(new java.awt.Font("Tahoma", 0, 16));
        headerLabel2.setText("V = {<SimpleExpression>, <Term>, <Factor>}");

        headerLabel3.setFont(new java.awt.Font("Tahoma", 0, 16));
        headerLabel3.setText("\u03a3 = {<Ident>, <Number>, +, -, *, /, (, )}");

        org.jdesktop.layout.GroupLayout HeaderViewlPanelLayout = new org.jdesktop.layout.GroupLayout(HeaderViewlPanel);
        HeaderViewlPanel.setLayout(HeaderViewlPanelLayout);
        HeaderViewlPanelLayout.setHorizontalGroup(
            HeaderViewlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(HeaderViewlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(HeaderViewlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(headerLabel1)
                    .add(headerLabel2)
                    .add(headerLabel3))
                .addContainerGap(1141, Short.MAX_VALUE))
        );
        HeaderViewlPanelLayout.setVerticalGroup(
            HeaderViewlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(HeaderViewlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(headerLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(headerLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(headerLabel3)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1496, Short.MAX_VALUE)
            .add(HeaderViewlPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 121, Short.MAX_VALUE)
            .add(HeaderViewlPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HeaderViewlPanel;
    private javax.swing.JLabel headerLabel1;
    private javax.swing.JLabel headerLabel2;
    private javax.swing.JLabel headerLabel3;
    // End of variables declaration//GEN-END:variables
    
}
