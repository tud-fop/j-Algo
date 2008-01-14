package org.jalgo.module.hoare.gui;

import javax.swing.text.JTextComponent;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.model.AndAssertion;
import org.jalgo.module.hoare.model.Assertion;
import org.jalgo.module.hoare.model.AssignVF;
import org.jalgo.module.hoare.model.IfVF;
import org.jalgo.module.hoare.model.NotAssertion;
import org.jalgo.module.hoare.model.StrongPreVF;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.model.WeakPostVF;

public class HTMLRenderer implements Renderer {

	/**
	 * für <a href="ID:Class" class="Class"> bzw <... class="Class">
	 */
	static String PreClass = "pre";

	static String PostClass = "post";

	static String VFClass = "vf";

	static String ActiveClass = "active";

	static String ImplClass = "impl";

	static String DescClass = "desc";

	static String LeafClass = "leaf";

	static boolean initialised = false;

	private GuiControl gui;

	/**
	 * Klasse, welche je nach vf-art den knoten rendert
	 * 
	 * @author Peter
	 * 
	 */

	private class VFDrawer {

		public final void render(StringBuffer result, VerificationFormula vf,
				Node presAttribs) {

			renderPre(result, vf);
			result.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			renderVF(result, vf, presAttribs);
			result.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			renderPost(result, vf);

			renderIcon(result, vf, gui.getNode(vf.getID()));
		}

		public void renderIcon(StringBuffer result, VerificationFormula vf,
				Node presAttribs) {
			// icon
			switch (presAttribs.getStatus()) {
			case ResultOK:
				result.append("<img src='"
						+ Messages.getResourceURL("hoare", "icon.resultOk")
						+ "' width=16 height=16>");
				break;
			case ResultWrong:
				result.append("<img src='"
						+ Messages.getResourceURL("hoare", "icon.resultWrong")
						+ "' width=16 height=16>");
				break;
			case Error:
				result.append("<img src='"
						+ Messages.getResourceURL("hoare", "icon.evalError")
						+ "' width=16 height=16>");
				break;
			case Evaluating:
				result.append("<img src='"
						+ Messages.getResourceURL("hoare", "icon.evaluating")
						+ "' width=16 height=16>");
				break;
			default:
				break;
			}
		}

		protected void renderPost(StringBuffer result, VerificationFormula vf) {
			result.append("<a href='" + vf.getID() + ":"
					+ HTMLRenderer.PostClass + "' class='"
					+ HTMLRenderer.PostClass + "'>{");

			result.append(vf.getPostAssertion().getHTMLString() + "}</a>");

			if (!((vf instanceof WeakPostVF) || (vf instanceof StrongPreVF))) {
				// damit die impl in dem kind der if-regel gemalt wird
				VerificationFormula parent = vf.getParent();
				if (parent != null)
					parent = parent.pureVF();
				if (parent instanceof IfVF) {
					Assertion tmp = new AndAssertion(vf.getParent()
							.getPreAssertion(), new NotAssertion(
							((IfVF) parent).getPi()));

					result
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					result.append("<a href='" + vf.getID() + ":"
							+ HTMLRenderer.VFClass + "' class='"
							+ HTMLRenderer.ImplClass + "'>");
					result.append(tmp.getHTMLString() + "=>"
							+ vf.getPostAssertion().getHTMLString()
							+ "</a>&nbsp;");

				}
			}

		}

		protected void renderVF(StringBuffer result, VerificationFormula vf,
				Node presAttribs) {
			result.append("<a href='" + vf.getID() + ":" + HTMLRenderer.VFClass
					+ "' class='" + HTMLRenderer.VFClass + "'>");
			if (presAttribs.isShowLabel())
				result.append(presAttribs.getLabel());
			else
				// < und > ersetzten

				result.append(gui.getCodeSegment(vf.getID()).replaceAll("<",
						"&lt;").replaceAll(">", "&gt;"));

			result.append("</a>");
		}

		protected void renderPre(StringBuffer result, VerificationFormula vf) {
			result.append("<a href='" + vf.getID() + ":"
					+ HTMLRenderer.PreClass + "' class='"
					+ HTMLRenderer.PreClass + "'>{");
			result.append(vf.getPreAssertion().getHTMLString() + "}</a>");

		}
	}

	private class StrongPreDrawer extends VFDrawer {
		/**
		 * renders parent.pre=> pre pre vf post
		 */
		protected void renderPre(StringBuffer result, VerificationFormula vf) {

			result.append("<a href='" + vf.getID() + ":" + HTMLRenderer.VFClass
					+ "' class='" + HTMLRenderer.ImplClass + "'>");

			result.append(vf.getParent().getPreAssertion().getHTMLString()
					+ "=>" + vf.getPreAssertion().getHTMLString() + "</a>");

			result
					.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

			result.append("<a href='" + vf.getID() + ":"
					+ HTMLRenderer.PreClass + "' class='"
					+ HTMLRenderer.PreClass + "'>{");
			result.append(vf.getPreAssertion().getHTMLString() + "}</a>");

		}
	}

	private class WeakPostDrawer extends VFDrawer {
		/**
		 * renders pre vf post post=> parent.post
		 */
		protected void renderPost(StringBuffer result, VerificationFormula vf) {
			result.append("<a href='" + vf.getID() + ":"
					+ HTMLRenderer.PostClass + "' class='"
					+ HTMLRenderer.PostClass + "'>{");
			result.append(vf.getPostAssertion().getHTMLString() + "}</a>");

			result
					.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

			result.append("<a href='" + vf.getID() + ":" + HTMLRenderer.VFClass
					+ "' class='" + HTMLRenderer.ImplClass + "'>");
			result.append(vf.getPostAssertion().getHTMLString() + "=>"
					+ vf.getParent().getPostAssertion().getHTMLString()
					+ "</a>&nbsp;");
		}
	}

	private VFDrawer getVFDrawer(Class<? extends VerificationFormula> c) {

		if (c.equals(StrongPreVF.class))
			return new StrongPreDrawer();
		if (c.equals(WeakPostVF.class))
			return new WeakPostDrawer();

		return new VFDrawer();
	}

	public HTMLRenderer(GuiControl gui) {
		this.gui = gui;
	}

	/**
	 * helper to find the truly parent, not the inserted one
	 * 
	 * @param vf
	 * @return the parent
	 */
	// private VerificationFormula getPureParent(VerificationFormula vf) {
	// if ((vf instanceof WeakPostVF)||(vf instanceof StrongPreVF))
	// return getPureParent(vf.getParent());
	// else
	// return vf;
	// }
	protected void createHTMLStructure(StringBuffer buffer,
			VerificationFormula vf) {

		if (vf.getChildren().size() > 0) {
			buffer.append("<td><table><tr valign='bottom'>");
			boolean drawRuleDesc = false;
			VerificationFormula conseqRule = null;
			for (int i = 0; i < vf.getChildren().size(); ++i) {
				if (gui.getNode(vf.getChildren().get(i).getID()).isVisible()) {
					createHTMLStructure(buffer, vf.getChildren().get(i));
					drawRuleDesc = true;
					if ((vf.getChildren().get(i) instanceof WeakPostVF)
							|| (vf.getChildren().get(i) instanceof StrongPreVF))
						conseqRule = vf.getChildren().get(i);
				}
			}
			if (drawRuleDesc) {
				if (conseqRule != null)
					buffer.append("<td width=5%>"
							+ Messages.getString("hoare", "rule." + conseqRule
									+ ".desc") + "</td>");
				else
					buffer.append("<td width=5%>"
							+ Messages.getString("hoare", "rule." + vf.pureVF()
									+ ".desc") + "</td>");
			}

			buffer.append("</tr><tr>");
		}

		if (vf.getChildren().size() == 2)
			buffer.append("<td colspan='3' ");
		else if (vf.getChildren().size() == 1)
			buffer.append("<td colspan='2' ");
		else
			buffer.append("<td ");

		buffer.append(" valign='bottom' nowrap='nowrap'>");

		if (vf.getChildren().size() > 0)
			buffer.append("<hr align='left' width='95%'>");

		if (gui.getActiveNode() == vf.getID())
			buffer.append("<span class='" + HTMLRenderer.ActiveClass + "'>");

		if (vf.isRuleApplied() && !vf.hasChildren()
				&& vf.getType().equals(AssignVF.class))
			buffer.append("<b>");

		getVFDrawer(vf.getClass()).render(buffer, vf, gui.getNode(vf.getID()));

		if (vf.isRuleApplied() && !vf.hasChildren()
				&& vf.getType().equals(AssignVF.class))
			buffer.append("</b>");

		if (gui.getActiveNode() == vf.getID())
			buffer.append("</span>");

		buffer.append("</td>");

		if (vf.getChildren().size() > 0)
			buffer.append("</tr></table></td>");

	}

	public void render(VerificationFormula vf, JTextComponent component) {
		StringBuffer buffer = new StringBuffer();
		// puffer, damit der baum unten steht
		buffer.append("<table><tr valign='bottom'>");
		createHTMLStructure(buffer, vf);
		buffer.append("</tr></table>");
		component.setText(buffer.toString());
	}

}