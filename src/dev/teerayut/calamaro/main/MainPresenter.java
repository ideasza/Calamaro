package dev.teerayut.calamaro.main;

public class MainPresenter implements MainInterface.Presenter {
	
	private MainInterface.View view;
	public MainPresenter(MainInterface.View view) {
		this.view = view;
	}

}
