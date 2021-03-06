package org.geogebra.web.web.gui;

import java.util.ArrayList;

import org.geogebra.common.gui.SetLabels;
import org.geogebra.common.main.Feature;
import org.geogebra.common.main.Localization;
import org.geogebra.common.util.Language;
import org.geogebra.common.util.Unicode;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Dialog for language switching
 *
 */
public class LanguageGUI extends MyHeaderPanel implements SetLabels {

	/**
	 * App
	 */
	final AppW app;
	private LanguageHeaderPanel header;
	private Label activeLanguage = new Label();
	private FlowPanel fp = new FlowPanel();
	private ArrayList<Label> labels;
	private int cols;

	/**
	 * @param app
	 *            application
	 */
	public LanguageGUI(AppW app) {
		this.app = app;
		this.setStyleName("languageGUI");
		addHeader();
		addContent();
	}

	private void addContent() {
		fp.setStyleName("contentPanel");

		labels = new ArrayList<Label>();
		cols = estimateCols();
		for (Language l : Language.values()) {
			if (!l.fullyTranslated && app.has(Feature.ALL_LANGUAGES)) {
				continue;
			}

			StringBuilder sb = new StringBuilder();

			String text = l.name;

			if (text != null) {

				char ch = text.toUpperCase().charAt(0);
				if (ch == Unicode.LeftToRightMark
				        || ch == Unicode.RightToLeftMark) {
					ch = text.charAt(1);
				} else {
					// make sure brackets are correct in Arabic, ie not )US)
					sb.setLength(0);
					sb.append(Unicode.LeftToRightMark);
					sb.append(text);
					sb.append(Unicode.LeftToRightMark);
					text = sb.toString();
				}

				final Label label = new Label(text);
				final Language current = l;

				if (current.localeGWT.equals(app.getLocalization()
				        .getLocaleStr())) {
					this.activeLanguage = label;
					activeLanguage.addStyleName("activeLanguage");
				}
				label.addClickHandler(getHandler(current, label));
				labels.add(label);
			}
		}
		placeLabels();

		this.setContentWidget(fp);
	}

	private void placeLabels() {
		int rows = (int) Math.ceil(labels.size() / (double) cols);
		for (int i = 0; i < rows * cols; i++) {
			int col = i % cols;
			int row = i / cols;
			if (col * rows + row < labels.size()) {
				fp.add(labels.get(col * rows + row));
			} else {
				// filler -- in last column we may need to skip some lines
				fp.add(new Label("\u00A0"));
			}
		}

		FlowPanel clear = new FlowPanel();
		clear.setStyleName("clear");
		fp.add(clear);

	}

	@Override
	public void onResize() {
		int newCols = estimateCols();
		if (newCols != cols) {
			cols = newCols;
			fp.clear();
			placeLabels();
		}
		super.onResize();
	}

	private int estimateCols() {
		int width = fp.getOffsetWidth(); // this one does not include scrollbar
		if (width == 0) {
			width = (int) app.getWidth(); // incl. scrollbar, but maybe fp not
											// attached yet
		}
		return Math.max(1, (width - 40) / 350);
	}

	private ClickHandler getHandler(final Language current, final Label label) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean newDirRTL = Localization
				        .rightToLeftReadingOrder(current.localeGWT);

				app.getLAF().storeLanguage(current.localeGWT);
				if (app.getLoginOperation().isLoggedIn()) {
					app.getLoginOperation()
					        .getGeoGebraTubeAPI()
					        .setUserLanguage(
					                current.localeGWT,
					                app.getLoginOperation().getModel()
					                        .getLoginToken());
				}

				app.setUnsaved();

				// On changing language from LTR/RTL the page will
				// reload.
				// The current workspace will be saved, and load
				// back after page reloading.
				// Otherwise only the language will change, and the
				// setting related with language.
				if (newDirRTL != app.getLocalization().rightToLeftReadingOrder) {
					// TODO change direction
				}
				app.setLanguage(current.localeGWT);
				LanguageGUI.this.setActiveLabel(label);
				LanguageGUI.this.close();
			}
		};
	}

	/**
	 * @param label
	 *            label to mark as active
	 */
	protected void setActiveLabel(Label label) {
		activeLanguage.removeStyleName("activeLanguage");
		activeLanguage = label;
		activeLanguage.addStyleName("activeLanguage");

	}

	private void addHeader() {
		this.header = new LanguageHeaderPanel(app.getLocalization(), this);

		this.setHeaderWidget(this.header);
		// this.addResizeListener(this.header);

	}

	@Override
	public void setLabels() {
		if (this.header != null) {
			this.header.setLabels();
		}
	}

	@Override
	public AppW getApp() {
		return app;
	}
}
