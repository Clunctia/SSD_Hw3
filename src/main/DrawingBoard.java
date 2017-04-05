package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter; 
	private List<GObject> gObjects;
	private List<GObject> target;

	private int gridSize = 10;

	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		target = new ArrayList<GObject>();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}

	public void addGObject(GObject gObject) {
		// TODO: Implement this method.
		gObjects.add(gObject);
		repaint();
	}

	public void groupAll() {
		// TODO: Implement this method.
		CompositeGObject group = new CompositeGObject();
		for(GObject go : gObjects){
			group.add(go);
		}
		group.recalculateRegion();
		clear();
		gObjects.add(group);
		repaint();

	}

	public void deleteSelected() {
		// TODO: Implement this method.
		for (GObject go : target) {
			gObjects.remove(go);
		}
		repaint();

	}

	public void clear() {
		// TODO: Implement this method.
		gObjects.clear();
		target.clear();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {

		// TODO: You need some variables here
		int eX, eY;
		boolean object = false, drag = false, move = false;

		private void deselectAll() {
			// TODO: Implement this 
			for(GObject go : gObjects){
				go.deselected();
			}
			target.clear();
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO: Implement this method.
			eX = e.getX();
			eY = e.getY();
			object = false;
			for(GObject go : gObjects){
				if(go.pointerHit(eX, eY)){
					object = true;
					if(!target.contains(go)){
						deselectAll();
						target.clear();
						go.selected();
						target.add(go);
						repaint();
					}
					break;
				}
			}
			if(!object){
				deselectAll();
				target.clear();
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO: Implement this method.
			drag = true;
			int x,y;
			if(object){
				move = true;
				x = e.getX();
				y = e.getY();
				for(GObject go : target){
					go.move(x-eX, y-eY);
				}
				eX = x;
				eY = y;
				repaint();
			} else {
				move = false;
				x = e.getX();
				y = e.getY();
				repaint();
				Graphics g = getGraphics();
				g.setColor(Color.MAGENTA);
				Graphics2D g2D = (Graphics2D) g;
				g2D.setStroke(new BasicStroke(5));

				if(eX <= x && eY <= y)
					g2D.drawRect(eX,eY,x-eX,y-eY);
				else if(eX > x && eY <= y)
					g2D.drawRect(x, eY, eX-x, y-eY);
				else if(eX > x && eY <= y)
					g2D.drawRect(eX, y, x-eX,eY-y);
				else
					g2D.drawRect(x, y, eX-x, eY-y);

			}
		}

		@Override
		public void mouseReleased(MouseEvent e){
			int x, y;
			if(drag &&!move){
				deselectAll();
				target.clear();
				x = e.getX();
				y = e.getY();
				for(GObject go : gObjects){
					if(go.covered(eX, eY, x, y)){
						target.add(go);
						go.selected();
					}
				}
				repaint();
			}
		}
	}

}