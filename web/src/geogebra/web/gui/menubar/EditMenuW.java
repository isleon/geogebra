package geogebra.web.gui.menubar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import geogebra.common.main.App;
import geogebra.web.gui.dialog.WebCamInputDialog;
import geogebra.common.util.CopyPaste;
import geogebra.web.gui.images.AppResources;
import geogebra.web.main.AppW;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;

/**
 * The "Edit" menu.
 */
public class EditMenuW extends MenuBar {

	/**
	 * Application instance
	 */
	App app;

	InsertImageMenuW iim;

	/**
	 * Constructs the "Edit" menu
	 * @param app Application instance
	 */
	public EditMenuW(App app) {

		super(true);
		this.app = app;
		addStyleName("GeoGebraMenuBar");
		iim = new InsertImageMenuW(app);
		initActions();
	}

	void initActions() {
		
		String noIcon = AppResources.INSTANCE.empty().getSafeUri().asString();
		/* layer values:
		 *  -1 means nothing selected
		 *  -2 means different layers selected
		 */
		int layer = app.getSelectedLayer();	
		boolean justCreated = !(app.getActiveEuclidianView().getEuclidianController().getJustCreatedGeos().isEmpty());

		
		
		

		clearItems();
		
		// undo menu
		if (app.getKernel().undoPossible())
			addItem(GeoGebraMenubarW.getMenuBarHtml(AppResources.INSTANCE
			        .edit_undo().getSafeUri().asString(), app.getMenu("Undo")),
			        true, new Command() {
				        public void execute() {
					        app.getGuiManager().undo();
				        }
			        });
		else
			addItem(GeoGebraMenubarW.getMenuBarHtmlGrayout(AppResources.INSTANCE
			        .edit_undo().getSafeUri().asString(), app.getMenu("Undo")),
			        true, new Command() {
				public void execute() {
					// do nothing
				}
			});


		// redo menu
		if (app.getKernel().redoPossible())
			addItem(GeoGebraMenubarW.getMenuBarHtml(AppResources.INSTANCE
			        .edit_redo().getSafeUri().asString(), app.getMenu("Redo")),
			        true, new Command() {
				        public void execute() {
					        app.getGuiManager().redo();
				        }
			        });
		else
			addItem(GeoGebraMenubarW.getMenuBarHtmlGrayout(AppResources.INSTANCE
			        .edit_redo().getSafeUri().asString(), app.getMenu("Redo")),
			        true, new Command() {
				public void execute() {
					// do nothing
				}
			});

		// separator
		addSeparator();
			
		// copy menu
		if (!app.getSelectedGeos().isEmpty())
			addItem(GeoGebraMenubarW.getMenuBarHtml(AppResources.INSTANCE
			        .edit_copy().getSafeUri().asString(), app.getMenu("Copy")),
			        true, new Command() {
				        public void execute() {
					        app.setWaitCursor();
					        CopyPaste.copyToXML(app, app.getSelectedGeos());
					        app.updateMenubar();
					        app.setDefaultCursor();
				        }
			        });
		else
			addItem(GeoGebraMenubarW.getMenuBarHtmlGrayout(AppResources.INSTANCE
			        .edit_copy().getSafeUri().asString(), app.getMenu("Copy")),
			        true, new Command() {
				        public void execute() {
					        // do nothing
				        }
			        });

		// paste menu
		if (!CopyPaste.isEmpty())
			addItem(GeoGebraMenubarW
			        .getMenuBarHtml(AppResources.INSTANCE.edit_paste()
			                .getSafeUri().asString(), app.getMenu("Paste")),
			        true, new Command() {
				        public void execute() {
					        app.setWaitCursor();
					        CopyPaste.pasteFromXML(app);
					        app.setDefaultCursor();
				        }
			        });
		else
			addItem(GeoGebraMenubarW
			        .getMenuBarHtmlGrayout(AppResources.INSTANCE.edit_paste()
			                .getSafeUri().asString(), app.getMenu("Paste")),
			        true, new Command() {
				        public void execute() {
					        // do nothing
				        }
			        });

		// copy graphics view menu
		addItem(GeoGebraMenubarW.getMenuBarHtml(AppResources.INSTANCE
		        .edit_copy().getSafeUri().asString(), app.getMenu("CopyImage")),
		        true, new Command() {
			        public void execute() {
				        ((AppW) app).copyEVtoClipboard();
			        }
		        });

		// separator
		addSeparator();

		addItem(GeoGebraMenubarW.getMenuBarHtml(noIcon, app.getMenu("InsertImageFrom")),
		        true, iim);

		addSeparator();

		// select all menu
		if (!app.getKernel().isEmpty())
			addItem(GeoGebraMenubarW.getMenuBarHtml(noIcon,
			        app.getMenu("SelectAll")), true, new Command() {
				public void execute() {
					app.selectAll(-1);
				}
			});
		else
			addItem(GeoGebraMenubarW.getMenuBarHtmlGrayout(noIcon,
			        app.getMenu("SelectAll")), true, new Command() {
				public void execute() {
					// do nothing
				}
			});
		

		
		
		if (layer != -1 || justCreated){
			addSeparator();
			addItem(GeoGebraMenubarW.getMenuBarHtml(AppResources.INSTANCE
			        .edit_copy().getSafeUri().asString(),
			        app.getMenu("Delete")), true, new Command() {
				public void execute() {
					app.deleteSelectedObjects();
				}
			});
		}
		

	}
	
}
