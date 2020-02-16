package easyjarexporter.handlers;

import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

public class ErrorHandler extends AbstractStatusHandler {

	@Override
	public void handle(StatusAdapter status, int style) {
		
		System.out.println("error handler has just worked");
	}

}
