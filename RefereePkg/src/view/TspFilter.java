package view;

import java.io.File;
import javax.swing.filechooser.FileFilter;

class TspFilter extends FileFilter {

	// Accept all directories and tsp files.
  @Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Utils.getExtension(f);

		if (extension != null) {

			if (extension.equals(Utils.getTaskspecextension())) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	// The description of this filter
  @Override
	public String getDescription() {
		return "TaskSpecification";
	}
}