package easyjarexporter.views;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import easyjarexporter.views.TestView.ViewLabelProvider;

public class JarManagementView extends ViewPart{

	public static final String ID = "easyjarexporter.views.JarManagementView";
	
	@Inject IWorkbench workbench;
	
	private TableViewer tableView;
	private int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
	private FileTransfer fileTransfer = FileTransfer.getInstance();
	Transfer[] types = new Transfer[] { fileTransfer };
	private Set<String> fileSet;
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		fileSet = new HashSet<String>();
	}

	@Override
	public void createPartControl(Composite parent) {
		tableView = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tableView.setContentProvider(ArrayContentProvider.getInstance());
		//tableView.setInput(fileSet);
		tableView.setLabelProvider(new ViewLabelProvider());
		fileSet.add("selamlar");
		tableView.setInput("selamlar");
		
		getSite().setSelectionProvider(tableView);
		
		
		tableView.addDropSupport(operations, types, new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void drop(DropTargetEvent event) {
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					String[] files = (String[]) event.data;
					for(String file : files) {
						fileSet.add(file);
					}
				}
			}
			
			@Override
			public void dragOver(DropTargetEvent event) {
				
				
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				if(event.detail == DND.DROP_DEFAULT) {
					if((operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					}
					else {
						event.detail = DND.DROP_NONE;
					}
				}
				
				if(fileTransfer.isSupportedType(event.currentDataType)) {
					if(event.detail != DND.DROP_COPY) {
						event.detail = DND.DROP_NONE;
					}
				}
			}
			
			@Override
			public void dragLeave(DropTargetEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dragEnter(DropTargetEvent event) {
				if(event.detail == DND.DROP_DEFAULT) {
					if((operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					}
					else {
						event.detail = DND.DROP_NONE;
					}
				}
				
				for(int i = 0; i < event.dataTypes.length; i++) {
					if(fileTransfer.isSupportedType(event.dataTypes[i])) {
						event.currentDataType = event.dataTypes[i];
						if(event.detail != DND.DROP_COPY) {
							event.detail = DND.DROP_NONE;
						}
						break;
					}
				}
			}
		});
	}

	@Override
	public void setFocus() {
		tableView.getControl().setFocus();
		
	}
	
	

}
