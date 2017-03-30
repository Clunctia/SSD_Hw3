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
	private GObject target;

	private int gridSize = 10;

	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
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
		for(GObject gObject : gObjects){
			group.add(gObject);
		}
		group.recalculateRegion();
		clear();
		gObjects.add(group);
		repaint();

	}

	public void deleteSelected() {
		// TODO: Implement this method.
		if(target != null){
			gObjects.remove(target);
			repaint();
		}
		
	}

	public void clear() {
		// TODO: Implement this method.
		gObjects.clear();
		target = null;
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
		
		private void deselectAll() {
			// TODO: Implement this 
			for(GObject go : gObjects){
				go.deselected();
			}
			target = null;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO: Implement this method.
			eX = e.getX();
			eY = e.getY();
			deselectAll();
			target = null;
			for(GObject go : gObjects){
				if(go.pointerHit(eX, eY)){
					go.selected();
					target = go;
					repaint();
					break;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO: Implement this method.
			if(target != null){
				int x = e.getX();
				int y = e.getY();
				target.move(x - eX, y - eY);
				eX = x;
				eY = y;
				repaint();
			}
		}
	}

}