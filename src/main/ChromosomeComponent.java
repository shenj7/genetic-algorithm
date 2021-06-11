package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ChromosomeComponent extends JComponent{
	private Chromosome chrom;
	private JButton[] geneBtns;
	
	public ChromosomeComponent(Chromosome c, JButton[] btns) {
		this.chrom = c;
		this.geneBtns = btns;
//		setPreferredSize(new Dimension((c.WIDTH+1)*10, (c.WIDTH+1)*10));//wrong/arbitrary?
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		chrom.drawOn(g2d);
	}
	
	public void updateButtons() {
		for(int i=0;i<chrom.getGenome().length;i++) {
			if(chrom.getGenome()[i]==1) {
				geneBtns[i].setBackground(Color.GREEN);
				geneBtns[i].setSelected(true);
			}
			else {
				geneBtns[i].setBackground(Color.BLACK);
				geneBtns[i].setSelected(true);
			}
		}
	}

	public Chromosome getChrom() {
		return chrom; 
	}
	
	public void setChrom(Chromosome c) {
		chrom = c; 
	}

	public JButton[] getGeneBtns() {
		return geneBtns;
	}

	public void setGeneBtns(JButton[] geneBtns) {
		this.geneBtns = geneBtns;
	}

}

