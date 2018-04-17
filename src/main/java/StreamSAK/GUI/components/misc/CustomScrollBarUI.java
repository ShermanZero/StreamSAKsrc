package main.java.StreamSAK.GUI.components.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {

	private final Dimension d = new Dimension();

	  @Override
	  protected JButton createDecreaseButton(int orientation) {
		  return new JButton() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() { return d; }
		  };
	  }

	  @Override
	  protected JButton createIncreaseButton(int orientation) {
		  return new JButton() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() { return d; }
		  };
	  }

	  @Override
	  protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
		  Graphics2D g2 = (Graphics2D) g.create();
		  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		  g2.setPaint(Color.DARK_GRAY);
		  g2.fillRect(r.x, r.y, r.width, r.height);
		  g2.dispose();
	  }

	  @Override
	  protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		  Graphics2D g2 = (Graphics2D) g.create();
		  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		  
		  Color color = Color.WHITE;
		  if(isThumbRollover())
			  color = Color.LIGHT_GRAY;
		  
		  g2.setPaint(color);
		  g2.fillRoundRect(r.x, r.y, r.width, r.height, 5, 5);
		  g2.dispose();
	  }

	  @Override
	  protected void setThumbBounds(int x, int y, int width, int height) {
		  super.setThumbBounds(x, y, 7, height);
		  scrollbar.repaint();
	  }
}
