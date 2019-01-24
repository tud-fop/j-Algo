package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.syndia.display.IDrawPanel;
import org.jalgo.module.ebnf.gui.wordalgorithm.events.WordExitListener;
import org.jalgo.module.ebnf.gui.wordalgorithm.events.WordSplitListener;
import org.jalgo.module.ebnf.gui.wordalgorithm.events.WordSynDiaListener;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import org.jalgo.module.ebnf.renderer.ReturnDiagram;
import org.jalgo.module.ebnf.renderer.WordAlgoRenderer;
import org.jalgo.module.ebnf.renderer.elements.RenderBase;
import org.jalgo.module.ebnf.renderer.elements.RenderBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderRepetition;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;
import org.jalgo.module.ebnf.renderer.wordalgorithm.Position;
import org.jalgo.module.ebnf.renderer.wordalgorithm.RenderEnd;
import org.jalgo.module.ebnf.renderer.wordalgorithm.ReturnAdress;

/**
 * This Panel contains a graphical representation of all SyntaxDiagrams. It
 * contains a <code>JScrollPanel</code>, so that the SyntaxDiagrams are
 * scrollable.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Observer, IDrawPanel,
		MouseWheelListener {

	// The Space between Border and the Diagrams.
	private final int BORDER_SPACE = 20;

	// The Y-Position of the last diagram drawn.
	// It's also the minimum height of mySynDiaPanel
	private int posY;

	// The Width of the widest diagram drawn.
	// It's also the minimum width of mySynDiaPanel
	private int posX;

	// The Size of the Font during rendering
	// (Default -1)
	private int fontSize = -1;

	// The SynDiaRenderer renders SyntaxDiagram. Needed to Update the
	// synDiaPanel.
	private WordAlgoRenderer wordAlgoRenderer;

	// The GuiController is needed from the Listeners
	private GuiController myGuiController;

	// Specifies, which SyntaxDiagrams uses which SynDiaPanel.
	private Map<String, SynDiaPanel> synDiaPanelMap;

	// Specifies, which SyntaxDiagrams contain which RenderElements.
	private Map<String, Map<RenderElement, SynDiaElem>> synDiaElemMap;

	// Contains all ReturnAdresses drawn.
	private Map<ReturnAdress, SynDiaPanel> retAdressMap;

	// Contains all BorderPanels to the SynDiaPanels. Needed during
	// JumpToDiagram.
	private List<SynDiaPanel> borderPanelList;

	// needed to Update the graphical view
	// (Just a copy of the WordAlgoModel).
	private WordAlgoModel myModel = null;

	private SynDiaSystem mySynDiaSystem;

	// Needed to print the actual Position in a diagram
	// during the algorithm. Must be Object because
	// position can be a SynDiaElem and also a SyntaxDiagram.
	private Object actualPosition;

	private Position actualRenderPosition = null;

	private SynDiaPanel actualPositionPanel = null;

	// Needed to get the Diagrams scrollable
	private JScrollPane scrollPane;

	// Contains all SyntaxDiagrams.
	private SynDiaSystemPanel mySynDiaPanel;

	// Specifies if returnAdresses can be removed
	private boolean returnAdressesDrawn = false;

	// Specifies if autoSize should be on
	private boolean autoSize = false;

	/**
	 * Construct a new <code>DrawPanel</code>.
	 * 
	 * @param aSynDiaSystem
	 *            The <code>SynDiaSystem</code> which should be drwan onto the
	 *            DrawPanel during initilization.
	 * @param myGuiController
	 *            The <code>GuiController</code> which should be used to
	 *            perform clicks on
	 *            <code>SynDiaElem/code>s and <code>Syntaxdiagram</code>s.
	 */
	public DrawPanel(SynDiaSystem aSynDiaSystem, GuiController myGuiController) {

		this.setLayout(null);
		this.setBackground(BACKGROUND_COLOR);

		// Draw Framborder
		this.setMinimumSize(new Dimension(Short.MAX_VALUE, 150));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " "
				+ Messages.getString("ebnf", "Border_Draw") + " ",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));

		this.myGuiController = myGuiController;

		mySynDiaSystem = aSynDiaSystem;

		setActualPosition();

		// paint the gui
		this.mySynDiaPanel = new SynDiaSystemPanel();
		scrollPane = new JScrollPane();
		init(aSynDiaSystem);
	}

	/**
	 * Initializes the Gui of the DrawPanel
	 * 
	 * @param aSynDiaSystem
	 *            The SynDiaSystem given to the Constructor.
	 */
	private void init(SynDiaSystem aSynDiaSystem) {

		// INIT **************************************************************
		initDiagrams(aSynDiaSystem);

		// SET **************************************************************

		// mySynDiaPanel must be at least as big as the Diagrams.
		this.mySynDiaPanel.setPreferredSize(new Dimension(posX, posY));
		// this.mySynDiaPanel.setSize(posX, posY);
		this.mySynDiaPanel.addMouseWheelListener(this);

		scrollPane.setViewportView(mySynDiaPanel);
		scrollPane.setBackground(this.getBackground());
		scrollPane.setVisible(true);
		// scrollPane.setPreferredSize(new Dimension(this.getVisibleRect().width
		// - DIFFERENCE, this.getVisibleRect().height - DIFFERENCE));
		// scrollPane.setPreferredSize(new Dimension(mySynDiaPanel.getWidth(),
		// mySynDiaPanel.getHeight()));

		// LAYOUT **************************************************************
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.add(scrollPane);

		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				wasResized();
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentShown(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	/**
	 * Creates and Initializes the graphical representation of the
	 * <code>SyntaxDiagram</code>s.
	 * 
	 * @param aSynDiaSystem
	 *            The <code>SynDiaSystem</code> which should be drawn.
	 */
	private void initDiagrams(SynDiaSystem aSynDiaSystem) {

		// Maps needed to repaint the Diagrams.
		synDiaPanelMap = new HashMap<String, SynDiaPanel>();

		synDiaElemMap = new HashMap<String, Map<RenderElement, SynDiaElem>>();

		retAdressMap = new HashMap<ReturnAdress, SynDiaPanel>();

		borderPanelList = new ArrayList<SynDiaPanel>();

		// Position of the first Diagram which is drawn should be the border
		// space
		posX = BORDER_SPACE;
		posY = BORDER_SPACE;

		// Get the names of all Diagrams
		List<String> diagramNameList = aSynDiaSystem.getLabelsOfVariables();
		// Print all Diagrams
		for (String aDiagramName : diagramNameList) {
			// Try to get the Diagram to each name and paint it
			try {
				// Each diagram gets it's own SynDiaPanel.
				SynDiaPanel aSynDiaPanel = new SynDiaPanel(this.getBackground());
				SyntaxDiagram aDiagram = aSynDiaSystem
						.getSyntaxDiagram(aDiagramName);
				paintDiagram(aSynDiaPanel, aDiagram);
				mySynDiaPanel.add(aSynDiaPanel);

				// The synDiaPanel is putted to the map to be found
				// again if the GUI has to be updated.
				synDiaPanelMap.put(aDiagramName, aSynDiaPanel);

				// each Diagram gets a BorderPanel which is a bit bigger than
				// the SynDiaPanel
				// The border Panel is used for MouseOver events during a jump
				// to a Diagram.
				int difference = 3;
				SynDiaPanel aBorderPanel = new SynDiaPanel(this.getBackground());
				aBorderPanel.setLocation(aSynDiaPanel.getX() - difference,
						aSynDiaPanel.getY() - difference);
				aBorderPanel.setSize(aSynDiaPanel.getWidth() + difference * 2,
						aSynDiaPanel.getHeight() + difference * 2);
				aBorderPanel.setVisible(true);
				// BorderPanel gets a Listener for JumpToDiagrams
				aBorderPanel.addMouseListener(new WordSynDiaListener(
						myGuiController, aDiagram));
				mySynDiaPanel.add(aBorderPanel);
				mySynDiaPanel.setComponentZOrder(aBorderPanel, mySynDiaPanel
						.getComponentCount() - 1);
				// Must be in List to be updated in Z-Order if jumpToDiagramm
				borderPanelList.add(aBorderPanel);
			} catch (ElementNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gets a Panel an draws a SyntaxDiagram onto it.
	 * 
	 * @param aSynDiaPanel
	 *            The <code>SynDiaPanel</code> the <code>SyntaxDiagram</code>
	 *<code>aDiagram</code>
	 *            should be drawn on.
	 * @param aDiagram
	 *            The <code>SyntaxDiagram</code> which should be drawn.
	 */
	private void paintDiagram(SynDiaPanel aSynDiaPanel, SyntaxDiagram aDiagram) {

		wordAlgoRenderer = new WordAlgoRenderer();

		// Set fontSite if not default.
		if (fontSize > -1) {
			wordAlgoRenderer.getRenderValues().setFontSize(fontSize);
		}

		// Try to get a RenderMap to the Diagram which should be drawn.
		ReturnDiagram renderMap = null;
		renderMap = wordAlgoRenderer.getRenderedDiagram(aDiagram);
		synDiaElemMap.put(aDiagram.getName(), renderMap.renderMap);

		// empty scrollPane
		aSynDiaPanel.removeAll();

		// Draw the Diagram Elements.
		List<RenderElement> lineBuffer = new ArrayList<RenderElement>();
		for (RenderElement re : renderMap.renderMap.keySet()) {
			if (!(re instanceof RenderTerminal)
					&& !(re instanceof RenderVariable)) {
				lineBuffer.add(re);
			} else {
				aSynDiaPanel.add(re);
				re.addMouseListener(new WordSynDiaListener(myGuiController,
						renderMap.renderMap.get(re)));
				re.setVisible(true);
				re.repaint();
			}
		}
		for (RenderElement re : lineBuffer) {
			aSynDiaPanel.add(re);
			re.setVisible(true);
			re.repaint();
			if (re instanceof RenderBranch || re instanceof RenderRepetition) {
				WordSplitListener aListener = new WordSplitListener(
						myGuiController, wordAlgoRenderer.getRenderValues(),
						renderMap.renderMap.get(re));
				re.addMouseListener(aListener);
				re.addMouseMotionListener(aListener);
			}
			if (re instanceof RenderBase) {
				RenderBase aRenderBase = (RenderBase) re;
				// draw End of Diagram
				int exitWidth = (int) Math.round(wordAlgoRenderer
						.getRenderValues().radius);
				RenderEnd diagramExit = new RenderEnd(exitWidth);
				diagramExit.setSize(exitWidth, renderMap.height);
				diagramExit.setLocation(aRenderBase.getWidth() - exitWidth, 0);
				diagramExit.setVisible(true);
				diagramExit.update();
				diagramExit.repaint();
				WordExitListener anExitListener = new WordExitListener(
						myGuiController, wordAlgoRenderer.getRenderValues(),
						aDiagram);
				diagramExit.addMouseListener(anExitListener);
				diagramExit.addMouseMotionListener(anExitListener);
				aSynDiaPanel.add(diagramExit);
			}
		}
		// Set Size and Position
		aSynDiaPanel.setLocation(BORDER_SPACE, posY);
		posY = posY + 2 * wordAlgoRenderer.getRenderValues().space
				+ renderMap.height;
		if (renderMap.width > posX) {
			posX = renderMap.width + BORDER_SPACE * 2;
		}

		aSynDiaPanel.setSize(renderMap.width + 40, renderMap.height);
		aSynDiaPanel.setVisible(true);

	}

	/**
	 * Used to repaint Diagrams which were allready drawn.
	 */
	private void repaintDiagrams(WordAlgoModel myModel) {

		// Remove old Position
		if (actualRenderPosition != null) {
			actualPositionPanel.remove(actualRenderPosition);
			actualPositionPanel.repaint();
			actualPositionPanel = null;
			actualRenderPosition = null;
		}

		// specifies, if position was allready drawn
		boolean positionDrawn = false;

		// Get all Diagrams
		SynDiaSystem aSynDiaSystem = myModel.getSynDiaSystem();

		// Get the names of all Diagrams
		List<String> diagramNameList = aSynDiaSystem.getLabelsOfVariables();

		// Interate over all Diagrams
		for (String aDiagramName : diagramNameList) {

			try {
				SyntaxDiagram aDiagram = aSynDiaSystem
						.getSyntaxDiagram(aDiagramName);
				// Get the name of each Diagram and paint the components
				// which schould be drawn.
				SynDiaPanel aSynDiaPanel = synDiaPanelMap.get(aDiagramName);

				Map<RenderElement, SynDiaElem> aRenderMap = synDiaElemMap
						.get(aDiagramName);

				// checkout if an Element like return adresses or a position
				// should be drawn (Should be if algrotithm is running or
				// finished).
				if (myModel.isAlgorithmRunning()
						|| myModel.isAlgorithmFinished()) {

					// Iterate over all Elements of each diagram.
					for (RenderElement re : aRenderMap.keySet()) {

						// If a return adress should be drawn, draw it
						if (!returnAdressesDrawn) {
							if (re instanceof RenderVariable) {
								Variable aVar = (Variable) aRenderMap.get(re);
								ReturnAdress anAdress = new ReturnAdress(
										myModel.getAdressNumber(aVar)
												.toString(),
										(RenderVariable) re, fontSize);
								aSynDiaPanel.add(anAdress);
								anAdress.update();
								anAdress.setVisible(true);
								aSynDiaPanel.setComponentZOrder(anAdress, 0);
								anAdress.repaint();
								anAdress
										.addMouseListener(new WordSynDiaListener(
												myGuiController, aRenderMap
														.get(re)));
								// Put the drawn adress in the AdressMap.
								retAdressMap.put(anAdress, aSynDiaPanel);
							}
						}

						// If a position should be drawn, draw it
						if (!positionDrawn) {
							// Checkout if the actualPosition is the Diagram
							// himself
							if (re instanceof RenderBase) {
								this.setActualPosition();
								if (actualPosition == aDiagram
										|| actualPosition == aDiagram.getRoot()
										|| actualPosition == null) {
									// Remove old Position
									if (actualRenderPosition != null) {
										actualPositionPanel
												.remove(actualRenderPosition);
									}
									actualRenderPosition = new Position(
											re,
											false,
											myModel.isPositionBehindElem(),
											wordAlgoRenderer.getRenderValues().font,
											false);
									actualPositionPanel = aSynDiaPanel;
									aSynDiaPanel.add(actualRenderPosition);
									actualRenderPosition.update();
									actualRenderPosition.setVisible(true);
									aSynDiaPanel.setComponentZOrder(
											actualRenderPosition, 0);
									actualRenderPosition.repaint();
									aSynDiaPanel.repaint();
									positionDrawn = true;
								}
							}
							// Is the actual Element also the actual Position in
							// the
							// Diagram?
							SynDiaElem actualElem = aRenderMap.get(re);
							if (actualElem != null
									&& actualElem == actualPosition) {
								// Needed to inform the Position, if the
								// Position
								// should be
								// behind the return Adress of the Variable
								boolean jumpOrNot = myModel.isJumpToDiagram();
								// Remove old Position
								if (actualRenderPosition != null) {
									actualPositionPanel
											.remove(actualRenderPosition);
								}
								boolean changeDirection = false;
								// Checkout if actual Position is a Repetition
								// or in a Repetition
								if (myModel.isPositionInRepetition()) {
									// If true get the Repetition and check its
									// order.
									changeDirection = !myModel
											.getFirstParentRepetition()
											.rightOrder();
								}
								actualRenderPosition = new Position(
										re,
										jumpOrNot,
										myModel.isPositionBehindElem(),
										wordAlgoRenderer.getRenderValues().font,
										changeDirection);
								actualPositionPanel = aSynDiaPanel;
								// Paint PositionElement
								aSynDiaPanel.add(actualRenderPosition);
								actualRenderPosition.update();
								actualRenderPosition.setVisible(true);
								aSynDiaPanel.setComponentZOrder(
										actualRenderPosition, 0);
								aSynDiaPanel.repaint();
								actualRenderPosition.repaint();
							}
						}
					}
				}
			}
			// If SyntaxDiagramm cannot be found, finish the word algorithm
			catch (ElementNotFoundException e) {
				System.out.println(e.getMessage());
				System.out
						.println("Error during paint of diagrams. Reswitch to SynDiaView");
				myGuiController.switchToSynDiaView();
			}
		}

		// If returnAdresses should'nt be drawn. Remove old Adresses
		if (!myModel.isAlgorithmRunning() && !myModel.isAlgorithmFinished()) {
			// Remove Adresses
			for (ReturnAdress aReturnAdress : retAdressMap.keySet()) {
				SynDiaPanel aSynDiaPanel = retAdressMap.get(aReturnAdress);
				aSynDiaPanel.remove(aReturnAdress);
				aSynDiaPanel.repaint();
			}
			retAdressMap.clear();
			returnAdressesDrawn = false;
		} else {
			returnAdressesDrawn = true;
		}
	}

	/**
	 * Method called by Observable if the DrawPanel should update its Gui.
	 */
	public void update(Observable anObservable, Object arg) {
		// Cast the Observable to WordAlgoModel and
		// update the parameters neede to paint the Panel.
		if (anObservable instanceof WordAlgoModel) {

			this.myModel = (WordAlgoModel) anObservable;
			setActualPosition();

			repaintDiagrams(myModel);

			// BorderPanels must be arranged in Z-Order if is jumpOrNot
			this.setDiagramZOrder(myModel, myModel.isJumpToDiagram());
		}
	}

	/**
	 * Sets actualPosition to the actual Position Needed to paint positions
	 */
	private void setActualPosition() {
		actualPosition = null;
		if (myModel != null) {
			// Addresses should be drawn if algorithm is already running.
			if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
				actualPosition = myModel.getPosition();
				// If it is a Concatenation
				if (actualPosition instanceof Concatenation) {
					Concatenation actualConcat = (Concatenation) actualPosition;
					// Checkout if parent is SyntaxDiagram or not
					if (actualConcat.getParent() == null) {
						actualPosition = actualConcat.getMySyntaxDiagram();
					} else {
						actualPosition = actualConcat.getParent();
					}
				}
			}
		}
	}

	/**
	 * A function called by an Observer to rearrange the system by size. In
	 * reality it only widen the system. To get a systen smaller, start with
	 * size = 10.
	 * 
	 * @param size
	 *            the size of the Font associated to the syntax diaram
	 */
	public int resizeSystem(int size, int smaller) {

		if (size > 9) {
			// Set FontSize
			this.fontSize = size;
		}

		// Reset SynDiaSystem and paint again
		mySynDiaPanel.removeAll();
		mySynDiaPanel.repaint();

		synDiaPanelMap = new HashMap<String, SynDiaPanel>();

		synDiaElemMap = new HashMap<String, Map<RenderElement, SynDiaElem>>();

		retAdressMap = new HashMap<ReturnAdress, SynDiaPanel>();

		this.init(mySynDiaSystem);

		if (myModel != null) {
			returnAdressesDrawn = false;
			this.repaintDiagrams(myModel);
		}

		// If autoSize is enabled, check if Diagrams should be drawn again.
		if (autoSize) {
			// if font size ist not set yet, set it
			if (size <= 0) {
				size = wordAlgoRenderer.getRenderValues().font.getSize();
			}

			// Calculate procentual Height and widht of the viewable part of the
			// Panel which is used by the diagrams.
			int procentualWidth = (int) Math
					.round(posX * 100 / this.getWidth());
			int procentualHeight = (int) Math.round(posY * 100
					/ this.getHeight());

			// Checkout if Frame is not filled at least to 80 per cent
			if ((procentualWidth < 80 && procentualHeight < 80) && smaller >= 0) {
				// Else draw again
				// save old fontSize
				int oldFontSize = size;
				// Checkout which direction uses more space
				if (procentualWidth > procentualHeight) {
					size = (int) Math.round(size * 100 / procentualWidth) - 1;
				} else {
					size = (int) Math.round(size * 100 / procentualHeight) - 1;
				}
				// If new size is the same as the old one, the algorithm stops.
				if (oldFontSize != size) {
					if (size <= 50) {
						resizeSystem(size, 1);
					}
				}
			}
		}

		return size;
	}

	/**
	 * This method sets the paramater autoSize deciding, whether the diagram
	 * size shall automatically fit to the screen size
	 * 
	 * @param autosize
	 *            true if AutoSize should be on
	 */
	public void setAutoSize(boolean autosize) {

		this.autoSize = autosize;
	}

	/**
	 * This method returns the actual size of the set font for rendering syntax
	 * diagrams
	 * 
	 * @return an int representing the font size
	 */
	public int getFontSize() {

		return wordAlgoRenderer.getRenderValues().font.getSize();

	}

	/**
	 * This method arranges the Z-Order of the BorderPanels. If onTop is true,
	 * the BorderPanels are on Top else they are in the background
	 * 
	 * @param myModel
	 *            The WordAlgoModel used during update
	 * @param onTop
	 *            If true, the BorderPanels are on Top else they are in the
	 *            background
	 */
	private void setDiagramZOrder(WordAlgoModel myModel, boolean onTop) {
		for (SynDiaPanel aBorderPanel : borderPanelList) {
			if (onTop) {
				mySynDiaPanel.setComponentZOrder(aBorderPanel, 0);
			} else {
				mySynDiaPanel.setComponentZOrder(aBorderPanel, mySynDiaPanel
						.getComponentCount() - 1);
			}
		}

	}

	/**
	 * Resizes the panel after resizing of jAlgo
	 * 
	 */
	private void wasResized() {
		JPanel contentPane = (JPanel) this.getParent().getParent();
		this.setPreferredSize(new Dimension(contentPane.getVisibleRect().width
				- myGuiController.STACK_WIDTH,
				contentPane.getVisibleRect().height));
	}

	/**
	 * Faster scrolling realised via this method
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		javax.swing.JScrollBar scrollbar = scrollPane.getVerticalScrollBar();

		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			int totalScrollAmount = e.getUnitsToScroll() * 4;
			scrollbar.setValue(scrollbar.getValue() + totalScrollAmount);
		}
	}

}
