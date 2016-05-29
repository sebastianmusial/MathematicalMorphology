package pl.polsl.mathematicalMorphology;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Controller {
	@FXML
	BorderPane root;
	@FXML
	MenuItem mniFileOpen, mniFileExit;
	@FXML
	Button btnApplyFilter, btnAcceptFilter, btnSaveImage;
	@FXML
	ImageView imgLeft, imgRight;
	@FXML
	ComboBox<Filter> filterChooser;
    @FXML
    TextField inpFileName;
	@FXML
	TextArea resultsArea;
	@FXML
	Label resultsLabel;
	@FXML
	Slider hrefSlider;

	LazyLoad<FileChooser> fileChooserSupplier = new LazyLoad().withSupplier(() -> new FileChooser());
	Filters filters = Filters.getFilters();
	FileChooser.ExtensionFilter extensionFilter;
	int acceptIterator = 1;

	@FXML
	void initialize() {
		enableButtons(false);
		for (Filter fn : filters) {
			filterChooser.getItems().add(fn);
		}
		filterChooser.setValue(filters.get(0));
		extensionFilter = new FileChooser.ExtensionFilter("Obrazki", ".jpg", ".jpeg", ".bmp", ".png");
		btnAcceptFilter.setDisable(true);
		resultsArea.setEditable(false);

		hrefSlider.setStyle("-fx-background-image: url('hue-slider.png'); -fx-background-repeat: no-repeat; -fx-background-size: contain;");
		hrefSlider.setShowTickMarks(false);
		hrefSlider.setShowTickLabels(false);
	}

	@FXML
	void handleApplyFilter() {
		filterChooser
				.getValue()
				.withImage(imgLeft.getImage())
				.filter((float) hrefSlider.getValue())
				.setImage(imgRight);
		btnAcceptFilter.setDisable(false);
		System.out.printf("H-ref: " + Double.toString(hrefSlider.getValue()) + "\n");
	}

    @FXML
    void handleAcceptFilter() {
		if(imgRight.getImage() != null) {
			String filterName = filterChooser.getValue().toString();
			resultsArea.appendText(acceptIterator + ". " + filterName + "\n");
			imgLeft.setImage(imgRight.getImage());
			imgRight.setImage(null);
			acceptIterator++;
			btnAcceptFilter.setDisable(true);
		}
	}

    @FXML
    void handleSaveImage() {
        String fileName = inpFileName.getText();
        System.out.println(fileName);

		File outFile = new File("./img_saved/" + fileName + ".png");
		outFile.getParentFile().mkdirs();

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(imgLeft.getImage(), null), "png", outFile);
			inpFileName.setText("");
		} catch (Exception s) {
			s.printStackTrace();
		}
    }

	@FXML
	void handleFileOpen(ActionEvent event) {
		getFile().ifPresent(choosen -> tryOpenFile(choosen));
	}

	@FXML
	void handleOpenTestImage() {
		File source = new File("./img/test.jpg");
		tryOpenFile(source);
	}

	private void tryOpenFile(File source) {
		try {
			source = source.getCanonicalFile();
			openFile(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openFile(File source) {
		String uri = source.toURI().toString();
		System.out.println("opening: " + uri);

		imgLeft.setImage(new Image(uri));

		enableButtons(true);
		resultsArea.setText("");
		acceptIterator = 1;
	}

    private Optional<File> getFile() {
        FileChooser fileChooser = fileChooserSupplier.get();
        File chosen = fileChooser.showOpenDialog(root.getScene().getWindow());
        return Optional.ofNullable(chosen);
    }

	@FXML
	private void enableButtons(boolean enable) {
		boolean disable = !enable;
		btnApplyFilter.setDisable(disable);
		filterChooser.setDisable(disable);
        inpFileName.setDisable(disable);
		resultsLabel.setDisable(disable);
        btnSaveImage.setDisable(disable);
		resultsArea.setDisable(disable);
		hrefSlider.setDisable(disable);
	}

	@FXML
	void handleFileExit(ActionEvent event) {
		Platform.exit();
	}
}
